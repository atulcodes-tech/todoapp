package com.todo.todoapp.repository;

import com.todo.todoapp.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    List<TodoEntity> findByOwnerEmail(String ownerEmail);

}
