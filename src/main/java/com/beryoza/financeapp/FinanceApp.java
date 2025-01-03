package com.beryoza.financeapp;

import com.beryoza.financeapp.controller.BudgetController;
import com.beryoza.financeapp.controller.TransactionController;
import com.beryoza.financeapp.controller.UserController;
import com.beryoza.financeapp.controller.WalletController;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.repository.UserRepository;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.service.BudgetService;
import com.beryoza.financeapp.service.UserService;
import com.beryoza.financeapp.service.WalletService;

import java.util.Scanner;

/**
 * Главный класс приложения "Домашние финансы".
 * Отвечает за инициализацию компонентов и запуск приложения.
 */
public class FinanceApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Инициализация репозиториев
        UserRepository userRepository = new UserRepository();
        WalletRepository walletRepository = new WalletRepository();

        // Инициализация сервисов
        UserService userService = new UserService(userRepository);
        WalletService walletService = new WalletService(walletRepository);
        BudgetService budgetService = new BudgetService();

        // Авторизация пользователя
        UserController userController = new UserController(userService, scanner);
        userController.start();
        User currentUser = userService.getCurrentUser();

        if (currentUser == null) {
            System.out.println("Авторизация не выполнена. Завершение работы приложения.");
            return;
        }

        // Инициализация контроллеров для текущего пользователя
        WalletController walletController = new WalletController(walletService, currentUser, scanner);
        BudgetController budgetController = new BudgetController(budgetService, currentUser, scanner);
        TransactionController transactionController = new TransactionController(walletService, currentUser, scanner);

        // Запуск главного меню
        mainMenu(scanner, userController, walletController, budgetController, transactionController);
    }

    /**
     * Главное меню приложения.
     *
     * @param scanner               Сканер для пользовательского ввода.
     * @param userController        Контроллер управления пользователями.
     * @param walletController      Контроллер управления кошельками.
     * @param budgetController      Контроллер управления категориями и бюджетами.
     * @param transactionController Контроллер управления транзакциями.
     */
    private static void mainMenu(Scanner scanner, UserController userController, WalletController walletController,
                                 BudgetController budgetController, TransactionController transactionController) {
        while (true) {
            System.out.println("Добро пожаловать в приложение \"Домашние финансы\"!");
            System.out.println("1. Управление пользователями");
            System.out.println("2. Управление кошельками");
            System.out.println("3. Управление категориями и бюджетами");
            System.out.println("4. Управление транзакциями");
            System.out.println("5. Выход");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> userController.start();
                case "2" -> walletController.start();
                case "3" -> budgetController.start();
                case "4" -> transactionController.start();
                case "5" -> {
                    System.out.println("Спасибо за использование приложения \"Домашние финансы\"! До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }
}