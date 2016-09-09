import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import models.Group;
import org.junit.*;

import play.Application;
import play.mvc.*;
import services.UserService;
import services.GroupService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class GroupIT {
    private final ObjectMapper mapper = new ObjectMapper();
    private Application application;

    @Before
    public void setupApplication() {
        Map<String, String> dataSourceConfig = new HashMap<>();
//        dataSourceConfig.put("db.default.url", "jdbc:h2:./test-database;INIT=RUNSCRIPT FROM './conf/create.sql'\\;");
        dataSourceConfig.put("db.default.url", "jdbc:h2:./test-database;");
        dataSourceConfig.put("jpa.default", "testPersistenceUnit");
//        dataSourceConfig.put("INIT", "RUNSCRIPT FROM './conf/create.sql'");
        application = fakeApplication(dataSourceConfig);
    }

    @Test
    public void testPost() throws JsonProcessingException {
        UserService userService = application.injector().instanceOf(UserService.class);
        User user1 = TestObjectFactory.newUser();
        User user2 = TestObjectFactory.newUser();
        Set<User> users = getPersistedUsers(userService, user1, user2);

        Group group = TestObjectFactory.newGroup();
        group.setUsers(users);
        group.setCreatedBy(user1);

        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyText(mapper.writeValueAsString(group))
                .header("Content-Type", "application/json")
                .uri("/groups");

        Result result = route(application, request);

        assertEquals(201, result.status());

        GroupService groupService = application.injector().instanceOf(GroupService.class);
        assertEquals(group.getUsers().size(), groupService.get(group.getName()).get().getUsers().size());

        user1 = userService.get(user1.getName()).get();
        assertEquals(1, user1.getGroups().size());
    }

    @Test
    public void testPut() {
        Group group = TestObjectFactory.newGroup();

        GroupService groupService = application.injector().instanceOf(GroupService.class);
        UserService userService = application.injector().instanceOf(UserService.class);
        User user1 = TestObjectFactory.newUser();
        User user2 = TestObjectFactory.newUser();
        Set<User> users = getPersistedUsers(userService, user1);
        group.setCreatedBy(user1);
        group.setUsers(users);

        groupService.create(group);
        users.addAll(getPersistedUsers(userService, user2));

        group.setUsers(users);
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson((JsonNode)mapper.valueToTree(group)).uri("/groups");
        Result result = route(application, request);

        assertEquals(200, result.status());
        assertEquals(group.getUsers().size(), groupService.get(group.getName()).get().getUsers().size());
    }

    @Test
    public void testGetAll() throws IOException {
        Group group1 = TestObjectFactory.newGroup();
        Group group2 = TestObjectFactory.newGroup();

        GroupService groupService = application.injector().instanceOf(GroupService.class);
        groupService.create(group1);
        groupService.create(group2);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/groups");
        Result result = route(application, request);

        assertEquals(200, result.status());
        Group[] groups = mapper.readValue(contentAsBytes(result).toArray(), Group[].class);
        assertTrue(Stream.of(groups).anyMatch(g -> g.getName().equals(group1.getName())));
        assertTrue(Stream.of(groups).anyMatch(g -> g.getName().equals(group2.getName())));
    }

    @Test
    public void testGet() throws IOException {
        Group group = TestObjectFactory.newGroup();

        GroupService groupService = application.injector().instanceOf(GroupService.class);
        groupService.create(group);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/groups/" + group.getName());
        Result result = route(application, request);

        assertEquals(200, result.status());
        Group groupFromResult = mapper.readValue(contentAsBytes(result).toArray(), Group.class);
        assertEquals(groupFromResult.getName(), group.getName());
    }

    @Test
    public void testRemove() throws IOException {
        Group group = TestObjectFactory.newGroup();

        GroupService groupService = application.injector().instanceOf(GroupService.class);
        groupService.create(group);

        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri("/groups/" + group.getName());
        Result result = route(application, request);

        Assert.assertEquals(200, result.status());
        assertFalse(groupService.get(group.getName()).isPresent());
    }

    private Set<User> getPersistedUsers(UserService service, User... users) {
        Set<User> userSet = new HashSet<>();
        for (User user : users) {
            service.create(user);
            userSet.add(user);
        }
        return userSet;
    }
}
