package com.example.webserviceindastrialproduct.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp//дата создания сразу будет записываться
    private LocalDateTime created;
    @UpdateTimestamp//во воремя обновления менять дату
    private LocalDateTime update;
    @ManyToOne//множество заказов к одному пользователю
    @JoinColumn(name = "user_id")
    private User user;
    private double sum;
    @OneToMany(cascade = CascadeType.ALL)//у одного заказа может быть много деталей
    private List<OrderDetails> details;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
