package ru.practicum.explore.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @Autowired
    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Get compilations by params: " +
                        "pinned {}, " +
                        "from {}, " +
                        "size {}",
                pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilation(@PathVariable long compilationId) {
        log.info("Get compilation by id {}", compilationId);
        return compilationService.getCompilation(compilationId);
    }
}
