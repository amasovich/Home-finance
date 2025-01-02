package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.util.DataValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Сервис для управления кошельками и транзакциями.
 */
public class WalletService {
    private final WalletRepository walletRepository;

    /**
     * Конструктор для инициализации WalletService.
     *
     * @param walletRepository Репозиторий для работы с кошельками и транзакциями.
     */
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Добавить новый кошелёк.
     *
     * @param user          Пользователь, которому добавляется кошелёк.
     * @param walletName    Название нового кошелька.
     * @param initialBalance Начальный баланс кошелька.
     */
    public void addWallet(User user, String walletName, double initialBalance) {
        try {
            // Проверка допустимости названия кошелька
            if (walletName == null || walletName.isBlank()) {
                throw new IllegalArgumentException("Название кошелька не может быть пустым.");
            }

            // Проверка длины названия кошелька
            if (!DataValidator.isStringLengthValid(walletName, 50)) {
                throw new IllegalArgumentException("Название кошелька не может быть длиннее 50 символов.");
            }

            // Проверка, что начальный баланс положительный
            if (!DataValidator.isPositiveNumber(String.valueOf(initialBalance))) {
                throw new IllegalArgumentException("Начальный баланс должен быть положительным числом.");
            }

            // Проверка диапазона начального баланса
            if (!DataValidator.isNumberInRange(initialBalance, 0, 100_000_000)) {
                throw new IllegalArgumentException("Начальный баланс должен быть в диапазоне от 0 до 100 000 000.");
            }

            // Проверка уникальности названия кошелька
            List<String> existingWalletNames = new ArrayList<>();
            for (Wallet wallet : user.getWallets()) {
                existingWalletNames.add(wallet.getName());
            }

            if (!DataValidator.isUniqueName(walletName, existingWalletNames)) {
                throw new IllegalArgumentException("Кошелёк с таким названием уже существует.");
            }

            // Создание нового кошелька
            Wallet wallet = new Wallet(walletName, initialBalance);
            user.addWallet(wallet);

            // Сохранение изменений
            walletRepository.saveWallets(user.getWallets(), user.getUsername());
            System.out.println("Кошелёк успешно добавлен.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении кошелька: " + e.getMessage());
        }
    }

    /**
     * Удалить кошелёк.
     *
     * @param user       Пользователь.
     * @param walletName Название кошелька для удаления.
     */
    public void removeWallet(User user, String walletName) {
        try {
            // Проверка, что название кошелька не пустое
            if (!DataValidator.isNonEmptyString(walletName)) {
                throw new IllegalArgumentException("Название кошелька не может быть пустым.");
            }

            // Проверка длины названия кошелька
            if (!DataValidator.isStringLengthValid(walletName, 50)) {
                throw new IllegalArgumentException("Название кошелька не может быть длиннее 50 символов.");
            }

            // Поиск кошелька
            Wallet wallet = user.findWalletByName(walletName);

            if (wallet == null) {
                throw new IllegalArgumentException("Кошелёк с таким названием не найден.");
            }

            // Удаление кошелька
            user.removeWallet(wallet);

            // Сохранение изменений
            walletRepository.saveWallets(user.getWallets(), user.getUsername());
            System.out.println("Кошелёк успешно удалён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при удалении кошелька: " + e.getMessage());
        }
    }

    /**
     * Вывести список кошельков пользователя.
     *
     * @param user Пользователь.
     */
    public void listWallets(User user) {
        // Проверяем, есть ли кошельки у пользователя
        if (user.getWallets().isEmpty()) {
            System.out.println("У вас нет кошельков.");
            return;
        }

        // Выводим список кошельков
        System.out.println("Ваши кошельки:");
        for (Wallet wallet : user.getWallets()) {
            System.out.println("- " + wallet.getName() + " (Баланс: " + wallet.getBalance() + ")");
        }
    }

    /**
     * Подсчитать общий доход и расходы по всем кошелькам.
     *
     * @param user Пользователь.
     */
    public void calculateFinances(User user) {
        // Инициализация переменных для общего дохода и расходов
        double totalIncome = 0;
        double totalExpenses = 0;

        // Проходим по всем кошелькам пользователя
        for (Wallet wallet : user.getWallets()) {
            // Обрабатываем транзакции каждого кошелька
            for (Transaction transaction : wallet.getTransactions()) {
                double amount = transaction.getAmount();
                if (amount > 0) {
                    // Если сумма положительная, добавляем к доходу
                    totalIncome += amount;
                } else {
                    // Если сумма отрицательная, добавляем к расходам
                    totalExpenses += amount;
                }
            }
        }

        // Вывод общего дохода и расходов
        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общие расходы: " + Math.abs(totalExpenses));
    }

