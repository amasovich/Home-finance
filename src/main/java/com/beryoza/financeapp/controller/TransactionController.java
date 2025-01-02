package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.service.WalletService;
import com.beryoza.financeapp.util.DataValidator;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Контроллер для управления транзакциями.
 * Добавлены подсчёты по категориям и оповещения.
 */
public class TransactionController {
    private final WalletService walletService;
    private final User user; // Текущий авторизованный пользователь
    private final Scanner scanner;

    /**
     * Конструктор. Инициализирует сервис кошельков и пользователя.
     *
     * @param walletService Сервис для работы с кошельками.
     * @param user          Авторизованный пользователь.
     */
    public TransactionController(WalletService walletService, User user) {
        this.walletService = walletService;
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает интерфейс для работы с транзакциями.
     */
    public void start() {
        System.out.println("Управление транзакциями:");
        while (true) {
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Просмотреть список транзакций");
            System.out.println("4. Подсчитать расходы и доходы по категориям");
            System.out.println("5. Вернуться в главное меню");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addTransaction(true);
                case "2" -> addTransaction(false);
                case "3" -> listTransactions();
                case "4" -> calculateByCategories();
                case "5" -> {
                    System.out.println("Возвращаемся в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Добавить доход или расход.
     *
     * @param isIncome Если true, то это доход, иначе расход.
     */
    private void addTransaction(boolean isIncome) {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        if (!DataValidator.isNonEmptyString(walletName)) {
            System.out.println("Название кошелька не может быть пустым.");
            return;
        }

        Wallet wallet = findWalletByName(walletName);
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
        if ((isIncome && amount <= 0) || (!isIncome && amount >= 0)) {
            System.out.println("Некорректная сумма для выбранного типа транзакции.");
            return;
        }

        System.out.print("Введите категорию: ");
        String categoryName = scanner.nextLine();
        if (!DataValidator.isNonEmptyString(categoryName)) {
            System.out.println("Категория не может быть пустой.");
            return;
        }

        Transaction transaction = new Transaction(amount,
                new com.beryoza.financeapp.model.Category(categoryName, 0), LocalDate.now());
        wallet.addTransaction(transaction);
        System.out.println("Транзакция успешно добавлена.");
    }

    /**
     * Просмотреть список транзакций.
     */
    private void listTransactions() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        Wallet wallet = findWalletByName(walletName);

        if (wallet == null) {
            System.out.println("Кошелёк не найден.");
            return;
        }

        System.out.println("Транзакции для кошелька \"" + wallet.getName() + "\":");
        for (Transaction transaction : wallet.getTransactions()) {
            System.out.println("- " + transaction.getDate() + ": " +
                    transaction.getAmount() + " (" + transaction.getCategory().getName() + ")");
        }
    }

    /**
     * Подсчитать доходы и расходы по категориям.
     */
    private void calculateByCategories() {
        Map<String, Double> categoryTotals = walletService.getWallets(user).stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        System.out.println("Доходы и расходы по категориям:");
        categoryTotals.forEach((category, total) ->
                System.out.println("- " + category + ": " + total));
    }

    /**
     * Найти кошелёк по названию.
     *
     * @param walletName Название кошелька.
     * @return Найденный кошелёк или null.
     */
    private Wallet findWalletByName(String walletName) {
        return walletService.getWallets(user).stream()
                .filter(wallet -> wallet.getName().equals(walletName))
                .findFirst()
                .orElse(null);
    }
}