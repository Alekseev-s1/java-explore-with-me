package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CommentRequestDto {
    @NotBlank(message = "text can't be null or blank")
    @Size(max = 7000, message = "text should be less than 7000 letters")
    private String text;
}
