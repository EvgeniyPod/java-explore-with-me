package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    /** Создание компиляции */
    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        addEventsInCompilation(compilation, compilationDto.getEvents());
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    /** Обновление компиляции по идентификатору */
    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest compilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId, "Компиляция с id " + compId + " не найдена"));

        CompilationMapper.toCompilation(compilationDto, compilation);
        addEventsInCompilation(compilation, compilationDto.getEvents());
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    /** Удаление компиляции по идентификатору */
    @Override
    public void deleteCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId,
                        "Компиляция с id " + compId + " не найдена"));
        compilation.getEvents().forEach(compilation::removeEvent);

        compilationRepository.deleteById(compId);
    }

    /** Получение списка компиляций с возможностью фильтрации */
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {

        PageRequest pageRequest = PageRequest.of(from, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageRequest).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        }

        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    /** Получение компиляции по идентификатору */
    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId,
                        "Компиляция с id " + compId + " не найдена"));
        return CompilationMapper.toCompilationDto(compilation);
    }

    /** Приватный метод для добавления событий к компиляции */
    @Transactional(readOnly = true)
    public Compilation addEventsInCompilation(Compilation compilation, Set<Integer> eventsId) {
        compilation.setEvents(new HashSet<>());
        if (eventsId != null && !eventsId.isEmpty()) {
            for (Event event : eventRepository.findAllById(eventsId)) {
                compilation.addEvent(event);
            }
        }
        return compilation;
    }
}
