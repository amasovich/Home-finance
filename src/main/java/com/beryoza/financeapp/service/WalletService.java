package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.util.DataValidator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для управления кошельками и транзакциями.
 * Интегрирует функционал добавления транзакций, подсчётов и управления кошельками.
 */
public class WalletService {
    private final WalletRepository walletRepository;

    /**
     * Конструктор. Инициализирует репозиторий кошельков.
     *
     * @param walletRepository Репозиторий для работы с данными кошельков и транзакций.
     */
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Добавить новый кошелёк пользователю.
     *
     * @param user          Пользователь, которому добавляется кошелёк.
     * @param walletName    Название нового кошелька.
     * @param initialBalance Начальный баланс кошелька.
     */
    public void addWallet(User user, String walletName, double initialBalance) {
        if (!DataValidator.isNonEmptyString(walletName)) {
            throw new IllegalArgumentException("Название кошелька не может быть пустым.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным.");
        }

        List<Wallet> wallets = user.getWallets();
        if (wallets.stream().anyMatch(wallet -> wallet.getName().equals(walletName))) {
            throw new IllegalArgumentException("Кошелёк с таким названием уже существует.");
        }

        Wallet newWallet = new Wallet(walletName, initialBalance);
        wallets.add(newWallet);
        walletRepository.saveWallets(wallets, user.getUsername());
    }

    /**
     * Удалить кошелёк пользователя.
     *
     * @param user       Пользователь, у которого удаляется кошелёк.
     * @param walletName Название кошелька, который нужно удалить.
     */
    public void removeWallet(User user, String walletName) {
        if (!DataValidator.isNonEmptyString(walletName)) {
            throw new IllegalArgumentException("Название кошелька не может быть пустым.");
        }

        List<Wallet> wallets = user.getWallets();
        if (wallets.removeIf(wallet -> wallet.getName().equals(walletName))) {
            System.out.println("Кошелёк успешно удалён.");
            walletRepository.saveWallets(wallets, user.getUsername());
        } else {
            System.out.println("Кошелёк с таким названием не найден.");
        }
    }

    /**
     * Добавить транзакцию в кошелёк пользователя.
     *
     * @param user        Пользователь.
     * @param wallet      Кошелёк, куда добавляется транзакция.
     * @param transaction Транзакция для добавления.
     */
    public void addTransaction(User user, Wallet wallet, Transaction transaction) {
        wallet.addTransaction(transaction);
        walletRepository.saveTransactions(wallet, user.getUsername());
    }

    /**
     * Удалить транзакцию из кошелька пользователя.
     *
     * @param user          Пользователь.
     * @param wallet        Кошелёк, из которого удаляется транзакция.
     * @param transactionId ID транзакции для удаления.
     */
    public void deleteTransaction(User user, Wallet wallet, String transactionId) {
        List<Transaction> transactions = wallet.getTransactions();
        boolean removed = transactions.removeIf(transaction -> transaction.getId().equals(transactionId));
        if (removed) {
            walletRepository.saveTransactions(wallet, user.getUsername());
            System.out.println("Транзакция успешно удалена.");
        } else {
            System.out.println("Транзакция с указанным ID не найдена.");
        }
    }

    /**
     * Подсчитать доходы и расходы по категориям для пользователя.
     *
     * @param user Пользователь.
     * @return Суммы доходов и расходов по категориям.
     */
    public Map<String, Double> calculateByCategories(User user) {
        return user.getWallets().stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    /**
     * Получить список всех кошельков пользователя.
     *
     * @param user Пользователь, чьи кошельки нужно получить.
     * @return Список кошельков.
     */
    public List<Wallet> getWallets(User user) {
        return user.getWallets();
    }

    /**
     * Загрузить список транзакций для кошелька пользователя.
     *
     * @param user       Пользователь.
     * @param walletName Название кошелька.
     * @return Список транзакций.
     */
    public List<Transaction> loadTransactions(User user, String walletName) {
        return walletRepository.loadTransactions(walletName, user.getUsername());
    }
}