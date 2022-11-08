package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.Comment;
import ru.practicum.explore.model.CommentState;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(long eventId);

    List<Comment> findAllByEventIdAndState(long eventId, CommentState state);
}

