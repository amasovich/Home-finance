package beryoza.financeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Кошелёк пользователя. Тут храним баланс и все операции (доходы/расходы).
 */
public class Wallet {
    // Название кошелька, например, "Наличные" или "Карта Сбербанка"
    private String name;

    // Текущий баланс кошелька
    private double balance;

    // Список всех транзакций по этому кошельку
    private List<Transaction> transactions;

    /**
     * Конструктор. Задаём название кошелька и начальный баланс.
     */
    public Wallet(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>(); // Начинаем с пустого списка транзакций
    }

    // Получить название кошелька
    public String getName() {
        return name;
    }

    // Переименовать кошелёк
    public void setName(String name) {
        this.name = name;
    }

    // Узнать текущий баланс
    public double getBalance() {
        return balance;
    }

    // Обновить баланс (например, если исправили ошибку в расчётах)
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Получить все транзакции
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // Полностью заменить список транзакций
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Добавляем транзакцию в список.
     * Заодно можно пересчитать баланс.
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.balance += transaction.getAmount(); // Обновляем баланс
    }

    /**
     * Удаляем транзакцию. Если нужно, пересчитаем баланс.
     */
    public void removeTransaction(Transaction transaction) {
        if (this.transactions.remove(transaction)) {
            this.balance -= transaction.getAmount(); // Корректируем баланс
        }
    }

    /**
     * Удобный способ вывести информацию о кошельке:
     * название, баланс и количество операций.
     */
    @Override
    public String toString() {
        return "Wallet{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", transactionsCount=" + transactions.size() +
                '}';
    }
}