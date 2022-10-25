package ru.practicum.explore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.CompilationDto;
import ru.practicum.explore.dto.NewCompilationDto;
import ru.practicum.explore.mapper.CompilationMapper;
import ru.practicum.explore.model.Compilation;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.repository.CompilationRepository;
import ru.practicum.explore.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationService(CompilationRepository compilationRepository,
                              EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    public CompilationDto getCompilation(long compId) {
        return CompilationMapper.toCompilationDto(getCompilationById(compId));
    }

    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findCompilationsByPinned(pinned, PageRequest.of(from / size, size));
        } else {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size)).getContent();
        }

        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = getEvents(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        events.forEach(event -> event.getCompilations().add(compilation));
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Transactional
    public void addEventToCompilation(long compId, long eventId) {
        Compilation compilation = getCompilationById(compId);
        Event event = getEvent(eventId);
        event.getCompilations().add(compilation);
        compilation.getEvents().add(event);
    }

    @Transactional
    public void pinCompilation(long compId) {
        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(true);
    }

    @Transactional
    public void unpinCompilation(long compId) {
        Compilation compilation = getCompilationById(compId);
        compilation.setPinned(false);
    }

    @Transactional
    public void deleteCompilation(long compId) {
        Compilation compilation = getCompilationById(compId);
        compilationRepository.delete(compilation);
    }

    @Transactional
    public void deleteEventFromCompilation(long compId, long eventId) {
        Compilation compilation = getCompilationById(compId);
        Event event = getEvent(eventId);
        event.getCompilations().remove(compilation);
        compilation.getEvents().remove(event);
    }

    private Compilation getCompilationById(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(unitNotFoundException("Подборка с id={0} не найдена", compId));
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(unitNotFoundException("Событие с id={0} не найдено", eventId));
    }

    private List<Event> getEvents(List<Long> ids) {
        return eventRepository.findAllById(ids);
    }
}
