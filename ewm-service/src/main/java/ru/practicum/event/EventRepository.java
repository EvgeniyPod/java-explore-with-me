package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findByIdAndState(int eventId, EventState state);

    @Query("select e from Event e join e.category c " +
            "where (:rangeStart is null and :rangeEnd is null " +
            "or :rangeStart is null and :rangeEnd is not null and e.eventDate <= cast(:rangeEnd as timestamp) " +
            "or :rangeStart is not null and :rangeEnd is null and e.eventDate >= cast(:rangeStart as timestamp) " +
            "or :rangeStart is not null and :rangeEnd is not null " +
            "and e.eventDate between cast(:rangeStart as timestamp) and cast(:rangeEnd as timestamp)" +
            ") " +
            "and (:text is null " +
            "or (upper(e.annotation) like upper('%'||:text||'%') " +
            "or upper(e.description) like upper('%'||:text||'%')" +
            ")" +
            ") " +
            "and (:paid is null or e.paid = :paid) " +
            "and (:onlyAvailable = false or e.participantLimit > e.confirmedRequests) " +
            "and (:categories is null or c.id in :categories)")
    Page<Event> findPublicEvents(@Param("text") String text,
                                 @Param("paid") Boolean paid,
                                 @Param("rangeStart") String rangeStart,
                                 @Param("rangeEnd") String rangeEnd,
                                 @Param("onlyAvailable") boolean onlyAvailable,
                                 @Param("categories") List<Integer> categories,
                                 Pageable page);

    @Query("select e from Event e join e.category c join e.user u " +
            "where (:rangeStart is null and :rangeEnd is null " +
            "or :rangeStart is null and :rangeEnd is not null and e.eventDate <= cast(:rangeEnd as timestamp) " +
            "or :rangeStart is not null and :rangeEnd is null and e.eventDate >= cast(:rangeStart as timestamp) " +
            "or :rangeStart is not null and :rangeEnd is not null " +
            "and e.eventDate between cast(:rangeStart as timestamp) and cast(:rangeEnd as timestamp)" +
            ") " +
            "and (:states is null or cast(e.state as text) in :states) " +
            "and (:users is null or u.id in :users) " +
            "and (:categories is null or c.id in :categories)")
    Page<Event> findAdminEvents(@Param("users") List<Integer> users,
                                @Param("states") List<String> states,
                                @Param("categories") List<Integer> categories,
                                @Param("rangeStart") String rangeStart,
                                @Param("rangeEnd") String rangeEnd,
                                Pageable page);

    Page<Event> findByUserId(int userId, Pageable page);

    Optional<Event> findByIdAndUserId(int eventId, int userId);

    List<Event> findByCategoryId(int catId);
}