package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.Wallet;

import java.io.IOException;
import java.util.List;

/**
 * Репозиторий для работы с данными кошельков.
 */
public class WalletRepository extends FileRepository {
    private static final String FILE_PATH_TEMPLATE = "wallets_%s.json"; // Шаблон пути к файлу для каждого пользователя

    /**
     * Сохранить список кошельков пользователя в файл.
     *
     * @param wallets Список кошельков для сохранения.
     * @param userId  ID пользователя.
     */
    public void saveWallets(List<Wallet> wallets, String userId) {
        String filePath = String.format(FILE_PATH_TEMPLATE, userId);
        try {
            saveDataToFile(filePath, wallets);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении кошельков для пользователя " + userId + ": " + e.getMessage());
        }
    }

    /**
     * Загрузить список кошельков пользователя из файла.
     *
     * @param userId ID пользователя.
     * @return Список кошельков.
     */
    public List<Wallet> loadWallets(String userId) {
        String filePath = String.format(FILE_PATH_TEMPLATE, userId);
        try {
            return loadDataFromFile(filePath, Wallet.class);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке кошельков для пользователя " + userId + ": " + e.getMessage());
            return List.of(); // Возвращаем пустой список при ошибке
        }
    }
}