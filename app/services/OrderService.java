package services;

import dto.OrderDto;
import models.Order;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Query;
import java.util.List;
import java.util.UUID;

@Singleton
public class OrderService {
    private final JPAApi api;

    @Inject
    public OrderService(JPAApi api) {
        this.api = api;
    }

    public List<Order> getAll() {
        return api.em().createQuery("from Order o", Order.class).getResultList();
    }

    public Order get(String id) {
        return api.em().find(Order.class, id);
    }

    public String create(Order order) {
//        Order order = convertToEntity(dto);
        String id = UUID.randomUUID().toString();
        order.setId(id);
        api.em().persist(order);
        return id;
    }

    public void update(Order order, String id) {
//        Order order = convertToEntity(dto);
        order.setId(id);
        api.em().merge(order);
    }

    public void delete(String id) {
        api.em().createQuery("delete Order where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Order convertToEntity(OrderDto dto) {
        Order order = new Order();
        order.setTitle(dto.getTitle());
        order.setLng(dto.getLng());
        order.setLat(dto.getLat());
        return order;
    }

    public OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setTitle(order.getTitle());
        dto.setLat(order.getLat());
        dto.setLng(order.getLng());
        return dto;
    }
}
