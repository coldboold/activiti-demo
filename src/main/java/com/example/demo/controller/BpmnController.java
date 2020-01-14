package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * 流程文件相关Api
 *
 * @author winily
 * @date 2020/1/14 10:09
 */
@RestController
@RequestMapping("/bpmn")
@Api(tags = "流程 bpmn 管理")
public class BpmnController {
    private final RepositoryService repositoryService;

    public BpmnController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }


    @ApiOperation(value = "根据流程 id 获取流程 bpmn 文件")
    @GetMapping
    private void getActivitiBPMN(String processDefinitionId, HttpServletResponse response) {
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        // 获取流程模型对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        byte[] bytes = bpmnXMLConverter.convertToXML(bpmnModel);

        // 获取流程名称
        String resourceName = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult()
                .getResourceName();

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + resourceName);
        response.setContentLength(bytes.length);
        BufferedOutputStream outStream;
        try {
            outStream = new BufferedOutputStream(response.getOutputStream());
            outStream.write(bytes, 0, bytes.length);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
