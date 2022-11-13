package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.Request;
import ru.practicum.explore.model.RequestStatus;
import ru.practicum.explore.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findRequestByRequesterAndEvent(User user, Event event);

    Optional<Request> findRequestByIdAndRequesterId(long requestId, long userId);

    List<Request> findRequestsByEvent(Event event);

    List<Request> findRequestsByEventAndStatus(Event event, RequestStatus status);

    List<Request> findRequestsByRequesterId(long userId);
}
