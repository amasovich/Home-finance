package com.beryoza.financeapp.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Утилитарный класс для проверки вводимых данных.
 */
public class DataValidator {

    /**
     * Проверяет, является ли строка числом.
     *
     * @param input Строка для проверки.
     * @return true, если строка является числом; иначе false.
     */
    public static boolean isNumeric(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Проверяет, является ли строка допустимым положительным числом.
     *
     * @param input Строка для проверки.
     * @return true, если строка является положительным числом; иначе false.
     */
    public static boolean isPositiveNumber(String input) {
        return isNumeric(input) && Double.parseDouble(input) > 0;
    }

    /**
     * Проверяет, является ли строка непустой.
     *
     * @param input Строка для проверки.
     * @return true, если строка не пуста; иначе false.
     */
    public static boolean isNonEmptyString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Проверяет, является ли строка допустимой категорией.
     *
     * @param category Строка для проверки.
     * @return true, если строка является допустимой категорией; иначе false.
     */
    public static boolean isValidCategory(String category) {
        return isNonEmptyString(category); // Можно расширить в будущем
    }

    /**
     * Проверяет, является ли строка корректной датой.
     *
     * @param dateStr Строка с датой.
     * @param format  Формат даты (например, "yyyy-MM-dd").
     * @return true, если дата корректна; иначе false.
     */
    public static boolean isValidDate(String dateStr, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Проверяет, находится ли число в заданном диапазоне.
     *
     * @param number Число для проверки.
     * @param min    Минимальное значение.
     * @param max    Максимальное значение.
     * @return true, если число в диапазоне; иначе false.
     */
    public static boolean isNumberInRange(double number, double min, double max) {
        return number >= min && number <= max;
    }

    /**
     * Проверяет, является ли строка уникальной среди существующих значений.
     *
     * @param name          Проверяемая строка.
     * @param existingNames Список существующих строк.
     * @return true, если строка уникальна; иначе false.
     */
    public static boolean isUniqueName(String name, List<String> existingNames) {
        return !existingNames.contains(name);
    }

    /**
     * Проверяет, является ли строка допустимой для расходов (отрицательное число).
     *
     * @param input Строка для проверки.
     * @return true, если строка отрицательное число; иначе false.
     */
    public static boolean isNegativeNumber(String input) {
        return isNumeric(input) && Double.parseDouble(input) < 0;
    }

    /**
     * Проверяет, не превышает ли длина строки заданное значение.
     *
     * @param input     Строка для проверки.
     * @param maxLength Максимальная длина.
     * @return true, если длина строки допустима; иначе false.
     */
    public static boolean isStringLengthValid(String input, int maxLength) {
        return isNonEmptyString(input) && input.length() <= maxLength;
    }

    /**
     * Проверяет, является ли логин допустимым.
     *
     * @param login Логин для проверки.
     * @return true, если логин допустим; иначе false.
     */
    public static boolean isValidLogin(String login) {
        return isNonEmptyString(login) && login.matches("^[a-zA-Z0-9_]{4,20}$");
    }

    /**
     * Проверяет, является ли пароль допустимым.
     *
     * @param password Пароль для проверки.
     * @return true, если пароль допустим; иначе false.
     */
    public static boolean isValidPassword(String password) {
        return isNonEmptyString(password) && password.length() >= 6;
    }
}