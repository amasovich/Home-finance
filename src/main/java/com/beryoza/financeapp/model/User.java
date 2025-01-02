package com.beryoza.financeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для представления пользователя.
 * Хранит логин, пароль и список кошельков.
 */
public class User {
    // Логин пользователя
    private String username;

    // Пароль пользователя
    private String password;

    // Все кошельки, которые добавил пользователь
    private List<Wallet> wallets;

    /**
     * Конструктор. Передаём логин и пароль,
     * а список кошельков пока будет пустой.
     *
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallets = new ArrayList<>(); // Список для кошельков пользователя
    }

    /**
     * Получить логин пользователя.
     *
     * @return Логин пользователя.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Установить новый логин пользователя.
     *
     * @param username Новый логин пользователя.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получить пароль пользователя.
     *
     * @return Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Установить новый пароль пользователя.
     *
     * @param password Новый пароль пользователя.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Получить список кошельков пользователя.
     *
     * @return Список кошельков.
     */
    public List<Wallet> getWallets() {
        return wallets;
    }

    /**
     * Установить новый список кошельков.
     *
     * @param wallets Новый список кошельков.
     */
    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    /**
     * Добавить кошелёк в список пользователя.
     *
     * @param wallet Кошелёк, который нужно добавить.
     */
    public void addWallet(Wallet wallet) {
        this.wallets.add(wallet);
    }

    /**
     * Удалить кошелёк из списка пользователя.
     *
     * @param wallet Кошелёк, который нужно удалить.
     */
    public void removeWallet(Wallet wallet) {
        this.wallets.remove(wallet);
    }

    /**
     * Получить строковое представление объекта User.
     *
     * @return Информация о пользователе в текстовом формате.
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", wallets=" + wallets +
                '}';
    }
}