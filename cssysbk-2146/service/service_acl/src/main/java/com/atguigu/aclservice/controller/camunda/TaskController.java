package com.atguigu.aclservice.controller.camunda;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.bus.BusTaskForm;
import com.atguigu.aclservice.entity.sys.User;
import com.atguigu.aclservice.service.bus.IBusTaskFormService;
import com.atguigu.aclservice.service.sys.UserService;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiParam;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/acl/task")
public class TaskController {

    @Autowired
    private TaskService taskRuntime;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IBusTaskFormService busTaskFormService;

    @Autowired
    private UserService userService;

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
            query.initializeFormKeys();
            List<Task> list = query.listPage((page - 1) * limit, limit);

            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            for (Task tk : list) {
                Map<String, Object> map = new HashMap<>();

                HistoricProcessInstanceQuery instanceQuery = historyService.createHistoricProcessInstanceQuery();
                instanceQuery.processInstanceId(tk.getProcessInstanceId());
                HistoricProcessInstance instance = instanceQuery.list().get(0);

                map.put("id", tk.getId());
                map.put("name", tk.getName());
                map.put("createTime", tk.getCreateTime());

                //执行人，null时前台显示未拾取
                if (tk.getAssignee() == null) {
                    map.put("assignee", "待拾取任务");
                } else {
                    map.put("assignee", tk.getAssignee());
                }

                map.put("processDefinitionId", tk.getProcessDefinitionId());
                map.put("processInstanceId", tk.getProcessInstanceId());

                map.put("businessKey", instance.getBusinessKey());
                map.put("processName", instance.getProcessDefinitionName());
                map.put("processDefinitionKey", instance.getProcessDefinitionKey());
                map.put("version", instance.getProcessDefinitionVersion());

                listMap.add(map);
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
            User user = userService.selectByUsername(username);

            String formPid = params.getString("formPid");
            String procinstId = params.getString("procinstId");
            String procdefId = params.getString("procdefId");

            // 保存业务表单
            BusTaskForm busTaskForm = new BusTaskForm();
            busTaskForm.setPid(formPid);
            busTaskForm.setProcinstId(procinstId);
            busTaskForm.setProcdefId(procdefId);
            busTaskForm.setUserId(user.getId());
            busTaskForm.setTitle(params.getString("title"));
            busTaskForm.setContent(params.getString("content"));
            busTaskForm.setGmtCreateUser(user.getId());
            busTaskForm.setGmtUpdateUser(user.getId());

            boolean save = busTaskFormService.save(busTaskForm);

            // 完成任务
            if (save) {
                TaskQuery query = taskRuntime.createTaskQuery();
                query.taskId(params.getString("taskId"));
                Task task = query.singleResult();

                if (task.getAssignee() == null) {
                    taskRuntime.claim(params.getString("taskId"), username);
                }
                taskRuntime.complete(params.getString("taskId"));
            } else {
                return R.error().message("完成任务失败");
            }

            return R.ok();
        } catch (Exception e) {
            return R.error().message("完成失败").data(R.DESC, e.toString());
        }
    }

}
