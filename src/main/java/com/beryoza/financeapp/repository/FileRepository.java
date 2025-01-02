package com.beryoza.financeapp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Базовый репозиторий для работы с файлами.
 * Предоставляет методы для сохранения и загрузки данных.
 */
public abstract class FileRepository {
    private final ObjectMapper objectMapper = new ObjectMapper(); // Для работы с JSON

    /**
     * Сохранить данные в файл.
     *
     * @param filePath Путь к файлу.
     * @param data     Данные для сохранения.
     * @param <T>      Тип данных.
     * @throws IOException Если произошла ошибка при записи.
     */
    protected <T> void saveDataToFile(String filePath, List<T> data) throws IOException {
        objectMapper.writeValue(new File(filePath), data);
    }

    /**
     * Загрузить данные из файла.
     *
     * @param filePath Путь к файлу.
     * @param type     Класс типа данных.
     * @param <T>      Тип данных.
     * @return Список данных, загруженных из файла.
     * @throws IOException Если произошла ошибка при чтении.
     */
    protected <T> List<T> loadDataFromFile(String filePath, Class<T> type) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>(); // Если файла нет, возвращаем пустой список
        }
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }
}