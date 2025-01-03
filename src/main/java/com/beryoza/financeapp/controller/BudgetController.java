package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.BudgetService;

import java.util.Scanner;

/**
 * Контроллер для управления категориями и бюджетами.
 */
public class BudgetController {
    private final BudgetService budgetService;
    private final User user; // Текущий авторизованный пользователь
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
            System.out.println("2. Обновить лимит категории");
            System.out.println("3. Просмотреть список категорий");
            System.out.println("4. Подсчитать состояние бюджета по категориям");
            System.out.println("5. Вернуться в главное меню");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addCategory();
                    case "2" -> updateBudgetLimit();
                    case "3" -> listCategories();
                    case "4" -> calculateBudgetState();
                    case "5" -> {
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
            System.out.print("Введите лимит бюджета (введите 0, если лимит не нужен): ");
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
