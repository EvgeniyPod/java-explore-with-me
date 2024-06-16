package ru.practicum.user;

import org.springframework.data.domain.Page;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    /**
     * Преобразует объект NewUserRequest в объект User.
     *
     * @param newUserRequest данные нового пользователя
     * @return объект User
     */
    public static User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    /**
     * Преобразует объект User в объект UserDto.
     *
     * @param user пользователь
     * @return объект UserDto
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Преобразует объект User в объект UserShortDto (краткая информация о пользователе).
     *
     * @param user пользователь
     * @return объект UserShortDto
     */
    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    /**
     * Преобразует объект Page<User> (страница пользователей) в список UserDto.
     *
     * @param users страница пользователей
     * @return список объектов UserDto
     */
    public static List<UserDto> toUserDto(Page<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
