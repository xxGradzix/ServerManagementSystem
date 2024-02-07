package com.xxgradzix.ServerManagementSystem.administrationTasks.controllers;

import com.xxgradzix.ServerManagementSystem.administrationTasks.entities.TaskEntity;
import com.xxgradzix.ServerManagementSystem.administrationTasks.service.TaskService;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/v1/task/admin")
public class TaskAdminController {

    private final TaskService taskService;

    public TaskAdminController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<Iterable<TaskEntity>> getAllTasks(@CurrentSecurityContext(expression = "authentication") Authentication authentication, @RequestParam(required = false) Boolean completed) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.getTasksByUserId(user.getId(), completed));
    }

}
