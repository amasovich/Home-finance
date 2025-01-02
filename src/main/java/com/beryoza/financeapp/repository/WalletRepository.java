package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Репозиторий для работы с данными кошельков и транзакций.
 * Сохраняет и загружает кошельки вместе с их транзакциями для каждого пользователя.
 */
public class WalletRepository extends FileRepository {
    // Шаблон пути к файлу для каждого пользователя
    private static final String FILE_PATH_TEMPLATE = "data/wallets/wallets_%s.json";

    /**
     * Конструктор. При создании экземпляра репозитория проверяет
     * наличие необходимой директории и создаёт её, если она отсутствует.
     */
    public WalletRepository() {
        ensureDirectoriesExist();
    }

    /**
     * Проверить наличие директории для хранения данных пользователей
     * и создать её, если она отсутствует.
     */
    private void ensureDirectoriesExist() {
        File directory = new File("data/wallets");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

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

    /**
     * Сохранить транзакции конкретного кошелька.
     *
     * @param wallet Кошелёк, транзакции которого нужно сохранить.
     * @param userId ID пользователя.
     */
    public void saveTransactions(Wallet wallet, String userId) {
        try {
            List<Wallet> wallets = loadWallets(userId);
            wallets = wallets.stream()
                    .map(w -> w.getName().equals(wallet.getName()) ? wallet : w)
                    .collect(Collectors.toList());
            saveWallets(wallets, userId);
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении транзакций для кошелька \"" + wallet.getName() + "\": " + e.getMessage());
        }
    }

    /**
     * Загрузить транзакции для конкретного кошелька.
     *
     * @param walletName Название кошелька.
     * @param userId     ID пользователя.
     * @return Список транзакций.
     */
    public List<Transaction> loadTransactions(String walletName, String userId) {
        try {
            List<Wallet> wallets = loadWallets(userId);
            return wallets.stream()
                    .filter(wallet -> wallet.getName().equals(walletName))
                    .flatMap(wallet -> wallet.getTransactions().stream())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке транзакций для кошелька \"" + walletName + "\": " + e.getMessage());
            return List.of();
        }
    }
}