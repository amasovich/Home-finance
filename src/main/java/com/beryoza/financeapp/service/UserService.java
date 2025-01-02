package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления пользователями.
 * Отвечает за регистрацию, авторизацию и получение данных о пользователях.
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
