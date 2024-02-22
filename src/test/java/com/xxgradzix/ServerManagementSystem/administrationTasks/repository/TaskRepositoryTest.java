package com.xxgradzix.ServerManagementSystem.administrationTasks.repository;

import com.xxgradzix.ServerManagementSystem.administrationTasks.entities.TaskEntity;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import com.xxgradzix.ServerManagementSystem.user.repository.UserEntityRepository;
import com.xxgradzix.ServerManagementSystem.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Date;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    public TaskRepository taskRepository;
    @Autowired
    public UserEntityRepository userRepository;
    @MockBean
    public UserService userService;
    @Test
    void findAllByAssignedUsersContainsOrderByDeadline() {

        // given

        UserEntity userEntity = UserEntity.builder()
                .username("test")
                .password("test")
                .email("test@email.com")
                .build();

        userRepository.save(userEntity);

        TaskEntity taskEntity = TaskEntity.builder()
                .name("test")
                .description("test")
                .deadline(new Date())
                .assignedUsers(Set.of(userEntity))
                .build();

        taskRepository.save(taskEntity);

        // when

        var result = taskRepository.findAllByAssignedUsersContainsOrderByDeadline(userEntity, null);
        // then

        assertThat(result).isNotEmpty();
        assertThat(result).contains(taskEntity);
    }

    @Test
    void findAllByAssignedUsersContainsAndCompletedOrderByDeadline() {

        // given
        UserEntity userEntity = UserEntity.builder()
                .username("test")
                .password("test")
                .email("test@email.com")
                .build();

        userRepository.save(userEntity);

        TaskEntity taskEntity = TaskEntity.builder()
                .name("test")
                .description("test")
                .deadline(new Date())
                .assignedUsers(Set.of(userEntity))
                .build();
        TaskEntity taskEntity2 = TaskEntity.builder()
                .name("test2")
                .description("test2")
                .deadline(new Date())
                .completed(true)
                .assignedUsers(Set.of(userEntity))
                .build();

        taskRepository.save(taskEntity);
        taskRepository.save(taskEntity2);

        // when

        var result = taskRepository.findAllByAssignedUsersContainsAndCompletedOrderByDeadline(userEntity, true, null);
        // then

        assertThat(result).isNotEmpty();
        assertThat(result).contains(taskEntity);
        assertThat(result).doesNotContain(taskEntity2);
    }
}