package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByEventIdAndEventUserId(int eventId, int userId);

    List<Request> findByUserId(int userId);

    List<Request> findByEventIdAndStatus(int eventId, RequestStatus status);

    List<Request> findByUserIdAndEventId(int userId, int eventId);
}