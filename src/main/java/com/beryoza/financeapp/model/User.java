package com.beryoza.financeapp.model;

/**
 * Класс для представления пользователя.
 * Хранит информацию о логине и пароле.
 * Данные о кошельках пользователя теперь хранятся отдельно.
 */
public class User {
    private String username; // Логин пользователя
    private String password; // Пароль пользователя

    /**
     * Конструктор для создания пользователя.
     *
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Получить логин пользователя.
     *
     * @return Логин пользователя.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Установить новый логин пользователя.
     *
     * @param username Новый логин пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получить пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Установить новый пароль пользователя.
     *
     * @param password Новый пароль пользователя.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Получить строковое представление объекта User.
     *
     * @return Информация о пользователе в текстовом формате.
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}