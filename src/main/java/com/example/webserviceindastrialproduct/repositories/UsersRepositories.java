package com.example.webserviceindastrialproduct.repositories;

import com.example.webserviceindastrialproduct.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UsersRepositories extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    boolean existsByName(String name);
    List<User> findAll();
    void deleteById(Long id);
}
