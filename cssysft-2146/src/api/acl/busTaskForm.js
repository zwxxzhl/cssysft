import request from '@/utils/request'

const api_name = '/admin/acl/bustaskform'

export default {
    update(menu) {
        return request({
            url: `${api_name}/update`,
            method: "put",
            data: menu
        })
    }
}
