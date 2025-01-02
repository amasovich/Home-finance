package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.User;

import java.io.IOException;
import java.util.List;

/**
 * Репозиторий для работы с данными пользователей.
 * Сохраняет и загружает данные пользователей из файла.
 */
public class UserRepository extends FileRepository {
    private static final String FILE_PATH = "users.json"; // Путь к файлу с пользователями

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
            return List.of(); // Возвращаем пустой список при ошибке
        }
    }
}