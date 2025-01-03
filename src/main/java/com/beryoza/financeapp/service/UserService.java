package com.beryoza.financeapp.service;

import com.beryoza.financeapp.model.Category;
import com.beryoza.financeapp.model.User;
import com.beryoza.financeapp.model.Wallet;
import com.beryoza.financeapp.repository.CategoryRepository;
import com.beryoza.financeapp.repository.UserRepository;
import com.beryoza.financeapp.repository.WalletRepository;
import com.beryoza.financeapp.util.DataValidator;

import java.util.List;

/**
 * Сервис для управления пользователями.
 * Обеспечивает регистрацию, авторизацию, изменение данных и выход из системы.
 */
public class UserService {
    private final UserRepository userRepository; // Репозиторий для работы с пользователями
    private final WalletRepository walletRepository; // Репозиторий для работы с кошельками
    private final CategoryRepository categoryRepository; // Репозиторий для работы с категориями
    private User currentUser; // Текущий авторизованный пользователь

    /**
     * Конструктор.
     *
     * @param userRepository    Репозиторий для работы с пользователями.
     * @param walletRepository  Репозиторий для работы с кошельками.
     * @param categoryRepository Репозиторий для работы с категориями.
     */
    public UserService(UserRepository userRepository, WalletRepository walletRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
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
            // Проверка уникальности
            validateUsername(username);
            validatePassword(password);

            List<User> users = userRepository.loadUsers();

            if (users.stream().anyMatch(user -> user.getUsername().equals(username))) {
                throw new IllegalArgumentException("Пользователь с таким логином уже существует.");
            }

            users.add(new User(username, password)); // Добавляем нового пользователя
            userRepository.saveUsers(users); // Сохраняем полный список

            System.out.println("Пользователь успешно зарегистрирован.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Найти пользователя по логину.
     *
     * @param username Логин пользователя.
     * @return Пользователь, если найден, иначе null.
     */
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
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
            // Проверяем формат логина и пароля
            validateUsername(username);
            validatePassword(password);

            // Ищем пользователя по логину
            User user = findUserByUsername(username);
            if (user == null) {
                System.out.println("Ошибка: Пользователь с логином '" + username + "' не найден.");
                return false;
            }

            // Проверяем пароль
            if (user.getPassword().equals(password)) {
                currentUser = user; // Устанавливаем текущего пользователя
                System.out.println("Добро пожаловать, " + user.getUsername() + "!");
                return true;
            }

            System.out.println("Ошибка: Неверный пароль.");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return false;
        }
    }

    /**
     * Изменение пароля пользователя.
     *
     * @param oldPassword Старый пароль.
     * @param newPassword Новый пароль.
     */
    public void changePassword(String oldPassword, String newPassword) {
        try {
            validatePassword(newPassword);

            if (currentUser == null || !currentUser.getPassword().equals(oldPassword)) {
                throw new IllegalArgumentException("Неверный старый пароль.");
            }

            List<User> users = userRepository.loadUsers();

            // Обновляем пароль текущего пользователя
            for (User user : users) {
                if (user.getUsername().equals(currentUser.getUsername())) {
                    user.setPassword(newPassword);
                    break;
                }
            }

            // Сохраняем изменения
            userRepository.saveUsers(users);

            // Обновляем текущего пользователя
            currentUser.setPassword(newPassword);

            System.out.println("Пароль успешно изменён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Изменение логина пользователя.
     *
     * @param newUsername Новый логин.
     */
    public void changeUsername(String newUsername) {
        try {
            validateUsername(newUsername);

            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не авторизован.");
            }

            // Проверяем, что логин уникален с помощью findUserByUsername
            if (findUserByUsername(newUsername) != null) {
                throw new IllegalArgumentException("Пользователь с таким логином уже существует.");
            }

            List<User> users = userRepository.loadUsers();

            // Обновляем данные текущего пользователя
            for (User user : users) {
                if (user.getUsername().equals(currentUser.getUsername())) {
                    user.setUsername(newUsername);
                    break;
                }
            }

            // Сохраняем изменения в списке пользователей
            userRepository.saveUsers(users);

            // Обновляем связанные данные
            updateWalletsUserId(currentUser.getUsername(), newUsername);
            updateCategoriesUserId(currentUser.getUsername(), newUsername);

            // Обновляем текущего пользователя
            currentUser.setUsername(newUsername);

            System.out.println("Логин успешно изменён.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Обновляет userId в кошельках.
     *
     * @param oldUserId Старый userId.
     * @param newUserId Новый userId.
     */
    private void updateWalletsUserId(String oldUserId, String newUserId) {
        List<Wallet> wallets = walletRepository.loadWalletsByUser(oldUserId);
        for (Wallet wallet : wallets) {
            wallet.setUserId(newUserId);
        }
        walletRepository.saveWallets(wallets);
    }

    /**
     * Обновляет userId в категориях.
     *
     * @param oldUserId Старый userId.
     * @param newUserId Новый userId.
     */
    private void updateCategoriesUserId(String oldUserId, String newUserId) {
        List<Category> categories = categoryRepository.findCategoriesByUserId(oldUserId);
        for (Category category : categories) {
            category.setUserId(newUserId);
        }
        categoryRepository.saveCategories(categories);
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
     * Проверяет корректность логина.
     *
     * @param username Логин для проверки.
     */
    private void validateUsername(String username) {
        if (!DataValidator.isNonEmptyString(username) || !DataValidator.isStringLengthValid(username, 20) ||
                !DataValidator.isValidLogin(username)) {
            throw new IllegalArgumentException("Некорректный логин.");
        }
    }

    /**
     * Проверяет корректность пароля.
     *
     * @param password Пароль для проверки.
     */
    private void validatePassword(String password) {
        if (!DataValidator.isNonEmptyString(password) || !DataValidator.isValidPassword(password)) {
            throw new IllegalArgumentException("Некорректный пароль.");
        }
    }
}