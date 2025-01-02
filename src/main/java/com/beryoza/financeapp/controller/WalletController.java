package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.WalletService;

import java.util.Scanner;

/**
 * Контроллер для управления кошельками пользователя.
 */
public class WalletController {
    private final WalletService walletService;
    private final User user; // Текущий авторизованный пользователь
    private final Scanner scanner;

    /**
     * Конструктор для инициализации WalletController.
     *
     * @param walletService Сервис для работы с кошельками.
     * @param user          Авторизованный пользователь.
     * @param scanner       Сканер для чтения пользовательского ввода.
     */
    public WalletController(WalletService walletService, User user, Scanner scanner) {
        this.walletService = walletService;
        this.user = user;
        this.scanner = scanner;
    }

    /**
     * Запуск главного меню управления кошельками.
     */
    public void start() {
        System.out.println("Управление кошельками:");
        while (true) {
            System.out.println("1. Добавить кошелёк");
            System.out.println("2. Удалить кошелёк");
            System.out.println("3. Просмотреть список кошельков");
            System.out.println("4. Подсчитать доходы и расходы");
            System.out.println("5. Вывести данные по бюджету");
            System.out.println("6. Вернуться в главное меню");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addWallet();
                case "2" -> removeWallet();
                case "3" -> listWallets();
                case "4" -> calculateFinances();
                case "5" -> displayBudgetData();
                case "6" -> {
                    System.out.println("Возвращаемся в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Метод для добавления нового кошелька.
     */
    private void addWallet() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        System.out.print("Введите начальный баланс: ");
        double initialBalance = Double.parseDouble(scanner.nextLine());

        walletService.addWallet(user, walletName, initialBalance);
    }

    /**
     * Метод для удаления кошелька.
     */
    private void removeWallet() {
        System.out.print("Введите название кошелька для удаления: ");
        String walletName = scanner.nextLine();

        walletService.removeWallet(user, walletName);
    }

    /**
     * Метод для отображения списка всех кошельков пользователя.
     */
    private void listWallets() {
        walletService.listWallets(user);
    }

    /**
     * Метод для подсчёта общего дохода и расходов по всем кошелькам.
     */
    private void calculateFinances() {
        walletService.calculateFinances(user);
    }

    /**
     * Метод для отображения данных по бюджету для каждого кошелька.
     */
    private void displayBudgetData() {
        walletService.displayBudgetData(user);
    }
}
