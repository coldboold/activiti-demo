package com.example.demo.controller;

import com.example.demo.vo.DemoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author winily
 * @date 2020/1/14 10:41
 */
@RestController
@RequestMapping("model")
@Api(tags = "模型管理")
public class ModelController {
    // 资源服务
    private final RepositoryService repositoryService;

    public ModelController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @PostMapping
    @ApiOperation(value = "流程转化为Model")
    public DemoVO translateToModel(@RequestParam("processDefinitionId") String processDefinitionId) {
        BpmnModel bpmnModel = this.repositoryService
                .getBpmnModel(processDefinitionId);

        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        // 获取流程模型对象
        byte[] bytes = bpmnXMLConverter.convertToXML(bpmnModel);

        Model model = this.repositoryService.newModel();

        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        System.out.println(processDefinition.getResourceName());


        model.setCategory(processDefinition.getCategory());
        model.setDeploymentId(processDefinition.getDeploymentId());
        model.setKey(processDefinition.getKey());
        model.setName(this.repositoryService
                .createDeploymentQuery()
                .deploymentId(processDefinition.getDeploymentId())
                .singleResult()
                .getName());
        model.setVersion(processDefinition.getVersion());
        model.setTenantId(processDefinition.getTenantId());

        this.repositoryService.saveModel(model);
        this.repositoryService.addModelEditorSource(model.getId(), bytes);

        return DemoVO.success();
    }

    @GetMapping
    @ApiOperation(value = "获取所有的模型")
    public DemoVO index() {
        List<Model> list = this.repositoryService.createModelQuery()
                .orderByCreateTime()
                .desc()
                .list();

        return DemoVO.success(list);
    }

}
