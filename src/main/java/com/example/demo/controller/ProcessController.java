package com.example.demo.controller;

import com.example.demo.entity.Process;
import com.example.demo.entity.RuntimeProcess;
import com.example.demo.vo.DemoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author winily
 * @date 2019/12/29 22:48
 */
@RestController
@RequestMapping("process")
@Api(tags = "流程管理")
public class ProcessController {
    // 资源服务
    private final RepositoryService repositoryService;
    // 运行服务
    private final RuntimeService runtimeService;
    // 历史服务
    private final HistoryService historyService;

    private final TaskService taskService;

    public ProcessController(HistoryService historyService, RepositoryService repositoryService, RuntimeService runtimeService, TaskService taskService) {
        this.historyService = historyService;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @ApiOperation(value = "上传文件部署流程")
    @PostMapping
    public DemoVO publication(@RequestParam("file") MultipartFile file, String name) {
        Deployment leave = null;
        try {
            leave = this.repositoryService
                    .createDeployment()
                    .addInputStream(file.getOriginalFilename(), file.getInputStream())
                    .name(name)
                    .deploy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(leave.getName());
        return DemoVO.success(leave);
    }

    @ApiOperation(value = "根据Key启动流程")
    @PostMapping("{key}")
    public DemoVO startProcessInstance(@PathVariable String key) {
//        Map<String, Object> map = new HashMap<>();
//        // key 为UEL表达式的变量名，value为将要为表达式设置的准确的值
//        map.put("assignee0", username);
        /**
         * startProcessInstanceByKey(key, map);
         * 该函数为根据流程 key 来启动一个流程实例
         * 其中第二个值就是启动流程时要被动态设置的变量名和值
         * 可以设置多个表达式变量
         */
//        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(key, map);
        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(key);

        Process process = new Process();
        process.setId(processInstance.getId());
        process.setDeploymentId(processInstance.getDeploymentId());
        process.setName(processInstance.getName());
        return DemoVO.success(process);
    }

    @ApiOperation(value = "获取所有流程")
    @GetMapping()
    public DemoVO index() {
        List<ProcessDefinition> processDefinitionList = this.repositoryService
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .asc()
                .list();
        List<Process> processes = new ArrayList<>();
        if (processDefinitionList != null && processDefinitionList.size() > 0) {
            for (ProcessDefinition pd : processDefinitionList) {
                Process process = new Process(pd);
                String deploymentId = pd.getDeploymentId();
                Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
                process.setDeploymentTime(deployment.getDeploymentTime());
                process.setName(deployment.getName());
                processes.add(process);
            }
        }
        return DemoVO.success(processes);
    }


}
