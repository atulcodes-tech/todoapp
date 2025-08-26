package com.todo.todoapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_todos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private Boolean completed;

    // 👇 Add user identification (email or username from OAuth2)
    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

}