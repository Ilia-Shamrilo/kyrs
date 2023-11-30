package com.example.webserviceindastrialproduct.repositories;

import com.example.webserviceindastrialproduct.models.Order;
import com.example.webserviceindastrialproduct.models.Product;
import com.example.webserviceindastrialproduct.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndProduct(User user, Product product);
    List<Order> findByUser(User user);
}
