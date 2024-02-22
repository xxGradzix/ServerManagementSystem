package com.xxgradzix.ServerManagementSystem.administrationTasks.service;

import com.xxgradzix.ServerManagementSystem.administrationTasks.entities.TaskEntity;
import com.xxgradzix.ServerManagementSystem.administrationTasks.repository.TaskRepository;
import com.xxgradzix.ServerManagementSystem.user.Role;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import com.xxgradzix.ServerManagementSystem.user.repository.UserEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserEntityRepository userEntityRepository;
    private AutoCloseable closeable;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, userEntityRepository);
    }

    @AfterEach
    void tearDown() throws Exception {

        closeable.close();
    }

    @Test
    void getTaskById() {
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1L)
                .name("test")
                .description("Lorem ipsum")
                .build();
        // when

        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(taskEntity));

        taskService.getTaskById(1L);
        // then
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(taskRepository).findById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(1L);
    }

    @Test
    void getTasksByUserId() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("test")
                .email("test@email.com")
                .build();
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1L)
                .name("test")
                .description("Lorem ipsum")
                .assignedUsers(Set.of(userEntity))
                .build();
        // when
        when(userEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(userEntity));
        when(taskRepository.findAllByAssignedUsersContainsOrderByDeadline(userEntity, null)).thenReturn(List.of(taskEntity));
        when(taskRepository.findAllByAssignedUsersContainsAndCompletedOrderByDeadline(userEntity, true, null)).thenReturn(List.of(taskEntity));

        taskService.getTasksByUserId(1L, null);

        ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        ArgumentCaptor<Sort> sortArgumentCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(taskRepository).findAllByAssignedUsersContainsOrderByDeadline(userEntityArgumentCaptor.capture(), sortArgumentCaptor.capture());

        assertThat(userEntityArgumentCaptor.getValue()).isEqualTo(userEntity);
        assertThat(sortArgumentCaptor.getValue()).isEqualTo(Sort.by("deadline").ascending());
    }

    @Test
    void getAllTasks() {

        // when
        taskService.getAllTasks();
        // then
        verify(taskRepository).findAll();
    }
    @Test
    void createTask() {

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("test")
                .email("test")
                .build();

        Date date = new Date();
        String name = "test";
        String description = "Lorem ipsum";

        // when
        taskService.createTask(userEntity, name, description, date);
        // then
        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository).save(taskEntityArgumentCaptor.capture());
        TaskEntity savedTaskEntity = taskEntityArgumentCaptor.getValue();

        assertThat(savedTaskEntity.getCreatedBy()).isEqualTo(userEntity);
        assertThat(savedTaskEntity.getName()).isEqualTo(name);
        assertThat(savedTaskEntity.getDescription()).isEqualTo(description);
        assertThat(savedTaskEntity.getDeadline()).isEqualTo(date);
    }

    @Test
    void assignUserToTask() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("test")
                .email("test")
                .roles(Set.of(Role.USER))
                .build();
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1L)
                .name("test")
                .description("Lorem ipsum")
                .assignedUsers(new HashSet<>())
                .build();
        // when
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(taskEntity));
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        taskService.assignUserToTask(1L, 1L);

        // then
        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository).save(taskEntityArgumentCaptor.capture());
        TaskEntity savedTaskEntity = taskEntityArgumentCaptor.getValue();

        assertThat(savedTaskEntity.getAssignedUsers()).contains(userEntity);
    }

    @Test
    void removeUserFromTask() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("test")
                .password("test")
                .email("test")
                .roles(Set.of(Role.USER))
                .build();
        HashSet<UserEntity> userEntities = new HashSet<>();
        userEntities.add(userEntity);
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1L)
                .name("test")
                .description("Lorem ipsum")
                .assignedUsers(userEntities)
                .build();
        // when
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(taskEntity));
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        taskService.removeUserFromTask(1L, 1L);

        // then
        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository).save(taskEntityArgumentCaptor.capture());
        TaskEntity savedTaskEntity = taskEntityArgumentCaptor.getValue();

        assertThat(savedTaskEntity.getAssignedUsers()).doesNotContain(userEntity);
    }

    @Test
    void markTaskAsCompleted() {
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1L)
                .name("test")
                .description("Lorem ipsum")
                .completed(false)
                .build();
        // when
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(taskEntity));

        taskService.markTaskAsCompleted(1L);
        // then
        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository).save(taskEntityArgumentCaptor.capture());
        TaskEntity savedTaskEntity = taskEntityArgumentCaptor.getValue();
        assertThat(savedTaskEntity.isCompleted()).isTrue();
    }

    @Test
    void setTaskDescription() {
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1L)
                .name("test")
                .description("Lorem ipsum")
                .build();
        String description = "test";

        // when
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(taskEntity));

        taskService.setTaskDescription(1L, description);
        // then
        ArgumentCaptor<TaskEntity> taskEntityArgumentCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(taskRepository).save(taskEntityArgumentCaptor.capture());
        TaskEntity savedTaskEntity = taskEntityArgumentCaptor.getValue();
        assertThat(savedTaskEntity.getDescription()).isEqualTo(description);
    }
}