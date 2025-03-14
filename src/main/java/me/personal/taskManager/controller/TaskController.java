package me.personal.taskManager.controller;

import me.personal.taskManager.model.TaskModel;
import me.personal.taskManager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@Transactional
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskModel>> listaTodos(){
        var tasks = taskService.getAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("buscaID/{id}")
    public ResponseEntity<TaskModel> listaTaskById(@PathVariable Long id){
        var task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("buscaTitulo/{titulo}")
    public ResponseEntity<TaskModel> listaTaskByTitulo(@PathVariable String titulo){
        var task = taskService.getTaskByTitulo(titulo);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskModel> criaTask(@RequestBody TaskModel receivedTask){
        var taskCreated = taskService.create(receivedTask);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(taskCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskModel> updateTask(@PathVariable Long id, @RequestBody TaskModel receivedTask){
        var taskUpdated = taskService.update(id,receivedTask);
        return ResponseEntity.ok(taskUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
