package com.beryoza.financeapp;

import com.beryoza.financeapp.controller.BudgetController;
import com.beryoza.financeapp.controller.TransactionController;
import com.beryoza.financeapp.controller.UserController;
import com.beryoza.financeapp.controller.WalletController;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.repository.CategoryRepository;
import com.beryoza.financeapp.repository.UserRepository;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.service.BudgetService;
import com.beryoza.financeapp.service.UserService;
import com.beryoza.financeapp.service.WalletService;

import java.util.Scanner;

/**
 * Главный класс приложения "Домашние финансы".
 * Отвечает за инициализацию компонентов и запуск главного меню приложения.
 */
public class FinanceApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Инициализация репозиториев
        UserRepository userRepository = new UserRepository();
        WalletRepository walletRepository = new WalletRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        // Инициализация сервисов
        UserService userService = new UserService(userRepository, walletRepository, categoryRepository);
        WalletService walletService = new WalletService(walletRepository);
        BudgetService budgetService = new BudgetService(walletRepository, categoryRepository);

        // Инициализация контроллеров
        UserController userController = new UserController(userService, scanner);

        // Запуск главного меню
        mainMenu(scanner, userController, userService, walletService, budgetService);
    }

    /**
     * Главное меню приложения.
     *
     * @param scanner         Сканер для пользовательского ввода.
     * @param userController  Контроллер управления пользователями.
     * @param userService     Сервис для работы с пользователями.
     * @param walletService   Сервис для работы с кошельками.
     * @param budgetService   Сервис для работы с бюджетом.
     */
    private static void mainMenu(Scanner scanner, UserController userController, UserService userService,
                                 WalletService walletService, BudgetService budgetService) {
        while (true) {
            System.out.println("Добро пожаловать в приложение \"Домашние финансы\"!");
            System.out.println("1. Войти");
            System.out.println("2. Зарегистрироваться");
            System.out.println("3. Выйти");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        User currentUser = authenticate(scanner, userService);
                        if (currentUser != null) {
                            System.out.println("Авторизация успешна. Добро пожаловать, " + currentUser.getUsername() + "!");
                            manageUserSession(scanner, currentUser, userController, walletService, budgetService);
                        } else {
                            System.out.println("Авторизация не выполнена. Проверьте логин и пароль.");
                        }
                    }
                    case "2" -> register(scanner, userService);
                    case "3" -> {
                        System.out.println("Спасибо за использование приложения \"Домашние финансы\"! До свидания!");
                        return;
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Метод для авторизации пользователя.
     *
     * @param scanner     Сканер для ввода данных.
     * @param userService Сервис для работы с пользователями.
     * @return Авторизованный пользователь или null, если авторизация не удалась.
     */
    private static User authenticate(Scanner scanner, UserService userService) {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (userService.authenticateUser(username, password)) {
            return userService.getCurrentUser();
        }
        return null;
    }

    /**
     * Метод для регистрации нового пользователя.
     *
     * @param scanner     Сканер для ввода данных.
     * @param userService Сервис для работы с пользователями.
     */
    private static void register(Scanner scanner, UserService userService) {
        try {
            System.out.print("Введите логин: ");
            String username = scanner.nextLine();
            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();

            userService.registerUser(username, password);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Меню после авторизации пользователя.
     *
     * @param scanner       Сканер для пользовательского ввода.
     * @param currentUser   Авторизованный пользователь.
     * @param userController Контроллер управления пользователями.
     * @param walletService Сервис для работы с кошельками.
     * @param budgetService Сервис для работы с бюджетом.
     */
    private static void manageUserSession(Scanner scanner, User currentUser, UserController userController,
                                          WalletService walletService, BudgetService budgetService) {
        WalletController walletController = new WalletController(walletService, currentUser, scanner);
        BudgetController budgetController = new BudgetController(budgetService, currentUser, scanner);
        TransactionController transactionController = new TransactionController(walletService, currentUser, scanner);

        while (true) {
            System.out.println("Меню пользователя:");
            System.out.println("1. Управление кошельками");
            System.out.println("2. Управление категориями и бюджетами");
            System.out.println("3. Управление транзакциями");
            System.out.println("4. Управление аккаунтом");
            System.out.println("5. Выйти из аккаунта");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> walletController.start();
                    case "2" -> budgetController.start();
                    case "3" -> transactionController.start();
                    case "4" -> userController.start();
                    case "5" -> {
                        System.out.println("Выход из аккаунта...");
                        return;
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }
}