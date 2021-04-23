package com.atguigu.aclservice.controller.activiti;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiParam;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.GetProcessInstancesPayloadBuilder;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Order;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
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
    private ProcessRuntime processRuntime;

    @Autowired
    private RepositoryService repositoryService;


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

        Page<ProcessInstance> processInstances;
        try {
            processInstances = processRuntime.processInstances(Pageable.of((page - 1) * limit, limit,
                    Order.by("startDate", Order.Direction.DESC)));
            List<ProcessInstance> list = processInstances.getContent();

            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for(ProcessInstance pi:list){
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pi.getId());
                hashMap.put("name", pi.getName());
                hashMap.put("status", pi.getStatus());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                hashMap.put("startDate", pi.getStartDate());
                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());

                //因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentId", pd.getDeploymentId());
                listMap.add(hashMap);
            }

            return R.ok().data(R.ITEMS, listMap).data("total", processInstances.getTotalItems());
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
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);

            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(params.getString("key"))
                    .withName(params.getString("name"))
                    //.withVariable("content", variable)
                    .withBusinessKey("自定义BusinessKey")
                    .build());

            return R.ok().data("name", processInstance.getName()).data("id", processInstance.getId());
        } catch (Exception e) {
            System.out.println("=======打印=======");
            System.out.println(e);
            return R.error().message("创建流程实例失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 删除实例
     */
    @DeleteMapping(value = "/deleteInstance")
    public R deleteInstance(@RequestBody JSONObject params) {
        try {

            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(params.getString("instanceId"))
                    .build()
            );

            return R.ok().data("name", processInstance.getName());
        }
        catch(Exception e) {
            return R.error().message("删除流程实例失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 挂起实例
     */
    @PutMapping(value = "/suspendInstance")
    public R suspendInstance(@RequestBody JSONObject params) {

        try {

            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(params.getString("instanceId"))
                    .build()
            );

            return R.ok().data("name", processInstance.getName());
        }
        catch(Exception e) {
            return R.error().message("挂起流程实例失败").data(R.DESC, e.toString());
        }
    }

    /**
     * 激活实例
     */
    @PutMapping(value = "/resumeInstance")
    public R resumeInstance(@RequestParam("instanceId") String instanceId) {

        try {

            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(instanceId)
                    .build()
            );

            return R.ok().data("name", processInstance.getName());
        }
        catch(Exception e) {
            return R.error().message("激活流程实例失败").data(R.DESC, e.toString());
        }
    }

    //获取参数
    @GetMapping(value = "/variables")
    public R variables(@RequestParam("instanceId") String instanceId) {
        try {
            List<VariableInstance> variableInstance = processRuntime.variables(ProcessPayloadBuilder
                    .variables()
                    .withProcessInstanceId(instanceId)
                    .build());

            return R.ok().data(variableInstance);
        }
        catch(Exception e) {
            return R.error().message("获取流程参数失败").data(R.DESC, e.toString());
        }
    }

}
