package org.example.hacarz.service;

import org.example.hacarz.entity.User;
import org.example.hacarz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern ALLOWED_CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z0-9.@_]+$");

    public Map<String, String> validateUserData(String login, String email, String password, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();

        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            errors.put("email", "Неверный формат email");
        }
        if (!password.equals(confirmPassword)) {
            errors.put("password", "Пароли не совпадают");
        }
        if (password.length() < 6) {
            errors.put("password", "Пароль меньше 6 символов");
        }
        if (login.length() < 4) {
            errors.put("login", "Логин меньше 4 символов");
        }
        if (!isAllowedCharacters(login)) {
            errors.put("login", "Логин содержит недопустимые символы");
        }
        if (!isAllowedCharacters(password)) {
            errors.put("password", "Пароль содержит недопустимые символы");
        }
        if (userRepository.existsByEmail(email)) {
            errors.put("email", "Email уже зарегистрирован");
        }
        if (userRepository.existsByLogin(login)) {
            errors.put("login", "Login уже зарегистрирован");
        }

        return errors;
    }

    public void saveUser(User user) {
        user.setReg_date(new Date());
        user.setRole("user");
        userRepository.save(user);
    }

    public Map<String, String> validatePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();

        if (!user.getPassword().equals(oldPassword)) {
            errors.put("oldPassword", "Неправильный старый пароль");
        }
        if (!newPassword.equals(confirmPassword)) {
            errors.put("newPassword", "Пароли не совпадают");
        }
        if (newPassword.length() < 6) {
            errors.put("newPassword", "Пароль должен содержать не менее 6 символов");
        }
        if (!isAllowedCharacters(newPassword)) {
            errors.put("newPassword", "Пароль содержит недопустимые символы");
        }

        if (errors.isEmpty()) {
            user.setPassword(newPassword);
            userRepository.save(user);
        }

        return errors;
    }

    private boolean isAllowedCharacters(String input) {
        return ALLOWED_CHARACTERS_PATTERN.matcher(input).matches();
    }

    public boolean authenticate(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            User user = userRepository.findByLogin(login);
            return login != null && !login.isEmpty() && password != null && !password.isEmpty()
                    && user.getPassword().equals(password);
        } else {
            return false;
        }
    }

    public User getUser(String login) {
        return userRepository.findByLogin(login);
    }

    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found with ID " + userId));
    }
}
