package me.personal.taskManager.service;

import org.springframework.transaction.annotation.Transactional;
import me.personal.taskManager.model.TaskModel;
import me.personal.taskManager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public List<TaskModel> getAll(){
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TaskModel getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public TaskModel getTaskByTitulo(String titulo){
        return taskRepository.findByTitulo(titulo);
    }

    public TaskModel create(TaskModel task){
        return taskRepository.save(task);
    }

    public TaskModel update(Long id, TaskModel taskToUpdate){
        TaskModel taskDb = taskRepository.findById(id).orElseThrow(NoSuchElementException::new);
        taskDb.setTitulo(taskToUpdate.getTitulo());
        taskDb.setDescricao(taskToUpdate.getDescricao());
        taskDb.setFeito(taskToUpdate.getFeito());
        return taskRepository.save(taskDb);
    }

    public void delete(Long id){
        TaskModel taskDb = taskRepository.findById(id).orElseThrow(NoSuchElementException::new);
        taskRepository.delete(taskDb);
    }
}
