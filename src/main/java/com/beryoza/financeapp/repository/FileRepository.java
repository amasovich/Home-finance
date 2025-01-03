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
        try {
            objectMapper.writeValue(new File(filePath), data);
            System.out.println("Данные успешно сохранены в " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении данных в " + filePath + ": " + e.getMessage());
            throw e;
        }
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
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>(); // Если файл отсутствует или пустой, возвращаем пустой список
        }
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }
}