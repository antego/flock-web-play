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

    @Transactional(readOnly = true)
    public Result getAll() {
        return ok(Json.toJson(orderService.getAll()));
    }

    @Transactional(readOnly = true)
    public Result get(String id) {
        return ok(Json.toJson(orderService.get(id)));
    }

    //todo think about answer
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public Result create() {
        try {
            return ok(orderService.create(mapper.treeToValue(request().body().asJson(), Order.class)));
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public Result update(String id) {
        try {
            orderService.update(mapper.treeToValue(request().body().asJson(), Order.class), id);
        } catch (JsonProcessingException e) {
            return badRequest(e.toString());
        }
        return ok();
    }

    @Transactional
    public Result delete(String id) {
        orderService.delete(id);
        return ok();
    }
}
