package services;

import models.Group;
import models.User;
import org.hibernate.Hibernate;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserService {
    private final JPAApi api;

    @Inject
    public UserService(JPAApi api) {
        this.api = api;
    }

    public List<User> getAll() {
        return api.withTransaction(em -> {
            List<User> list = em.createQuery("from User o", User.class).getResultList();
            return list;
        });
    }

    public Optional<User> get(String name) {
        return api.withTransaction(em -> {
            User user = em.find(User.class, name);
            return Optional.ofNullable(user);
        });
    }

    public void create(User user) throws IllegalArgumentException {
        if (user.getGroups() != null && !user.getGroups().isEmpty()) {
            throw new IllegalArgumentException("Can't create user with group");
        }
        api.withTransaction(em -> {
            em.persist(user);
            return null;
        });
    }

    public void update(User user) {
        api.withTransaction(em -> {
            user.setLastActionTime(Instant.now().toString());
            em.merge(user);
            return null;
        });
    }

    public int delete(String name) {
        return api.withTransaction(em -> em.createQuery("delete User where name = :name")
                .setParameter("name", name)
                .executeUpdate());
    }
}