    /**
     * Вывести данные по бюджету для каждого кошелька.
     *
     * @param user Пользователь.
     */
    public void displayBudgetData(User user) {
        // Проходим по всем кошелькам пользователя
        for (Wallet wallet : user.getWallets()) {
            System.out.println("Кошелёк: " + wallet.getName());

            // Выводим все транзакции для текущего кошелька
            for (Transaction transaction : wallet.getTransactions()) {
                System.out.println("- Транзакция: " + transaction.getAmount() +
                        " (" + transaction.getCategory().getName() + ")");
            }

            // Выводим текущий баланс кошелька
            System.out.println("Текущий баланс: " + wallet.getBalance());
        }
    }

    /**
     * Добавить транзакцию.
     *
     * @param user          Пользователь.
     * @param walletName    Название кошелька.
     * @param amountInput   Сумма транзакции (строка).
     * @param categoryName  Название категории транзакции.
     * @param isIncome      Если true, транзакция доходная, иначе расход.
     */
    public void addTransaction(User user, String walletName, String amountInput, String categoryName, boolean isIncome) {
        try {
            // Проверка, что название кошелька не пустое
            if (!DataValidator.isNonEmptyString(walletName)) {
                throw new IllegalArgumentException("Название кошелька не может быть пустым.");
            }

            // Проверка длины названия кошелька
            if (!DataValidator.isStringLengthValid(walletName, 50)) {
                throw new IllegalArgumentException("Название кошелька не может быть длиннее 50 символов.");
            }

            // Проверка суммы транзакции
            double amount;
            try {
                amount = Double.parseDouble(amountInput);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Сумма транзакции должна быть числом.");
            }

            if (!isIncome) {
                amount = -amount;
            }

            if (!DataValidator.isNumberInRange(amount, -10_000_000, 10_000_000)) {
                throw new IllegalArgumentException("Сумма транзакции должна быть в диапазоне от -10 000 000 до 10 000 000.");
            }

            // Проверка, что категория не пустая
            if (!DataValidator.isNonEmptyString(categoryName)) {
                throw new IllegalArgumentException("Название категории не может быть пустым.");
            }

            // Проверка длины названия категории
            if (!DataValidator.isStringLengthValid(categoryName, 30)) {
                throw new IllegalArgumentException("Название категории не может быть длиннее 30 символов.");
            }

            // Поиск кошелька
            Wallet wallet = user.findWalletByName(walletName);

            if (wallet == null) {
                throw new IllegalArgumentException("Кошелёк с таким названием не найден.");
            }

            // Создание транзакции и добавление её в кошелёк
            Transaction transaction = new Transaction(amount, new Category(categoryName), LocalDate.now());
            wallet.addTransaction(transaction);

            // Сохранение транзакции
            walletRepository.saveTransactions(wallet, user.getUsername());
            System.out.println("Транзакция успешно добавлена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении транзакции: " + e.getMessage());
        }
    }

    /**
     * Удалить транзакцию.
     *
     * @param user          Пользователь.
     * @param walletName    Название кошелька.
     * @param transactionId ID транзакции для удаления.
     */
    public void deleteTransaction(User user, String walletName, String transactionId) {
        try {
            // Проверка, что название кошелька не пустое
            if (!DataValidator.isNonEmptyString(walletName)) {
                throw new IllegalArgumentException("Название кошелька не может быть пустым.");
            }

            // Проверка длины названия кошелька
            if (!DataValidator.isStringLengthValid(walletName, 50)) {
                throw new IllegalArgumentException("Название кошелька не может быть длиннее 50 символов.");
            }

            // Проверка, что ID транзакции не пустое
            if (!DataValidator.isNonEmptyString(transactionId)) {
                throw new IllegalArgumentException("ID транзакции не может быть пустым.");
            }

            // Поиск кошелька
            Wallet wallet = user.findWalletByName(walletName);

            if (wallet == null) {
                throw new IllegalArgumentException("Кошелёк с таким названием не найден.");
            }

            // Поиск транзакции
            Transaction transaction = wallet.findTransactionById(transactionId);
            if (transaction == null) {
                throw new IllegalArgumentException("Транзакция с таким ID не найдена.");
            }

            // Удаление транзакции
            wallet.removeTransaction(transaction);

            // Сохранение изменений
            walletRepository.saveTransactions(wallet, user.getUsername());
            System.out.println("Транзакция успешно удалена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при удалении транзакции: " + e.getMessage());
        }
    }

