package com.example.demo.controller;

import com.example.demo.entity.Process;
import com.example.demo.vo.DemoVO;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hiems
 * @date 2019/12/29 22:48
 * @description
 */
@RestController
@RequestMapping("process")
public class ProcessController {
    // 资源服务
    @Autowired
    private RepositoryService repositoryService;

    // 运行服务
    @Autowired
    private RuntimeService runtimeService;

    // 历史服务
    @Autowired
    private HistoryService historyService;

    @ApiOperation(value="根据Key启动流程并动态设置代理人")
    @PostMapping("{key}")
    public DemoVO startProcessInstance(@PathVariable String key, String username) {

        Map<String,Object> map = new HashMap<>();
        map.put("assignee0", username);

        ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(key, map);
        Process process = new Process();
        process.setId(processInstance.getId());
        process.setDeploymentId(processInstance.getDeploymentId());
        process.setName(processInstance.getName());
        process.setDescription(processInstance.getDescription());
        return DemoVO.success(process);
    }

    @ApiOperation(value="获取所有流程")
    @GetMapping()
    public DemoVO getProcess() {

        List<Process> list = new ArrayList<>();

        this.repositoryService
                .createProcessDefinitionQuery()
                .list()
                .forEach(item -> {
                    Process process = new Process();
                    process.setId(item.getId());
                    process.setKey(item.getKey());
                    process.setDeploymentId(item.getDeploymentId());
                    process.setCategory(item.getCategory());
                    process.setName(item.getName());
                    process.setVersion(item.getVersion());
                    process.setDescription(item.getDescription());
                    process.setDiagramResourceName(item.getDiagramResourceName());
                    process.setResourceName(item.getResourceName());
                    list.add(process);
                });

        return DemoVO.success(list);
    }




}
