package beryoza.financeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для представления пользователя.
 * Тут храним логин, пароль и кошельки.
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
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallets = new ArrayList<>(); // Список для кошельков пользователя
    }

    // Получить логин
    public String getUsername() {
        return username;
    }

    // Установить новый логин
    public void setUsername(String username) {
        this.username = username;
    }

    // Получить пароль
    public String getPassword() {
        return password;
    }

    // Установить новый пароль
    public void setPassword(String password) {
        this.password = password;
    }

    // Достаём список кошельков пользователя
    public List<Wallet> getWallets() {
        return wallets;
    }

    // Заменяем текущий список кошельков новым
    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    /**
     * Добавляем кошелёк в список.
     * Например, пользователь решил завести "Кошелёк для отпусков".
     */
    public void addWallet(Wallet wallet) {
        this.wallets.add(wallet);
    }

    /**
     * Удаляем кошелёк из списка.
     * Например, если он больше не нужен.
     */
    public void removeWallet(Wallet wallet) {
        this.wallets.remove(wallet);
    }

    /**
     * Чисто для удобства. Можно распечатать инфу о пользователе и его кошельках.
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", wallets=" + wallets +
                '}';
    }
}