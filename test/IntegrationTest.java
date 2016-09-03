import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Order;
import org.junit.*;

import play.Application;
import play.mvc.*;
import services.OrderService;

import java.io.IOException;
import java.util.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class IntegrationTest {
    private Application application;
    private final ObjectMapper mapper = new ObjectMapper();

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
    public void testPost() {
        Order order = new Order();
        order.setId("testId");
        order.setLat("lat");
        order.setLng("lng");
        order.setDescription("title");

        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyText("{\"lat\": \"lat\", \"lng\": \"lng\", \"description\": \"title\"}\n")
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
        Order order = new Order();
        order.setId("testId");
        order.setLat("lat");
        order.setLng("lng");
        order.setDescription("title");

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
        Order order1 = new Order();
        order1.setId("testId1");
        order1.setLat("lat");
        order1.setLng("lng");
        order1.setDescription("title");

        Order order2 = new Order();
        order2.setId("testId1");
        order2.setLat("lat");
        order2.setLng("lng");
        order2.setDescription("title");

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
        Order order1 = new Order();
        order1.setId("testId1");
        order1.setLat("lat");
        order1.setLng("lng");
        order1.setDescription("title");

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
        Order order1 = new Order();
        order1.setId("testId1");
        order1.setLat("lat");
        order1.setLng("lng");
        order1.setDescription("title");

        OrderService orderService = application.injector().instanceOf(OrderService.class);
        String id = orderService.create(order1);

        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri("/orders/" + id);
        Result result = route(application, request);

        Assert.assertEquals(200, result.status());
        assertFalse(orderService.get(id).isPresent());
    }
}
