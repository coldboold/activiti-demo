package com.example.demo.controller;

import com.example.demo.entity.Process;
import com.example.demo.util.ActivitiUtils;
import com.example.demo.util.FlowUtils;
import com.example.demo.vo.DemoVO;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author winily
 * @date 2019/12/29 22:48
 */
@RestController
@RequestMapping("process")
@Api(tags = "流程管理")
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

        System.out.println("historyService" + this.historyService);

        List<Process> processes = new ArrayList<>();

        if (processDefinitionList != null && processDefinitionList.size() > 0) {
            for (ProcessDefinition pd : processDefinitionList) {
                Process process = new Process(pd);
                String deploymentId = pd.getDeploymentId();
                Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
                process.setDeploymentTime(deployment.getDeploymentTime());
                processes.add(process);
            }
        }


        return DemoVO.success(processes);
    }


    @Autowired
    private FlowUtils flowUtils;
    /**
     * <p>查看当前流程图</p>
     *
     * @param instanceId 流程实例
     * @param response   void 响应
     * @author FRH
     * @time 2018年12月10日上午11:14:12
     * @version 1.0
     */
    @ResponseBody
    @ApiOperation(value = "查看流程图")
    @GetMapping(value = "/showImg")
    public ResponseEntity traceprocess(HttpServletResponse response, @RequestParam("instanceId") String instanceId) throws Exception {

        System.out.println(instanceId);
        InputStream in = this.flowUtils.getResourceDiagramInputStream(instanceId);
        System.out.println("in" + in);
        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ResponseEntity<>(inputStreamResource, new HttpHeaders(), HttpStatus.OK);
    }

}
