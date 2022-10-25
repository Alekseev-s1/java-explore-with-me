package ru.practicum.explore.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Sort;
import ru.practicum.explore.model.State;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    public List<Event> findAllPublicEvents(String text,
                                           List<Long> categoryIds,
                                           Boolean paid,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Sort sort,
                                           int from,
                                           int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(event.get("state"), State.PUBLISHED));

        if (text != null) {
            predicates.add(cb.or(
                    cb.like(cb.upper(event.get("annotation")), "%" + text.toUpperCase() + "%"),
                    cb.like(cb.upper(event.get("description")), "%" + text.toUpperCase() + "%")
            ));
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            predicates.add(cb.in(event.get("category").get("id")).value(categoryIds));
        }

        if (paid != null) {
            predicates.add(cb.equal(event.get("paid"), paid));
        }

        if (rangeStart != null) {
            predicates.add(cb.greaterThan(event.get("eventDate"), rangeStart));
        } else {
            LocalDateTime now = LocalDateTime.now();
            predicates.add(cb.greaterThan(event.get("eventDate"), now));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThan(event.get("eventDate"), rangeEnd));
        }

        return entityManager
                .createQuery(query.select(event).where(cb.and(predicates.toArray(Predicate[]::new)))
                        .orderBy(cb.desc(event.get(sort.equals(Sort.EVENT_DATE) ? "eventDate" : "views"))))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Event> findAllAdminEvents(List<Long> userIds,
                                          List<State> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from,
                                          int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> event = query.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

        if (userIds != null && !userIds.isEmpty()) {
            predicates.add(cb.in(event.get("initiator").get("id")).value(userIds));
        }

        if (states != null && !states.isEmpty()) {
            predicates.add(cb.in(event.get("state")).value(states));
        }

        if (categories != null && !categories.isEmpty()) {
            predicates.add(cb.in(event.get("category").get("id")).value(categories));
        }

        if (rangeStart != null) {
            predicates.add(cb.greaterThan(event.get("eventDate"), rangeStart));
        } else {
            LocalDateTime now = LocalDateTime.now();
            predicates.add(cb.greaterThan(event.get("eventDate"), now));
        }

        if (rangeEnd != null) {
            predicates.add(cb.lessThan(event.get("eventDate"), rangeEnd));
        }

        return entityManager.createQuery(query.select(event).where(cb.and(predicates.toArray(Predicate[]::new))))
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
    }
}
