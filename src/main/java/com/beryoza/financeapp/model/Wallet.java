package com.beryoza.financeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для представления кошелька.
 * Связан с пользователем через поле userId.
 * Хранит информацию о названии, балансе и транзакциях.
 */
public class Wallet {
    private String userId; // Идентификатор пользователя, которому принадлежит кошелёк
    private String name; // Название кошелька
    private double balance; // Баланс кошелька
    private List<Transaction> transactions; // Список транзакций по кошельку

    /**
     * Конструктор для создания кошелька.
     *
     * @param userId  Идентификатор пользователя, которому принадлежит кошелёк.
     * @param name    Название кошелька.
     * @param balance Начальный баланс кошелька.
     */
    public Wallet(String userId, String name, double balance) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    /**
     * Получить идентификатор пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Установить идентификатор пользователя.
     *
     * @param userId Идентификатор пользователя.
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
     * Установить название кошелька.
     *
     * @param name Название кошелька.
     */
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
     *
     * @param balance Новый баланс кошелька.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Получить список транзакций по кошельку.
     *
     * @return Список транзакций.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Установить новый список транзакций.
     *
     * @param transactions Новый список транзакций.
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Добавить транзакцию в кошелёк и обновить баланс.
     *
     * @param transaction Транзакция для добавления.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        balance += transaction.getAmount();
    }

    /**
     * Удалить транзакцию из кошелька и скорректировать баланс.
     *
     * @param transaction Транзакция для удаления.
     */
    public void removeTransaction(Transaction transaction) {
        if (transactions.remove(transaction)) {
            balance -= transaction.getAmount();
        }
    }

    /**
     * Найти транзакцию по ID.
     *
     * @param id Идентификатор транзакции.
     * @return Транзакция, если найдена; иначе null.
     */
    public Transaction findTransactionById(String id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(id)) {
                return transaction;
            }
        }
        return null;
    }

    /**
     * Получить строковое представление кошелька.
     *
     * @return Информация о кошельке в текстовом формате.
     */
    @Override
    public String toString() {
        return "Wallet{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions.size() +
                '}';
    }
}
