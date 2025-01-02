package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.repository.WalletRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для управления кошельками и транзакциями.
 */
public class WalletService {
    private final WalletRepository walletRepository;

    /**
     * Конструктор для инициализации WalletService.
     *
     * @param walletRepository Репозиторий для работы с кошельками и транзакциями.
     */
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Добавить новый кошелёк.
     *
     * @param user          Пользователь, которому добавляется кошелёк.
     * @param walletName    Название нового кошелька.
     * @param initialBalance Начальный баланс кошелька.
     */
    public void addWallet(User user, String walletName, double initialBalance) {
        if (walletName == null || walletName.isBlank()) {
            System.out.println("Ошибка: Название кошелька не может быть пустым.");
            return;
        }
        if (initialBalance < 0) {
            System.out.println("Ошибка: Начальный баланс не может быть отрицательным.");
            return;
        }

        if (user.getWallets().stream().anyMatch(wallet -> wallet.getName().equals(walletName))) {
            System.out.println("Ошибка: Кошелёк с таким названием уже существует.");
            return;
        }

        Wallet wallet = new Wallet(walletName, initialBalance);
        user.addWallet(wallet);

        try {
            walletRepository.saveWallets(user.getWallets(), user.getUsername());
            System.out.println("Кошелёк успешно добавлен.");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении кошелька: " + e.getMessage());
        }
    }

    /**
     * Удалить кошелёк.
     *
     * @param user       Пользователь.
     * @param walletName Название кошелька для удаления.
     */
    public void removeWallet(User user, String walletName) {
        if (walletName == null || walletName.isBlank()) {
            System.out.println("Ошибка: Название кошелька не может быть пустым.");
            return;
        }

        Wallet wallet = user.getWallets().stream()
                .filter(w -> w.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (wallet == null) {
            System.out.println("Ошибка: Кошелёк с таким названием не найден.");
            return;
        }

        user.removeWallet(wallet);

        try {
            walletRepository.saveWallets(user.getWallets(), user.getUsername());
            System.out.println("Кошелёк успешно удалён.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении кошелька: " + e.getMessage());
        }
    }

    /**
     * Вывести список кошельков пользователя.
     *
     * @param user Пользователь.
     */
    public void listWallets(User user) {
        if (user.getWallets().isEmpty()) {
            System.out.println("У вас нет кошельков.");
        } else {
            user.getWallets().forEach(wallet ->
                    System.out.println("- " + wallet.getName() + " (Баланс: " + wallet.getBalance() + ")"));
        }
    }

    /**
     * Подсчитать общий доход и расходы по всем кошелькам.
     *
     * @param user Пользователь.
     */
    public void calculateFinances(User user) {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Wallet wallet : user.getWallets()) {
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
     *
     * @param user Пользователь.
     */
    public void displayBudgetData(User user) {
        user.getWallets().forEach(wallet -> {
            System.out.println("Кошелёк: " + wallet.getName());
            wallet.getTransactions().forEach(transaction ->
                    System.out.println("- Транзакция: " + transaction.getAmount() +
                            " (" + transaction.getCategory().getName() + ")"));
            System.out.println("Текущий баланс: " + wallet.getBalance());
        });
    }

    /**
     * Добавить транзакцию.
     *
     * @param user          Пользователь.
     * @param walletName    Название кошелька.
     * @param amountInput   Сумма транзакции (строка).
     * @param categoryName  Название категории транзакции.
     * @param isIncome      Если true, транзакция доходная, иначе расход.
     */
    public void addTransaction(User user, String walletName, String amountInput, String categoryName, boolean isIncome) {
        double amount;
        try {
            amount = Double.parseDouble(amountInput);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Сумма должна быть числом.");
            return;
        }

        if (!isIncome) {
            amount = -amount;
        }

        Wallet wallet = user.getWallets().stream()
                .filter(w -> w.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (wallet == null) {
            System.out.println("Ошибка: Кошелёк не найден.");
            return;
        }

        Transaction transaction = new Transaction(amount, new Category(categoryName), LocalDate.now());
        wallet.addTransaction(transaction);

        try {
            walletRepository.saveTransactions(wallet, user.getUsername());
            System.out.println("Транзакция успешно добавлена.");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении транзакции: " + e.getMessage());
        }
    }

    /**
     * Удалить транзакцию.
     *
     * @param user          Пользователь.
     * @param walletName    Название кошелька.
     * @param transactionId ID транзакции для удаления.
     */
    public void deleteTransaction(User user, String walletName, String transactionId) {
        Wallet wallet = user.getWallets().stream()
                .filter(w -> w.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (wallet == null) {
            System.out.println("Ошибка: Кошелёк не найден.");
            return;
        }

        Transaction transaction = wallet.findTransactionById(transactionId);
        if (transaction == null) {
            System.out.println("Ошибка: Транзакция не найдена.");
            return;
        }

        wallet.removeTransaction(transaction);

        try {
            walletRepository.saveTransactions(wallet, user.getUsername());
            System.out.println("Транзакция успешно удалена.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении транзакции: " + e.getMessage());
        }
    }

    /**
     * Подсчитать транзакции по категориям.
     *
     * @param user Пользователь.
     */
    public void calculateByCategories(User user) {
        Map<String, Double> totals = user.getWallets().stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        if (totals.isEmpty()) {
            System.out.println("Нет данных для подсчёта.");
            return;
        }

        System.out.println("Доходы и расходы по категориям:");
        totals.forEach((category, total) ->
                System.out.println("- " + category + ": " + total));
    }

    /**
     * Отобразить список транзакций для кошелька.
     *
     * @param user       Пользователь.
     * @param walletName Название кошелька.
     */
    public void listTransactions(User user, String walletName) {
        Wallet wallet = user.getWallets().stream()
                .filter(w -> w.getName().equals(walletName))
                .findFirst()
                .orElse(null);

        if (wallet == null) {
            System.out.println("Ошибка: Кошелёк не найден.");
            return;
        }

        if (wallet.getTransactions().isEmpty()) {
            System.out.println("Транзакции отсутствуют.");
        } else {
            System.out.println("Транзакции для кошелька \"" + wallet.getName() + "\":");
            wallet.getTransactions().forEach(transaction ->
                    System.out.println("- " + transaction.getDate() + ": " + transaction.getAmount() +
                            " (" + transaction.getCategory().getName() + ")"));
        }
    }
}