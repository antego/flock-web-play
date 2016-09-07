import models.Courier;
import models.Order;

import java.util.Random;
import java.util.UUID;

public class TestObjectFactory {
    private static Random random = new Random();

    public static Order newOrder() {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setLng(random.nextDouble() + "");
        order.setLat(random.nextDouble() + "");
        order.setDescription("desc" + random.nextInt());
        order.setFromAddress("to" + random.nextInt());
        order.setSubject("subj" + random.nextInt());
        order.setToAddress("from" + random.nextInt());
        return order;
    }

    public static Courier newCourier() {
        Courier courier = new Courier();
        courier.setId(UUID.randomUUID().toString());
        courier.setName("name" + random.nextInt());
        courier.setReadyForOrder(random.nextBoolean());
        courier.setSuccessDeliveries(random.nextInt());
        return courier;
    }
}
