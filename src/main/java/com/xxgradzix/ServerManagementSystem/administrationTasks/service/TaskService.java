package com.xxgradzix.ServerManagementSystem.administrationTasks.service;

import com.xxgradzix.ServerManagementSystem.administrationTasks.entities.TaskEntity;
import com.xxgradzix.ServerManagementSystem.administrationTasks.exception.ApiRequestException;
import com.xxgradzix.ServerManagementSystem.administrationTasks.repository.TaskRepository;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import com.xxgradzix.ServerManagementSystem.user.repository.UserEntityRepository;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserEntityRepository userEntityRepository;

    public TaskService(TaskRepository taskRepository, UserEntityRepository userEntityRepository) {
        this.taskRepository = taskRepository;
        this.userEntityRepository = userEntityRepository;
    }

    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ApiRequestException("Task not found"));
    }

    public Iterable<TaskEntity> getTasksByUserId(Long userId, @Nullable Boolean completed) {
        UserEntity user = userEntityRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found"));
        if(completed != null) return taskRepository.findAllByAssignedUsersContainsAndCompletedOrderByDeadline(user, completed, Sort.by("deadline").ascending());
        return taskRepository.findAllByAssignedUsersContainsOrderByDeadline(user, Sort.by("deadline").ascending());
    }
    public Iterable<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    public TaskEntity createTask(UserEntity creator, String name, String description, Date deadline) {

        TaskEntity task = TaskEntity.builder()
                .completed(false)
                .name(name)
                .description(description)
                .assignedUsers(new HashSet<>())
                .createdBy(creator)
                .creationDate(new Date())
                .deadline(deadline)
                .build();

        return taskRepository.save(task);
    }

    public TaskEntity assignUserToTask(Long taskId, Long userId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new ApiRequestException("Task not found"));
        UserEntity user = userEntityRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found"));
        Set<UserEntity> assignedUsers = task.getAssignedUsers();
        assignedUsers.add(user);
        task.setAssignedUsers(assignedUsers);
        return taskRepository.save(task);
    }
    public TaskEntity removeUserFromTask(Long taskId, Long userId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new ApiRequestException("Task not found"));
        UserEntity user = userEntityRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found"));
        task.getAssignedUsers().remove(user);
        return taskRepository.save(task);
    }

    public TaskEntity markTaskAsCompleted(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new ApiRequestException("Task not found"));
        task.setCompleted(true);
        return taskRepository.save(task);
    }
    public TaskEntity setTaskDescription(Long taskId, String description) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(() -> new ApiRequestException("Task not found"));
        task.setDescription(description);
        return taskRepository.save(task);
    }




}
