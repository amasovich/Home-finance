package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.util.DataValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Сервис для управления категориями и бюджетами.
 * Обеспечивает работу с категориями, их лимитами и подсчётом состояния бюджета.
 */
public class BudgetService {
    private final WalletRepository walletRepository;

    /**
     * Конструктор для инициализации BudgetService.
     *
     * @param walletRepository Репозиторий для работы с кошельками.
     */
    public BudgetService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Добавить новую категорию пользователю.
     *
     * @param user         Пользователь.
     * @param categoryName Название новой категории.
     * @param budgetLimit  Лимит бюджета для категории.
     */
    public void addCategory(User user, String categoryName, double budgetLimit) {
        try {
            validateCategoryName(categoryName);
            validateBudgetLimit(budgetLimit);

            List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
            for (Wallet wallet : wallets) {
                for (Transaction transaction : wallet.getTransactions()) {
                    if (transaction.getCategory().getName().equals(categoryName)) {
                        throw new IllegalArgumentException("Категория с таким названием уже существует.");
                    }
                }
            }

            System.out.println("Категория успешно добавлена. " +
                    "Добавьте транзакцию, чтобы категория появилась в отчётах.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Обновить лимит бюджета для существующей категории.
     *
     * @param user         Пользователь.
     * @param categoryName Название категории.
     * @param newLimit     Новый лимит бюджета.
     */
    public void updateBudgetLimit(User user, String categoryName, double newLimit) {
        try {
            validateCategoryName(categoryName);
            validateBudgetLimit(newLimit);

            List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
            boolean isUpdated = false;

            for (Wallet wallet : wallets) {
                for (Transaction transaction : wallet.getTransactions()) {
                    if (transaction.getCategory().getName().equals(categoryName)) {
                        transaction.getCategory().setBudgetLimit(newLimit);
                        isUpdated = true;
                    }
                }
            }

            if (!isUpdated) {
                throw new IllegalArgumentException("Категория с таким названием не найдена.");
            }

            walletRepository.saveWallets(wallets);
            System.out.println("Лимит бюджета успешно обновлён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Список категорий пользователя.
     *
     * @param user Пользователь.
     */
    public void listCategories(User user) {
        List<Category> categories = extractCategories(user);

        if (categories.isEmpty()) {
            System.out.println("Категории отсутствуют.");
            return;
        }

        System.out.println("Ваши категории:");
        for (Category category : categories) {
            System.out.println("- " + category.getName() +
                    (category.getBudgetLimit() > 0 ? " (Лимит: " + category.getBudgetLimit() + ")" : ""));
        }
    }

    /**
     * Подсчитать состояние бюджета по категориям.
     *
     * @param user Пользователь.
     */
    public void calculateBudgetState(User user) {
        Map<String, Double> expensesByCategory = new HashMap<>();
        List<Category> categories = extractCategories(user);

        List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
        for (Wallet wallet : wallets) {
            for (Transaction transaction : wallet.getTransactions()) {
                String categoryName = transaction.getCategory().getName();
                expensesByCategory.put(categoryName,
                        expensesByCategory.getOrDefault(categoryName, 0.0) + transaction.getAmount());
            }
        }

        System.out.println("Состояние бюджета по категориям:");
        for (Category category : categories) {
            double expenses = Math.abs(expensesByCategory.getOrDefault(category.getName(), 0.0));
            double remainingBudget = category.getBudgetLimit() - expenses;

            System.out.println("- " + category.getName() +
                    ": Лимит: " + category.getBudgetLimit() +
                    ", Расходы: " + expenses +
                    ", Остаток: " + remainingBudget);
        }
    }

    /**
     * Валидация названия категории.
     *
     * @param categoryName Название категории.
     */
    private void validateCategoryName(String categoryName) {
        if (!DataValidator.isNonEmptyString(categoryName) ||
                !DataValidator.isStringLengthValid(categoryName, 30)) {
            throw new IllegalArgumentException("Некорректное название категории.");
        }
    }

    /**
     * Валидация лимита бюджета.
     *
     * @param budgetLimit Лимит бюджета.
     */
    private void validateBudgetLimit(double budgetLimit) {
        if (!DataValidator.isPositiveNumber(String.valueOf(budgetLimit)) ||
                !DataValidator.isNumberInRange(budgetLimit, 0, 100_000_000)) {
            throw new IllegalArgumentException("Некорректный лимит бюджета.");
        }
    }

    /**
     * Извлечь уникальные категории из транзакций пользователя.
     *
     * @param user Пользователь.
     * @return Список уникальных категорий.
     */
    private List<Category> extractCategories(User user) {
        List<Category> categories = new ArrayList<>();
        List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());

        for (Wallet wallet : wallets) {
            for (Transaction transaction : wallet.getTransactions()) {
                if (!categories.contains(transaction.getCategory())) {
                    categories.add(transaction.getCategory());
                }
            }
        }

        return categories;
    }
}