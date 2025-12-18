package com.edvalld.task_service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getTasksForUser(String username){
        List<TaskDTO> tasks = new ArrayList<>();
        for (Task task : taskRepository.findAll()) {
            if(task.getUser().equals(username)){
                tasks.add(new TaskDTO(task.getTitle(), task.getTask()));
            }
        }
        return tasks;
    }

    public TaskDTO createTask(String user, TaskDTO taskDTO){
        Task task = new Task();
        task.setTitle(taskDTO.name());
        task.setTask(taskDTO.description());
        task.setUser(user);

        taskRepository.save(task);

        return taskDTO;
    }

    public boolean deleteTask(String user, UUID id){
        if(taskRepository.findById(id).isEmpty()) return false;
        if(user.equals(taskRepository.findById(id).get().getUser())){
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
