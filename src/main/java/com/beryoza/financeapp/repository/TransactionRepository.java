package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.Transaction;

import java.io.IOException;
import java.util.List;

/**
 * Репозиторий для работы с данными транзакций.
 * Сохраняет и загружает данные транзакций из файла.
 */
public class TransactionRepository extends FileRepository {
    private static final String FILE_PATH = "transactions.json"; // Путь к файлу с транзакциями

    /**
     * Сохранить список транзакций в файл.
     *
     * @param transactions Список транзакций для сохранения.
     */
    public void saveTransactions(List<Transaction> transactions) {
        try {
            saveDataToFile(FILE_PATH, transactions);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении транзакций: " + e.getMessage());
        }
    }

    /**
     * Загрузить список транзакций из файла.
     *
     * @return Список транзакций.
     */
    public List<Transaction> loadTransactions() {
        try {
            return loadDataFromFile(FILE_PATH, Transaction.class);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке транзакций: " + e.getMessage());
            return List.of(); // Возвращаем пустой список при ошибке
        }
    }
}