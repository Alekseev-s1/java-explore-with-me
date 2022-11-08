package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.CommentRequestDto;
import ru.practicum.explore.dto.CommentResponseDto;
import ru.practicum.explore.exception.UserIsNotParticipantException;
import ru.practicum.explore.exception.WrongOwnerException;
import ru.practicum.explore.exception.WrongStateException;
import ru.practicum.explore.mapper.CommentMapper;
import ru.practicum.explore.model.*;
import ru.practicum.explore.repository.CommentRepository;
import ru.practicum.explore.repository.EventRepository;
import ru.practicum.explore.repository.RequestRepository;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public List<CommentResponseDto> getComments(long eventId, boolean isPublished) {
        Event event = getEventById(eventId);
        List<Comment> comments;

        if (isPublished) {
            comments = commentRepository.findAllByEventIdAndState(eventId, CommentState.PUBLISHED);
        } else {
            comments = commentRepository.findAllByEventId(eventId);
        }

        return comments.stream()
                .map(CommentMapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto addComment(long userId, long eventId, CommentRequestDto commentRequestDto) {
        User author = getUserById(userId);
        Event event = getEventById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongStateException("Добавить комментарий можно только к опубликованному событию");
        }
        if (event.isRequestModeration()) {
            Optional<Request> request = requestRepository.findRequestByRequesterAndEvent(author, event);
            if (!(request.isPresent() && request.get().getStatus().equals(RequestStatus.CONFIRMED))) {
                throw new UserIsNotParticipantException("Добавить комментарий может только пользователь с " +
                        "подтвержденным запросом на участие");
            }
        }

        Comment comment = CommentMapper.toComment(commentRequestDto, author, event);
        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDto updateCommentByUser(long userId, long eventId, long commentId, CommentRequestDto commentRequestDto) {
        User author = getUserById(userId);
        Event event = getEventById(eventId);
        Comment comment = getCommentById(commentId);

        if (comment.getAuthor().getId() != userId) {
            throw new WrongOwnerException(
                    String.format("Пользователь с id=%d не является автором комментария с id=%d", userId, commentId)
            );
        }
        if (comment.getEvent().getId() != eventId) {
            throw new WrongOwnerException(
                    String.format("Комментарий с id=%d не относится к событию с id=%d", commentId, eventId)
            );
        }
        if (event.isRequestModeration()) {
            Optional<Request> request = requestRepository.findRequestByRequesterAndEvent(author, event);
            if (!(request.isPresent() && request.get().getStatus().equals(RequestStatus.CONFIRMED))) {
                throw new UserIsNotParticipantException("Добавить комментарий может только пользователь с " +
                        "подтвержденным запросом на участие");
            }
        }

        comment.setText(commentRequestDto.getText());
        return CommentMapper.toCommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateCommentByAdmin(long eventId, long commentId, CommentRequestDto commentRequestDto) {
        Event event = getEventById(eventId);
        Comment comment = getCommentById(commentId);

        if (comment.getEvent().getId() != eventId) {
            throw new WrongOwnerException(
                    String.format("Комментарий с id=%d не относится к событию с id=%d", commentId, eventId)
            );
        }

        comment.setText(commentRequestDto.getText());
        return CommentMapper.toCommentResponseDto(comment);
    }

    @Transactional
    public void deleteCommentByUser(long userId, long eventId, long commentId) {
        Comment comment = getCommentById(commentId);

        if (comment.getAuthor().getId() != userId) {
            throw new WrongOwnerException(
                    String.format("Пользователь с id=%d не является автором комментария с id=%d", userId, commentId)
            );
        }
        if (comment.getEvent().getId() != eventId) {
            throw new WrongOwnerException(
                    String.format("Комментарий с id=%d не относится к событию с id=%d", commentId, eventId)
            );
        }

        comment.setState(CommentState.DELETED);
    }

    @Transactional
    public void deleteCommentByAdmin(long eventId, long commentId) {
        Comment comment = getCommentById(commentId);

        if (comment.getEvent().getId() != eventId) {
            throw new WrongOwnerException(
                    String.format("Комментарий с id=%d не относится к событию с id=%d", commentId, eventId)
            );
        }

        comment.setState(CommentState.DELETED);
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(unitNotFoundException("Пользователь с id={0} не найден", userId));
    }

    private Event getEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(unitNotFoundException("Событие с id={0} не найдено", eventId));
    }

    private Comment getCommentById(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(unitNotFoundException("Комментарий с id={0} не найден", commentId));
    }
}
