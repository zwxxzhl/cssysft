import request from '@/utils/request'

const api_name = '/admin/acl/flow'

export default {
  machine(params) {
    return request({
      url: `${api_name}/machine`,
      method: "get",
      params: params
    })
  }
}
