import request from '@/utils/request'

const api_name = '/admin/acl/processDefinition'

export default {

  //部署流程
  addDeploymentByString(obj) {
    return request({
      url: `${api_name}/addDeploymentByString`,
      method: 'post',
      data: obj
    })
  },
  //查询流程定义列表
  getProcessDefinition(page, limit, searchObj) {
    return request({
      url: `${api_name}/getDefinitions/${page}/${limit}`,
      method: 'get',
      params: searchObj // url查询字符串或表单键值对
    })
  },
  //查看流程定义(获取xml)
  getProcessDefineXml(params) {
    return request({
      url: `${api_name}/getProcessDefinitionXml`,
      method: 'get',
      params: { id: params.id }
    })
  },
  //删除流程定义
  deleteProcessDefinition(params) {
    return request({
      url: `${api_name}/delDefinition`,
      method: 'delete',
      data: { deploymentId: params.deploymentId, cascade: params.cascade }
    })
  },
  //--------------------------------------------------------------------//
  //查询实例列表
  getInstances(page, limit, searchObj) {
    return request({
      url: `${api_name}/getInstances/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  //挂起实例
  suspendInstance(instanceId) {
    return request({
      url: `${api_name}/suspendInstance`,
      method: 'put',
      data: { instanceId }
    })
  },
  //激活实例
  resumeInstance(instanceId) {
    return request({
      url: `${api_name}/resumeInstance`,
      method: 'put',
      data: { instanceId }
    })
  },
  //删除实例
  deleteInstance(instanceId) {
    return request({
      url: `${api_name}/deleteInstance`,
      method: 'delete',
      data: { instanceId }
    })
  },
  //--------------------------------------------------------------------//
  //查询我的任务列表
  getTasks(page, limit, searchObj) {
    return request({
      url: `${api_name}/getTasks/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },



  updateById(user) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: user
    })
  },
  getAssign(userId) {
    return request({
      url: `${api_name}/toAssign/${userId}`,
      method: 'get'
    })
  },
  saveAssign(userId, roleId) {
    return request({
      url: `${api_name}/doAssign`,
      method: 'post',
      params: { userId, roleId }
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },
  removeRows(idList) {
    return request({
      url: `${api_name}/batchRemove`,
      method: 'delete',
      data: idList
    })
  }
}
