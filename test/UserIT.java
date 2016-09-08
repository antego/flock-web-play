import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import models.Group;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import services.UserService;
import services.GroupService;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;
import static play.test.Helpers.contentAsBytes;
import static play.test.Helpers.route;

public class UserIT {
    private final ObjectMapper mapper = new ObjectMapper();
    private Application application;

    @Before
    public void setupApplication() {
        Map<String, String> dataSourceConfig = new HashMap<>();
//        dataSourceConfig.put("db.default.url", "jdbc:h2:./test-database;INIT=RUNSCRIPT FROM './conf/create.sql'\\;");
        dataSourceConfig.put("db.default.url", "jdbc:h2:./test-database;DB_CLOSE_DELAY=-1");
        dataSourceConfig.put("jpa.default", "testPersistenceUnit");
//        dataSourceConfig.put("INIT", "RUNSCRIPT FROM './conf/create.sql'");
        application = fakeApplication(dataSourceConfig);
    }

    @Test
    public void testPost() throws JsonProcessingException {
        GroupService groupService = application.injector().instanceOf(GroupService.class);
        Group group1 = TestObjectFactory.newGroup();
        groupService.create(group1);

        Group group2 = TestObjectFactory.newGroup();
        groupService.create(group2);

        User user = TestObjectFactory.newUser();
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        user.setGroups(groups);

        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyText(mapper.writeValueAsString(user))
                .header("Content-Type", "application/json")
                .uri("/users");

        assertEquals(400, route(application, request).status());

        user.setGroups(null);
        request = new Http.RequestBuilder().method("POST")
                .bodyText(mapper.writeValueAsString(user))
                .header("Content-Type", "application/json")
                .uri("/users");
        Result result = route(application, request);
        assertEquals(201, result.status());

        UserService userService = application.injector().instanceOf(UserService.class);
        assertEquals(user.getName(), userService.get(user.getName()).get().getName());
    }

    @Test
    public void testPut() {
        GroupService groupService = application.injector().instanceOf(GroupService.class);
        User user = TestObjectFactory.newUser();

        UserService userService = application.injector().instanceOf(UserService.class);
        userService.create(user);

        Group group1 = TestObjectFactory.newGroup();
        Group group2 = TestObjectFactory.newGroup();
        user.setGroups(getPersistedGroups(
                groupService,
                group1,
                group2));

        assertEquals(2, groupService.getAll().size());

        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson((JsonNode)mapper.valueToTree(user)).uri("/users/" + user.getName());
        Result result = route(application, request);

        assertEquals(200, result.status());
        User userFromService = userService.get(user.getName()).get();
        assertEquals(user.getGroups().size(), userFromService.getGroups().size());
    }

    @Test
    public void testGetAll() throws IOException {
        User user1 = TestObjectFactory.newUser();
        User user2 = TestObjectFactory.newUser();

        UserService userService = application.injector().instanceOf(UserService.class);
        userService.create(user1);
        userService.create(user2);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/users");
        Result result = route(application, request);

        assertEquals(200, result.status());
        User[] couriers = mapper.readValue(contentAsBytes(result).toArray(), User[].class);
        assertTrue(Arrays.asList(couriers).contains(user1));
        assertTrue(Arrays.asList(couriers).contains(user2));
    }

    @Test
    public void testGet() throws IOException {
        User user = TestObjectFactory.newUser();

        UserService userService = application.injector().instanceOf(UserService.class);
        userService.create(user);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/users/" + user.getName());
        Result result = route(application, request);

        assertEquals(200, result.status());
        User userFromResult = mapper.readValue(contentAsBytes(result).toArray(), User.class);
        assertEquals(user.getName(), userFromResult.getName());
    }

    @Test
    public void testRemove() throws IOException {
        User user = TestObjectFactory.newUser();

        UserService userService = application.injector().instanceOf(UserService.class);
        userService.create(user);

        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri("/users/" + user.getName());
        Result result = route(application, request);

        Assert.assertEquals(200, result.status());
        assertFalse(userService.get(user.getName()).isPresent());
    }

    private List<Group> getPersistedGroups(GroupService service, Group... groups) {
        List<Group> groupList = new ArrayList<>();
        for (Group group : groups) {
            service.create(group);
            groupList.add(group);
        }
        return groupList;
    }
}
