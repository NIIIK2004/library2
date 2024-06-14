package com.example.library.validation;

import com.example.library.impl.UserImpl;
import com.example.library.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.validation.constraints.Size;
import org.springframework.validation.BindingResult;

@Component
@AllArgsConstructor
public class RegistrationValidator {
    private final UserImpl userImpl;

    public void validate(@Size(max = 30) User user, BindingResult result) {
        User existingUser = userImpl.findByUsername(user.getUsername());

        if (existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) {
            result.rejectValue("username", null, "Пользователь с таким логином уже зарегистрирован!");
        }

        if (user.getUsername().isEmpty()) {
            result.rejectValue("username", null, "Поле 'Логин' не может быть пустым!");
        }

        if (user.getUsername().length() > 30) {
            result.rejectValue("username", null, "Поле 'Логин' не может превышать более 30 символов!");
        }

        if (user.getPassword().length() < 6) {
            result.rejectValue("password", null, "Пароль должен содержать не менее 6 символов!");
        }

        if (user.getName().length() > 30) {
            result.rejectValue("name", null, "Поле 'Имя' не может превышать более 30 символов!");
        }

        if (user.getName().isEmpty()) {
            result.rejectValue("name", null, "Поле 'Имя' не может быть пустым!");
        }

        if (user.getName().matches(".*\\d.*")) {
            result.rejectValue("name", null, "Поле 'Имя' не может содержать в себе цифры");
        }

        if (user.getEmail().isEmpty()) {
            result.rejectValue("email", null, "Поле 'Почта' не может быть пустым!");
        }
    }
}
