package me.personal.taskManager.controller;

import me.personal.taskManager.DTO.TaskDTO;
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
    public ResponseEntity<List<TaskDTO>> listaTodos(){
        var tasks = taskService.getAll();
        var taskDTO = tasks.stream().map(TaskDTO::new).toList();
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping("findID/{id}")
    public ResponseEntity<TaskDTO> listaTaskById(@PathVariable Long id){
        var task = taskService.getTaskById(id);
        return ResponseEntity.ok(new TaskDTO(task));
    }

    @GetMapping("findTitulo/{titulo}")
    public ResponseEntity<TaskDTO> listaTaskByTitulo(@PathVariable String titulo){
        var task = taskService.getTaskByTitulo(titulo);
        return ResponseEntity.ok(new TaskDTO(task));
    }

    @PostMapping("create")
    public ResponseEntity<TaskDTO> criaTask(@RequestBody TaskDTO receivedTask){
        var taskCreated = taskService.create(receivedTask.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TaskDTO(taskCreated));
    }

    @PutMapping("updateID/{id}")
    public ResponseEntity<TaskDTO> updateTaskId(@PathVariable Long id, @RequestBody TaskDTO receivedTask){
        var taskUpdated = taskService.update(id,receivedTask.toModel());
        return ResponseEntity.ok(new TaskDTO(taskUpdated));
    }

    @PutMapping("updateTitulo/{titulo}")
    public ResponseEntity<TaskDTO> updateTaskTitulo(@PathVariable String titulo, @RequestBody TaskDTO receivedTask){
        var taskFinded = taskService.getTaskByTitulo(titulo);
        var taskUpdated = taskService.update(taskFinded.getId(),receivedTask.toModel());
        return ResponseEntity.ok(new TaskDTO(taskUpdated));
    }

    @PutMapping("done/{id}")
    public ResponseEntity<TaskDTO> taskDone(@PathVariable Long id){
        var taskFinded = taskService.getTaskById(id);
        taskFinded.setFeito(true);
        var taskUpdated = taskService.update(id, taskFinded);
        return ResponseEntity.ok(new TaskDTO(taskUpdated));
    }

    @DeleteMapping("deleteID/{id}")
    public ResponseEntity<Void> deleteTaskId(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("deleteTitulo/{titulo}")
    public ResponseEntity<Void> deleteTaskTitulo(@PathVariable String titulo){
        var taskFinded = taskService.getTaskByTitulo(titulo);
        taskService.delete(taskFinded.getId());
        return ResponseEntity.noContent().build();
    }
}
