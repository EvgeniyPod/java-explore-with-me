package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.DateFormatter;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.exception.InvalidEventStateOrDate;
import ru.practicum.exception.InvalidPathVariableException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    /**
     * Найти событие по идентификатору и состоянию.
     *
     * @param eventId идентификатор события
     * @return полное DTO события
     * @throws ObjectNotFoundException если событие не найдено
     */
    @Override
    public EventFullDto findByIdAndState(int eventId) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Событие с id " + eventId + " не найдено"));

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    /**
     * Найти публичные события с учетом фильтров и сортировки.
     *
     * @param text          текст для поиска в названии или аннотации
     * @param categories    массив идентификаторов категорий
     * @param paid          флаг оплачиваемости
     * @param rangeStart    начальная дата для фильтрации по дате события
     * @param rangeEnd      конечная дата для фильтрации по дате события
     * @param onlyAvailable флаг доступности только на текущую дату
     * @param sort          поле для сортировки (EVENT_DATE или VIEWS)
     * @param from          смещение страницы
     * @param size          размер страницы
     * @return список кратких DTO публичных событий
     * @throws InvalidPathVariableException если передан некорректный путь или параметр
     */
    @Override
    public List<EventShortDto> findPublicEvents(String text,
                                                Integer[] categories,
                                                Boolean paid,
                                                String rangeStart,
                                                String rangeEnd,
                                                boolean onlyAvailable,
                                                String sort,
                                                int from,
                                                int size) {
        Sort sortBy;
        if (sort.equalsIgnoreCase("EVENT_DATE")) {
            sortBy = Sort.by(Sort.Direction.DESC, "eventDate");
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            sortBy = Sort.by(Sort.Direction.DESC, "views");
        } else {
            throw new InvalidPathVariableException("Некорректный параметр сортировки: " + sort);
        }
        Pageable page = PageRequest.of(from, size, sortBy);

        if (categories != null) {
            List<Integer> categoriesInList = new ArrayList<>(Arrays.asList(categories));
            if (categoriesInList.stream().anyMatch((category -> category <= 0))) {
                throw new InvalidPathVariableException("Некорректное значение параметра categories");
            }
        }

        return EventMapper.toEventShortDto(eventRepository.findPublicEvents(text, paid, rangeStart, rangeEnd,
                onlyAvailable, categories == null ? null : Arrays.asList(categories), page));
    }

    /**
     * Обновить событие администратором.
     *
     * @param eventId  идентификатор события
     * @param eventDto данные для обновления
     * @return полное DTO обновленного события
     * @throws ObjectNotFoundException      если событие не найдено
     * @throws InvalidPathVariableException если передан некорректный путь или параметр
     * @throws InvalidEventStateOrDate      если состояние события некорректно для публикации
     */
    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest eventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Событие с id " + eventId + " не найдено"));

        if (eventDto.getEventDate() != null
                && LocalDateTime.now().plusHours(1).isAfter(DateFormatter.toLocalDateTime(eventDto.getEventDate()))) {
            throw new InvalidPathVariableException("Дата события слишком поздняя");
        }

        if (!event.getState().equals(EventState.PENDING) && !eventDto.getStateAction().toString().isBlank()) {
            throw new InvalidEventStateOrDate("Невозможно опубликовать событие, так как оно не в состоянии: PENDING");
        }

        if (eventDto.getStateAction() != null) {
            switch (eventDto.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    break;
            }
        }

        event.setPublishedOn(LocalDateTime.now());

        Category category = null;
        Integer categoryId = eventDto.getCategory();
        if (categoryId != null) {
            category = CategoryMapper.toCategory(categoryId, categoryService.getCategoryById(categoryId));
        }

        Location location = null;
        LocationDto locationDto = eventDto.getLocation();
        if (locationDto != null) {
            location = locationRepository.save(LocationMapper.toLocation(locationDto));
        }

        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(eventDto, event, category, location, event.getUser())));
    }

    /**
     * Найти события администратора с учетом фильтров.
     *
     * @param users      массив идентификаторов пользователей
     * @param states     массив состояний событий
     * @param categories массив идентификаторов категорий
     * @param rangeStart начальная дата для фильтрации по дате события
     * @param rangeEnd   конечная дата для фильтрации по дате события
     * @param from       смещение страницы
     * @param size       размер страницы
     * @return список полных DTO событий администратора
     * @throws InvalidPathVariableException если передан некорректный путь или параметр
     */
    @Override
    public List<EventFullDto> findAdminEvents(Integer[] users,
                                              String[] states,
                                              Integer[] categories,
                                              String rangeStart,
                                              String rangeEnd,
                                              int from,
                                              int size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));

        if (categories != null) {
            List<Integer> categoriesInList = new ArrayList<>(Arrays.asList(categories));
            if (categoriesInList.stream().anyMatch((category -> category <= 0))) {
                throw new InvalidPathVariableException("Некорректное значение параметра categories");
            }
        }

        if (users != null) {
            List<Integer> usersInList = new ArrayList<>(Arrays.asList(users));
            if (usersInList.stream().anyMatch((user -> user <= 0))) {
                throw new InvalidPathVariableException("Некорректное значение параметра users");
            }
        }

        return EventMapper.toEventFullDto(eventRepository
                .findAdminEvents(users == null ? null : Arrays.asList(users),
                        states == null ? null : Arrays.asList(states),
                        categories == null ? null : Arrays.asList(categories),
                        rangeStart, rangeEnd, page));
    }

    /**
     * Создать новое событие пользователем.
     *
     * @param userId   идентификатор пользователя
     * @param eventDto данные для создания события
     * @return полное DTO созданного события
     * @throws InvalidPathVariableException если передан некорректный путь или параметр
     */
    @Override
    public EventFullDto createEvent(int userId, NewEventDto eventDto) {
        LocalDateTime now = LocalDateTime.now();

        if (now.plusHours(2).isAfter(DateFormatter.toLocalDateTime(eventDto.getEventDate()))) {
            throw new InvalidPathVariableException("Дата события должна быть в будущем");
        }

        User user = userService.getUserById(userId);

        Category category = CategoryMapper.toCategory(eventDto.getCategory(), categoryService.getCategoryById(eventDto.getCategory()));

        Location location = null;
        if (eventDto.getLocation() != null) {
            location = locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
        }

        Event event = EventMapper.toEvent(eventDto, category, location);
        event.setState(EventState.PENDING);
        event.setUser(user);
        event.setCreatedOn(now);

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    /**
     * Найти события пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @param from   смещение страницы
     * @param size   размер страницы
     * @return список кратких DTO событий пользователя
     */
    @Override
    public List<EventShortDto> findByUserId(int userId, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return EventMapper.toEventShortDto(eventRepository.findByUserId(userId, page));
    }

    /**
     * Найти событие по идентификатору и идентификатору пользователя.
     *
     * @param eventId идентификатор события
     * @param userId  идентификатор пользователя
     * @return полное DTO события пользователя
     * @throws ObjectNotFoundException если событие не найдено
     */
    @Override
    public EventFullDto findByIdAndUserId(int eventId, int userId) {
        return EventMapper.toEventFullDto(eventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId,
                        "Событие с id " + eventId + ", созданное пользователем " + userId + " не найдено")));
    }

    /**
     * Обновить событие пользователем.
     *
     * @param userId   идентификатор пользователя
     * @param eventId  идентификатор события
     * @param eventDto данные для обновления
     * @return полное DTO обновленного события
     * @throws ObjectNotFoundException      если событие не найдено
     * @throws InvalidPathVariableException если передан некорректный путь или параметр
     * @throws InvalidEventStateOrDate      если состояние события некорректно для обновления
     */
    @Override
    public EventFullDto updateUserEvent(int userId, int eventId, UpdateEventUserRequest eventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Событие с id " + eventId + " не найдено"));

        if (eventDto.getEventDate() != null
                && LocalDateTime.now().plusHours(2).isAfter(DateFormatter.toLocalDateTime(eventDto.getEventDate()))) {
            throw new InvalidPathVariableException("Дата события слишком поздняя");
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new InvalidEventStateOrDate("Можно изменять только события в состоянии PENDING или CANCELED");
        }

        User user = userService.getUserById(userId);

        Integer catId = eventDto.getCategory();
        Category category = new Category();
        if (catId != null) {
            category = CategoryMapper.toCategory(catId, categoryService.getCategoryById(catId));
        }

        LocationDto locationDto = eventDto.getLocation();
        Location location = new Location();
        if (locationDto != null) {
            location = locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
        }

        EventStateActionUser eventStateActionUser = eventDto.getStateAction();
        if (eventStateActionUser != null) {
            if (eventStateActionUser.equals(EventStateActionUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (eventStateActionUser.equals(EventStateActionUser.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }

        return EventMapper.toEventFullDto(eventRepository
                .save(EventMapper.toEvent(eventDto, event, category, location, user)));
    }

    /**
     * Получить событие по его идентификатору.
     *
     * @param eventId идентификатор события
     * @return объект события
     * @throws ObjectNotFoundException если событие не найдено
     */
    @Override
    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Событие с id " + eventId + " не найдено"));
    }
}
