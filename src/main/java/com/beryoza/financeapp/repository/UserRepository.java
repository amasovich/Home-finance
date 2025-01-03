package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с данными пользователей.
 * Обеспечивает загрузку и сохранение пользователей в файл.
 * <p>
 * Поля:
 * - {@code String FILE_PATH} — путь к файлу, где хранятся данные пользователей.
 */
public class UserRepository extends FileRepository {
    private static final String FILE_PATH = "data/users/users.json";

    /**
     * Конструктор. Проверяет наличие директории и файла для пользователей.
     * Если они отсутствуют, создаёт их.
     */
    public UserRepository() {
        ensureDirectoriesExist();
        ensureFileExists();
    }

    /**
     * Проверяет наличие директории для хранения данных.
     * Если директория отсутствует, создаёт её.
     */
    private void ensureDirectoriesExist() {
        File directory = new File("data/users");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Проверяет наличие файла для хранения пользователей.
     * Если файл отсутствует или пустой, создаёт его и инициализирует пустым списком.
     */
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                saveUsers(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла пользователей: " + e.getMessage());
        }
    }

    /**
     * Сохраняет список пользователей в файл.
     *
     * @param users Список пользователей для сохранения.
     */
    public void saveUsers(List<User> users) {
        try {
            saveDataToFile(FILE_PATH, users);
            System.out.println("Данные пользователей успешно сохранены.");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении пользователей: " + e.getMessage());
        }
    }

    /**
     * Загружает список пользователей из файла.
     *
     * @return Список пользователей.
     */
    public List<User> loadUsers() {
        try {
            List<User> users = loadDataFromFile(FILE_PATH, User.class);
            if (users == null) {
                users = new ArrayList<>();
            }
            return users;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке пользователей: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Ищет пользователя по имени.
     *
     * @param username Логин пользователя для поиска.
     * @return Пользователь, если найден, иначе null.
     */
    public User findUserByUsername(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}