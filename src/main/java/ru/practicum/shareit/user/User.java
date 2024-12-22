package ru.practicum.shareit.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Email
    @Column(name = "email")
    private String email;
}
