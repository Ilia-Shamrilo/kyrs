package com.example.webserviceindastrialproduct.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<String> characteristics;
    private double price;
    @Enumerated(EnumType.STRING)
    private Category categories;
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<String> photos;
    @ManyToMany(mappedBy = "basketProduct")
    private List<User> users;
    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Order orders;
}
