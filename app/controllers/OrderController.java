package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDto;
import models.Courier;
import models.Order;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.CourierService;
import services.OrderService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;



@Singleton
public class OrderController extends Controller {
    private final CourierService courierService;
    private final OrderService orderService;
    private final ObjectMapper mapper;

    @Inject
    public OrderController(OrderService orderService, CourierService courierService, ObjectMapper mapper) {
        this.courierService = courierService;
        this.orderService = orderService;
        this.mapper = mapper;
    }

    public Result getAll() {
        return ok(Json.toJson(orderService.getAll()));
    }

    public Result get(String id) {
        Optional<Order> order = orderService.get(id);
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
            Order order = mapper.treeToValue(request().body().asJson(), Order.class);
            enhanceOrderWithCourier(order);
            return created(orderService.create(order));
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(String id) {
        try {
            Order order = mapper.treeToValue(request().body().asJson(), Order.class);
            enhanceOrderWithCourier(order);
            orderService.update(order, id);
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
        return ok();
    }

    public Result delete(String id) {
        int count = orderService.delete(id);
        if (count == 0) {
            return notFound();
        }
        return ok();
    }

    private void enhanceOrderWithCourier(Order order) throws IllegalArgumentException {
        if (order.getCourier() != null) {
            if (order.getCourier().getId() != null) {
                Courier courier = courierService.get(order.getCourier().getId()).get();
                order.setCourier(courier);
            } else {
                throw new IllegalArgumentException("Order ID must be specified");
            }
        }
    }
}
