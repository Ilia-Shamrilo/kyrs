package com.example.webserviceindastrialproduct.services;

import com.example.webserviceindastrialproduct.models.User;
import com.example.webserviceindastrialproduct.repositories.UsersRepositories;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    UsersRepositories usersRepositories;
    //для шифрование пароля
    private BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    // Метод для проверки уникальности имени пользователя
    private boolean isUsernameUnique(String username) {
        return !usersRepositories.existsByName(username);
    }
    public void signUp(User user){
        if (!isUsernameUnique(user.getName())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
            user.setPassword(encoder().encode(user.getPassword()));
            usersRepositories.save(user);
    }
    @Transactional
    public void saveUser(User user){
        usersRepositories.save(user);
    }
    public Optional<User> findName(String name){
        return usersRepositories. findByName(name);
    }
}
