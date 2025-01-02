package com.beryoza.financeapp.model;

/**
 * Категория транзакций. Например, "Еда", "Развлечения", "Транспорт".
 * Ещё можно задавать лимит бюджета для контроля расходов.
 */
public class Category {
    // Название категории, например, "Еда" или "Развлечения"
    private String name;

    // Лимит бюджета для этой категории
    private double budgetLimit;

    /**
     * Конструктор. Создаём категорию с названием и опциональным лимитом.
     *
     * @param name Название категории.
     * @param budgetLimit Лимит бюджета (или 0, если лимит не нужен).
     */
    public Category(String name, double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
    }

    /**
     * Альтернативный конструктор для категории без лимита.
     *
     * @param name Название категории.
     */
    public Category(String name) {
        this(name, 0); // Лимит по умолчанию — 0
    }

    /**
     * Получить название категории.
     *
     * @return Название категории.
     */
    public String getName() {
        return name;
    }

    /**
     * Задать новое название для категории.
     *
     * @param name Новое название категории.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить текущий лимит бюджета.
     *
     * @return Лимит бюджета.
     */
    public double getBudgetLimit() {
        return budgetLimit;
    }

    /**
     * Установить новый лимит бюджета для категории.
     *
     * @param budgetLimit Новый лимит бюджета.
     */
    public void setBudgetLimit(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }

    /**
     * Вывести строковое представление категории.
     *
     * @return Информация о категории в текстовом формате.
     */
    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", budgetLimit=" + budgetLimit +
                '}';
    }
}