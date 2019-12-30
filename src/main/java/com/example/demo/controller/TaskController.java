package com.example.demo.controller;

import com.example.demo.vo.DemoVO;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * @author hiems
 * @date 2019/12/30 1:07
 * @description
 */
@RestController
@RequestMapping("task")
public class TaskController {
    // 任务服务
    @Autowired
    private TaskService taskService;

    @ApiOperation(value="获取用户所有任务")
    @GetMapping()
    public DemoVO getTask(String username) {
        List<Task> list = this.taskService
                .createTaskQuery()
                .taskAssignee(username)
                .list();
        return DemoVO.success(list);
    }
}
