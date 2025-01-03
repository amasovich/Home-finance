package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 * Репозиторий для работы с данными пользователей.
 * Сохраняет и загружает данные пользователей из файла.
 */
public class UserRepository extends FileRepository {
    // Путь к файлу с пользователями
    private static final String FILE_PATH = "data/users/users.json";

    /**
     * Конструктор. При создании экземпляра репозитория проверяет
     * наличие необходимой директории и создаёт её, если она отсутствует.
     */
    public UserRepository() {
        ensureDirectoriesExist();
        ensureFileExists();
    }

    /**
     * Проверить наличие директории для хранения данных пользователей
     * и создать её, если она отсутствует.
     */
    private void ensureDirectoriesExist() {
        File directory = new File("data/users");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile(); // Создаём файл, если он отсутствует
                saveUsers(new ArrayList<>()); // Сохраняем пустой список пользователей
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла: " + e.getMessage());
        }
    }

    /**
     * Сохранить список пользователей в файл.
     *
     * @param users Список пользователей для сохранения.
     */
    public void saveUsers(List<User> users) {
        try {
            saveDataToFile(FILE_PATH, users);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении пользователей: " + e.getMessage());
        }
    }

    /**
     * Загрузить список пользователей из файла.
     *
     * @return Список пользователей.
     */
    public List<User> loadUsers() {
        try {
            return loadDataFromFile(FILE_PATH, User.class);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке пользователей: " + e.getMessage());
            return new ArrayList<>(); // Возвращаем пустой изменяемый список при ошибке
        }
    }
}