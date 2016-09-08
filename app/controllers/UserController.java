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
public class UserController extends Controller {
    private final UserService userService;
    private final GroupService groupService;
    private final ObjectMapper mapper;

    @Inject
    public UserController(GroupService groupService, UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        this.groupService = groupService;
        this.mapper = mapper;
    }

    public Result getAll() {
        return ok(Json.toJson(userService.getAll()));
    }

    public Result get(String name) {
        Optional<User> user = userService.get(name);
        if (user.isPresent()) {
            return ok(Json.toJson(user));
        } else {
            return notFound();
        }
    }

    //todo think about answer
    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        try {
            User user = mapper.treeToValue(request().body().asJson(), User.class);
            if (user.getGroups() != null) {
                return badRequest("Can't create courier with order. Use PUT to assign order to courier.");
            }
            userService.create(user);
            return created();
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(String name) {
        try {
            User user = mapper.treeToValue(request().body().asJson(), User.class);
            findGroupsForUser(user);
            userService.update(user);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return badRequest(e.toString());
        }
        return ok();
    }

    public Result delete(String name) {
        int count = userService.delete(name);
        if (count == 0) {
            return notFound();
        }
        return ok();
    }

    private void findGroupsForUser(User user) throws IllegalArgumentException {
        if (user.getGroups() != null) {
            List<Group> newGroups = new ArrayList<>();
            for (Group rawGroup : user.getGroups()) {
                if (rawGroup != null && !"".equals(rawGroup.getName())) {
                    Group group = groupService.get(rawGroup.getName()).get();
                    newGroups.add(group);
                } else {
                    throw new IllegalArgumentException("Group name must be specified");
                }
            }
            user.setGroups(newGroups);
        }
    }
}
