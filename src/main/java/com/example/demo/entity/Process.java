package com.example.demo.entity;

import lombok.Data;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.Date;

@Data
public class Process {

    private String id;// 流程定义的key+版本+随机生成数
    private String name;// .bpmn文件中的name属性值
    private String key;// .bpmn文件中的id属性值
    private Integer version;// 当流程定义的key值相同的相同下，版本升级，默认1
    private String resourceName;
    private String deploymentId;
    private Date deploymentTime;

    public Process(ProcessDefinition processDefinition) {
        this.id = processDefinition.getId();// 流程定义的key+版本+随机生成数
        this.name = processDefinition.getName();// .bpmn文件中的name属性值
        this.key = processDefinition.getKey();// .bpmn文件中的key属性值
        this.version = processDefinition.getVersion();// 当流程定义的key值相同的相同下，版本升级，默认1
        this.resourceName = processDefinition.getResourceName();
        this.resourceName = processDefinition.getDiagramResourceName();
        this.deploymentId = processDefinition.getDeploymentId();
    }

    public Process() {
    }
}
