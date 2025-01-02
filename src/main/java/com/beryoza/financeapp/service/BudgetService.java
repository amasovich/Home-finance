package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.util.DataValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
            // Проверка, что название категории не пустое
            if (!DataValidator.isNonEmptyString(categoryName)) {
                throw new IllegalArgumentException("Название категории не может быть пустым.");
            }

            // Проверка длины названия категории
            if (!DataValidator.isStringLengthValid(categoryName, 30)) {
                throw new IllegalArgumentException("Название категории не может быть длиннее 30 символов.");
            }

            // Проверка допустимости названия категории
            if (!DataValidator.isValidCategory(categoryName)) {
                throw new IllegalArgumentException("Название категории недопустимо.");
            }

            // Проверка, что лимит бюджета положительный
            if (!DataValidator.isPositiveNumber(String.valueOf(budgetLimit))) {
                throw new IllegalArgumentException("Лимит бюджета должен быть положительным числом.");
            }

            // Проверка диапазона лимита бюджета
            if (!DataValidator.isNumberInRange(budgetLimit, 0, 100_000_000)) {
                throw new IllegalArgumentException("Лимит бюджета должен быть в диапазоне от 0 до 100 000 000.");
            }

            // Проверка уникальности категории
            List<String> existingCategoryNames = new ArrayList<>();
            for (Wallet wallet : user.getWallets()) {
                for (Transaction transaction : wallet.getTransactions()) {
                    String existingCategoryName = transaction.getCategory().getName();
                    if (!existingCategoryNames.contains(existingCategoryName)) {
                        existingCategoryNames.add(existingCategoryName);
                    }
                }
            }

            if (!DataValidator.isUniqueName(categoryName, existingCategoryNames)) {
                throw new IllegalArgumentException("Категория с таким названием уже существует.");
            }

            // Логика добавления категории
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
            // Проверка, что название категории не пустое
            if (!DataValidator.isNonEmptyString(categoryName)) {
                throw new IllegalArgumentException("Название категории не может быть пустым.");
            }

            // Проверка длины названия категории
            if (!DataValidator.isStringLengthValid(categoryName, 30)) {
                throw new IllegalArgumentException("Название категории не может быть длиннее 30 символов.");
            }

            // Проверка, что лимит бюджета положительный
            if (!DataValidator.isPositiveNumber(String.valueOf(newLimit))) {
                throw new IllegalArgumentException("Новый лимит бюджета должен быть положительным числом.");
            }

            // Проверка диапазона нового лимита бюджета
            if (!DataValidator.isNumberInRange(newLimit, 0, 100_000_000)) {
                throw new IllegalArgumentException("Новый лимит бюджета должен быть в диапазоне от 0 до 100 000 000.");
            }

            // Поиск категории
            boolean isUpdated = false;
            for (Wallet wallet : user.getWallets()) {
                for (Transaction transaction : wallet.getTransactions()) {
                    Category category = transaction.getCategory();
                    if (category.getName().equals(categoryName)) {
                        category.setBudgetLimit(newLimit);
                        isUpdated = true;
                    }
                }
            }

            if (!isUpdated) {
                throw new IllegalArgumentException("Категория с таким названием не найдена.");
            }

            System.out.println("Лимит бюджета успешно обновлён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении лимита бюджета: " + e.getMessage());
        }
    }

    /**
     * Получить и вывести список всех категорий пользователя.
     *
     * @param user Пользователь, чьи категории нужно получить.
     */
    public void listCategories(User user) {
        // Список для хранения уникальных категорий
        List<Category> categories = new ArrayList<>();

        // Проходим по всем кошелькам пользователя
        for (Wallet wallet : user.getWallets()) {
            for (Transaction transaction : wallet.getTransactions()) {
                if (!categories.contains(transaction.getCategory())) {
                    categories.add(transaction.getCategory());
                }
            }
        }

        // Проверяем, есть ли категории
        if (categories.isEmpty()) {
            System.out.println("Категории отсутствуют."); // Сообщение, если категорий нет
        } else {
            System.out.println("Ваши категории:");
            // Выводим все категории
            categories.forEach(category -> {
                // Формируем строку вывода с лимитом, если он установлен
                System.out.println("- " + category.getName() +
                        (category.getBudgetLimit() > 0 ? " (Лимит: " + category.getBudgetLimit() + ")" : ""));
            });
        }
    }

    /**
     * Подсчитать и вывести состояние бюджета по категориям.
     *
     * @param user Пользователь, для которого подсчитывается состояние бюджета.
     */
    public void calculateBudgetState(User user) {
        // Суммируем транзакции по категориям
        Map<String, Double> expensesByCategory = new HashMap<>();
        for (Wallet wallet : user.getWallets()) {
            for (Transaction transaction : wallet.getTransactions()) {
                String categoryName = transaction.getCategory().getName();
                expensesByCategory.put(
                        categoryName,
                        expensesByCategory.getOrDefault(categoryName, 0.0) + transaction.getAmount()
                );
            }
        }

        // Собираем уникальные категории пользователя
        List<Category> categories = new ArrayList<>();
        for (Wallet wallet : user.getWallets()) {
            for (Transaction transaction : wallet.getTransactions()) {
                if (!categories.contains(transaction.getCategory())) {
                    categories.add(transaction.getCategory());
                }
            }
        }

        // Проверка наличия категорий
        if (categories.isEmpty()) {
            System.out.println("Категории отсутствуют.");
            return;
        }

        // Вывод состояния бюджета по категориям
        System.out.println("Состояние бюджета по категориям:");
        for (Category category : categories) {
            double expenses = Math.abs(expensesByCategory.getOrDefault(category.getName(), 0.0));
            double remainingBudget = category.getBudgetLimit() - expenses;

            System.out.println("- " + category.getName() +
                    ": Лимит: " + category.getBudgetLimit() +
                    ", Расходы: " + expenses +
                    ", Оставшийся бюджет: " + remainingBudget);
        }
    }
}