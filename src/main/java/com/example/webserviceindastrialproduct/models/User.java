package com.example.webserviceindastrialproduct.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String number;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToMany
    private List<Product> basketProduct;
    @OneToMany
    private List<Order> orders;
}