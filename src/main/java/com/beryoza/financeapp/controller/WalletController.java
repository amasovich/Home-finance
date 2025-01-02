package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.service.WalletService;
import com.beryoza.financeapp.util.DataValidator;

import java.util.Scanner;

/**
 * Контроллер для управления кошельками пользователя.
 * Добавлены подсчёты доходов, расходов и оповещения.
 */
public class WalletController {
    private final WalletService walletService;
    private final User user; // Текущий авторизованный пользователь
    private final Scanner scanner;

    /**
     * Конструктор. Инициализирует сервис кошельков и пользователя.
     *
     * @param walletService Сервис для работы с кошельками.
     * @param user          Авторизованный пользователь.
     */
    public WalletController(WalletService walletService, User user) {
        this.walletService = walletService;
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает интерфейс для работы с кошельками.
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
     * Добавить новый кошелёк.
     */
    private void addWallet() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        if (!DataValidator.isNonEmptyString(walletName)) {
            System.out.println("Название кошелька не может быть пустым.");
            return;
        }

        System.out.print("Введите начальный баланс: ");
        String balanceInput = scanner.nextLine();
        if (!DataValidator.isPositiveNumber(balanceInput)) {
            System.out.println("Баланс должен быть положительным числом.");
            return;
        }

        double initialBalance = Double.parseDouble(balanceInput);
        walletService.addWallet(user, walletName, initialBalance);
        System.out.println("Кошелёк успешно добавлен.");
    }

    /**
     * Удалить существующий кошелёк.
     */
    private void removeWallet() {
        System.out.print("Введите название кошелька для удаления: ");
        String walletName = scanner.nextLine();

        walletService.removeWallet(user, walletName);
        System.out.println("Кошелёк успешно удалён.");
    }

    /**
     * Просмотреть список всех кошельков пользователя.
     */
    private void listWallets() {
        System.out.println("Ваши кошельки:");
        for (Wallet wallet : walletService.getWallets(user)) {
            System.out.println("- " + wallet.getName() + " (Баланс: " + wallet.getBalance() + ")");
        }
    }

    /**
     * Подсчитать общий доход и расходы по всем кошелькам.
     */
    private void calculateFinances() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Wallet wallet : walletService.getWallets(user)) {
            for (Transaction transaction : wallet.getTransactions()) {
                if (transaction.getAmount() > 0) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += transaction.getAmount();
                }
            }
        }

        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общие расходы: " + Math.abs(totalExpenses));
    }

    /**
     * Вывести данные по бюджету для каждого кошелька.
     */
    private void displayBudgetData() {
        for (Wallet wallet : walletService.getWallets(user)) {
            System.out.println("Кошелёк: " + wallet.getName());
            for (Transaction transaction : wallet.getTransactions()) {
                System.out.println("- Транзакция: " + transaction.getAmount() +
                        " (" + transaction.getCategory().getName() + ")");
            }
            System.out.println("Текущий баланс: " + wallet.getBalance());
        }
    }
}