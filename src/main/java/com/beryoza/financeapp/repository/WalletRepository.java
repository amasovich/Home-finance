package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с кошельками и транзакциями.
 * Хранит данные кошельков в одном файле, предоставляя возможность фильтрации по userId.
 * <p>
 * Поля:
 * - {@code String FILE_PATH} — путь к файлу, где хранятся данные всех кошельков.
 */
public class WalletRepository extends FileRepository {
    private static final String FILE_PATH = "data/wallets/wallets.json";

    /**
     * Конструктор. Проверяет наличие директории и файла для кошельков.
     * Если они отсутствуют, создаёт их.
     */
    public WalletRepository() {
        ensureDirectoriesExist();
        ensureFileExists();
    }

    /**
     * Проверяет наличие директории для хранения данных.
     * Если директория отсутствует, создаёт её.
     */
    private void ensureDirectoriesExist() {
        File directory = new File("data/wallets");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Проверяет наличие файла для хранения кошельков.
     * Если файл отсутствует или пустой, создаёт его и инициализирует пустым списком.
     */
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                saveWallets(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла кошельков: " + e.getMessage());
        }
    }

    /**
     * Сохранить список кошельков в файл.
     *
     * @param wallets Список кошельков для сохранения.
     */
    public void saveWallets(List<Wallet> wallets) {
        try {
            saveDataToFile(FILE_PATH, wallets);
            System.out.println("Данные кошельков успешно сохранены.");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении кошельков: " + e.getMessage());
        }
    }

    /**
     * Загрузить список всех кошельков из файла.
     *
     * @return Список всех кошельков.
     */
    public List<Wallet> loadWallets() {
        try {
            List<Wallet> wallets = loadDataFromFile(FILE_PATH, Wallet.class);
            if (wallets == null) {
                wallets = new ArrayList<>();
            }
            return wallets;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке кошельков: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Загружает кошельки для указанного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список кошельков, принадлежащих пользователю.
     */
    public List<Wallet> loadWalletsByUser(String userId) {
        List<Wallet> allWallets = loadWallets();
        List<Wallet> userWallets = new ArrayList<>();
        for (Wallet wallet : allWallets) {
            if (wallet.getUserId().equals(userId)) {
                userWallets.add(wallet);
            }
        }
        return userWallets;
    }

    /**
     * Сохранить или обновить кошелёк.
     *
     * @param wallet Кошелёк для сохранения.
     */
    public void saveWallet(Wallet wallet) {
        try {
            List<Wallet> wallets = loadWallets();

            boolean walletUpdated = false;

            for (int i = 0; i < wallets.size(); i++) {
                Wallet existingWallet = wallets.get(i);
                if (existingWallet.getUserId().equals(wallet.getUserId()) &&
                        existingWallet.getName().equals(wallet.getName())) {
                    wallets.set(i, wallet);
                    walletUpdated = true;
                    break;
                }
            }

            if (!walletUpdated) {
                wallets.add(wallet);
            }

            saveWallets(wallets);
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении кошелька: " + e.getMessage());
        }
    }
}