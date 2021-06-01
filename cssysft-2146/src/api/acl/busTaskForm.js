import request from '@/utils/request'

const api_name = '/admin/acl/bustaskform'

export default {
    find(id) {
        return request({
            url: `${api_name}/find`,
            method: "get",
            params: { id }
        })
    },
    update(form) {
        return request({
            url: `${api_name}/update`,
            method: "put",
            data: form
        })
    }
}
