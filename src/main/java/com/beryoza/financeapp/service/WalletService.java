package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.repository.CategoryRepository;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.util.DataValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
     * Добавить новый кошелёк для пользователя.
     *
     * @param user           Пользователь, которому добавляется кошелёк.
     * @param walletName     Название нового кошелька.
     * @param initialBalance Начальный баланс кошелька.
     */
    public void addWallet(User user, String walletName, double initialBalance) {
        try {
            validateWalletName(walletName);
            validateBalance(initialBalance);

            Wallet newWallet = new Wallet(user.getUsername(), walletName, initialBalance);

            walletRepository.saveWallet(newWallet);
            System.out.println("Кошелёк успешно добавлен.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении кошелька: " + e.getMessage());
        }
    }

    /**
     * Удалить кошелёк пользователя.
     *
     * @param user       Пользователь.
     * @param walletName Название кошелька для удаления.
     */
    public void removeWallet(User user, String walletName) {
        try {
            validateWalletName(walletName);

            // Загружаем кошельки пользователя
            List<Wallet> userWallets = walletRepository.loadWalletsByUser(user.getUsername());

            // Ищем кошелёк для удаления
            Wallet walletToRemove = null;
            for (Wallet wallet : userWallets) {
                if (wallet.getName().equals(walletName)) {
                    walletToRemove = wallet;
                    break;
                }
            }

            if (walletToRemove == null) {
                throw new IllegalArgumentException("Кошелёк с таким названием не найден.");
            }

            // Удаляем кошелёк
            userWallets.remove(walletToRemove);
            walletRepository.saveWallets(userWallets);

            System.out.println("Кошелёк успешно удалён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Вывести список кошельков пользователя.
     *
     * @param user Пользователь.
     */
    public void listWallets(User user) {
        try {
            List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());

            if (wallets.isEmpty()) {
                System.out.println("У вас нет кошельков.");
                return;
            }

            System.out.println("Ваши кошельки:");
            for (Wallet wallet : wallets) {
                System.out.println("- " + wallet.getName() + " (Баланс: " + wallet.getBalance() + ")");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке кошельков: " + e.getMessage());
        }
    }

    /**
     * Валидация имени кошелька.
     *
     * @param walletName Название кошелька.
     */
    private void validateWalletName(String walletName) {
        if (!DataValidator.isNonEmptyString(walletName) || !DataValidator.isStringLengthValid(walletName, 50)) {
            throw new IllegalArgumentException("Некорректное название кошелька.");
        }
    }

    /**
     * Валидация баланса кошелька.
     *
     * @param balance Баланс кошелька.
     */
    private void validateBalance(double balance) {
        if (!DataValidator.isPositiveNumber(String.valueOf(balance)) ||
                !DataValidator.isNumberInRange(balance, 1, 100_000_000)) {
            throw new IllegalArgumentException("Некорректный баланс.");
        }
    }

    /**
     * Подсчитать общий доход и расходы по всем кошелькам.
     *
     * @param user Пользователь.
     */
    public void calculateFinances(User user) {
        double totalIncome = 0;
        double totalExpenses = 0;

        List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
        for (Wallet wallet : wallets) {
            for (Transaction transaction : wallet.getTransactions()) {
                if (transaction.getAmount() > 0) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += transaction.getAmount();
                }
            }
        }

        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общие расходы: " + Math.abs(totalExpenses));
    }

    /**
     * Вывести данные по бюджету для каждого кошелька.
     *
     * @param user Пользователь.
     */
    public void displayBudgetData(User user) {
        CategoryRepository categoryRepository = new CategoryRepository();
        List<Category> userCategories = categoryRepository.findCategoriesByUserId(user.getUsername());
        List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());

        for (Wallet wallet : wallets) {
            System.out.println("Кошелёк: " + wallet.getName());
            System.out.printf("Баланс: %.2f\n", wallet.getBalance());
            System.out.println("Транзакции:");

            for (Transaction transaction : wallet.getTransactions()) {
                String categoryName = transaction.getCategory().getName();
                String transactionCategory = userCategories.stream()
                        .anyMatch(c -> c.getName().equals(categoryName)) ? transaction.getCategory().getName() : "[Категория не найдена]";

                System.out.printf("  - Дата: %s, Сумма: %.2f, Категория: %s\n",
                        transaction.getDate(), transaction.getAmount(), transactionCategory);
            }

            System.out.println();
        }
    }

    /**
     * Добавить транзакцию в кошелёк.
     *
     * @param user         Пользователь.
     * @param walletName   Название кошелька.
     * @param amount       Сумма транзакции.
     * @param categoryName Название категории транзакции.
     * @param isIncome     Указывает, является ли транзакция доходом.
     */
    public void addTransaction(User user, String walletName, double amount, String categoryName, boolean isIncome) {
        try {
            List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());

            Wallet targetWallet = null;
            for (Wallet wallet : wallets) {
                if (wallet.getName().equals(walletName)) {
                    targetWallet = wallet;
                    break;
                }
            }

            if (targetWallet == null) {
                throw new IllegalArgumentException("Кошелёк с названием \"" + walletName + "\" не найден.");
            }

            CategoryRepository categoryRepository = new CategoryRepository();
            Category category = categoryRepository.findCategoryByName(user.getUsername(), categoryName);

            if (category == null) {
                throw new IllegalArgumentException("Категория с названием \"" + categoryName + "\" не найдена.");
            }

            double adjustedAmount = isIncome ? amount : -amount;
            Transaction transaction = new Transaction(adjustedAmount, category, LocalDate.now());
            targetWallet.addTransaction(transaction);

            walletRepository.saveWallet(targetWallet);
            System.out.println("Транзакция успешно добавлена.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении транзакции: " + e.getMessage());
        }
    }

    /**
     * Удалить транзакцию из кошелька.
     *
     * @param user          Пользователь.
     * @param walletName    Название кошелька.
     * @param transactionId ID транзакции.
     */
    public void deleteTransaction(User user, String walletName, String transactionId) {
        try {
            List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
            for (Wallet wallet : wallets) {
                if (wallet.getName().equals(walletName)) {
                    Transaction transaction = wallet.findTransactionById(transactionId);
                    if (transaction != null) {
                        wallet.removeTransaction(transaction);
                        walletRepository.saveWallet(wallet);
                        System.out.println("Транзакция успешно удалена.");
                        return;
                    }
                }
            }
            System.out.println("Кошелёк или транзакция не найдены.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении транзакции: " + e.getMessage());
        }
    }

    /**
     * Редактировать транзакцию.
     *
     * @param user            Пользователь.
     * @param walletName      Название кошелька.
     * @param transactionId   ID транзакции.
     * @param newAmount       Новая сумма.
     * @param newCategoryName Новая категория транзакции.
     * @param newDateStr      Новая дата в формате yyyy-MM-dd.
     */
    public void editTransaction(User user, String walletName, String transactionId, double newAmount, String newCategoryName, String newDateStr) {
        try {
            // Проверка формата даты
            if (!DataValidator.isValidDate(newDateStr, "yyyy-MM-dd")) {
                throw new IllegalArgumentException("Дата \"" + newDateStr + "\" имеет неверный формат. Ожидается формат yyyy-MM-dd.");
            }

            List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
            for (Wallet wallet : wallets) {
                if (wallet.getName().equals(walletName)) {
                    Transaction transaction = wallet.findTransactionById(transactionId);
                    if (transaction != null) {
                        CategoryRepository categoryRepository = new CategoryRepository();
                        Category newCategory = categoryRepository.findCategoryByName(user.getUsername(), newCategoryName);

                        if (newCategory == null) {
                            throw new IllegalArgumentException("Категория с названием \"" + newCategoryName + "\" не найдена.");
                        }

                        transaction.setAmount(newAmount);
                        transaction.setCategory(newCategory);
                        transaction.setDate(LocalDate.parse(newDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                        walletRepository.saveWallet(wallet);
                        System.out.println("Транзакция успешно отредактирована.");
                        return;
                    }
                }
            }
            System.out.println("Кошелёк или транзакция не найдены.");
        } catch (Exception e) {
            System.out.println("Ошибка при редактировании транзакции: " + e.getMessage());
        }
    }

    /**
     * Подсчитать доходы и расходы по категориям.
     *
     * @param user Пользователь.
     */
    public void calculateByCategories(User user) {
        Map<String, Double> categoryTotals = new HashMap<>();
        CategoryRepository categoryRepository = new CategoryRepository();
        List<Category> userCategories = categoryRepository.findCategoriesByUserId(user.getUsername());

        List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
        for (Wallet wallet : wallets) {
            for (Transaction transaction : wallet.getTransactions()) {
                String categoryName = transaction.getCategory().getName();
                if (userCategories.stream().anyMatch(c -> c.getName().equals(categoryName))) {
                    categoryTotals.put(categoryName, categoryTotals.getOrDefault(categoryName, 0.0) + transaction.getAmount());
                }
            }
        }

        System.out.println("Доходы и расходы по категориям:");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Вывести список транзакций для указанного кошелька.
     *
     * @param user       Пользователь.
     * @param walletName Название кошелька.
     */
    public void listTransactions(User user, String walletName) {
        try {
            List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
            for (Wallet wallet : wallets) {
                if (wallet.getName().equals(walletName)) {
                    System.out.println("Транзакции для кошелька \"" + walletName + "\":");
                    for (Transaction transaction : wallet.getTransactions()) {
                        System.out.printf("  - Дата: %s, Сумма: %.2f, Категория: %s, ID: %s\n",
                                transaction.getDate(),
                                transaction.getAmount(),
                                transaction.getCategory().getName(),
                                transaction.getId());
                    }
                    return;
                }
            }
            System.out.println("Кошелёк с названием \"" + walletName + "\" не найден.");
        } catch (Exception e) {
            System.out.println("Ошибка при выводе списка транзакций: " + e.getMessage());
        }
    }
}
