package com.beryoza.financeapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс для представления пользователя.
 * Хранит информацию о логине и пароле.
 * Данные о кошельках пользователя хранятся отдельно.
 */
public class User {
    private String username; // Логин пользователя
    private String password; // Пароль пользователя

    /**
     * Конструктор для десериализации Jackson.
     *
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     */
    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("password") String password) {
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
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}