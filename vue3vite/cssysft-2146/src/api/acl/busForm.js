import request from '../../utils/request'

const api_name = '/admin/acl/busform'

export default {
  page(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },
  select(params) {
    return request({
      url: `${api_name}/select`,
      method: "get",
      params: params
    })
  },
  update(form) {
    return request({
      url: `${api_name}/updateCus`,
      method: "put",
      data: form
    })
  },
  save(form) {
    return request({
      url: `${api_name}/saveCus`,
      method: "post",
      data: form
    })
  },
  remove(ids) {
    return request({
      url: `${api_name}/remove/logic`,
      method: "delete",
      data: ids
    })
  }
}
