import request from '@/utils/request'

const api_name = '/admin/acl/bustaskform'

export default {
    selectTree(procinstId){
        return request({
            url: `${api_name}/selectTree`,
            method: "get",
            params: { procinstId }
        })
    },
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
    },
    save(form) {
        return request({
            url: `${api_name}/save`,
            method: "post",
            data: form
        })
    }
}
