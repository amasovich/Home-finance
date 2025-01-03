package com.beryoza.financeapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс для представления категории транзакций.
 * Лимит бюджета можно задавать для контроля расходов/доходов.
 */
public class Category {
    // Название категории, например, "Еда" или "Развлечения".
    private String name;

    // Лимит бюджета для этой категории.
    private double budgetLimit;

    /**
     * Конструктор для десериализации Jackson.
     * Используется для создания объекта из JSON.
     *
     * @param name        Название категории.
     * @param budgetLimit Лимит бюджета.
     */
    @JsonCreator
    public Category(@JsonProperty("name") String name,
                    @JsonProperty("budgetLimit") double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
    }

    /**
     * Альтернативный конструктор для создания категории без лимита.
     *
     * @param name Название категории.
     */
    public Category(String name) {
        this(name, 0); // Лимит по умолчанию равен 0.
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
     * Установить новое название категории.
     * Этот метод позволяет изменить название существующей категории.
     *
     * @param name Новое название категории.
     */
    @Deprecated
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
     * Получить строковое представление объекта категории.
     *
     * @return Строковое представление объекта категории.
     */
    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", budgetLimit=" + budgetLimit +
                '}';
    }
}