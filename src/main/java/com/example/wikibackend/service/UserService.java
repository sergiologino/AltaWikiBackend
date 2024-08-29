package com.example.wikibackend.service;

import com.example.wikibackend.model.User;
import com.example.wikibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Регистрация нового пользователя
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Поиск пользователя по ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Удаление пользователя
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

