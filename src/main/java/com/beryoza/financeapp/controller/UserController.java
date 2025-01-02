package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.service.UserService;
import java.util.Scanner;

/**
 * Контроллер для управления пользователями.
 */
public class UserController {
    private final UserService userService; // Сервис для управления пользователями
    private final Scanner scanner; // Сканер для чтения ввода с консоли

    /**
     * Конструктор. Инициализирует контроллер с сервисом пользователей.
     *
     * @param userService Сервис для работы с пользователями.
     * @param scanner Сканер для чтения пользовательского ввода.
     */
    public UserController(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    /**
     * Запускает главный цикл меню для управления пользователями.
     */
    public void start() {
        System.out.println("Добро пожаловать! Выберите действие:");
        while (true) {
            System.out.println("1. Зарегистрироваться");
            System.out.println("2. Авторизоваться");
            System.out.println("3. Изменить логин");
            System.out.println("4. Изменить пароль");
            System.out.println("5. Выход");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> changeUsername();
                case "4" -> changePassword();
                case "5" -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Метод для регистрации нового пользователя.
     */
    private void register() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        userService.registerUser(username, password);
    }

    /**
     * Метод для авторизации пользователя.
     */
    private void login() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        userService.authenticateUser(username, password);
    }

    /**
     * Метод для изменения пароля пользователя.
     */
    private void changePassword() {
        System.out.print("Введите старый пароль: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Введите новый пароль: ");
        String newPassword = scanner.nextLine();

        userService.changePassword(oldPassword, newPassword);
    }

    /**
     * Метод для изменения логина пользователя.
     */
    private void changeUsername() {
        System.out.print("Введите старый логин: ");
        String oldUsername = scanner.nextLine();
        System.out.print("Введите новый логин: ");
        String newUsername = scanner.nextLine();

        userService.changeUsername(oldUsername, newUsername);
    }
}