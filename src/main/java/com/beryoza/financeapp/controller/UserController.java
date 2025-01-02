package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.UserService;

import java.util.Scanner;

/**
 * Контроллер для управления пользователями.
 * Обрабатывает команды для регистрации и авторизации.
 */
public class UserController {
    private final UserService userService;
    private final Scanner scanner;

    /**
     * Конструктор. Инициализирует сервис пользователей и ввод через консоль.
     *
     * @param userService Сервис для управления пользователями.
     */
    public UserController(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает интерфейс для работы с пользователями.
     */
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

    /**
     * Регистрация нового пользователя.
     */
    private void register() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        userService.registerUser(username, password);
        System.out.println("Пользователь успешно зарегистрирован.");
    }

    /**
     * Авторизация пользователя.
     */
    private void login() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        User user = userService.authenticateUser(username, password);
        if (user != null) {
            System.out.println("Добро пожаловать, " + user.getUsername() + "!");
            // Здесь можно добавить вызов других контроллеров, например, WalletController
        } else {
            System.out.println("Неверный логин или пароль.");
        }
    }
}