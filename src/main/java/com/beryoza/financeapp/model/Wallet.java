package com.beryoza.financeapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для представления кошелька.
 * Связан с пользователем через поле userId.
 * Хранит информацию о названии, балансе и транзакциях.
 * <p>
 * Поля:
 * - {@code String userId} — идентификатор пользователя, которому принадлежит кошелёк.
 * - {@code String name} — название кошелька.
 * - {@code double balance} — текущий баланс кошелька.
 * - {@link List}<{@link Transaction}> transactions — список транзакций, связанных с кошельком.
 */
public class Wallet {
    private String userId;
    private String name;
    private double balance;
    private List<Transaction> transactions;

    /**
     * Конструктор для десериализации Jackson.
     *
     * @param userId       Идентификатор пользователя.
     * @param name         Название кошелька.
     * @param balance      Баланс кошелька.
     * @param transactions Список транзакций.
     */
    @JsonCreator
    public Wallet(@JsonProperty("userId") String userId,
                  @JsonProperty("name") String name,
                  @JsonProperty("balance") double balance,
                  @JsonProperty("transactions") List<Transaction> transactions) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
        this.transactions = transactions != null ? transactions : new ArrayList<>();
    }

    /**
     * Конструктор для создания нового кошелька.
     *
     * @param userId  Идентификатор пользователя.
     * @param name    Название кошелька.
     * @param balance Начальный баланс кошелька.
     */
    public Wallet(String userId, String name, double balance) {
        this(userId, name, balance, new ArrayList<>());
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
    public String toString() {
        return "Wallet{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions.size() +
                '}';
    }
}