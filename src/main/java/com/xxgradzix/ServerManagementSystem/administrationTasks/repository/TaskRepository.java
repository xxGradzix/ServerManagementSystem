package com.xxgradzix.ServerManagementSystem.administrationTasks.repository;

import com.xxgradzix.ServerManagementSystem.administrationTasks.entities.TaskEntity;
import com.xxgradzix.ServerManagementSystem.user.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByAssignedUsersContainsOrderByDeadline(UserEntity userEntity, Sort sort);
    List<TaskEntity> findAllByAssignedUsersContainsAndCompletedOrderByDeadline(UserEntity userEntity, boolean completed, Sort sort);
}
