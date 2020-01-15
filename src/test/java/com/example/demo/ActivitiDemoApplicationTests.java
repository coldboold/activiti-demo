package com.example.demo;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ActivitiDemoApplicationTests {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Test
    void contextLoads() {
        Deployment leave = this.repositoryService
                .createDeployment()
                .addClasspathResource("process/resign.bpmn")
                .name("离职申请审批流程")
                .deploy();
        System.out.println(leave.getName());
        // DeploymentEntity[id=03b83179-2aa3-11ea-8d1e-1c1b0ddbc473, name=请假流程]

//        Task leave = this.taskService
//                .createTaskQuery()
//                .processDefinitionKey("leave")
//                .singleResult();
//        System.out.println(leave.getAssignee());

//        HistoricProcessInstance processInstance = historyService
//                .createHistoricProcessInstanceQuery()
//                .processInstanceId("58912f81-2aad-11ea-aa23-1c1b0ddbc473")
//                .singleResult();
//        System.out.println(processInstance.getName());
    }

}
