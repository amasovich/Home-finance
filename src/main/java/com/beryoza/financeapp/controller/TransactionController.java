package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.BudgetService;
import com.beryoza.financeapp.service.WalletService;

import java.util.List;
import java.util.Scanner;

/**
 * Контроллер для управления транзакциями.
 * <p>
 * Поля:
 * - {@link WalletService} walletService — сервис для работы с кошельками и транзакциями.
 * - {@link BudgetService} budgetService — сервис для работы с бюджетами.
 * - {@link User} user — текущий авторизованный пользователь.
 * - {@link Scanner} scanner — сканер для чтения пользовательского ввода.
 */
public class TransactionController {
    private final WalletService walletService;
    private final BudgetService budgetService;
    private final User user;
    private final Scanner scanner;

    /**
     * Конструктор для инициализации TransactionController.
     *
     * @param walletService Сервис для работы с кошельками и транзакциями.
     * @param budgetService Сервис для работы с бюджетами.
     * @param user          Авторизованный пользователь.
     * @param scanner       Сканер для чтения пользовательского ввода.
     */
    public TransactionController(WalletService walletService, BudgetService budgetService, User user, Scanner scanner) {
        this.walletService = walletService;
        this.budgetService = budgetService;
        this.user = user;
        this.scanner = scanner;
    }

    /**
     * Запуск главного меню управления транзакциями.
     */
    public void start() {
        System.out.println("Управление транзакциями. Выберите действие:");
        while (true) {
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Просмотреть транзакции");
            System.out.println("4. Удалить транзакцию");
            System.out.println("5. Редактировать транзакцию");
            System.out.println("6. Вернуться в главное меню");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addTransaction(true);
                    case "2" -> addTransaction(false);
                    case "3" -> listTransactions();
                    case "4" -> deleteTransaction();
                    case "5" -> editTransaction();
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
     * Метод для добавления транзакции (доход или расход).
     *
     * @param isIncome Если true, то это доход, иначе расход.
     */
    private void addTransaction(boolean isIncome) {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();
            System.out.print("Введите сумму: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Введите категорию: ");
            String categoryName = scanner.nextLine();

            walletService.addTransaction(user, walletName, amount, categoryName, isIncome);

            List<String> warnings = budgetService.checkBudgetLimits(user);
            if (!warnings.isEmpty()) {
                System.out.println("Предупреждения:");
                warnings.forEach(System.out::println);
            }

            String expenseWarning = walletService.checkExpenseExceedsIncome(user);
            if (!expenseWarning.isEmpty()) {
                System.out.println(expenseWarning);
            }

            System.out.println("Транзакция успешно добавлена.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число для суммы.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении транзакции: " + e.getMessage());
        }
    }

    /**
     * Метод для удаления транзакции.
     */
    private void deleteTransaction() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();
            System.out.print("Введите ID транзакции для удаления: ");
            String transactionId = scanner.nextLine();

            walletService.deleteTransaction(user, walletName, transactionId);
            System.out.println("Транзакция успешно удалена.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении транзакции: " + e.getMessage());
        }
    }

    /**
     * Метод для отображения списка транзакций.
     */
    private void listTransactions() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();

            walletService.listTransactions(user, walletName);
        } catch (Exception e) {
            System.out.println("Ошибка при отображении транзакций: " + e.getMessage());
        }
    }

    /**
     * Метод для редактирования транзакции.
     */
    private void editTransaction() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();
            System.out.print("Введите ID транзакции для редактирования: ");
            String transactionId = scanner.nextLine();
            System.out.print("Введите новую сумму транзакции: ");
            double newAmount = Double.parseDouble(scanner.nextLine());
            System.out.print("Введите новую категорию: ");
            String newCategory = scanner.nextLine();
            System.out.print("Введите новую дату транзакции (yyyy-MM-dd): ");
            String newDateStr = scanner.nextLine();

            walletService.editTransaction(user, walletName, transactionId, newAmount, newCategory, newDateStr);

            List<String> warnings = budgetService.checkBudgetLimits(user);
            if (!warnings.isEmpty()) {
                System.out.println("Предупреждения:");
                warnings.forEach(System.out::println);
            }

            String expenseWarning = walletService.checkExpenseExceedsIncome(user);
            if (!expenseWarning.isEmpty()) {
                System.out.println(expenseWarning);
            }

            System.out.println("Транзакция успешно отредактирована.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число для суммы.");
        } catch (Exception e) {
            System.out.println("Ошибка при редактировании транзакции: " + e.getMessage());
        }
    }
}