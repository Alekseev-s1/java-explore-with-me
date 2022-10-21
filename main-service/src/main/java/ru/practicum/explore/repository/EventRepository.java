package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