    /**
     * Редактировать транзакцию.
     *
     * @param user          Пользователь.
     * @param walletName    Название кошелька.
     * @param transactionId ID транзакции для редактирования.
     * @param newAmountStr  Новая сумма транзакции.
     * @param newCategory   Новая категория транзакции.
     * @param newDateStr    Новая дата транзакции в формате "yyyy-MM-dd".
     */
    public void editTransaction(User user, String walletName, String transactionId, String newAmountStr, String newCategory, String newDateStr) {
        try {
            // Проверка, что название кошелька не пустое
            if (!DataValidator.isNonEmptyString(walletName)) {
                throw new IllegalArgumentException("Название кошелька не может быть пустым.");
            }

            // Проверка длины названия кошелька
            if (!DataValidator.isStringLengthValid(walletName, 50)) {
                throw new IllegalArgumentException("Название кошелька не может быть длиннее 50 символов.");
            }

            // Проверка, что ID транзакции не пустое
            if (!DataValidator.isNonEmptyString(transactionId)) {
                throw new IllegalArgumentException("ID транзакции не может быть пустым.");
            }

            // Поиск кошелька
            Wallet wallet = user.findWalletByName(walletName);

            if (wallet == null) {
                throw new IllegalArgumentException("Кошелёк с таким названием не найден.");
            }

            // Поиск транзакции
            Transaction transaction = wallet.findTransactionById(transactionId);
            if (transaction == null) {
                throw new IllegalArgumentException("Транзакция с таким ID не найдена.");
            }

            // Проверка суммы транзакции
            double newAmount;
            try {
                newAmount = Double.parseDouble(newAmountStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Сумма должна быть числом.");
            }

            if (!DataValidator.isNumberInRange(newAmount, -10_000_000, 10_000_000)) {
                throw new IllegalArgumentException("Сумма транзакции должна быть в диапазоне от -10 000 000 до 10 000 000.");
            }

            // Проверка категории
            if (!DataValidator.isNonEmptyString(newCategory)) {
                throw new IllegalArgumentException("Название категории не может быть пустым.");
            }

            if (!DataValidator.isStringLengthValid(newCategory, 30)) {
                throw new IllegalArgumentException("Название категории не может быть длиннее 30 символов.");
            }

            // Проверка даты транзакции
            if (!DataValidator.isValidDate(newDateStr, "yyyy-MM-dd")) {
                throw new IllegalArgumentException("Некорректный формат даты. Ожидается формат yyyy-MM-dd.");
            }

            // Обновление данных транзакции
            transaction.setAmount(newAmount);
            transaction.setCategory(new Category(newCategory));
            transaction.setDate(LocalDate.parse(newDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            // Сохранение изменений
            walletRepository.saveTransactions(wallet, user.getUsername());
            System.out.println("Транзакция успешно отредактирована.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении изменений: " + e.getMessage());
        }
    }

    /**
     * Подсчитать транзакции по категориям.
     *
     * @param user Пользователь.
     */
    public void calculateByCategories(User user) {
        // Словарь для хранения итоговых сумм по категориям
        Map<String, Double> totals = new HashMap<>();

        // Проходим по всем кошелькам пользователя
        for (Wallet wallet : user.getWallets()) {
            // Для каждого кошелька обрабатываем транзакции
            for (Transaction transaction : wallet.getTransactions()) {
                String categoryName = transaction.getCategory().getName();
                double currentTotal = totals.getOrDefault(categoryName, 0.0);
                totals.put(categoryName, currentTotal + transaction.getAmount());
            }
        }

        // Проверка наличия данных для подсчёта
        if (totals.isEmpty()) {
            System.out.println("Нет данных для подсчёта.");
            return;
        }

        // Вывод итогов по категориям
        System.out.println("Доходы и расходы по категориям:");
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Отобразить список транзакций для кошелька.
     *
     * @param user       Пользователь.
     * @param walletName Название кошелька.
     */
    public void listTransactions(User user, String walletName) {
        try {
            // Проверка, что название кошелька не пустое
            if (!DataValidator.isNonEmptyString(walletName)) {
                throw new IllegalArgumentException("Название кошелька не может быть пустым.");
            }

            // Поиск кошелька
            Wallet wallet = user.findWalletByName(walletName);

            if (wallet == null) {
                throw new IllegalArgumentException("Кошелёк с названием \"" + walletName + "\" не найден.");
            }

            // Загрузка транзакций
            List<Transaction> transactions = walletRepository.loadTransactions(walletName, user.getUsername());

            // Проверяем наличие транзакций
            if (transactions.isEmpty()) {
                System.out.println("Транзакции отсутствуют.");
            } else {
                // Вывод транзакций
                System.out.println("Транзакции для кошелька \"" + walletName + "\":");
                for (Transaction transaction : transactions) {
                    System.out.println("- " + transaction.getDate() + ": " + transaction.getAmount() +
                            " (" + transaction.getCategory().getName() + ")");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке транзакций: " + e.getMessage());
        }
    }
}