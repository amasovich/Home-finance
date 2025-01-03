package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.BudgetService;

import java.util.Scanner;

/**
 * Контроллер для управления категориями и бюджетами.
 * <p>
 * Поля:
 * - {@link BudgetService} budgetService — сервис для работы с категориями и бюджетами.
 * - {@link User} user — текущий авторизованный пользователь.
 * - {@link Scanner} scanner — сканер для чтения пользовательского ввода.
 */
public class BudgetController {
    private final BudgetService budgetService;
    private final User user;
    private final Scanner scanner;

    /**
     * Конструктор для инициализации BudgetController.
     *
     * @param budgetService Сервис для работы с категориями и бюджетами.
     * @param user          Текущий авторизованный пользователь.
     * @param scanner       Сканер для чтения пользовательского ввода.
     */
    public BudgetController(BudgetService budgetService, User user, Scanner scanner) {
        this.budgetService = budgetService;
        this.user = user;
        this.scanner = scanner;
    }

    /**
     * Запуск главного меню для управления категориями и бюджетами.
     */
    public void start() {
        System.out.println("Управление категориями и бюджетами. Выберите действие:");
        while (true) {
            System.out.println("1. Добавить категорию");
            System.out.println("2. Переименовать категорию");
            System.out.println("3. Обновить лимит категории");
            System.out.println("4. Просмотреть список категорий");
            System.out.println("5. Подсчитать состояние бюджета по категориям");
            System.out.println("6. Вернуться в главное меню");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addCategory();
                    case "2" -> renameCategory();
                    case "3" -> updateBudgetLimit();
                    case "4" -> listCategories();
                    case "5" -> calculateBudgetState();
                    case "6" -> {
                        System.out.println("Выход в главное меню.");
                        return;
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Метод для добавления новой категории.
     */
    private void addCategory() {
        try {
            System.out.print("Введите название категории: ");
            String categoryName = scanner.nextLine();
            System.out.print("Введите лимит бюджета (от 0 до 100_000_000): ");
            double budgetLimit = Double.parseDouble(scanner.nextLine());

            budgetService.addCategory(user, categoryName, budgetLimit);
            System.out.println("Категория \"" + categoryName + "\" успешно добавлена.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число для лимита бюджета.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении категории: " + e.getMessage());
        }
    }

    /**
     * Метод для переименования категории.
     */
    private void renameCategory() {
        try {
            System.out.print("Введите текущее название категории: ");
            String currentName = scanner.nextLine();
            System.out.print("Введите новое название категории: ");
            String newName = scanner.nextLine();

            budgetService.renameCategory(user, currentName, newName);
            System.out.println("Категория успешно переименована.");
        } catch (Exception e) {
            System.out.println("Ошибка при переименовании категории: " + e.getMessage());
        }
    }

    /**
     * Метод для обновления лимита бюджета категории.
     */
    private void updateBudgetLimit() {
        try {
            System.out.print("Введите название категории: ");
            String categoryName = scanner.nextLine();
            System.out.print("Введите новый лимит бюджета: ");
            double newLimit = Double.parseDouble(scanner.nextLine());

            budgetService.updateBudgetLimit(user, categoryName, newLimit);
            System.out.println("Лимит для категории \"" + categoryName + "\" успешно обновлён.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число для нового лимита.");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении лимита категории: " + e.getMessage());
        }
    }

    /**
     * Метод для отображения списка всех категорий пользователя.
     */
    private void listCategories() {
        try {
            budgetService.listCategories(user);
        } catch (Exception e) {
            System.out.println("Ошибка при выводе списка категорий: " + e.getMessage());
        }
    }

    /**
     * Метод для подсчёта и отображения состояния бюджета по категориям.
     */
    private void calculateBudgetState() {
        try {
            budgetService.calculateBudgetState(user);
        } catch (Exception e) {
            System.out.println("Ошибка при подсчёте состояния бюджета: " + e.getMessage());
        }
    }
}
