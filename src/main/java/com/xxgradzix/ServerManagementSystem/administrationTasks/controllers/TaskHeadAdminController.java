package com.xxgradzix.ServerManagementSystem.administrationTasks.controllers;

import com.xxgradzix.ServerManagementSystem.administrationTasks.entities.CreateTaskRequest;
import com.xxgradzix.ServerManagementSystem.administrationTasks.entities.TaskEntity;
import com.xxgradzix.ServerManagementSystem.administrationTasks.service.TaskService;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/task/headAdmin")
public class TaskHeadAdminController {

    private final TaskService taskService;

    public TaskHeadAdminController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<Iterable<TaskEntity>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<Iterable<TaskEntity>> getTasksByUser(@PathVariable(name = "userId") Long userId, @RequestParam(required = false) Boolean completed) {
        return ResponseEntity.ok(taskService.getTasksByUserId(userId, completed));
    }
    @PutMapping("description/{taskId}")
    public ResponseEntity<TaskEntity> updateTaskDescription(@PathVariable(name = "taskId") Long taskId, @RequestBody String description) {
        return ResponseEntity.ok(taskService.setTaskDescription(taskId, description));
    }
    @PostMapping("/create")
    public ResponseEntity<TaskEntity> createTask(@CurrentSecurityContext(expression = "authentication") Authentication authentication, @RequestBody CreateTaskRequest request) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.createTask(user, request.getName(), request.getDescription(), request.getDeadline()));
    }

    @PutMapping("/assign/task/{taskId}/user/{userId}")
    public ResponseEntity<TaskEntity> assignUserToTask(@PathVariable(name = "taskId") Long taskId, @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(taskService.assignUserToTask(taskId, userId));
    }

    @PutMapping("/remove/task/{taskId}/user/{userId}")
    public ResponseEntity<TaskEntity> removeUserFromTask(@PathVariable(name = "taskId") Long taskId, @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(taskService.removeUserFromTask(taskId, userId));
    }

    @PutMapping("/complete/{taskId}")
    public ResponseEntity<TaskEntity> completeTask(@PathVariable(name = "taskId") Long taskId) {
        return ResponseEntity.ok(taskService.markTaskAsCompleted(taskId));
    }


}
