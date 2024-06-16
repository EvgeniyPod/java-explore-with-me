package ru.practicum.place;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.place.dto.NewPlaceDto;
import ru.practicum.place.dto.PlaceDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    /**
     * Создать новое место.
     *
     * @param placeDto данные нового места
     * @return DTO созданного места
     */
    @Override
    @Transactional
    public PlaceDto createPlace(NewPlaceDto placeDto) {
        Place place = PlaceMapper.toPlace(placeDto);
        return PlaceMapper.toPlaceDto(placeRepository.save(place));
    }

    /**
     * Обновить информацию о месте.
     *
     * @param placeId  идентификатор места для обновления
     * @param placeDto новые данные о месте
     * @return обновленное DTO места
     * @throws ObjectNotFoundException если место не найдено
     */
    @Override
    @Transactional
    public PlaceDto updatePlace(int placeId, PlaceDto placeDto) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ObjectNotFoundException(placeId,
                        "Место с id " + placeId + " не найдено"));
        return PlaceMapper.toPlaceDto(placeRepository.save(PlaceMapper.toPlace(place, placeDto)));
    }

    /**
     * Получить список мест
     *
     * @param from начальная позиция
     * @param size количество записей
     * @return список DTO мест
     */
    @Override
    @Transactional(readOnly = true)
    public List<PlaceDto> getPlaces(int from, int size) {
        final Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return PlaceMapper.toPlaceDto(placeRepository.findAll(page));
    }

    /**
     * Удалить место по его идентификатору.
     *
     * @param placeId идентификатор места для удаления
     * @throws ObjectNotFoundException если место не найдено
     */
    @Override
    @Transactional
    public void deletePlace(int placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ObjectNotFoundException(placeId,
                        "Место с id " + placeId + " не найдено"));

        placeRepository.delete(place);
    }

    /**
     * Получить место по его идентификатору.
     *
     * @param placeId идентификатор места
     * @return DTO места
     * @throws ObjectNotFoundException если место не найдено
     */
    @Override
    @Transactional(readOnly = true)
    public PlaceDto getPlaceById(int placeId) {
        return PlaceMapper.toPlaceDto(placeRepository.findById(placeId)
                .orElseThrow(() -> new ObjectNotFoundException(placeId,
                        "Место с id " + placeId + " не найдено")));
    }
}
