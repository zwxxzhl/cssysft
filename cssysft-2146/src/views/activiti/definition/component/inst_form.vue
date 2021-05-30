<template>
  <el-form class="dialog-content" ref="refForm" :model="form" :rules="rules" label-width="60px">
    <el-row>
      <el-col :span="24">
        <el-form-item label="标 题" prop="title">
          <el-input v-model="form.title" placeholder="请输入任务标题"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="内 容" prop="content">
          <el-input type="textarea" :rows="2" placeholder="请输入任务内容" v-model="form.content"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <el-row class="dialog-bottom" type="flex" justify="center">
    <el-col :span="4">
      <el-button size="medium" type="info" icon="el-icon-close"
                 @click="onPageClose">取消</el-button>
    </el-col>
    <el-col :span="4">
      <el-button size="medium" type="primary" icon="el-icon-check"
                 :loading="loading" @click="onSaveOrUpdate">保存</el-button>
    </el-col>
  </el-row>
</template>

<script setup>
import {ref, useContext, defineEmit, inject, getCurrentInstance} from "vue";

import activitiApi from "../../../../api/acl/activiti";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const {expose} = useContext();
const emit = defineEmit(['page-close']);

const dialogData = inject('dialogData');
const dialogClose = inject('dialogClose');

const form = ref({});
const loading = ref(false);
const refForm = ref(null);
const rules = ref({
  title: [{required: true, trigger: 'blur', message: '任务标题必须输入'}],
  content: [{required: true, trigger: 'blur', message: '任务内容必须输入'}]
});

const initData = () => {
  console.log('initData');
}

const onSaveOrUpdate = () => {
  refForm.value.validate(valid => {
    if (valid) {
      loading.value = true;
      activitiApi.startInstance({
        key: row.key,
        form: form.value
      }).then(res => {
        if (res.success) {
          globalProperties.$message.success(res.message);
        }
        loading.value = false;
      })
    }
  })
}

const onPageClose = () => {
  dialogClose();
}

expose({
  initData
});
</script>


<style scoped>

</style>