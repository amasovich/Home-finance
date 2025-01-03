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
 * Предоставляет функциональность для работы с кошельками, их балансами, транзакциями,
 * а также взаимодействие с категориями транзакций.
 * <p>
 * Поля:
 * - {@link WalletRepository} walletRepository — репозиторий для работы с кошельками и транзакциями.
 * - {@link CategoryRepository} categoryRepository — репозиторий для работы с категориями транзакций.
 */
public class WalletService {
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Конструктор для инициализации WalletService.
     *
     * @param walletRepository   Репозиторий для работы с кошельками и транзакциями.
     * @param categoryRepository Репозиторий для работы с категориями транзакций.
     */
    public WalletService(WalletRepository walletRepository, CategoryRepository categoryRepository) {
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
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

            List<Wallet> userWallets = walletRepository.loadWalletsByUser(user.getUsername());

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

            userWallets.remove(walletToRemove);
            walletRepository.saveWallets(userWallets);

            System.out.println("Кошелёк успешно удалён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Метод для переименования кошелька.
     *
     * @param user         Пользователь.
     * @param currentName  Текущее название кошелька.
     * @param newName      Новое название кошелька.
     */
    public void renameWallet(User user, String currentName, String newName) {
        validateWalletName(newName);
        List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());
        Wallet walletToRename = null;

        for (Wallet wallet : wallets) {
            if (wallet.getName().equals(currentName)) {
                walletToRename = wallet;
                break;
            }
        }

        if (walletToRename == null) {
            throw new IllegalArgumentException("Кошелёк с названием \"" + currentName + "\" не найден.");
        }

        walletToRename.setName(newName);
        walletRepository.saveWallets(wallets);
    }

    /**
     * Метод для обновления баланса кошелька.
     *
     * @param user         Пользователь.
     * @param walletName   Название кошелька.
     * @param newBalance   Новый баланс кошелька.
     */
    public void updateWalletBalance(User user, String walletName, double newBalance) {
        validateBalance(newBalance);
        List<Wallet> wallets = walletRepository.loadWalletsByUser(user.getUsername());

        for (Wallet wallet : wallets) {
            if (wallet.getName().equals(walletName)) {
                wallet.setBalance(newBalance);
                walletRepository.saveWallet(wallet);
                System.out.println("Баланс кошелька успешно обновлён.");
                return;
            }
        }
        throw new IllegalArgumentException("Кошелёк с названием \"" + walletName + "\" не найден.");
    }

    /**
     * Перевод средств между кошельками.
     *
     * @param senderUser    Пользователь-отправитель.
     * @param senderWallet  Название кошелька-отправителя.
     * @param receiverUser  Пользователь-получатель.
     * @param receiverWallet Название кошелька-получателя.
     * @param amount        Сумма перевода.
     */
    public void transferFunds(User senderUser, String senderWallet, User receiverUser, String receiverWallet, double amount) {
        if (!DataValidator.isPositiveNumber(String.valueOf(amount))) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной.");
        }

        Wallet sender = null, receiver = null;
        List<Wallet> senderWallets = walletRepository.loadWalletsByUser(senderUser.getUsername());
        for (Wallet wallet : senderWallets) {
            if (wallet.getName().equals(senderWallet)) {
                sender = wallet;
                break;
            }
        }

        List<Wallet> receiverWallets = walletRepository.loadWalletsByUser(receiverUser.getUsername());
        for (Wallet wallet : receiverWallets) {
            if (wallet.getName().equals(receiverWallet)) {
                receiver = wallet;
                break;
            }
        }

        if (sender == null) {
            throw new IllegalArgumentException("Кошелек отправителя \"" + senderWallet + "\" не найден.");
        }
        if (receiver == null) {
            throw new IllegalArgumentException("Кошелек получателя \"" + receiverWallet + "\" не найден.");
        }
        if (sender.getBalance() < amount) {
            throw new IllegalArgumentException("Недостаточно средств на кошельке отправителя.");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        walletRepository.saveWallet(sender);
        walletRepository.saveWallet(receiver);

        System.out.println("Перевод успешно выполнен: " + amount + " из \"" + senderWallet + "\" в \"" + receiverWallet + "\".");
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
        if (!DataValidator.isPositiveNumber(String.valueOf(balance)) || !DataValidator.isNumberInRange(balance, 1, 100_000_000)) {
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
     * Проверяет, превышают ли расходы пользователя доходы.
     *
     * @param user Пользователь, для которого выполняется проверка.
     * @return Строка с предупреждением, если расходы превышают доходы; иначе пустая строка.
     */
    public String checkExpenseExceedsIncome(User user) {
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

        if (Math.abs(totalExpenses) > totalIncome) {
            return "Предупреждение: Общие расходы превышают доходы!";
        }
        return "";
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
