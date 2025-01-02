package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления категориями и бюджетами.
 * Отвечает за добавление категорий, установку лимитов и их обновление.
 */
public class BudgetService {

    /**
     * Добавить новую категорию пользователю.
     *
     * @param user         Пользователь, которому добавляется категория.
     * @param categoryName Название новой категории.
     * @param budgetLimit  Лимит бюджета для категории.
     */
    public void addCategory(User user, String categoryName, double budgetLimit) {
        Category newCategory = new Category(categoryName, budgetLimit);
        user.getWallets().forEach(wallet -> wallet.getTransactions()
                .forEach(transaction -> {
                    if (transaction.getCategory().getName().equals(categoryName)) {
                        throw new IllegalArgumentException("Категория уже существует.");
                    }
                }));
    }

    /**
     * Обновить лимит бюджета для существующей категории.
     *
     * @param user         Пользователь, для которого обновляется лимит.
     * @param categoryName Название категории, лимит которой нужно обновить.
     * @param newLimit     Новый лимит бюджета.
     */
    public void updateBudgetLimit(User user, String categoryName, double newLimit) {
        user.getWallets().stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .filter(transaction -> transaction.getCategory().getName().equals(categoryName))
                .findFirst()
                .ifPresent(transaction -> transaction.getCategory().setBudgetLimit(newLimit));
    }

    /**
     * Получить все категории, доступные пользователю.
     *
     * @param user Пользователь, чьи категории нужно получить.
     * @return Список категорий.
     */
    public List<Category> getCategories(User user) {
        List<Category> categories = new ArrayList<>();
        user.getWallets().forEach(wallet -> wallet.getTransactions()
                .forEach(transaction -> {
                    if (!categories.contains(transaction.getCategory())) {
                        categories.add(transaction.getCategory());
                    }
                }));
        return categories;
    }
}