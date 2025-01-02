package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.util.DataValidator;

import java.util.List;

/**
 * Сервис для управления кошельками.
 * Добавлены проверки корректности вводимых данных.
 */
public class WalletService {
    private final UserService userService;

    /**
     * Конструктор. Получает ссылку на сервис пользователей.
     *
     * @param userService Сервис для работы с пользователями.
     */
    public WalletService(UserService userService) {
        this.userService = userService;
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
        user.addWallet(newWallet);
    }

    /**
     * Удалить кошелёк у пользователя.
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
        } else {
            System.out.println("Кошелёк с таким названием не найден.");
        }
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
}