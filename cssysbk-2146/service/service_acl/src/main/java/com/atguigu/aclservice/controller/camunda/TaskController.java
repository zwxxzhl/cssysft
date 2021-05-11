package com.atguigu.aclservice.controller.camunda;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiParam;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/acl/task")
public class TaskController {

    @Autowired
    private TaskService taskRuntime;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询我的代办任务
     */
    @GetMapping(value = "/getTasks/{page}/{limit}")
    public R getTasks(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable int page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable int limit,

            @ApiParam(name = "searchObj", value = "查询对象") ProcessDefinition searchObj) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            TaskQuery query = taskRuntime.createTaskQuery();
            query.taskAssignee(username);
            List<Task> list = query.listPage((page - 1) * limit, limit);

            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (Task tk : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", tk.getId());
                hashMap.put("name", tk.getName());
                hashMap.put("createdDate", tk.getCreateTime());

                //执行人，null时前台显示未拾取
                if (tk.getAssignee() == null) {
                    hashMap.put("assignee", "待拾取任务");
                } else {
                    hashMap.put("assignee", tk.getAssignee());//
                }

                ProcessDefinition processDefinition = repositoryService.getProcessDefinition(tk.getProcessDefinitionId());
                hashMap.put("instanceName", processDefinition.getName());
                listMap.add(hashMap);
            }

            long count = query.count();

            return R.ok().data(R.ITEMS, listMap).data("total", count);
        } catch (Exception e) {
            return R.error().message("获取我的代办任务失败").data(R.DESC, e.toString());
        }
    }

    /**
     *  直接完成代办
     */
    @PutMapping(value = "/completeTask")
    public R completeTask(@RequestBody JSONObject params) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            TaskQuery query = taskRuntime.createTaskQuery();
            query.taskId(params.getString("taskId"));
            Task task = query.singleResult();

            if (task.getAssignee() == null) {
                taskRuntime.claim(params.getString("taskId"), username);
            }
            taskRuntime.complete(params.getString("taskId"));

            return R.ok();
        } catch (Exception e) {
            return R.error().message("完成失败").data(R.DESC, e.toString());
        }
    }

}
