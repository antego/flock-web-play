import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Courier;
import models.Order;
import org.junit.*;

import play.Application;
import play.mvc.*;
import services.CourierService;
import services.OrderService;

import java.io.IOException;
import java.util.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class OrderIT {
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
        Courier courier = TestObjectFactory.newCourier();
        CourierService courierService = application.injector().instanceOf(CourierService.class);
        String courierId = courierService.create(courier);
        courier.setId(courierId);

        Order order = TestObjectFactory.newOrder();
        order.setCourier(courier);

        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyText(mapper.writeValueAsString(order))
                .header("Content-Type", "application/json")
                .uri("/orders");

        Result result = route(application, request);

        assertEquals(201, result.status());

        String id = contentAsString(result);
        UUID.fromString(id);

        OrderService orderService = application.injector().instanceOf(OrderService.class);
        order.setId(id);
        assertTrue(order.equals(orderService.get(id).get()));
    }

    @Test
    public void testPut() {
        Order order = TestObjectFactory.newOrder();

        OrderService orderService = application.injector().instanceOf(OrderService.class);
        orderService.create(order);

        order.setLng("newLng");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson((JsonNode)mapper.valueToTree(order)).uri("/orders/" + order.getId());
        Result result = route(application, request);

        assertEquals(200, result.status());
        assertTrue(order.equals(orderService.get(order.getId()).get()));
    }

    @Test
    public void testGetAll() throws IOException {
        Order order1 = TestObjectFactory.newOrder();
        Order order2 = TestObjectFactory.newOrder();

        OrderService orderService = application.injector().instanceOf(OrderService.class);
        orderService.create(order1);
        orderService.create(order2);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/orders");
        Result result = route(application, request);

        assertEquals(200, result.status());
        Order[] orders = mapper.readValue(contentAsBytes(result).toArray(), Order[].class);
        assertTrue(Arrays.asList(orders).contains(order1));
        assertTrue(Arrays.asList(orders).contains(order2));
    }

    @Test
    public void testGet() throws IOException {
        Order order1 = TestObjectFactory.newOrder();

        OrderService orderService = application.injector().instanceOf(OrderService.class);
        String id = orderService.create(order1);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/orders/" + id);
        Result result = route(application, request);

        assertEquals(200, result.status());
        Order orderFromResult = mapper.readValue(contentAsBytes(result).toArray(), Order.class);
        assertEquals(orderFromResult, order1);
    }

    @Test
    public void testRemove() throws IOException {
        Order order1 = TestObjectFactory.newOrder();

        OrderService orderService = application.injector().instanceOf(OrderService.class);
        String id = orderService.create(order1);

        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri("/orders/" + id);
        Result result = route(application, request);

        Assert.assertEquals(200, result.status());
        assertFalse(orderService.get(id).isPresent());
    }
}
