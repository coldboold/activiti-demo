package com.example.demo.entity;

import lombok.Data;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Date;

/**
 * @author winily
 * @date 2020/1/14 15:32
 */
@Data
public class RuntimeProcess {
    private String id;// id
    private String name;// 名称
    private String processDefinitionKey;// key
    private String task;
    private Date startTime;
    private Boolean suspended;


    public RuntimeProcess(ProcessInstance processInstance) {
        this.id = processInstance.getId();
        this.name = processInstance.getProcessDefinitionName();
        this.processDefinitionKey = processInstance.getProcessDefinitionKey();
        this.startTime = processInstance.getStartTime();
    }
}
