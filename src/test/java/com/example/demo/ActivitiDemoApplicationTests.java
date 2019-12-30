package com.example.demo;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
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

    @Test
    void contextLoads() {
//        Deployment leave = this.repositoryService
//                .createDeployment()
//                .addClasspathResource("process/normalization.bpmn")
//                .name("转正流程")
//                .deploy();
//        System.out.println(leave.getName());
        // DeploymentEntity[id=03b83179-2aa3-11ea-8d1e-1c1b0ddbc473, name=请假流程]

        Task leave = this.taskService
                .createTaskQuery()
                .processDefinitionKey("leave")
                .singleResult();
        System.out.println(leave.getAssignee());


    }

}
