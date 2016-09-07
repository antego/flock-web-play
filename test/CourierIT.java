import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Courier;
import models.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import services.CourierService;
import services.OrderService;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;
import static play.test.Helpers.contentAsBytes;
import static play.test.Helpers.route;

public class CourierIT {
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
        Order order = TestObjectFactory.newOrder();
        OrderService orderService = application.injector().instanceOf(OrderService.class);
        String orderId = orderService.create(order);
        order.setId(orderId);

        Courier courier = TestObjectFactory.newCourier();
        courier.setOrder(order);

        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .bodyText(mapper.writeValueAsString(courier))
                .header("Content-Type", "application/json")
                .uri("/couriers");

        assertEquals(400, route(application, request).status());

        courier.setOrder(null);
        request = new Http.RequestBuilder().method("POST")
                .bodyText(mapper.writeValueAsString(courier))
                .header("Content-Type", "application/json")
                .uri("/couriers");
        Result result = route(application, request);
        assertEquals(201, result.status());

        String id = contentAsString(result);
        UUID.fromString(id);

        CourierService courierService = application.injector().instanceOf(CourierService.class);
        courier.setId(id);
        assertTrue(courier.equals(courierService.get(id).get()));
    }

    @Test
    public void testPut() {
        Courier courier = TestObjectFactory.newCourier();

        CourierService courierService = application.injector().instanceOf(CourierService.class);
        courierService.create(courier);

        courier.setName("newName");
        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .bodyJson((JsonNode)mapper.valueToTree(courier)).uri("/couriers/" + courier.getId());
        Result result = route(application, request);

        assertEquals(200, result.status());
        assertTrue(courier.equals(courierService.get(courier.getId()).get()));
    }

    @Test
    public void testGetAll() throws IOException {
        Courier courier1 = TestObjectFactory.newCourier();
        Courier courier2 = TestObjectFactory.newCourier();

        CourierService courierService = application.injector().instanceOf(CourierService.class);
        courierService.create(courier1);
        courierService.create(courier2);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/couriers");
        Result result = route(application, request);

        assertEquals(200, result.status());
        Courier[] couriers = mapper.readValue(contentAsBytes(result).toArray(), Courier[].class);
        assertTrue(Arrays.asList(couriers).contains(courier1));
        assertTrue(Arrays.asList(couriers).contains(courier2));
    }

    @Test
    public void testGet() throws IOException {
        Courier courier = TestObjectFactory.newCourier();

        CourierService courierService = application.injector().instanceOf(CourierService.class);
        String id = courierService.create(courier);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri("/couriers/" + id);
        Result result = route(application, request);

        assertEquals(200, result.status());
        Courier courierFromResult = mapper.readValue(contentAsBytes(result).toArray(), Courier.class);
        assertEquals(courierFromResult, courier);
    }

    @Test
    public void testRemove() throws IOException {
        Courier courier = TestObjectFactory.newCourier();

        CourierService courierService = application.injector().instanceOf(CourierService.class);
        String id = courierService.create(courier);

        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri("/couriers/" + id);
        Result result = route(application, request);

        Assert.assertEquals(200, result.status());
        assertFalse(courierService.get(id).isPresent());
    }
}
