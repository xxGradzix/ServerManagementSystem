package com.xxgradzix.ServerManagementSystem.administrationTasks.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class CreateTaskRequest {

    private String name;
    private String description;
    private Date deadline;

}
