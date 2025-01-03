package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.Transaction;
import com.beryoza.financeapp.model.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с кошельками и транзакциями.
 * Хранит данные кошельков в одном файле, фильтруя их по userId.
 */
public class WalletRepository extends FileRepository {
    // Путь к файлу для хранения всех кошельков
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
            directory.mkdirs(); // Создаём директорию
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
                file.createNewFile(); // Создаём файл
                saveWallets(new ArrayList<>()); // Сохраняем пустой список
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
            saveDataToFile(FILE_PATH, wallets); // Используем метод из FileRepository
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
            // Загружаем список кошельков из файла
            List<Wallet> wallets = loadDataFromFile(FILE_PATH, Wallet.class);
            if (wallets == null) {
                wallets = new ArrayList<>(); // Возвращаем пустой список, если файл пустой
            }
            return wallets;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке кошельков: " + e.getMessage());
            return new ArrayList<>(); // Возвращаем пустой список при ошибке
        }
    }

    /**
     * Загружает кошельки для указанного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список кошельков, принадлежащих пользователю.
     */
    public List<Wallet> loadWalletsByUser(String userId) {
        List<Wallet> allWallets = loadWallets(); // Загружаем все кошельки
        List<Wallet> userWallets = new ArrayList<>();
        for (Wallet wallet : allWallets) {
            if (wallet.getUserId().equals(userId)) {
                userWallets.add(wallet); // Фильтруем по userId
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
            List<Wallet> wallets = loadWallets(); // Загружаем все кошельки

            boolean walletUpdated = false;

            // Обновляем существующий кошелёк
            for (int i = 0; i < wallets.size(); i++) {
                Wallet existingWallet = wallets.get(i);
                if (existingWallet.getUserId().equals(wallet.getUserId()) &&
                        existingWallet.getName().equals(wallet.getName())) {
                    wallets.set(i, wallet); // Обновляем данные кошелька
                    walletUpdated = true;
                    break;
                }
            }

            // Если кошелёк не найден, добавляем новый
            if (!walletUpdated) {
                wallets.add(wallet);
            }

            // Сохраняем обновлённый список кошельков
            saveWallets(wallets);
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении кошелька: " + e.getMessage());
        }
    }

    /**
     * Сохранить транзакции для указанного кошелька.
     *
     * @param wallet Кошелёк, транзакции которого нужно сохранить.
     */
    public void saveTransactions(Wallet wallet) {
        List<Wallet> wallets = loadWallets(); // Загружаем все кошельки
        boolean walletFound = false;

        // Обновляем существующий кошелёк
        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).getName().equals(wallet.getName()) &&
                    wallets.get(i).getUserId().equals(wallet.getUserId())) {
                wallets.set(i, wallet); // Обновляем транзакции кошелька
                walletFound = true;
                break;
            }
        }

        // Если кошелёк не найден, выбрасываем исключение
        if (!walletFound) {
            throw new IllegalArgumentException("Кошелёк с названием \"" + wallet.getName() +
                    "\" не найден для пользователя \"" + wallet.getUserId() + "\".");
        }

        saveWallets(wallets); // Сохраняем изменения
    }

    /**
     * Загружает транзакции для указанного кошелька.
     *
     * @param walletName Название кошелька.
     * @param userId     Идентификатор пользователя.
     * @return Список транзакций, связанных с кошельком.
     */
    public List<Transaction> loadTransactions(String walletName, String userId) {
        List<Wallet> wallets = loadWalletsByUser(userId); // Загружаем кошельки пользователя
        for (Wallet wallet : wallets) {
            if (wallet.getName().equals(walletName)) {
                return wallet.getTransactions(); // Возвращаем список транзакций
            }
        }
        return new ArrayList<>(); // Если кошелёк не найден
    }
}