package com.beryoza.financeapp.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Класс для представления категории транзакций.
 * Лимит бюджета можно задавать для контроля расходов/доходов.
 */
public class Category {
    private String userId;  // Идентификатор пользователя
    private String name;    // Название категории
    private double budgetLimit; // Лимит бюджета

    /**
     * Конструктор для десериализации Jackson.
     *
     * @param userId      Идентификатор пользователя.
     * @param name        Название категории.
     * @param budgetLimit Лимит бюджета.
     */
    @JsonCreator
    public Category(@JsonProperty("userId") String userId,
                    @JsonProperty("name") String name,
                    @JsonProperty("budgetLimit") double budgetLimit) {
        this.userId = userId;
        this.name = name;
        this.budgetLimit = budgetLimit;
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
     * Получить название категории.
     *
     * @return Название категории.
     */
    public String getName() {
        return name;
    }

    /**
     * Установить новое название категории.
     *
     * @param name Новое название категории.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить лимит бюджета категории.
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
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", budgetLimit=" + budgetLimit +
                '}';
    }
}