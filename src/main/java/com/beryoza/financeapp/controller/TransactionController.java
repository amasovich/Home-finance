package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.WalletService;

import java.util.Scanner;

/**
 * Контроллер для управления транзакциями.
 */
public class TransactionController {
    private final WalletService walletService;
    private final User user;
    private final Scanner scanner;

    /**
     * Конструктор для инициализации TransactionController.
     *
     * @param walletService Сервис для работы с кошельками и транзакциями.
     * @param user          Авторизованный пользователь.
     * @param scanner       Сканер для чтения пользовательского ввода.
     */
    public TransactionController(WalletService walletService, User user, Scanner scanner) {
        this.walletService = walletService;
        this.user = user;
        this.scanner = scanner;
    }

    /**
     * Запуск главного меню управления транзакциями.
     */
    public void start() {
        System.out.println("Управление транзакциями:");
        while (true) {
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Просмотреть транзакции");
            System.out.println("4. Удалить транзакцию");
            System.out.println("5. Редактировать транзакцию");
            System.out.println("6. Подсчитать по категориям");
            System.out.println("7. Вернуться в главное меню");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addTransaction(true);
                case "2" -> addTransaction(false);
                case "3" -> listTransactions();
                case "4" -> deleteTransaction();
                case "5" -> editTransaction();
                case "6" -> calculateByCategories();
                case "7" -> {
                    System.out.println("Выход в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Метод для добавления транзакции (доход или расход).
     *
     * @param isIncome Если true, то это доход, иначе расход.
     */
    private void addTransaction(boolean isIncome) {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        System.out.print("Введите сумму: ");
        String amountInput = scanner.nextLine();
        System.out.print("Введите категорию: ");
        String categoryName = scanner.nextLine();

        walletService.addTransaction(user, walletName, amountInput, categoryName, isIncome);
    }

    /**
     * Метод для удаления транзакции.
     */
    private void deleteTransaction() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        System.out.print("Введите ID транзакции для удаления: ");
        String transactionId = scanner.nextLine();

        walletService.deleteTransaction(user, walletName, transactionId);
    }

    /**
     * Метод для отображения списка транзакций.
     */
    private void listTransactions() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();

        walletService.listTransactions(user, walletName);
    }

    /**
     * Метод для редактирования транзакции.
     */
    public void editTransaction() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        System.out.print("Введите ID транзакции для редактирования: ");
        String transactionId = scanner.nextLine();
        System.out.print("Введите новую сумму транзакции: ");
        String newAmountStr = scanner.nextLine();
        System.out.print("Введите новую категорию: ");
        String newCategory = scanner.nextLine();
        System.out.print("Введите новую дату транзакции (yyyy-MM-dd): ");
        String newDateStr = scanner.nextLine();

        walletService.editTransaction(user, walletName, transactionId, newAmountStr, newCategory, newDateStr);
    }

    /**
     * Метод для подсчёта транзакций по категориям.
     */
    private void calculateByCategories() {
        walletService.calculateByCategories(user);
    }
}