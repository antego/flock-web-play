package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Courier;
import models.Order;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.CourierService;
import services.OrderService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;


@Singleton
public class CourierController extends Controller {
    private final CourierService courierService;
    private final OrderService orderService;
    private final ObjectMapper mapper;

    @Inject
    public CourierController(OrderService orderService, CourierService courierService, ObjectMapper mapper) {
        this.courierService = courierService;
        this.orderService = orderService;
        this.mapper = mapper;
    }

    public Result getAll() {
        return ok(Json.toJson(courierService.getAll()));
    }

    public Result get(String id) {
        Optional<Courier> courier = courierService.get(id);
        if (courier.isPresent()) {
            return ok(Json.toJson(courier));
        } else {
            return notFound();
        }
    }

    //todo think about answer
    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        try {
            Courier courier = mapper.treeToValue(request().body().asJson(), Courier.class);
            if (courier.getOrder() != null) {
                return badRequest("Can't create courier with order. Use PUT to assign order to courier.");
            }
            return created(courierService.create(courier));
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(String id) {
        try {
            Courier courier = mapper.treeToValue(request().body().asJson(), Courier.class);
            enhanceCourierWithOrder(courier);
            courierService.update(courier, id);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return badRequest(e.toString());
        }
        return ok();
    }

    public Result delete(String id) {
        int count = courierService.delete(id);
        if (count == 0) {
            return notFound();
        }
        return ok();
    }

    private void enhanceCourierWithOrder(Courier courier) throws IllegalArgumentException {
        if (courier.getOrder() != null) {
            if (courier.getOrder().getId() != null) {
                Order order = orderService.get(courier.getOrder().getId()).get();
                courier.setOrder(order);
            } else {
                throw new IllegalArgumentException("Order ID must be specified");
            }
        }
    }
}
