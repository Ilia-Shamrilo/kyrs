package com.example.webserviceindastrialproduct.repositories;

import com.example.webserviceindastrialproduct.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    void deleteById(Long id);
}
