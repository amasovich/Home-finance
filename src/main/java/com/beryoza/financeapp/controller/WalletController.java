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
        System.out.println("Управление кошельками. Выберите действие:");
        while (true) {
            System.out.println("1. Добавить кошелёк");
            System.out.println("2. Удалить кошелёк");
            System.out.println("3. Просмотреть список кошельков");
            System.out.println("4. Подсчитать доходы и расходы");
            System.out.println("5. Вывести данные по бюджету");
            System.out.println("6. Вернуться в главное меню");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addWallet();
                    case "2" -> removeWallet();
                    case "3" -> listWallets();
                    case "4" -> calculateFinances();
                    case "5" -> displayBudgetData();
                    case "6" -> {
                        System.out.println("Выход в главное меню.");
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
     * Метод для добавления нового кошелька.
     */
    private void addWallet() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();
            System.out.print("Введите начальный баланс: ");
            double initialBalance = Double.parseDouble(scanner.nextLine());

            walletService.addWallet(user, walletName, initialBalance);
            System.out.println("Кошелёк успешно добавлен.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число для баланса.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении кошелька: " + e.getMessage());
        }
    }

    /**
     * Метод для удаления кошелька.
     */
    private void removeWallet() {
        try {
            System.out.print("Введите название кошелька для удаления: ");
            String walletName = scanner.nextLine();

            walletService.removeWallet(user, walletName);
            System.out.println("Кошелёк успешно удалён.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении кошелька: " + e.getMessage());
        }
    }

    /**
     * Метод для отображения списка всех кошельков пользователя.
     */
    private void listWallets() {
        try {
            walletService.listWallets(user);
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка кошельков: " + e.getMessage());
        }
    }

    /**
     * Метод для подсчёта общего дохода и расходов по всем кошелькам.
     */
    private void calculateFinances() {
        try {
            walletService.calculateFinances(user);
        } catch (Exception e) {
            System.out.println("Ошибка при подсчёте финансов: " + e.getMessage());
        }
    }

    /**
     * Метод для отображения данных по бюджету для каждого кошелька.
     */
    private void displayBudgetData() {
        try {
            walletService.displayBudgetData(user);
        } catch (Exception e) {
            System.out.println("Ошибка при отображении данных по бюджету: " + e.getMessage());
        }
    }
}