package com.example.demo.controller;

import com.example.demo.entity.RuntimeProcess;
import com.example.demo.vo.DemoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author winily
 * @date 2020/1/14 16:22
 */
@RestController
@RequestMapping("process_instance")
@Api(tags = "流程实例管理")
public class ProcessInstanceController {
    // 资源服务
    private final RepositoryService repositoryService;
    // 运行服务
    private final RuntimeService runtimeService;
    // 历史服务
    private final HistoryService historyService;

    private final TaskService taskService;

    public ProcessInstanceController(HistoryService historyService, RepositoryService repositoryService, RuntimeService runtimeService, TaskService taskService) {
        this.historyService = historyService;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    @ApiOperation(value = "获取所有正在运行中的流程实例")
    @GetMapping("")
    public DemoVO runtimeProcess() {
        List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery()
                .orderByProcessDefinitionId()
                .asc()
                .list();

        List<RuntimeProcess> runtimeProcesses = new ArrayList<>();
        if (processInstances != null && processInstances.size() > 0) {
            for (ProcessInstance processInstance : processInstances) {
                RuntimeProcess runtimeProcess = new RuntimeProcess(processInstance);

                Task task = this.taskService.createTaskQuery()
                        .processInstanceId(processInstance.getId())
                        .singleResult();

                runtimeProcess.setTask(task.getName());
                // 获取流程部署名称
                Deployment deployment = repositoryService.createDeploymentQuery()
                        .deploymentId(processInstance.getDeploymentId())
                        .singleResult();

                ProcessInstance suspendedProcessInstance = this.runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .suspended()
                        .singleResult();

                runtimeProcess.setSuspended(true);
                if (suspendedProcessInstance == null) {
                    runtimeProcess.setSuspended(false);
                }
                runtimeProcess.setName(deployment.getName());
                runtimeProcesses.add(runtimeProcess);
            }
        }
        return DemoVO.success(runtimeProcesses);
    }

    @ApiOperation(value = "挂起/激活/流程实例, state 0 激活， !0 挂起")
    @PutMapping("/{state}/{processInstanceId}")
    public DemoVO suspendedProcess(@PathVariable("state") int state, @PathVariable("processInstanceId") String processInstanceId) {
        if (state == 0) {
            this.runtimeService.activateProcessInstanceById(processInstanceId);
        } else {
            this.runtimeService.suspendProcessInstanceById(processInstanceId);
        }
        return DemoVO.success();
    }

    @ApiOperation(value = "删除流程实例")
    @DeleteMapping("/{processInstanceId}")
    public DemoVO deleteProcess(@PathVariable("processInstanceId") String processInstanceId, String deleteReason) {
        this.runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
        return DemoVO.success();
    }
}
