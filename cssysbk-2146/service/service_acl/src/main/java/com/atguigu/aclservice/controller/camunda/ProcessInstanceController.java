package com.atguigu.aclservice.controller.camunda;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.bus.BusTaskForm;
import com.atguigu.aclservice.entity.bus.ProcinstSub;
import com.atguigu.aclservice.entity.sys.User;
import com.atguigu.aclservice.service.bus.IBusTaskFormService;
import com.atguigu.aclservice.service.bus.IProcinstSubService;
import com.atguigu.aclservice.service.sys.UserService;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiParam;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin/acl//processInstance")
public class ProcessInstanceController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IProcinstSubService procinstSubService;

    @Autowired
    private IBusTaskFormService busTaskFormService;

    @Autowired
    private UserService userService;

    /**
     * 分页查询流程实例
     */
    @GetMapping(value = "/getInstances/{page}/{limit}")
    public R getInstances(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable int page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable int limit,

            @ApiParam(name = "searchObj", value = "查询对象") ProcessDefinition searchObj) {

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.selectByUsername(username);

            HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
            query.tenantIdIn(user.getId());
            List<HistoricProcessInstance> list = query.listPage((page - 1) * limit, limit);

            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for (HistoricProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pi.getId());
                hashMap.put("name", pi.getProcessDefinitionName());
                hashMap.put("status", pi.getState());
                hashMap.put("businessKey", pi.getBusinessKey());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                hashMap.put("startDate", pi.getStartTime());
                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());

                ProcessDefinition def = repositoryService.getProcessDefinition(pi.getProcessDefinitionId());
                hashMap.put("resourceName", def.getResourceName());
                hashMap.put("deploymentId", def.getDeploymentId());
                listMap.add(hashMap);
            }

            long count = query.count();

            return R.ok().data(R.ITEMS, listMap).data("total", count);
        } catch (Exception e) {
            return R.error().message("查询流程实例失败").data(R.DESC, e.toString());
        }
    }


    /**
     * 启动实例
     */
    @PutMapping(value = "/startInstance")
    public R startInstance(@RequestBody JSONObject params) {
        try {
            //携带变量
            //Map<String, Object> variables = new HashMap<>();
            //variables.put("userId", "test");

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.selectByUsername(username);

            // 保存业务表单
            BusTaskForm busTaskForm = new BusTaskForm();
            busTaskForm.setPid("0");
            busTaskForm.setUserId(user.getId());
            busTaskForm.setTitle(params.getString("title"));
            busTaskForm.setContent(params.getString("content"));
            busTaskForm.setGmtCreateUser(user.getId());
            busTaskForm.setGmtUpdateUser(user.getId());

            boolean save = busTaskFormService.save(busTaskForm);

            ProcessInstance processInstance;
            if (save) {
                processInstance = runtimeService.startProcessInstanceByKey(
                        params.getString("key"),
                        busTaskForm.getId());

                // 更新赋值流程实例id/流程定义id
                busTaskForm.setProcinstId(processInstance.getProcessInstanceId());
                busTaskForm.setProcdefId(processInstance.getProcessDefinitionId());
                busTaskFormService.updateById(busTaskForm);
            } else {
                return R.error().message("创建流程实例业务表单失败");
            }

            return R.ok().data("name", processInstance.getBusinessKey()).data("id", processInstance.getId());
        } catch (Exception e) {
            return R.error().message("创建流程实例失败").data(R.DESC, e.toString() + "local:" + e.getLocalizedMessage());
        }
    }

    /**
     * 启动子实例
     */
    @PutMapping(value = "/startSubInstance")
    public R startSubInstance(@RequestBody JSONObject params) {
        try {
            //携带变量
            //Map<String, Object> variables = new HashMap<>();
            //variables.put("userId", "test");

            String key = params.getString("key");
            String formPid = params.getString("formPid");
            String procinstId = params.getString("procinstId");

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.selectByUsername(username);

            // 保存业务表单
            BusTaskForm busTaskForm = new BusTaskForm();
            busTaskForm.setPid(formPid);
            busTaskForm.setUserId(user.getId());
            busTaskForm.setTitle(params.getString("title"));
            busTaskForm.setContent(params.getString("content"));
            busTaskForm.setGmtCreateUser(user.getId());
            busTaskForm.setGmtUpdateUser(user.getId());

            boolean save = busTaskFormService.save(busTaskForm);

            ProcessInstance subProcinst;
            if (save) {
                // 启动子实例
                subProcinst = runtimeService.startProcessInstanceByKey(
                        key,
                        busTaskForm.getId());

                try {
                    // 保存关系
                    ProcinstSub procinstSub = new ProcinstSub();
                    procinstSub.setUserId(user.getId());
                    procinstSub.setProcinstId(procinstId);
                    procinstSub.setSubProcinstId(subProcinst.getId());
                    procinstSub.setGmtCreateUser(user.getId());
                    procinstSub.setGmtUpdateUser(user.getId());
                    procinstSubService.save(procinstSub);

                    // 更新任务表单，赋值实例id、定义id
                    busTaskForm.setProcinstId(subProcinst.getProcessInstanceId());
                    busTaskForm.setProcdefId(subProcinst.getProcessDefinitionId());
                    busTaskFormService.updateById(busTaskForm);
                } catch (Exception e) {
                    runtimeService.deleteProcessInstance(subProcinst.getId(), "创建流程子实例关系失败，删除");
                    busTaskFormService.removeById(busTaskForm.getId());
                    throw new RuntimeException("创建流程子实例失败");
                }
            } else {
                return R.error().message("创建流程实例业务表单失败");
            }

            return R.ok().data("name", subProcinst.getBusinessKey()).data("id", subProcinst.getId());
        } catch (Exception e) {
            return R.error().message("创建流程子实例失败").data(R.DESC, e.toString() + "local:" + e.getLocalizedMessage());
        }
    }

    /**
     * 删除实例
     */
    @DeleteMapping(value = "/deleteInstance")
    public R deleteInstance(@RequestBody JSONObject params) {
        try {
            runtimeService.deleteProcessInstance(params.getString("instanceId"), "流程实例删除");
            return R.ok();
        } catch (Exception e) {
            return R.error().message("删除流程实例失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 挂起实例
     */
    @PutMapping(value = "/suspendInstance")
    public R suspendInstance(@RequestBody JSONObject params) {
        try {
            runtimeService.suspendProcessInstanceById(params.getString("instanceId"));
            return R.ok();
        } catch (Exception e) {
            return R.error().message("挂起流程实例失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 激活实例
     */
    @PutMapping(value = "/resumeInstance")
    public R resumeInstance(@RequestBody JSONObject params) {
        try {
            runtimeService.activateProcessInstanceById(params.getString("instanceId"));
            return R.ok();
        } catch (Exception e) {
            return R.error().message("激活流程实例失败").data(R.DESC, e.toString());
        }
    }

}
