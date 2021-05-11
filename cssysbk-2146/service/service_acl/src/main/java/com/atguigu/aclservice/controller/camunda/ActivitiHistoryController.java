package com.atguigu.aclservice.controller.camunda;

import com.atguigu.utils.utils.R;
import io.swagger.annotations.ApiParam;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin/acl/camundaHistory")
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

            ArrayList<Map<String, Object>> listMap = new ArrayList<>();
            for (HistoricTaskInstance instance : historicTaskInstances) {
                Map<String, Object> map = new HashMap<>();
                ProcessDefinition def = repositoryService.getProcessDefinition(instance.getProcessDefinitionId());

                map.put("id", instance.getId());
                map.put("name", instance.getName());
                map.put("assignee", instance.getAssignee());
                map.put("startTime", instance.getStartTime());
                map.put("endTime", instance.getEndTime());
                map.put("processDefinitionId", instance.getProcessDefinitionId());
                map.put("processDefinitionKey", instance.getProcessDefinitionKey());
                map.put("processInstanceId", instance.getProcessInstanceId());

                map.put("processName", def.getName());
                map.put("version", def.getVersion());

                listMap.add(map);
            }

            long count = query.count();

            return R.ok().data(R.ITEMS, listMap).data("total", count);
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
            HistoricProcessInstance hisProIns = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            //System.out.println(hisProIns.getProcessDefinitionName()+" "+hisProIns.getProcessDefinitionKey());
            //===================已完成节点
            List<HistoricActivityInstance> finished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .finished()
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .list();
            Set<String> highPoint = new HashSet<>();
            finished.forEach(t -> highPoint.add(t.getActivityId()));

            //=================待完成节点
            List<HistoricActivityInstance> unfinished = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).unfinished().list();
            Set<String> waitingToDo = new HashSet<>();
            unfinished.forEach(t -> waitingToDo.add(t.getActivityId()));

            //=================iDo 我执行过的
            //获取当前用户
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            Set<String> iDo = new HashSet<>();
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery().taskAssignee(username).finished().processInstanceId(instanceId).list();
            taskInstanceList.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

            //===========高亮线
            Set<String> highLine = new HashSet<>();
            //获取流程定义的bpmn模型
            BpmnModelInstance bpmn = repositoryService.getBpmnModelInstance(hisProIns.getProcessDefinitionId());
            //已完成任务列表 可直接使用上面写过的
            List<HistoricActivityInstance> finishedList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .finished()
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .list();
            int finishedNum = finishedList.size();
            //循环 已完成的节点
            for (int i = 0; i < finishedNum; i++) {
                HistoricActivityInstance finItem = finishedList.get(i);
                //根据 任务key 获取 bpmn元素
                ModelElementInstance domElement = bpmn.getModelElementById(finItem.getActivityId());
                //转换成 flowNode流程节点 才能获取到 输出线 和输入线
                FlowNode act = (FlowNode)domElement;
                Collection<SequenceFlow> outgoing = act.getOutgoing();
                //循环当前节点的 向下分支
                outgoing.forEach(v->{
                    String tarId = v.getTarget().getId();
                    //已完成
                    for (int j = 0; j < finishedNum; j++) {
                        //循环历史完成节点 和当前完成节点的向下分支比对
                        //如果当前完成任务 的结束时间 等于 下个任务的开始时间
                        HistoricActivityInstance setpFinish = finishedList.get(j);
                        String finxId = setpFinish.getActivityId();
                        if(tarId.equals(finxId)){
                            if(finItem.getEndTime().equals(setpFinish.getStartTime())){
                                highLine.add(v.getId());
                            }
                        }
                    }
                    //待完成
                    for (int j = 0; j < unfinished.size(); j++) {
                        //循环待节点 和当前完成节点的向下分支比对
                        HistoricActivityInstance setpUnFinish = unfinished.get(j);
                        String finxId = setpUnFinish.getActivityId();
                        if(tarId.equals(finxId)){
                            if(finItem.getEndTime().equals(setpUnFinish.getStartTime())){
                                highLine.add(v.getId());
                            }
                        }
                    }

                });
            }

            //返回结果
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
