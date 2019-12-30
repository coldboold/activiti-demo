package com.example.demo.entity;

import lombok.Data;
import org.activiti.engine.repository.Deployment;

@Data
public class Process {

    private String id;
    private String key;
    private String deploymentId;
    private String category;
    private String name;
    private Integer version;
    private String description;
    private String diagramResourceName;
    private String resourceName;

}
