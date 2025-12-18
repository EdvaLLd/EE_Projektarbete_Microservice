package com.edvalld.task_service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<TaskDTO>> getMyTasks(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.getTasksForUser(username));
    }
    @GetMapping
    public ResponseEntity<Task> getTaskById(Authentication authentication, UUID taskId) {
        String username = authentication.getName();
        if(taskRepository.findById(taskId).isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(taskRepository.findById(taskId).get());
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @RequestBody TaskDTO dto,
            Authentication authentication
    ) {
        return ResponseEntity.status(201).body(taskService.createTask(
                authentication.getName(),
                dto
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(Authentication authentication, @PathVariable UUID id) {
        if(taskService.deleteTask(authentication.getName(), id)){
            return ResponseEntity.status(200).body("Deleted task");
        }
        return ResponseEntity.status(404).body("Task not found");
    }
}
