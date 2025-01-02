package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;

import java.util.List;

/**
 * Сервис для управления кошельками.
 * Отвечает за добавление, удаление кошельков и получение информации о них.
 */
public class WalletService {
    private final UserService userService; // Для работы с пользователями

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
        List<Wallet> wallets = user.getWallets();
        wallets.removeIf(wallet -> wallet.getName().equals(walletName));
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