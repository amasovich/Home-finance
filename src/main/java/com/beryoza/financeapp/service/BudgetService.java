package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.repository.CategoryRepository;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.util.DataValidator;

import java.util.*;

/**
 * Сервис для управления категориями и бюджетами.
 * Обеспечивает работу с категориями, их лимитами и подсчётом состояния бюджета.
 */
public class BudgetService {
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Конструктор для инициализации BudgetService.
     *
     * @param walletRepository   Репозиторий для работы с кошельками.
     * @param categoryRepository Репозиторий для работы с категориями.
     */
    public BudgetService(WalletRepository walletRepository, CategoryRepository categoryRepository) {
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Добавить новую категорию пользователю.
     *
     * @param user         Пользователь.
     * @param categoryName Название новой категории.
     * @param budgetLimit  Лимит бюджета для категории.
     */
    public void addCategory(User user, String categoryName, double budgetLimit) {
        validateCategoryName(categoryName);
        validateBudgetLimit(budgetLimit);

        Category existingCategory = categoryRepository.findCategoryByName(user.getUsername(), categoryName);

        if (existingCategory != null) {
            throw new IllegalArgumentException("Категория с таким названием уже существует.");
        }

        Category newCategory = new Category(user.getUsername(), categoryName, budgetLimit);
        List<Category> categories = categoryRepository.loadCategories();
        categories.add(newCategory);
        categoryRepository.saveCategories(categories);

        System.out.println("Категория успешно добавлена.");
    }

    /**
     * Метод для переименования категории.
     *
     * @param user         Пользователь.
     * @param currentName  Текущее название категории.
     * @param newName      Новое название категории.
     */
    public void renameCategory(User user, String currentName, String newName) {
        validateCategoryName(newName);

        List<Category> categories = categoryRepository.findCategoriesByUserId(user.getUsername());
        for (Category category : categories) {
            if (category.getName().equals(currentName)) {
                category.setName(newName);
                categoryRepository.saveCategories(categories);
                System.out.println("Категория успешно переименована.");
                return;
            }
        }
        throw new IllegalArgumentException("Категория с названием \"" + currentName + "\" не найдена.");
    }

    /**
     * Обновить лимит бюджета для существующей категории.
     *
     * @param user         Пользователь.
     * @param categoryName Название категории.
     * @param newLimit     Новый лимит бюджета.
     */
    public void updateBudgetLimit(User user, String categoryName, double newLimit) {
        validateCategoryName(categoryName);
        validateBudgetLimit(newLimit);

        List<Category> categories = categoryRepository.loadCategories();
        boolean updated = false;

        for (Category category : categories) {
            if (category.getUserId().equals(user.getUsername()) && category.getName().equals(categoryName)) {
                category.setBudgetLimit(newLimit);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new IllegalArgumentException("Категория с таким названием не найдена.");
        }

        categoryRepository.saveCategories(categories);
        System.out.println("Лимит бюджета для категории \"" + categoryName + "\" успешно обновлён.");
    }

    /**
     * Список категорий пользователя.
     *
     * @param user Пользователь.
     */
    public void listCategories(User user) {
        List<Category> categories = categoryRepository.findCategoriesByUserId(user.getUsername());

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
        List<Category> categories = categoryRepository.findCategoriesByUserId(user.getUsername());

        List<Transaction> transactions = new ArrayList<>();
        for (Wallet wallet : walletRepository.loadWalletsByUser(user.getUsername())) {
            transactions.addAll(wallet.getTransactions());
        }

        for (Transaction transaction : transactions) {
            String categoryName = transaction.getCategory().getName();
            expensesByCategory.put(categoryName,
                    expensesByCategory.getOrDefault(categoryName, 0.0) + transaction.getAmount());
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
        if (!DataValidator.isNumberInRange(budgetLimit, 0, 100_000_000)) {
            throw new IllegalArgumentException("Некорректный лимит бюджета.");
        }
    }
}