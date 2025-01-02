package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.UserService;
import com.beryoza.financeapp.util.DataValidator;

import java.util.Scanner;

/**
 * Контроллер для управления пользователями.
 * Добавлены проверки вводимых данных.
 */
public class UserController {
    private final UserService userService;
    private final Scanner scanner;

    public UserController(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Добро пожаловать! Выберите действие:");
        while (true) {
            System.out.println("1. Зарегистрироваться");
            System.out.println("2. Авторизоваться");
            System.out.println("3. Выход");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void register() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        if (!DataValidator.isValidLogin(username)) {
            System.out.println("Некорректный логин. Используйте буквы, цифры или '_'. Длина: 4-20 символов.");
            return;
        }

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        if (!DataValidator.isValidPassword(password)) {
            System.out.println("Пароль должен быть минимум 6 символов.");
            return;
        }

        userService.registerUser(username, password);
        System.out.println("Пользователь успешно зарегистрирован.");
    }

    private void login() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        if (!DataValidator.isNonEmptyString(username)) {
            System.out.println("Логин не может быть пустым.");
            return;
        }

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        if (!DataValidator.isNonEmptyString(password)) {
            System.out.println("Пароль не может быть пустым.");
            return;
        }

        User user = userService.authenticateUser(username, password);
        if (user != null) {
            System.out.println("Добро пожаловать, " + user.getUsername() + "!");
            // Вызов других контроллеров, если нужно
        } else {
            System.out.println("Неверный логин или пароль.");
        }
    }
}