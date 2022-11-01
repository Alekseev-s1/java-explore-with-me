package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.CategoryDto;
import ru.practicum.explore.dto.NewCategoryDto;
import ru.practicum.explore.exception.UnavailableOperationException;
import ru.practicum.explore.mapper.CategoryMapper;
import ru.practicum.explore.model.Category;
import ru.practicum.explore.model.Event;
import ru.practicum.explore.repository.CategoryRepository;
import ru.practicum.explore.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.exception.UnitNotFoundException.unitNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryDto getCategory(long categoryId) {
        return CategoryMapper.toCategoryDto(getCategoryById(categoryId));
    }

    public List<CategoryDto> getCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = getCategoryById(categoryDto.getId());
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Transactional
    public void deleteCategory(long categoryId) {
        Category category = getCategoryById(categoryId);
        List<Event> events = eventRepository.findEventsByCategory(category);

        if (!events.isEmpty()) {
            throw new UnavailableOperationException(
                    String.format("Нельзя удалить категорию, так как с ней связаны события с id %s", events)
            );
        } else {
            categoryRepository.delete(category);
        }
    }

    private Category getCategoryById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(unitNotFoundException("Категория с categoryId = {0} не найдена", categoryId));
    }
}
