package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDto;
import models.Order;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.OrderService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;



@Singleton
public class OrderController extends Controller {
    private final OrderService orderService;
    private final ObjectMapper mapper;

    @Inject
    public OrderController(OrderService orderService, ObjectMapper mapper) {
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
            return created(orderService.create(mapper.treeToValue(request().body().asJson(), Order.class)));
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result update(String id) {
        try {
            orderService.update(mapper.treeToValue(request().body().asJson(), Order.class), id);
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
}
