package ru.practicum.explore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {
    @NotBlank(message = "title can't be null or blank")
    private String title;
    private Boolean pinned = false;
    private List<Integer> events;
}
