package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.service.UserService;

import java.util.Scanner;

/**
 * Контроллер для управления пользователями.
 * <p>
 * Поля:
 * - {@link UserService} userService — сервис для работы с пользователями.
 * - {@link Scanner} scanner — сканер для чтения пользовательского ввода.
 */
public class UserController {
    private final UserService userService;
    private final Scanner scanner;

    /**
     * Конструктор для инициализации UserController.
     *
     * @param userService Сервис для работы с пользователями.
     * @param scanner     Сканер для чтения пользовательского ввода.
     */
    public UserController(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    /**
     * Меню управления пользователями.
     */
    public void start() {
        System.out.println("Управление аккаунтом:");
        while (true) {
            System.out.println("1. Изменить логин");
            System.out.println("2. Изменить пароль");
            System.out.println("3. Вернуться в главное меню");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> changeUsername();
                    case "2" -> changePassword();
                    case "3" -> {
                        System.out.println("Возврат в главное меню.");
                        return;
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Метод для изменения логина пользователя.
     */
    private void changeUsername() {
        System.out.print("Введите новый логин: ");
        String newUsername = scanner.nextLine();
        userService.changeUsername(newUsername);
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
}