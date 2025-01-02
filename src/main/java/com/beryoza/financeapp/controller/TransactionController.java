package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.service.WalletService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Контроллер для управления транзакциями.
 * Позволяет добавлять доходы и расходы, а также просматривать список транзакций.
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
            System.out.println("4. Вернуться в главное меню");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addTransaction(true);
                case "2" -> addTransaction(false);
                case "3" -> listTransactions();
                case "4" -> {
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
        List<Wallet> wallets = walletService.getWallets(user);
        Wallet selectedWallet = wallets.stream()
                .filter(wallet -> wallet.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (selectedWallet == null) {
            System.out.println("Кошелёк не найден.");
            return;
        }

        System.out.print("Введите сумму: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
            if (!isIncome) {
                amount = -amount; // Отрицательная сумма для расходов
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод суммы. Попробуйте снова.");
            return;
        }

        System.out.print("Введите категорию: ");
        String categoryName = scanner.nextLine();

        Transaction transaction = new Transaction(amount, new com.beryoza.financeapp.model.Category(categoryName, 0), LocalDate.now());
        selectedWallet.addTransaction(transaction);
        System.out.println("Транзакция успешно добавлена.");
    }

    /**
     * Просмотреть список транзакций.
     */
    private void listTransactions() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        List<Wallet> wallets = walletService.getWallets(user);
        Wallet selectedWallet = wallets.stream()
                .filter(wallet -> wallet.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (selectedWallet == null) {
            System.out.println("Кошелёк не найден.");
            return;
        }

        System.out.println("Транзакции для кошелька \"" + selectedWallet.getName() + "\":");
        for (Transaction transaction : selectedWallet.getTransactions()) {
            System.out.println("- " + transaction.getDate() + ": " +
                    transaction.getAmount() + " (" + transaction.getCategory().getName() + ")");
        }
    }
}