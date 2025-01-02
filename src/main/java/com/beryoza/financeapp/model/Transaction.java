package com.beryoza.financeapp.model;

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
     * Конструктор. Создаём новую транзакцию с указанием суммы, категории и даты.
     *
     * @param amount    Сумма транзакции. Положительная для доходов, отрицательная для расходов.
     * @param category  Категория транзакции.
     * @param date      Дата транзакции.
     */
    public Transaction(double amount, Category category, LocalDate date) {
        this.id = UUID.randomUUID().toString(); // Генерация уникального идентификатора
        this.amount = amount;
        this.category = category;
        this.date = date;
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
     * Резервный метод. Может быть полезен для редактирования транзакции.
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
     * Резервный метод. Может быть полезен для изменения категории.
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
     * Резервный метод. Может быть полезен для корректировки даты.
     *
     * @param date Новая дата транзакции.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Вывод информации о транзакции: сумма, категория, дата.
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