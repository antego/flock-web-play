package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.User;
import models.Group;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;
import services.GroupService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;


@Singleton
public class GroupController extends Controller {
    private final UserService userService;
    private final GroupService groupService;
    private final ObjectMapper mapper;

    @Inject
    public GroupController(GroupService groupService, UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.groupService = groupService;
        this.mapper = mapper;
    }

    public Result getAll() {
        return ok(Json.toJson(groupService.getAll()));
    }

    public Result get(String name) {
        Optional<Group> order = groupService.get(name);
        if (order.isPresent()) {
            return ok(Json.toJson(order));
        } else {
            return notFound();
        }
    }

    //todo think about answer
    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        try {
            Group group = mapper.treeToValue(request().body().asJson(), Group.class);
            findUsersForGroup(group);
            groupService.create(group);
            return created();
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(String name) {
        try {
            Group group = mapper.treeToValue(request().body().asJson(), Group.class);
            findUsersForGroup(group);
            groupService.update(group);
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
        return ok();
    }

    public Result delete(String name) {
        int count = groupService.delete(name);
        if (count == 0) {
            return notFound();
        }
        return ok();
    }

    private void findUsersForGroup(Group group) throws IllegalArgumentException {
        if (group.getUsers() != null) {
            Set<User> newUsers = new HashSet<>();
            for (User rawUser : group.getUsers()) {
                if (rawUser != null && !"".equals(rawUser.getName())) {
                    User user = userService.get(rawUser.getName()).get();
                    newUsers.add(user);
                } else {
                    throw new IllegalArgumentException("Order ID must be specified");
                }
            }
            group.setUsers(newUsers);
        }
    }
}
