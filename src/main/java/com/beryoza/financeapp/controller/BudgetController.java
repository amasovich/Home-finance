package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.BudgetService;
import com.beryoza.financeapp.service.WalletService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Контроллер для управления категориями и бюджетами.
 * Добавлены подсчёты состояния бюджета и оставшихся лимитов.
 */
public class BudgetController {
    private final BudgetService budgetService;
    private final WalletService walletService;
    private final User user; // Текущий авторизованный пользователь
    private final Scanner scanner;

    /**
     * Конструктор. Инициализирует сервисы и пользователя.
     *
     * @param budgetService Сервис для работы с категориями и бюджетами.
     * @param walletService Сервис для работы с кошельками.
     * @param user          Авторизованный пользователь.
     */
    public BudgetController(BudgetService budgetService, WalletService walletService, User user) {
        this.budgetService = budgetService;
        this.walletService = walletService;
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает интерфейс для работы с категориями и бюджетами.
     */
    public void start() {
        System.out.println("Управление категориями и бюджетами:");
        while (true) {
            System.out.println("1. Добавить категорию");
            System.out.println("2. Обновить лимит категории");
            System.out.println("3. Просмотреть список категорий");
            System.out.println("4. Подсчитать состояние бюджета по категориям");
            System.out.println("5. Вернуться в главное меню");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addCategory();
                case "2" -> updateBudgetLimit();
                case "3" -> listCategories();
                case "4" -> calculateBudgetState();
                case "5" -> {
                    System.out.println("Возвращаемся в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Добавить новую категорию.
     */
    private void addCategory() {
        System.out.print("Введите название категории: ");
        String categoryName = scanner.nextLine();
        System.out.print("Введите лимит бюджета (введите 0, если лимит не нужен): ");
        double budgetLimit;
        try {
            budgetLimit = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод лимита. Попробуйте снова.");
            return;
        }

        budgetService.addCategory(user, categoryName, budgetLimit);
        System.out.println("Категория успешно добавлена.");
    }

    /**
     * Обновить лимит для существующей категории.
     */
    private void updateBudgetLimit() {
        System.out.print("Введите название категории: ");
        String categoryName = scanner.nextLine();
        System.out.print("Введите новый лимит бюджета: ");
        double newLimit;
        try {
            newLimit = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод лимита. Попробуйте снова.");
            return;
        }

        budgetService.updateBudgetLimit(user, categoryName, newLimit);
        System.out.println("Лимит бюджета успешно обновлён.");
    }

    /**
     * Просмотреть список всех категорий.
     */
    private void listCategories() {
        List<Category> categories = budgetService.getCategories(user);
        System.out.println("Ваши категории:");
        for (Category category : categories) {
            System.out.println("- " + category.getName() +
                    (category.getBudgetLimit() > 0 ? " (Лимит: " + category.getBudgetLimit() + ")" : ""));
        }
    }

    /**
     * Подсчитать состояние бюджета по категориям.
     */
    private void calculateBudgetState() {
        Map<String, Double> expensesByCategory = walletService.getWallets(user).stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        System.out.println("Состояние бюджета по категориям:");
        for (Category category : budgetService.getCategories(user)) {
            double expenses = expensesByCategory.getOrDefault(category.getName(), 0.0);
            double remainingBudget = category.getBudgetLimit() - Math.abs(expenses);
            System.out.println("- " + category.getName() + ": Лимит: " + category.getBudgetLimit() +
                    ", Расходы: " + Math.abs(expenses) +
                    ", Оставшийся бюджет: " + remainingBudget);
        }
    }
}