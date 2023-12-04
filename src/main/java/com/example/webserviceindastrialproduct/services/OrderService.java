package com.example.webserviceindastrialproduct.services;

import com.example.webserviceindastrialproduct.models.Order;
import com.example.webserviceindastrialproduct.models.Product;
import com.example.webserviceindastrialproduct.models.User;
import com.example.webserviceindastrialproduct.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    OrderRepository orderRepository;
    public void createOrder(Order order){
        orderRepository.save(order);
    }

    public Optional<Order> findOrderByUserAndProduct(User user, Product product) {
        return orderRepository.findByUserAndProduct(user, product);
    }
    public List<Order> findByUser(User user){
        return orderRepository.findByUser(user);
    }
    public Order findById(Long id){
        return orderRepository.findById(id).get();
    }
    @Transactional
    public void deleteOrder(Order order){
        orderRepository.delete(order);
    }
}
