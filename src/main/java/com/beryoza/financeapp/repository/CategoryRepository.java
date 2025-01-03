package com.beryoza.financeapp.repository;

import com.beryoza.financeapp.model.Category;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для работы с категориями и их бюджетами.
 */
public class CategoryRepository extends FileRepository {
    private static final String FILE_PATH = "data/categories/categories.json";

    /**
     * Конструктор, проверяющий наличие файла для категорий.
     * Если файл отсутствует, создаётся новый.
     */
    public CategoryRepository() {
        ensureDirectoriesExist();
        ensureFileExists();
    }

    /**
     * Убедиться, что директория для хранения категорий существует.
     */
    private void ensureDirectoriesExist() {
        File directory = new File("data/categories");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Убедиться, что файл для категорий существует.
     * Если файл пуст, инициализировать его пустым списком.
     */
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                saveCategories(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла категорий: " + e.getMessage());
        }
    }

    /**
     * Сохранить список категорий в файл.
     *
     * @param categories Список категорий.
     */
    public void saveCategories(List<Category> categories) {
        try {
            saveDataToFile(FILE_PATH, categories);
            System.out.println("Категории успешно сохранены.");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении категорий: " + e.getMessage());
        }
    }

    /**
     * Загрузить список категорий из файла.
     *
     * @return Список категорий.
     */
    public List<Category> loadCategories() {
        try {
            return loadDataFromFile(FILE_PATH, Category.class);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке категорий: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}