<template>
  <div class="app-container">
    <el-form ref="refUserForm" :model="user" :rules="validateRules" label-width="120px">
      <el-form-item label="部门" prop="relations">
        <el-cascader
          style="width: 100%"
          v-model="user.relations"
          :options="depOptions"
          :props="{expandTrigger: 'hover', checkStrictly: true, value: 'id', label: 'depName'}"
        />
      </el-form-item>
      <el-form-item label="账号" prop="username">
        <el-input v-model="user.username"/>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="user.nickName"/>
      </el-form-item>
      <el-form-item label="工号" prop="userNo">
        <el-input v-model="user.userNo"/>
      </el-form-item>

      <el-form-item v-if="!user.id" label="密码" prop="password">
        <el-input v-model="user.password" type="password"/>
      </el-form-item>


      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import userApi from '../../../api/acl/user'
import depApi from "../../../api/acl/dep";

const defaultForm = {
  username: '',
  userNo: '',
  nickName: '',
  password: '',
  relations: null
}

const validPass = (rule, value, callback) => {
  if (value.length < 6) {
    callback(new Error('密码不能小于6位'))
  } else {
    callback()
  }
}

const validRelations = (rule, value, callback) => {
  if (!value || value.length === 0) {
    callback(new Error('部门必须选择'))
  } else {
    callback()
  }
}

export default {
  data() {
    return {
      user: defaultForm,
      depOptions: [],
      saveBtnDisabled: false, // 保存按钮是否禁用,
      validateRules: {
        relations: [{required: true, trigger: 'change', validator: validRelations}],
        username: [{ required: true, trigger: 'blur', message: '用户名必须输入' }],
        password: [{ required: true, trigger: 'blur', validator: validPass }]
      }
    }
  },
  watch: {
    $route(to, from) {
      this.init()
    }
  },
  created() {
    this.init();
    this.depTree();
  },
  methods: {
    init() {
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id
        this.fetchDataById(id)
      } else {
        // 对象拓展运算符：拷贝对象，而不是赋值对象的引用
        this.user = { ...defaultForm }
      }
    },
    depTree() {
      depApi.selectTree({}).then(res => {
        this.depOptions = res.data.items;
      })
    },
    saveOrUpdate() {
      this.$refs.refUserForm.validate(valid => {
        if (valid) {
          this.saveBtnDisabled = true // 防止表单重复提交

          if (this.user.relations.length > 0) {
            this.user.depId = this.user.relations[this.user.relations.length - 1];
            this.user.depRelation = this.user.relations.join(",");
          }

          if (!this.user.id) {
            this.saveData()
          } else {
            this.updateData()
          }
        }
      })
    },
    saveData() {
      userApi.save(this.user).then(response => {
        // debugger
        if (response.success) {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.$router.push({ path: '/dispatchweb/acl/user/list' })
        }
      })
    },
    updateData() {
      userApi.update(this.user).then(response => {
        if (response.success) {
          this.$message({
            type: 'success',
            message: response.message
          })
          this.$router.push({ path: '/dispatchweb/acl/user/list' })
        }
      })
    },
    fetchDataById(id) {
      userApi.getById(id).then(response => {
        this.user = response.data
        if (this.user.depRelation) {
          this.user.relations = this.user.depRelation.split(',');
        }
      })
    }

  }
}
</script>
