import models.User;
import models.Group;

import java.util.Random;
import java.util.UUID;

public class TestObjectFactory {
    private static Random random = new Random();

    public static Group newGroup() {
        Group group = new Group();
        group.setName("name" + random.nextInt());
        return group;
    }

    public static User newUser() {
        User user = new User();
        user.setLat(random.nextDouble() + "");
        user.setLng(random.nextDouble() + "");
        user.setName("name" + random.nextInt());
        return user;
    }
}
