package services;

import dto.OrderDto;
import models.Order;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class OrderService {
    private final JPAApi api;

    @Inject
    public OrderService(JPAApi api) {
        this.api = api;
    }

    public List<Order> getAll() {
        return api.withTransaction(em -> em.createQuery("from Order o", Order.class).getResultList());
    }

    public Optional<Order> get(String id) {
        return api.withTransaction(em -> Optional.ofNullable(em.find(Order.class, id)));
    }

    public String create(Order order) {
        String id = UUID.randomUUID().toString();
        order.setId(id);
        api.withTransaction(em -> {
            em.persist(order);
            return null;
        });
        return id;
    }

    public void update(Order order, String id) {
        order.setId(id);
        api.withTransaction(em -> em.merge(order));
    }

    public int delete(String id) {
        return api.withTransaction(em -> em.createQuery("delete Order where id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }
}
