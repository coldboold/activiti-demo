package com.example.demo;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ActivitiDemoApplicationTests {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    void contextLoads() {
        Deployment leave = this.repositoryService
                .createDeployment()
                .addClasspathResource("process/leave.bpmn")
                .name("请假流程")
                .deploy();
        System.out.println(leave);
        // DeploymentEntity[id=ed1af703-2a57-11ea-9347-005056c00008, name=请假流程]
    }

}
