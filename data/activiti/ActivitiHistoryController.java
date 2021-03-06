package com.atguigu.aclservice.controller.activiti;

import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiParam;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/acl/activitiHistory")
public class ActivitiHistoryController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    /**
     * 用户历史任务
     */
    @GetMapping(value = "/getHistoricTaskInstance/{page}/{limit}")
    public R getHistoryInstances(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable int page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable int limit,

            @ApiParam(name = "searchObj", value = "查询对象") ProcessDefinition searchObj) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
            query.taskAssignee(username).orderByHistoricTaskInstanceEndTime().asc();

            List<HistoricTaskInstance> historicTaskInstances = query.listPage((page - 1) * limit, limit);
            long count = query.count();

            return R.ok().data(R.ITEMS, historicTaskInstances).data("total", count);
        } catch (Exception e) {
            return R.error().message("获取历史任务失败").data(R.DESC, e.toString());
        }
    }

    //任务实例历史
    @GetMapping(value = "/getInstancesByPiID")
    public R getInstancesByPiID(@RequestParam("piID") String piID) {
        try {

            //--------------------------------------------另一种写法-------------------------
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .processInstanceId(piID)
                    .list();

            return R.ok().data(R.ITEMS, historicTaskInstances);
        } catch (Exception e) {
            return R.error().message("获取历史任务失败").data(R.DESC, e.toString());
        }

    }

    /**
     * 流程图高亮
     */
    @GetMapping("/gethighLine")
    public R gethighLine(@RequestParam("instanceId") String instanceId) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceId).singleResult();
            //获取bpmnModel对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            //因为我们这里只定义了一个Process 所以获取集合中的第一个即可
            Process process = bpmnModel.getProcesses().get(0);
            //获取所有的FlowElement信息
            Collection<FlowElement> flowElements = process.getFlowElements();

            Map<String, String> map = new HashMap<>();
            for (FlowElement flowElement : flowElements) {
                //判断是否是连线
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String ref = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(ref + targetRef, sequenceFlow.getId());
                }
            }

            //获取流程实例 历史节点(全部)
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .list();
            //各个历史节点   两两组合 key
            Set<String> keyList = new HashSet<>();
            for (HistoricActivityInstance i : list) {
                for (HistoricActivityInstance j : list) {
                    if (i != j) {
                        keyList.add(i.getActivityId() + j.getActivityId());
                    }
                }
            }
            //高亮连线ID
            Set<String> highLine = new HashSet<>();
            keyList.forEach(s -> highLine.add(map.get(s)));


            //获取流程实例 历史节点（已完成）
            List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .finished()
                    .list();
            //高亮节点ID
            Set<String> highPoint = new HashSet<>();
            listFinished.forEach(s -> highPoint.add(s.getActivityId()));

            //获取流程实例 历史节点（待办节点）
            List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .unfinished()
                    .list();

            //需要移除的高亮连线
            Set<String> set = new HashSet<>();
            //待办高亮节点
            Set<String> waitingToDo = new HashSet<>();
            listUnFinished.forEach(s -> {
                waitingToDo.add(s.getActivityId());

                for (FlowElement flowElement : flowElements) {
                    //判断是否是 用户节点
                    if (flowElement instanceof UserTask) {
                        UserTask userTask = (UserTask) flowElement;

                        if (userTask.getId().equals(s.getActivityId())) {
                            List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                            //因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
                            if (outgoingFlows != null && outgoingFlows.size() > 0) {
                                outgoingFlows.forEach(a -> {
                                    if (a.getSourceRef().equals(s.getActivityId())) {
                                        set.add(a.getId());
                                    }
                                });
                            }
                        }
                    }
                }
            });

            highLine.removeAll(set);

            //获取当前用户
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            //存放 高亮 我的办理节点
            Set<String> iDo = new HashSet<>();

            //当前用户已完成的任务
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(username)
                    .finished()
                    .processInstanceId(instanceId).list();

            taskInstanceList.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

            Map<String, Object> reMap = new HashMap<>(7);
            reMap.put("highPoint", highPoint);
            reMap.put("highLine", highLine);
            reMap.put("waitingToDo", waitingToDo);
            reMap.put("iDo", iDo);

            return R.ok().data(reMap);
        } catch (Exception e) {
            return R.error().message("获取实例详情失败").data(R.DESC, e.toString());
        }
    }


}
