package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.util.DataValidator;
import com.beryoza.financeapp.repository.UserRepository;

import java.util.List;

/**
 * Сервис для управления пользователями.
 * Интегрирован с UserRepository для работы с файлами.
 */
public class UserService {
    // Список всех зарегистрированных пользователей
    private final List<User> users;

    // Поле для работы с репозиторием пользователей
    private final UserRepository userRepository;

    /**
     * Конструктор. Инициализируем репозиторий и загружаем пользователей.
     *
     * @param userRepository Репозиторий для работы с данными пользователей.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.users = userRepository.loadUsers(); // Загрузка данных из файла
    }

    /**
     * Регистрация нового пользователя.
     * Данные автоматически сохраняются в файл.
     *
     * @param username Логин нового пользователя.
     * @param password Пароль нового пользователя.
     */
    public void registerUser(String username, String password) {
        if (!DataValidator.isNonEmptyString(username)) {
            System.out.println("Логин не может быть пустым.");
            return;
        }
        if (!DataValidator.isValidLogin(username)) {
            System.out.println("Некорректный логин. Используйте буквы, цифры или '_'. Длина: 4-20 символов.");
            return;
        }
        if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
            System.out.println("Пользователь с таким логином уже существует.");
            return;
        }
        if (!DataValidator.isValidPassword(password)) {
            System.out.println("Пароль должен быть минимум 6 символов.");
            return;
        }

        User newUser = new User(username, password);
        users.add(newUser);

        try {
            userRepository.saveUsers(users); // Сохраняем данные в файл
            System.out.println("Пользователь успешно зарегистрирован.");
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    /**
     * Авторизация пользователя.
     *
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     */
    public void authenticateUser(String username, String password) {
        if (!DataValidator.isNonEmptyString(username)) {
            System.out.println("Ошибка: Логин не может быть пустым.");
            return;
        }
        if (!DataValidator.isNonEmptyString(password)) {
            System.out.println("Ошибка: Пароль не может быть пустым.");
            return;
        }

        User user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (user != null) {
            System.out.println("Добро пожаловать, " + user.getUsername() + "!");
        } else {
            System.out.println("Ошибка: Неверный логин или пароль.");
        }
    }

    /**
     * Изменение пароля пользователя.
     *
     * @param oldPassword Старый пароль.
     * @param newPassword Новый пароль.
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (!DataValidator.isValidPassword(newPassword)) {
            System.out.println("Ошибка: Пароль должен быть минимум 6 символов.");
            return;
        }

        boolean isUpdated = false;
        for (User user : users) {
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                isUpdated = true;
            }
        }

        if (isUpdated) {
            try {
                userRepository.saveUsers(users);
                System.out.println("Пароль успешно изменён.");
            } catch (Exception e) {
                System.out.println("Ошибка при сохранении данных: " + e.getMessage());
            }
        } else {
            System.out.println("Ошибка: Неверный старый пароль.");
        }
    }

    /**
     * Изменение логина пользователя.
     *
     * @param oldUsername Старый логин.
     * @param newUsername Новый логин.
     */
    public void changeUsername(String oldUsername, String newUsername) {
        if (users.stream().anyMatch(user -> user.getUsername().equals(newUsername))) {
            System.out.println("Ошибка: Пользователь с таким логином уже существует.");
            return;
        }
        if (!DataValidator.isValidLogin(newUsername)) {
            System.out.println("Ошибка: Некорректный логин. Используйте буквы, цифры или '_'. Длина: 4-20 символов.");
            return;
        }

        boolean isUpdated = false;
        for (User user : users) {
            if (user.getUsername().equals(oldUsername)) {
                user.setUsername(newUsername);
                isUpdated = true;
            }
        }

        if (isUpdated) {
            try {
                userRepository.saveUsers(users);
                System.out.println("Логин успешно изменён.");
            } catch (Exception e) {
                System.out.println("Ошибка при сохранении данных: " + e.getMessage());
            }
        } else {
            System.out.println("Ошибка: Старый логин не найден.");
        }
    }

    /**
     * Получить список всех зарегистрированных пользователей.
     *
     * @return Список пользователей.
     * @deprecated Этот метод в текущей версии не используется.
     * Возможно, пригодится для администраторской панели или аналитики в будущем.
     */
    @Deprecated
    public List<User> getAllUsers() {
        return users;
    }
}