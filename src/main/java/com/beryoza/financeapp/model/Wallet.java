package com.beryoza.financeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Кошелёк пользователя. Хранит информацию о названии, балансе и транзакциях.
 */
public class Wallet {
    // Название кошелька, например, "Наличные" или "Карта Сбербанка"
    private String name;

    // Текущий баланс кошелька
    private double balance;

    // Список всех транзакций по этому кошельку
    private List<Transaction> transactions;

    /**
     * Конструктор. Задаём название кошелька и начальный баланс.
     *
     * @param name    Название кошелька.
     * @param balance Начальный баланс.
     */
    public Wallet(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>(); // Начинаем с пустого списка транзакций
    }

    /**
     * Получить название кошелька.
     *
     * @return Название кошелька.
     */
    public String getName() {
        return name;
    }

    /**
     * Установить новое название кошелька.
     * Резервный метод. Может быть полезен для редактирования названия.
     *
     * @param name Новое название кошелька.
     */
    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить текущий баланс кошелька.
     *
     * @return Баланс кошелька.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Установить новый баланс кошелька.
     * Резервный метод. Может быть полезен для корректировок.
     *
     * @param balance Новый баланс кошелька.
     */
    @Deprecated
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Получить список всех транзакций по кошельку.
     *
     * @return Список транзакций.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Установить новый список транзакций.
     * Резервный метод. Может быть полезен для массового обновления транзакций.
     *
     * @param transactions Новый список транзакций.
     */
    @Deprecated
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Добавить транзакцию в список и обновить баланс кошелька.
     *
     * @param transaction Транзакция, которую нужно добавить.
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.balance += transaction.getAmount(); // Обновляем баланс
    }

    /**
     * Удалить транзакцию из списка и скорректировать баланс кошелька.
     *
     * @param transaction Транзакция, которую нужно удалить.
     */
    public void removeTransaction(Transaction transaction) {
        if (this.transactions.remove(transaction)) {
            this.balance -= transaction.getAmount(); // Корректируем баланс
        }
    }

    public Transaction findTransactionById(String id) {
        return transactions.stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Вывести строковое представление кошелька: название, баланс и количество операций.
     *
     * @return Информация о кошельке в текстовом формате.
     */
    @Override
    public String toString() {
        return "Wallet{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", transactionsCount=" + transactions.size() +
                '}';
    }
}
