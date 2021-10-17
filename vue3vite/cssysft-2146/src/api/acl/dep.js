import request from '@/utils/request'

const api_name = '/admin/acl/dep'

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
    selectTree(params) {
        return request({
            url: `${api_name}/selectTree`,
            method: "get",
            params: params
        })
    },
    update(form) {
        return request({
            url: `${api_name}/update`,
            method: "put",
            data: form
        })
    },
    save(form) {
        return request({
            url: `${api_name}/save`,
            method: "post",
            data: form
        })
    },
    remove(ids) {
        return request({
            url: `${api_name}/remove`,
            method: "delete",
            data: ids
        })
    }
}
