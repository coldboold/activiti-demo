package com.example.demo.controller;

import com.example.demo.entity.TaskEntity;
import com.example.demo.vo.DemoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiems
 * @date 2019/12/30 1:07
 * @description
 */
@RestController
@RequestMapping("task")
@Api(tags = "任务管理")
public class TaskController {
    // 任务服务
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiOperation(value = "获取用户所有任务")
    @GetMapping()
    public DemoVO getTask(String username) {
        List<Task> tasks = this.taskService
                .createTaskQuery()
                .taskAssignee(username)
                .list();
        List<TaskEntity> taskEntities = new ArrayList<>();

        tasks.forEach(task -> {
            TaskEntity taskEntity = new TaskEntity(task);
            taskEntities.add(taskEntity);
        });
        return DemoVO.success(taskEntities);
    }

    @ApiOperation(value = "任务办理")
    @PostMapping()
    public DemoVO complete(String taskId) {
        this.taskService.complete(taskId);
        return DemoVO.success();
    }
}
