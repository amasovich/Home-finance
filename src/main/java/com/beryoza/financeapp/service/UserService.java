package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.util.DataValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления пользователями.
 * Добавлены проверки корректности вводимых данных.
 */
public class UserService {
    // Список всех зарегистрированных пользователей
    private List<User> users;

    /**
     * Конструктор. Инициализируем пустой список пользователей.
     */
    public UserService() {
        this.users = new ArrayList<>();
    }

    /**
     * Регистрация нового пользователя.
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
