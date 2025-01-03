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
     * Проверяет существование директории для хранения категорий.
     * Если директория отсутствует, она создаётся.
     */
    private void ensureDirectoriesExist() {
        File directory = new File("data/categories");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Проверяет существование файла для хранения категорий.
     * Если файл отсутствует или пуст, он создаётся и инициализируется пустым списком категорий.
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
     * Сохраняет список категорий в файл.
     *
     * @param categories Список категорий для сохранения.
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
     * Загружает список категорий из файла.
     *
     * @return Список категорий, если файл успешно загружен; пустой список в случае ошибки.
     */
    public List<Category> loadCategories() {
        try {
            return loadDataFromFile(FILE_PATH, Category.class);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке категорий: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Поиск категорий для указанного пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Список категорий, принадлежащих пользователю.
     */
    public List<Category> findCategoriesByUserId(String userId) {
        List<Category> allCategories = loadCategories();
        List<Category> userCategories = new ArrayList<>();
        for (Category category : allCategories) {
            if (category.getUserId().equals(userId)) {
                userCategories.add(category);
            }
        }
        return userCategories;
    }

    /**
     * Поиск категории по названию и userId.
     *
     * @param userId Идентификатор пользователя.
     * @param name   Название категории.
     * @return Категория, если найдена; иначе null.
     */
    public Category findCategoryByName(String userId, String name) {
        List<Category> allCategories = loadCategories();
        for (Category category : allCategories) {
            if (category.getUserId().equals(userId) && category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}