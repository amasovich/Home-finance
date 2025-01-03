package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.service.UserService;
import com.beryoza.financeapp.service.WalletService;
import com.beryoza.financeapp.util.DataValidator;

import java.util.Scanner;

/**
 * Контроллер для управления кошельками пользователя.
 */
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;
    private final User user; // Текущий авторизованный пользователь
    private final Scanner scanner;

    /**
     * Конструктор для инициализации WalletController.
     *
     * @param walletService Сервис для работы с кошельками.
     * @param userService   Сервис для работы с пользователями.
     * @param user          Авторизованный пользователь.
     * @param scanner       Сканер для чтения пользовательского ввода.
     */
    public WalletController(WalletService walletService, UserService userService, User user, Scanner scanner) {
        this.walletService = walletService;
        this.userService = userService; // Инициализация UserService
        this.user = user;
        this.scanner = scanner;
    }

    /**
     * Запуск главного меню управления кошельками.
     */
    public void start() {
        System.out.println("Управление кошельками. Выберите действие:");
        while (true) {
            System.out.println("1. Добавить кошелёк");
            System.out.println("2. Удалить кошелёк");
            System.out.println("3. Переименовать кошелёк");
            System.out.println("4. Обновить баланс кошелка");
            System.out.println("5. Просмотреть список кошельков");
            System.out.println("6. Подсчитать доходы и расходы");
            System.out.println("7. Вывести данные по кошелькам и бюджету");
            System.out.println("8. Перевести средства между кошельками");
            System.out.println("9. Вернуться в главное меню");

            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addWallet();
                    case "2" -> removeWallet();
                    case "3" -> renameWallet();
                    case "4" -> updateWalletBalance();
                    case "5" -> listWallets();
                    case "6" -> calculateFinances();
                    case "7" -> displayBudgetData();
                    case "8" -> transferFunds();
                    case "9" -> {
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
     * Метод для добавления нового кошелька.
     */
    private void addWallet() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();
            System.out.print("Введите начальный баланс: ");
            double initialBalance = Double.parseDouble(scanner.nextLine());

            walletService.addWallet(user, walletName, initialBalance);
            System.out.println("Кошелёк успешно добавлен.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число для баланса.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении кошелька: " + e.getMessage());
        }
    }

    /**
     * Метод для удаления кошелька.
     */
    private void removeWallet() {
        try {
            System.out.print("Введите название кошелька для удаления: ");
            String walletName = scanner.nextLine();

            walletService.removeWallet(user, walletName);
            System.out.println("Кошелёк успешно удалён.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении кошелька: " + e.getMessage());
        }
    }

    /**
     * Метод для переименования кошелька.
     */
    private void renameWallet() {
        try {
            System.out.print("Введите текущее название кошелька: ");
            String currentName = scanner.nextLine();
            System.out.print("Введите новое название кошелька: ");
            String newName = scanner.nextLine();

            walletService.renameWallet(user, currentName, newName);
            System.out.println("Название кошелька успешно изменено.");
        } catch (Exception e) {
            System.out.println("Ошибка при переименовании кошелька: " + e.getMessage());
        }
    }

    /**
     * Метод для обновления баланса кошелька.
     */
    private void updateWalletBalance() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();
            System.out.print("Введите новый баланс: ");
            double newBalance = Double.parseDouble(scanner.nextLine());

            walletService.updateWalletBalance(user, walletName, newBalance);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число.");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении баланса: " + e.getMessage());
        }
    }

    /**
     * Метод для отображения списка всех кошельков пользователя.
     */
    private void listWallets() {
        try {
            walletService.listWallets(user);
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка кошельков: " + e.getMessage());
        }
    }

    /**
     * Метод для подсчёта общего дохода и расходов по всем кошелькам.
     */
    private void calculateFinances() {
        try {
            walletService.calculateFinances(user);
        } catch (Exception e) {
            System.out.println("Ошибка при подсчёте финансов: " + e.getMessage());
        }
    }

    /**
     * Метод для отображения данных по бюджету для каждого кошелька.
     */
    private void displayBudgetData() {
        try {
            walletService.displayBudgetData(user);
        } catch (Exception e) {
            System.out.println("Ошибка при отображении данных по бюджету: " + e.getMessage());
        }
    }

    /**
     * Метод для перевода средств между кошельками.
     */
    private void transferFunds() {
        try {
            System.out.print("Введите название вашего кошелька: ");
            String senderWallet = scanner.nextLine();
            System.out.print("Введите логин получателя: ");
            String receiverUsername = scanner.nextLine();
            System.out.print("Введите название кошелька получателя: ");
            String receiverWallet = scanner.nextLine();
            System.out.print("Введите сумму перевода: ");
            String amountInput = scanner.nextLine();

            // Проверка валидности суммы перевода
            if (!DataValidator.isNumeric(amountInput) || !DataValidator.isPositiveNumber(amountInput)) {
                System.out.println("Ошибка: Введите положительное число для суммы.");
                return;
            }
            double amount = Double.parseDouble(amountInput);

            // Ищем пользователя-получателя
            User receiverUser = userService.findUserByUsername(receiverUsername);
            if (receiverUser == null) {
                System.out.println("Ошибка: Пользователь с логином \"" + receiverUsername + "\" не найден.");
                return;
            }

            // Выполняем перевод
            walletService.transferFunds(user, senderWallet, receiverUser, receiverWallet, amount);
        } catch (Exception e) {
            System.out.println("Ошибка при переводе средств: " + e.getMessage());
        }
    }
}