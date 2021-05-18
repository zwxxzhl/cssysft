import request from '@/utils/request'

const api_definition_name = '/admin/acl/processDefinition';
const api_instance_name = '/admin/acl/processInstance';
const api_task_name = '/admin/acl/task';
const api_history_name = '/admin/acl/camundaHistory';

export default {

  //部署流程
  addDeploymentByString(obj) {
    return request({
      url: `${api_definition_name}/addDeploymentByString`,
      method: 'post',
      data: obj
    })
  },
  //查询流程定义列表
  getProcessDefinition(page, limit, searchObj) {
    return request({
      url: `${api_definition_name}/getDefinitions/${page}/${limit}`,
      method: 'get',
      params: searchObj // url查询字符串或表单键值对
    })
  },
  //查看流程定义(获取xml)
  getProcessDefineXml(params) {
    return request({
      url: `${api_definition_name}/getProcessDefinitionXml`,
      method: 'get',
      params: {id: params.id}
    })
  },
  //删除流程定义
  deleteProcessDefinition(params) {
    return request({
      url: `${api_definition_name}/delDefinition`,
      method: 'delete',
      data: {deploymentId: params.deploymentId, cascade: params.cascade}
    })
  },
  //--------------------------------------------------------------------//
  //启动实例
  startInstance(params) {
    return request({
      url: `${api_instance_name}/startInstance`,
      method: 'put',
      data: {key: params.key, name: params.name, variable: params.variable}
    })
  },
  //查询实例列表
  getInstances(page, limit, searchObj) {
    return request({
      url: `${api_instance_name}/getInstances/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //挂起实例
  suspendInstance(instanceId) {
    return request({
      url: `${api_instance_name}/suspendInstance`,
      method: 'put',
      data: {instanceId}
    })
  },
  //激活实例
  resumeInstance(instanceId) {
    return request({
      url: `${api_instance_name}/resumeInstance`,
      method: 'put',
      data: {instanceId}
    })
  },
  //删除实例
  deleteInstance(instanceId) {
    return request({
      url: `${api_instance_name}/deleteInstance`,
      method: 'delete',
      data: {instanceId}
    })
  },
  //--------------------------------------------------------------------//
  //查询我的任务列表
  getTasks(page, limit, searchObj) {
    return request({
      url: `${api_task_name}/getTasks/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //完成任务
  completeTask(taskId) {
    return request({
      url: `${api_task_name}/completeTask`,
      method: 'put',
      data: {taskId}
    })
  },
  //--------------------------------------------------------------------//
  //获取用户历史实例
  getHistoricTaskInstance(page, limit, searchObj) {
    return request({
      url: `${api_history_name}/getHistoricTaskInstance/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //获取流程实例中高亮节点数据
  gethighLine(instanceId) {
    return request({
      url: `${api_history_name}/gethighLine`,
      method: 'get',
      params: {instanceId}
    })
  }
}
