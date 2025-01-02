package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.util.DataValidator;
import com.beryoza.financeapp.repository.UserRepository;

import java.util.List;

/**
 * Сервис для управления пользователями.
 * Интегрирован с UserRepository для работы с файлами.
 */
public class UserService {
    // Список всех зарегистрированных пользователей
    private List<User> users;

    // Поле для работы с репозиторием пользователей
    private final UserRepository userRepository;

    /**
     * Конструктор. Инициализируем репозиторий и загружаем пользователей.
     *
     * @param userRepository Репозиторий для работы с данными пользователей.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.users = userRepository.loadUsers(); // Загрузка данных из файла
    }

    /**
     * Регистрация нового пользователя.
     * Данные автоматически сохраняются в файл.
     *
     * @param username Логин нового пользователя.
     * @param password Пароль нового пользователя.
     */
    public void registerUser(String username, String password) {
        if (!DataValidator.isNonEmptyString(username)) {
            throw new IllegalArgumentException("Логин не может быть пустым.");
        }
        if (!DataValidator.isValidLogin(username)) {
            throw new IllegalArgumentException("Некорректный логин. Используйте буквы, цифры или '_'. Длина: 4-20 символов.");
        }
        if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует.");
        }
        if (!DataValidator.isValidPassword(password)) {
            throw new IllegalArgumentException("Пароль должен быть минимум 6 символов.");
        }

        User newUser = new User(username, password);
        users.add(newUser);
        userRepository.saveUsers(users); // Сохраняем данные в файл
    }

    /**
     * Авторизация пользователя.
     *
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     * @return Объект пользователя, если авторизация успешна; иначе null.
     */
    public User authenticateUser(String username, String password) {
        if (!DataValidator.isNonEmptyString(username)) {
            throw new IllegalArgumentException("Логин не может быть пустым.");
        }
        if (!DataValidator.isNonEmptyString(password)) {
            throw new IllegalArgumentException("Пароль не может быть пустым.");
        }

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Получить список всех зарегистрированных пользователей.
     *
     * @return Список пользователей.
     */
    public List<User> getAllUsers() {
        return users;
    }
}