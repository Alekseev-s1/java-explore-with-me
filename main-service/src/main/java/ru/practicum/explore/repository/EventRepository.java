package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.EventState;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByIdAndState(long id, EventState state);

    Optional<Event> findEventByIdAndInitiatorId(long eventId, long userId);

    List<Event> findEventsByCategory(Category category);

    List<Event> findEventsByInitiatorId(long userId, Pageable pageable);
}
