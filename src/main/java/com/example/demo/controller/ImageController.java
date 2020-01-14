package com.example.demo.controller;

import com.example.demo.util.FlowUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

/**
 * 流程图相关Api
 *
 * @author winily
 * @date 2020/1/14 9:41
 */
@RestController
@RequestMapping("/image")
@Api(tags = "生成流程图")
public class ImageController {
    private final RepositoryService repositoryService;

    private final FlowUtils flowUtils;

    public ImageController(RepositoryService repositoryService, FlowUtils flowUtils) {
        this.repositoryService = repositoryService;
        this.flowUtils = flowUtils;
    }

    /**
     * <p>生成正在运行的流程已运行节点高亮图，只能生成正在运行的流程流程图</p>
     *
     * @param instanceId
     * @return
     */
    @ResponseBody
    @ApiOperation(value = "生成正在运行的流程已运行节点高亮图，只能生成正在运行的流程流程图")
    @GetMapping(value = "/running")
    public ResponseEntity a(@RequestParam("instanceId") String instanceId) {
        InputStream in = this.flowUtils.getResourceDiagramInputStream(instanceId);
        return new ResponseEntity<>(new InputStreamResource(in), new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * <p>生成普通流程图，无高亮节点</p>
     *
     * @param processDefinitionId
     * @return
     */
    @ResponseBody
    @ApiOperation(value = "生成流程图")
    @GetMapping(value = "/process")
    public ResponseEntity traceProcess(@RequestParam("processDefinitionId") String processDefinitionId) {
        ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "宋体", "微软雅黑", "黑体");
        return new ResponseEntity<>(new InputStreamResource(in), new HttpHeaders(), HttpStatus.OK);
    }
}
