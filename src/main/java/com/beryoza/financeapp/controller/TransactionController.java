package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.service.WalletService;
import com.beryoza.financeapp.util.DataValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Контроллер для управления транзакциями.
 * Реализует команды добавления, удаления, просмотра и подсчёта транзакций.
 */
public class TransactionController {
    private final WalletService walletService;
    private final User user;
    private final Scanner scanner;

    /**
     * Конструктор. Инициализирует контроллер транзакций.
     *
     * @param walletService Сервис для работы с кошельками и транзакциями.
     * @param user          Авторизованный пользователь.
     */
    public TransactionController(WalletService walletService, User user) {
        this.walletService = walletService;
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает интерфейс управления транзакциями.
     */
    public void start() {
        System.out.println("Управление транзакциями:");
        while (true) {
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Просмотреть транзакции");
            System.out.println("4. Удалить транзакцию");
            System.out.println("5. Подсчитать по категориям");
            System.out.println("6. Вернуться в главное меню");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addTransaction(true);
                case "2" -> addTransaction(false);
                case "3" -> listTransactions();
                case "4" -> deleteTransaction();
                case "5" -> calculateByCategories();
                case "6" -> {
                    System.out.println("Возвращаемся в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Добавить транзакцию (доход или расход).
     *
     * @param isIncome Если true, то это доход, иначе расход.
     */
    private void addTransaction(boolean isIncome) {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        Wallet wallet = walletService.getWallets(user).stream()
                .filter(w -> w.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (wallet == null) {
            System.out.println("Кошелёк не найден.");
            return;
        }

        System.out.print("Введите сумму: ");
        String amountInput = scanner.nextLine();
        if (!DataValidator.isNumeric(amountInput)) {
            System.out.println("Сумма должна быть числом.");
            return;
        }

        double amount = Double.parseDouble(amountInput);
        if (!isIncome) {
            amount = -amount; // Расходы отрицательные
        }

        System.out.print("Введите категорию: ");
        String categoryName = scanner.nextLine();
        if (!DataValidator.isNonEmptyString(categoryName)) {
            System.out.println("Категория не может быть пустой.");
            return;
        }

        Transaction transaction = new Transaction(amount,
                new com.beryoza.financeapp.model.Category(categoryName, 0), LocalDate.now());
        walletService.addTransaction(user, wallet, transaction);
        System.out.println("Транзакция успешно добавлена.");
    }

    /**
     * Удалить транзакцию.
     */
    private void deleteTransaction() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        Wallet wallet = walletService.getWallets(user).stream()
                .filter(w -> w.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (wallet == null) {
            System.out.println("Кошелёк не найден.");
            return;
        }

        System.out.println("Список транзакций:");
        List<Transaction> transactions = wallet.getTransactions();
        transactions.forEach(transaction -> System.out.println(
                "- ID: " + transaction.getId() + ", Сумма: " + transaction.getAmount() +
                        ", Категория: " + transaction.getCategory().getName()));

        System.out.print("Введите ID транзакции для удаления: ");
        String transactionId = scanner.nextLine();
        walletService.deleteTransaction(user, wallet, transactionId);
    }

    /**
     * Просмотреть список транзакций.
     */
    private void listTransactions() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        Wallet wallet = walletService.getWallets(user).stream()
                .filter(w -> w.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (wallet == null) {
            System.out.println("Кошелёк не найден.");
            return;
        }

        System.out.println("Транзакции для кошелька \"" + wallet.getName() + "\":");
        wallet.getTransactions().forEach(transaction -> System.out.println(
                "- " + transaction.getDate() + ": " + transaction.getAmount() +
                        " (" + transaction.getCategory().getName() + ")"));
    }

    /**
     * Подсчитать транзакции по категориям.
     */
    private void calculateByCategories() {
        Map<String, Double> totals = walletService.calculateByCategories(user);
        System.out.println("Доходы и расходы по категориям:");
        totals.forEach((category, total) -> System.out.println("- " + category + ": " + total));
    }
}