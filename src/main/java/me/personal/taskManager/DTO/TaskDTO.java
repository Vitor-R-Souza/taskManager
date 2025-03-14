package me.personal.taskManager.DTO;

import me.personal.taskManager.model.TaskModel;

import java.util.Optional;

public record TaskDTO(
        Long id,
        String titulo,
        String descricao,
        Boolean feito
) {
    public TaskDTO(TaskModel model){
        this(
                model.getId(),
                Optional.ofNullable(model.getTitulo()).orElse("Tarefa" + model.getId()),
                model.getDescricao(),
                Optional.ofNullable(model.getFeito()).orElse(false)
        );
    }

    public TaskModel toModel(){
        TaskModel model = new TaskModel();
        model.setId(this.id);
        model.setTitulo(Optional.ofNullable(this.titulo).orElse("Tarefa" + model.getId()));
        model.setDescricao(this.descricao);
        model.setFeito(Optional.ofNullable(this.feito).orElse(false));
        return model;
    }



}
