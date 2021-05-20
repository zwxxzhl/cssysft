package com.atguigu.aclservice.controller.camunda;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.aclservice.entity.ProcinstSub;
import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.IProcinstSubService;
import com.atguigu.aclservice.service.UserService;
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
import java.util.Map;

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

            HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
            List<HistoricProcessInstance> list = query.listPage((page - 1) * limit, limit);

            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for (HistoricProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pi.getId());
                hashMap.put("name", pi.getProcessDefinitionName());
                hashMap.put("status", pi.getState());
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

            //Map<String, Object> variables = new HashMap<>();
            //variables.put("userId", "test");

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    params.getString("key"),
                    "BusinessKey");

            return R.ok().data("name", processInstance.getBusinessKey()).data("id", processInstance.getId());
        } catch (Exception e) {
            System.out.println("=======打印=======");
            System.out.println(e);
            return R.error().message("创建流程实例失败").data(R.DESC, e.toString() + "local:" + e.getLocalizedMessage());
        }
    }

    /**
     * 启动子实例
     */
    @PutMapping(value = "/startSubInstance")
    public R startSubInstance(@RequestBody JSONObject params) {
        try {

            String key = params.getString("key");
            String procinstId = params.getString("procinstId");

            //携带变量
            //Map<String, Object> variables = new HashMap<>();
            //variables.put("userId", "test");

            //启动子实例
            ProcessInstance subProcinst = runtimeService.startProcessInstanceByKey(
                    key,
                    "BusinessKey");

            //保存关系
            try {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userService.selectByUsername(username);

                ProcinstSub procinstSub = new ProcinstSub();
                procinstSub.setUserId(user.getId());
                procinstSub.setProcinstId(procinstId);
                procinstSub.setSubProcinstId(subProcinst.getId());
                procinstSub.setGmtCreateUser(user.getId());
                procinstSub.setGmtUpdateUser(user.getId());
                procinstSubService.save(procinstSub);
            } catch (Exception e) {
                runtimeService.deleteProcessInstance(subProcinst.getId(), "创建流程子实例关系失败，删除");
                throw new RuntimeException("创建流程子实例失败");
            }

            return R.ok().data("name", subProcinst.getBusinessKey()).data("id", subProcinst.getId());
        } catch (Exception e) {
            System.out.println("=======打印=======");
            System.out.println(e);
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
