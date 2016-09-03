package services;

import models.Courier;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class CourierService {
    private final JPAApi api;

    @Inject
    public CourierService(JPAApi api) {
        this.api = api;
    }

    public List<Courier> getAll() {
        return api.withTransaction(em -> em.createQuery("from Courier o", Courier.class).getResultList());
    }

    public Optional<Courier> get(String id) {
        return api.withTransaction(em -> Optional.ofNullable(em.find(Courier.class, id)));
    }

    public String create(Courier courier) throws IllegalArgumentException {
        String id = UUID.randomUUID().toString();
        courier.setId(id);
        if (courier.getOrder() != null) {
            throw new IllegalArgumentException("Can't create courier with order");
        }
        api.withTransaction(em -> {
            em.persist(courier);
            return null;
        });
        return id;
    }

    public void update(Courier courier, String id) {
        courier.setId(id);
        api.withTransaction(em -> em.merge(courier));
    }

    public int delete(String id) {
        return api.withTransaction(em -> em.createQuery("delete Courier where id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }
}
