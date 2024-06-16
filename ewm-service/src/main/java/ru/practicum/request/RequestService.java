package ru.practicum.request;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);

    List<ParticipationRequestDto> getRequests(int userId);

    List<ParticipationRequestDto> findByUserIdAndEventId(int userId, int eventId);

    EventRequestStatusUpdateResult updateEventRequests(int userId, int eventId, EventRequestStatusUpdateRequest requests);
}