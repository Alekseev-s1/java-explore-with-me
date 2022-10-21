package ru.practicum.explore.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Sort;
import ru.practicum.explore.model.State;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomEventRepository {
    private final EntityManager entityManager;

    @Autowired
    public CustomEventRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Event> findAll(String text,
                               List<Long> categories,
                               Boolean paid,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Sort sort,
                               State state,
                               int from,
                               int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (text != null) {
            predicates.add(cb.or(
                    cb.like(cb.upper(event.get("annotation")), "%" + text.toUpperCase() + "%"),
                    cb.like(cb.upper(event.get("description")), "%" + text.toUpperCase() + "%")
            ));
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(cb.in(event.get("category").get("id")).value(categories));
        }

        if (paid != null) {
            predicates.add(cb.equal(event.get("paid"), paid));
        }

        if (rangeStart != null) {
            predicates.add(cb.greaterThan(event.get("eventDate"), rangeEnd));
        } else {
            LocalDateTime now = LocalDateTime.now();
            predicates.add(cb.greaterThan(event.get("eventDate"), now));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThan(event.get("eventDate"), rangeEnd));
        }

        if (state != null) {
            predicates.add(cb.equal(event.get("state"), state));
        }

        return entityManager
                .createQuery(query.select(event).where(cb.and(predicates.toArray(Predicate[]::new)))
                        .orderBy(cb.desc(event.get(sort.toString().toLowerCase()))))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }
}
