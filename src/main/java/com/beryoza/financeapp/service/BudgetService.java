package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис для управления категориями и бюджетами.
 * Обработка данных и вывод сообщений перенесены из контроллера.
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
        try {
            if (categoryName == null || categoryName.isBlank()) {
                throw new IllegalArgumentException("Название категории не может быть пустым.");
            }
            if (budgetLimit < 0) {
                throw new IllegalArgumentException("Лимит бюджета не может быть отрицательным.");
            }

            user.getWallets().forEach(wallet -> wallet.getTransactions().forEach(transaction -> {
                if (transaction.getCategory().getName().equals(categoryName)) {
                    throw new IllegalArgumentException("Категория уже существует.");
                }
            }));

            System.out.println("Категория успешно добавлена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Обновить лимит бюджета для существующей категории.
     *
     * @param user         Пользователь, для которого обновляется лимит.
     * @param categoryName Название категории, лимит которой нужно обновить.
     * @param newLimit     Новый лимит бюджета.
     */
    public void updateBudgetLimit(User user, String categoryName, double newLimit) {
        try {
            if (categoryName == null || categoryName.isBlank()) {
                throw new IllegalArgumentException("Название категории не может быть пустым.");
            }
            if (newLimit < 0) {
                throw new IllegalArgumentException("Новый лимит бюджета не может быть отрицательным.");
            }

            user.getWallets().stream()
                    .flatMap(wallet -> wallet.getTransactions().stream())
                    .filter(transaction -> transaction.getCategory().getName().equals(categoryName))
                    .findFirst()
                    .ifPresentOrElse(
                            transaction -> transaction.getCategory().setBudgetLimit(newLimit),
                            () -> {
                                throw new IllegalArgumentException("Категория не найдена.");
                            });

            System.out.println("Лимит бюджета успешно обновлён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Получить и вывести список всех категорий пользователя.
     *
     * @param user Пользователь, чьи категории нужно получить.
     */
    public void listCategories(User user) {
        List<Category> categories = new ArrayList<>();
        user.getWallets().forEach(wallet -> wallet.getTransactions().forEach(transaction -> {
            if (!categories.contains(transaction.getCategory())) {
                categories.add(transaction.getCategory());
            }
        }));

        if (categories.isEmpty()) {
            System.out.println("Категории отсутствуют.");
        } else {
            System.out.println("Ваши категории:");
            categories.forEach(category ->
                    System.out.println("- " + category.getName() +
                            (category.getBudgetLimit() > 0 ? " (Лимит: " + category.getBudgetLimit() + ")" : "")));
        }
    }

    /**
     * Подсчитать и вывести состояние бюджета по категориям.
     *
     * @param user Пользователь, для которого подсчитывается состояние бюджета.
     */
    public void calculateBudgetState(User user) {
        Map<String, Double> expensesByCategory = user.getWallets().stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        List<Category> categories = new ArrayList<>();
        user.getWallets().forEach(wallet -> wallet.getTransactions().forEach(transaction -> {
            if (!categories.contains(transaction.getCategory())) {
                categories.add(transaction.getCategory());
            }
        }));

        if (categories.isEmpty()) {
            System.out.println("Категории отсутствуют.");
            return;
        }

        System.out.println("Состояние бюджета по категориям:");
        categories.forEach(category -> {
            double expenses = expensesByCategory.getOrDefault(category.getName(), 0.0);
            double remainingBudget = category.getBudgetLimit() - Math.abs(expenses);
            System.out.println("- " + category.getName() + ": Лимит: " + category.getBudgetLimit() +
                    ", Расходы: " + Math.abs(expenses) +
                    ", Оставшийся бюджет: " + remainingBudget);
        });
    }
}