package com.beryoza.financeapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Финансовая транзакция. Например, покупка продуктов или получение зарплаты.
 * Хранит информацию о сумме, категории и дате операции.
 */
public class Transaction {
    private final String id; // Уникальный идентификатор
    private double amount;   // Сумма транзакции
    private Category category; // Категория транзакции
    private LocalDate date;  // Дата транзакции

    /**
     * Конструктор для десериализации Jackson.
     *
     * @param id       Уникальный идентификатор транзакции.
     * @param amount   Сумма транзакции. Положительная для доходов, отрицательная для расходов.
     * @param category Категория транзакции.
     * @param date     Дата транзакции.
     */
    @JsonCreator
    public Transaction(@JsonProperty("id") String id,
                       @JsonProperty("amount") double amount,
                       @JsonProperty("category") Category category,
                       @JsonProperty("date") LocalDate date) {
        this.id = id != null ? id : UUID.randomUUID().toString(); // Генерация идентификатора, если он отсутствует
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    /**
     * Конструктор для создания новой транзакции.
     *
     * @param amount   Сумма транзакции.
     * @param category Категория транзакции.
     * @param date     Дата транзакции.
     */
    public Transaction(double amount, Category category, LocalDate date) {
        this(UUID.randomUUID().toString(), amount, category, date);
    }

    /**
     * Получить уникальный идентификатор транзакции.
     *
     * @return Уникальный идентификатор транзакции.
     */
    public String getId() {
        return id;
    }

    /**
     * Получить сумму транзакции.
     *
     * @return Сумма транзакции.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Установить сумму транзакции.
     * Может быть полезно для редактирования транзакции.
     *
     * @param amount Новая сумма транзакции.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Получить категорию транзакции.
     *
     * @return Категория транзакции.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Установить новую категорию транзакции.
     *
     * @param category Новая категория транзакции.
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Получить дату транзакции.
     *
     * @return Дата транзакции.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Установить новую дату транзакции.
     *
     * @param date Новая дата транзакции.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Строковое представление транзакции: сумма, категория, дата.
     *
     * @return Строковое представление транзакции.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", category=" + category +
                ", date=" + date +
                '}';
    }
}