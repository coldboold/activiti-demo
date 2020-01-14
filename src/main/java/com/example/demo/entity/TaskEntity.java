package com.example.demo.entity;

import lombok.Data;
import org.activiti.engine.task.Task;

import java.util.Date;

/**
 * @author winily
 * @date 2020/1/14 16:38
 */
@Data
public class TaskEntity {
    private String assignee;
    private String formKey;
    private String id;
    private String name;
    private Date createTime;
    private String taskDefinitionKey;

    public TaskEntity(Task task) {
        this.assignee = task.getAssignee();
        this.formKey = task.getFormKey();
        this.id = task.getId();
        this.name = task.getName();
        this.createTime = task.getCreateTime();
        this.taskDefinitionKey = task.getTaskDefinitionKey();
    }
}
