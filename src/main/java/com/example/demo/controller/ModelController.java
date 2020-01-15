package com.example.demo.controller;

import com.example.demo.vo.DemoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除模型")
    public DemoVO deleteModel(@PathVariable("id") String id) {
        this.repositoryService.deleteModel(id);
        return DemoVO.success();
    }

    @GetMapping("/bpmn/{id}")
    @ApiOperation(value = "导出模型部署文件")
    public DemoVO getModelBpmn(@PathVariable("id") String id, HttpServletResponse response) {

        Model model = this.repositoryService.getModel(id);
        byte[] modelEditorSource = this.repositoryService.getModelEditorSource(model.getId());
        String filename = model.getName() + ".bpmn";

        try {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(filename, "Utf-8"));
            response.setContentLength(modelEditorSource.length);
            BufferedOutputStream outStream;
            outStream = new BufferedOutputStream(response.getOutputStream());
            outStream.write(modelEditorSource, 0, modelEditorSource.length);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DemoVO.success();
    }
}
