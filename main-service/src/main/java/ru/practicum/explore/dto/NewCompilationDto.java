package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
public class NewCompilationDto {
    @NotBlank(message = "title can't be null or blank")
    private String title;
    private boolean pinned = false;
    private List<Long> events;
}
