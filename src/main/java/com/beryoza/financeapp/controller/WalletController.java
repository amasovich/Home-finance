package com.beryoza.financeapp.controller;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.service.WalletService;

import java.util.Scanner;

/**
 * Контроллер для управления кошельками пользователя.
 * Позволяет добавлять, удалять кошельки и просматривать их список.
 */
public class WalletController {
    private final WalletService walletService;
    private final User user; // Текущий авторизованный пользователь
    private final Scanner scanner;

    /**
     * Конструктор. Инициализирует сервис кошельков и пользователя.
     *
     * @param walletService Сервис для работы с кошельками.
     * @param user          Авторизованный пользователь.
     */
    public WalletController(WalletService walletService, User user) {
        this.walletService = walletService;
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запускает интерфейс для работы с кошельками.
     */
    public void start() {
        System.out.println("Управление кошельками:");
        while (true) {
            System.out.println("1. Добавить кошелёк");
            System.out.println("2. Удалить кошелёк");
            System.out.println("3. Просмотреть список кошельков");
            System.out.println("4. Вернуться в главное меню");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addWallet();
                case "2" -> removeWallet();
                case "3" -> listWallets();
                case "4" -> {
                    System.out.println("Возвращаемся в главное меню.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    /**
     * Добавить новый кошелёк.
     */
    private void addWallet() {
        System.out.print("Введите название кошелька: ");
        String walletName = scanner.nextLine();
        System.out.print("Введите начальный баланс: ");
        double initialBalance;
        try {
            initialBalance = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод баланса. Попробуйте снова.");
            return;
        }

        walletService.addWallet(user, walletName, initialBalance);
        System.out.println("Кошелёк успешно добавлен.");
    }

    /**
     * Удалить существующий кошелёк.
     */
    private void removeWallet() {
        System.out.print("Введите название кошелька для удаления: ");
        String walletName = scanner.nextLine();

        walletService.removeWallet(user, walletName);
        System.out.println("Кошелёк успешно удалён.");
    }

    /**
     * Просмотреть список всех кошельков пользователя.
     */
    private void listWallets() {
        System.out.println("Ваши кошельки:");
        for (Wallet wallet : walletService.getWallets(user)) {
            System.out.println("- " + wallet.getName() + " (Баланс: " + wallet.getBalance() + ")");
        }
    }
}