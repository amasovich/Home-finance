package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с данными пользователей.
 * Обеспечивает загрузку и сохранение пользователей в файл.
 */
public class UserRepository extends FileRepository {
    // Путь к файлу, где хранятся данные пользователей
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
            directory.mkdirs(); // Создаём директорию
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
                file.createNewFile(); // Создаём файл
                saveUsers(new ArrayList<>()); // Сохраняем пустой список
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
            saveDataToFile(FILE_PATH, users); // Записываем полный список в файл
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
            List<User> users = loadDataFromFile(FILE_PATH, User.class); // Загружаем список пользователей
            if (users == null) {
                users = new ArrayList<>(); // Инициализируем пустой список, если файл пустой
            }
            return users;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке пользователей: " + e.getMessage());
            return new ArrayList<>(); // Возвращаем пустой список при ошибке
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
                return user; // Возвращаем найденного пользователя
            }
        }
        return null; // Если пользователь не найден
    }
}