package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventService;
import ru.practicum.event.EventState;
import ru.practicum.exception.InvalidEventStateOrDate;
import ru.practicum.exception.InvalidParticipationRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final EventService eventService;

    private final EventRepository eventRepository;

    /**
     * Создает запрос на участие пользователя в событии.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return созданный ParticipationRequestDto
     * @throws InvalidParticipationRequest если запрос некорректен
     */
    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        if (eventRepository.findByIdAndUserId(eventId, userId).isPresent()) {
            throw new InvalidParticipationRequest("Неверный запрос на участие в событии " +
                    eventId + " от инициатора " + userId);
        }

        Event event = eventService.getEventById(eventId);

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= event.getConfirmedRequests()
                || !event.getState().equals(EventState.PUBLISHED)
                || requestRepository.findByUserIdAndEventId(userId, eventId).size() > 0) {
            throw new InvalidParticipationRequest("Неверный запрос на участие в событии " +
                    eventId + " от пользователя " + userId);
        }

        User user = userService.getUserById(userId);

        RequestStatus status = RequestStatus.PENDING;
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        ParticipationRequestDto request = ParticipationRequestDto.builder()
                .status(status)
                .build();

        return RequestMapper.toRequestDto(requestRepository
                .save(RequestMapper.toRequest(request, user, event, LocalDateTime.now())));
    }

    /**
     * Отменяет запрос на участие пользователя в событии.
     *
     * @param userId    идентификатор пользователя
     * @param requestId идентификатор запроса на участие
     * @return отмененный ParticipationRequestDto
     * @throws ObjectNotFoundException если запрос не найден
     */
    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(requestId, "Запрос с идентификатором " + requestId + " не найден"));

        if (requestRepository.findByUserId(userId).isEmpty()) {
            throw new ObjectNotFoundException(requestId, "Пользователь " + userId + " не имеет доступа к запросу " + requestId);
        }

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    /**
     * Возвращает список запросов пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список ParticipationRequestDto
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(int userId) {
        return RequestMapper.toRequestDto(requestRepository.findByUserId(userId));
    }

    /**
     * Возвращает список запросов пользователя по идентификатору события.
     *
     * @param userId  идентификатор пользователя
     * @param eventId идентификатор события
     * @return список ParticipationRequestDto
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findByUserIdAndEventId(int userId, int eventId) {
        return RequestMapper.toRequestDto(requestRepository.findByEventIdAndEventUserId(eventId, userId));
    }

    /**
     * Обновляет статусы запросов на участие в событии.
     *
     * @param userId   идентификатор пользователя
     * @param eventId  идентификатор события
     * @param requests запросы на обновление статусов
     * @return результат обновления статусов
     * @throws InvalidEventStateOrDate если состояние события не позволяет выполнить обновление
     */
    @Override
    public EventRequestStatusUpdateResult updateEventRequests(int userId,
                                                              int eventId,
                                                              EventRequestStatusUpdateRequest requests) {
        boolean isNeedAllToConfirm = false;
        boolean isNeedAllToCancel = false;

        Event event = eventService.getEventById(eventId);

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new InvalidEventStateOrDate("Достигнут лимит участников");
        }

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            isNeedAllToConfirm = true;
        }

        List<Request> requestsToUpdate = new ArrayList<>();
        List<Request> requestList = new ArrayList<>(requestRepository.findAllById(requests.getRequestIds()));
        for (Request request : requestList) {
            if (request.getStatus().equals(RequestStatus.PENDING)) {
                requestsToUpdate.add(request);
            }
        }

        for (Request request : requestsToUpdate) {
            if (isNeedAllToCancel) {
                request.setStatus(RequestStatus.CANCELED);
            } else if (requests.getStatus().equals(RequestStatus.CONFIRMED)) {
                request.setStatus(RequestStatus.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                event = eventRepository.save(event);
                if (!isNeedAllToConfirm && event.getParticipantLimit() <= event.getConfirmedRequests()) {
                    isNeedAllToCancel = true;
                }
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
        }

        requestRepository.saveAll(requestsToUpdate);

        List<ParticipationRequestDto> confirmedRequests = RequestMapper.toRequestDto(requestRepository
                .findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        List<ParticipationRequestDto> rejectedRequests = RequestMapper.toRequestDto(requestRepository
                .findByEventIdAndStatus(eventId, RequestStatus.REJECTED));

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}
