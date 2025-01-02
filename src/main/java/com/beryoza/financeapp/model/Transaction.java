package beryoza.financeapp.model;

import java.time.LocalDate;

/**
 * Финансовая транзакция. Например, покупка продуктов или получение зарплаты.
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
     */
    public Transaction(double amount, Category category, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Узнать сумму транзакции
    public double getAmount() {
        return amount;
    }

    // Изменить сумму транзакции (например, исправить ошибку)
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Получить категорию
    public Category getCategory() {
        return category;
    }

    // Установить новую категорию
    public void setCategory(Category category) {
        this.category = category;
    }

    // Узнать дату транзакции
    public LocalDate getDate() {
        return date;
    }

    // Установить другую дату (например, если транзакцию внесли позже)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Выводим инфу о транзакции: сумма, категория, дата.
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