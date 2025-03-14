package me.personal.taskManager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.personal.taskManager.DTO.TaskDTO;
import me.personal.taskManager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Sistema de gestão de tarefas")
@RestController
@RequestMapping("/tasks")
@Transactional
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Realiza o GET retornando todas as tarefas", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefas encontradas"),
            @ApiResponse(responseCode = "404", description = "Tarefas não encontradas")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> listaTodos(){
        var tasks = taskService.getAll();
        var taskDTO = tasks.stream().map(TaskDTO::new).toList();
        return ResponseEntity.ok(taskDTO);
    }

    @Operation(summary = "Realiza o GET usando o ID de um registro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("findID/{id}")
    public ResponseEntity<TaskDTO> listaTaskById(@PathVariable Long id){
        var task = taskService.getTaskById(id);
        return ResponseEntity.ok(new TaskDTO(task));
    }

    @Operation(summary = "Realiza o GET usando o Titulo de um registro", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("findTitulo/{titulo}")
    public ResponseEntity<TaskDTO> listaTaskByTitulo(@PathVariable String titulo){
        var task = taskService.getTaskByTitulo(titulo);
        return ResponseEntity.ok(new TaskDTO(task));
    }

    @Operation(summary = "Cria uma tarefa", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa criada"),
            @ApiResponse(responseCode = "422", description = "Tarefa invalida")
    })
    @PostMapping("create")
    public ResponseEntity<TaskDTO> criaTask(@RequestBody TaskDTO receivedTask){
        var taskCreated = taskService.create(receivedTask.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TaskDTO(taskCreated));
    }

    @Operation(summary = "Atualiza a tarefa usando o ID", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "422", description = "Tarefa invalida")
    })
    @PutMapping("updateID/{id}")
    public ResponseEntity<TaskDTO> updateTaskId(@PathVariable Long id, @RequestBody TaskDTO receivedTask){
        var taskUpdated = taskService.update(id,receivedTask.toModel());
        return ResponseEntity.ok(new TaskDTO(taskUpdated));
    }

    @Operation(summary = "Atualiza a tarefa usando o Titulo", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "422", description = "Tarefa invalida")
    })
    @PutMapping("updateTitulo/{titulo}")
    public ResponseEntity<TaskDTO> updateTaskTitulo(@PathVariable String titulo, @RequestBody TaskDTO receivedTask){
        var taskFinded = taskService.getTaskByTitulo(titulo);
        var taskUpdated = taskService.update(taskFinded.getId(),receivedTask.toModel());
        return ResponseEntity.ok(new TaskDTO(taskUpdated));
    }

    @Operation(summary = "Atualiza a coluna DONE de uma tarefa para true", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "422", description = "Tarefa invalida")
    })
    @PutMapping("done/{id}")
    public ResponseEntity<TaskDTO> taskDone(@PathVariable Long id){
        var taskFinded = taskService.getTaskById(id);
        taskFinded.setFeito(true);
        var taskUpdated = taskService.update(id, taskFinded);
        return ResponseEntity.ok(new TaskDTO(taskUpdated));
    }

    @Operation(summary = "Deleta uma tarefa usando o ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não econtrada")
    })
    @DeleteMapping("deleteID/{id}")
    public ResponseEntity<Void> deleteTaskId(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta uma tarefa usando o Titulo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não econtrada")
    })
    @DeleteMapping("deleteTitulo/{titulo}")
    public ResponseEntity<Void> deleteTaskTitulo(@PathVariable String titulo){
        var taskFinded = taskService.getTaskByTitulo(titulo);
        taskService.delete(taskFinded.getId());
        return ResponseEntity.noContent().build();
    }
}
