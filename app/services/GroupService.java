package services;

import models.Group;
import org.hibernate.Hibernate;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class GroupService {
    private final JPAApi api;

    @Inject
    public GroupService(JPAApi api) {
        this.api = api;
    }

    public List<Group> getAll() {
        return api.withTransaction(em -> {
            List<Group> groups = em.createQuery("from Group g", Group.class).getResultList();
            groups.forEach(g -> Hibernate.initialize(g.getUsers()));
            return groups;
        });
    }

    public Optional<Group> get(String name) {
        return api.withTransaction(em -> {
            Optional<Group> group = Optional.ofNullable(em.find(Group.class, name));
            if (group.isPresent()) {
                Hibernate.initialize(group.get().getUsers());
            }
            return group;

        });
    }

    public void create(Group order) {
        api.withTransaction(em -> {
            em.persist(order);
            return null;
        });
    }

    public void update(Group group) {
        api.withTransaction(em -> {
            em.merge(group);
            return null;
        });
    }

    public int delete(String name) {
        return api.withTransaction(em -> em.createQuery("delete Group where name = :name")
                .setParameter("name", name)
                .executeUpdate());
    }
}
