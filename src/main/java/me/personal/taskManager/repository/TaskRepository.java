package me.personal.taskManager.repository;

import me.personal.taskManager.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    TaskModel findByTitulo(String titulo);
    boolean existsByTitulo(String titulo);
}
