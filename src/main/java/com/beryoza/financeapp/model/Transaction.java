package com.beryoza.financeapp.model;

import java.time.LocalDate;

/**
 * Финансовая транзакция. Например, покупка продуктов или получение зарплаты.
 * Хранит информацию о сумме, категории и дате операции.
 */
public class Transaction {
    // Сумма транзакции (положительная для доходов, отрицательная для расходов)
    private double amount;

    // Категория транзакции, например, "Еда" или "Транспорт"
    private Category category;

    // Дата, когда была совершена транзакция
    private LocalDate date;

    /**
     * Конструктор. Создаём новую транзакцию с указанием суммы, категории и даты.
     *
     * @param amount    Сумма транзакции. Положительная для доходов, отрицательная для расходов.
     * @param category  Категория транзакции (например, "Еда").
     * @param date      Дата, когда была совершена транзакция.
     */
    public Transaction(double amount, Category category, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
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
     * Используйте это, если нужно исправить значение.
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
     * Установить новую категорию для транзакции.
     *
     * @param category Новая категория транзакции.
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Получить дату транзакции.
     *
     * @return Дата, когда была совершена транзакция.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Установить новую дату транзакции.
     * Это может быть полезно, если транзакция была добавлена позже реальной даты.
     *
     * @param date Новая дата транзакции.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Выводим информацию о транзакции: сумма, категория, дата.
     *
     * @return Строковое представление транзакции.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", category=" + category +
                ", date=" + date +
                '}';
    }
}