package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.util.DataValidator;
import com.beryoza.financeapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления пользователями.
 * Интегрирован с UserRepository для работы с файлами.
 */
public class UserService {
    // Список всех зарегистрированных пользователей
    private final List<User> users;

    // Репозиторий для работы с пользователями
    private final UserRepository userRepository;

    // Текущий авторизованный пользователь
    private User currentUser;
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
        try {
            // Проверка, что логин не пустой
            if (!DataValidator.isNonEmptyString(username)) {
                throw new IllegalArgumentException("Логин не может быть пустым.");
            }

            // Проверка длины логина
            if (!DataValidator.isStringLengthValid(username, 20)) {
                throw new IllegalArgumentException("Логин не может быть длиннее 20 символов.");
            }

            // Проверка формата логина
            if (!DataValidator.isValidLogin(username)) {
                throw new IllegalArgumentException("Некорректный логин. Используйте буквы, цифры или '_'. Длина: 4-20 символов.");
            }

            // Проверка уникальности логина
            List<String> existingUsernames = new ArrayList<>();
            for (User user : users) {
                existingUsernames.add(user.getUsername());
            }

            if (!DataValidator.isUniqueName(username, existingUsernames)) {
                throw new IllegalArgumentException("Пользователь с таким логином уже существует.");
            }

            // Проверка пароля
            if (!DataValidator.isValidPassword(password)) {
                throw new IllegalArgumentException("Пароль должен быть минимум 6 символов.");
            }

            // Создание нового пользователя
            User newUser = new User(username, password);
            users.add(newUser);

            // Сохранение пользователя
            userRepository.saveUsers(users);
            System.out.println("Пользователь успешно зарегистрирован.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    /**
     * Авторизация пользователя.
     *
     * @param username Логин пользователя.
     * @param password Пароль пользователя.
     * @return true, если авторизация успешна; иначе false.
     */
    public boolean authenticateUser(String username, String password) {
        try {
            // Проверка, что логин не пустой
            if (!DataValidator.isNonEmptyString(username)) {
                throw new IllegalArgumentException("Логин не может быть пустым.");
            }

            // Проверка длины логина
            if (!DataValidator.isStringLengthValid(username, 20)) {
                throw new IllegalArgumentException("Логин не может быть длиннее 20 символов.");
            }

            // Проверка, что пароль не пустой
            if (!DataValidator.isNonEmptyString(password)) {
                throw new IllegalArgumentException("Пароль не может быть пустым.");
            }

            // Поиск пользователя по логину и паролю
            for (User u : users) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    currentUser = u; // Устанавливаем текущего пользователя
                    System.out.println("Добро пожаловать, " + u.getUsername() + "!");
                    return true;
                }
            }

            System.out.println("Ошибка: Неверный логин или пароль.");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Ошибка при авторизации: " + e.getMessage());
            return false;
        }
    }

    /**
     * Получить текущего авторизованного пользователя.
     *
     * @return Текущий пользователь или null, если пользователь не авторизован.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Выход из системы. Сбрасывает текущего пользователя.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Изменение пароля пользователя.
     *
     * @param oldPassword Старый пароль.
     * @param newPassword Новый пароль.
     */
    public void changePassword(String oldPassword, String newPassword) {
        try {
            // Проверка, что старый пароль не пустой
            if (!DataValidator.isNonEmptyString(oldPassword)) {
                throw new IllegalArgumentException("Старый пароль не может быть пустым.");
            }

            // Проверка, что новый пароль не пустой
            if (!DataValidator.isNonEmptyString(newPassword)) {
                throw new IllegalArgumentException("Новый пароль не может быть пустым.");
            }

            // Проверка длины нового пароля
            if (!DataValidator.isValidPassword(newPassword)) {
                throw new IllegalArgumentException("Пароль должен быть минимум 6 символов.");
            }

            // Обновление пароля
            boolean isUpdated = false;
            for (User user : users) {
                if (user.getPassword().equals(oldPassword)) {
                    user.setPassword(newPassword);
                    isUpdated = true;
                }
            }

            // Проверка результата обновления
            if (isUpdated) {
                userRepository.saveUsers(users); // Сохраняем изменения
                System.out.println("Пароль успешно изменён.");
            } else {
                throw new IllegalArgumentException("Неверный старый пароль.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    /**
     * Изменение логина пользователя.
     *
     * @param oldUsername Старый логин.
     * @param newUsername Новый логин.
     */
    public void changeUsername(String oldUsername, String newUsername) {
        try {
            // Проверка, что старый логин не пустой
            if (!DataValidator.isNonEmptyString(oldUsername)) {
                throw new IllegalArgumentException("Старый логин не может быть пустым.");
            }

            // Проверка, что новый логин не пустой
            if (!DataValidator.isNonEmptyString(newUsername)) {
                throw new IllegalArgumentException("Новый логин не может быть пустым.");
            }

            // Проверка длины нового логина
            if (!DataValidator.isStringLengthValid(newUsername, 20)) {
                throw new IllegalArgumentException("Логин не может быть длиннее 20 символов.");
            }

            // Проверка формата нового логина
            if (!DataValidator.isValidLogin(newUsername)) {
                throw new IllegalArgumentException("Некорректный логин. Используйте буквы, цифры или '_'. Длина: 4-20 символов.");
            }

            // Проверка уникальности нового логина
            List<String> existingUsernames = new ArrayList<>();
            for (User user : users) {
                existingUsernames.add(user.getUsername());
            }

            if (!DataValidator.isUniqueName(newUsername, existingUsernames)) {
                throw new IllegalArgumentException("Пользователь с таким логином уже существует.");
            }

            // Обновление логина
            boolean isUpdated = false;
            for (User user : users) {
                if (user.getUsername().equals(oldUsername)) {
                    user.setUsername(newUsername);
                    isUpdated = true;
                }
            }

            // Проверка результата обновления
            if (isUpdated) {
                userRepository.saveUsers(users); // Сохранение изменений
                System.out.println("Логин успешно изменён.");
            } else {
                throw new IllegalArgumentException("Старый логин не найден.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
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