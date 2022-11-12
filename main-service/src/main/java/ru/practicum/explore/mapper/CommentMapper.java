package ru.practicum.explore.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.CommentRequestDto;
import ru.practicum.explore.dto.CommentResponseDto;
import ru.practicum.explore.dto.UserDto;
import ru.practicum.explore.model.Comment;
import ru.practicum.explore.model.CommentState;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static Comment toComment(CommentRequestDto commentRequestDto, User author, Event event) {
        return Comment.builder()
                .text(commentRequestDto.getText())
                .author(author)
                .event(event)
                .createdOn(LocalDateTime.now())
                .state(CommentState.PUBLISHED)
                .build();
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .author(comment.getAuthor() == null ?
                        UserDto.builder().name("Anonymous").build() :
                        UserMapper.toUserDto(comment.getAuthor()))
                .state(comment.getState())
                .createdOn(comment.getCreatedOn())
                .build();
    }
}
