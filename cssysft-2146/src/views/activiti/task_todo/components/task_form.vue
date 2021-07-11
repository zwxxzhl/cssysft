<template>
  <el-form class="dialog-content" ref="refForm" :model="form" :rules="rules" label-width="60px"
           :disabled="openType === enums.formType.detail">
    <el-row>
      <el-col :span="24">
        <el-form-item label="标 题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="内 容" prop="content">
          <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="form.content"></el-input>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <el-row class="dialog-bottom" type="flex" justify="center">
    <el-col :span="6">
      <el-button size="medium" type="info" icon="el-icon-close"
                 @click="onPageClose">关闭
      </el-button>
    </el-col>
    <el-col :span="6" v-if="openType !== enums.formType.detail">
      <el-button size="medium" type="danger" icon="el-icon-check"
                 :loading="loading" @click="onSaveOrUpdate(true)">直接完成
      </el-button>
    </el-col>
    <el-col :span="6" v-if="openType !== enums.formType.detail">
      <el-button size="medium" type="primary" icon="el-icon-check"
                 :loading="loading" @click="onSaveOrUpdate(false)">阶段汇报
      </el-button>
    </el-col>
  </el-row>
</template>

<script setup>
import {ref, useContext, defineEmit, inject, getCurrentInstance, onMounted} from "vue";

import activitiApi from "../../../../api/acl/activiti";
import busTaskFormApi from "../../../../api/acl/busTaskForm";
import enums from "../../../../utils/enums";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const {expose} = useContext();
const emit = defineEmit(['complete']);

const dialogData = inject('dialogData');
const openType = inject('openType');
const dialogClose = inject('dialogClose');

const form = ref({});
const loading = ref(false);
const refForm = ref(null);
const rules = ref({
  title: [{required: true, trigger: 'blur', message: '标题必须输入'}],
  content: [{required: true, trigger: 'blur', message: '内容必须输入'}]
});

const initData = () => {
  loading.value = false;
  form.value = {};
  if (enums.formType.add !== openType.value) {
    // 更新，获取表单数据
    busTaskFormApi.find(dialogData.value.businessKey).then(res => {
      form.value = res.data;
    })
  }
}

onMounted(() => {
  initData();
});

const updateForm = () => {
  loading.value = true;
  busTaskFormApi.update(form.value).then(res => {
    if (res.success) {
      globalProperties.$message.success(res.message);
    }
    loading.value = false;
    dialogClose();
  })
}

const saveForm = () => {
  loading.value = true;
  busTaskFormApi.save({
    taskId: dialogData.value.id,
    formPid: dialogData.value.businessKey,
    procinstId: dialogData.value.processInstanceId,
    procdefId: dialogData.value.processDefinitionId,
    title: form.value.title,
    content: form.value.content
  }).then(res => {
    if (res.success) {
      globalProperties.$message.success(res.message);
    }
    loading.value = false;
    dialogClose();
  })
}

const complete = () => {
  activitiApi.completeTask({
    taskId: dialogData.value.id,
    formPid: dialogData.value.businessKey,
    procinstId: dialogData.value.processInstanceId,
    procdefId: dialogData.value.processDefinitionId,
    title: form.value.title,
    content: form.value.content
  }).then(res => {
    if (res.success) {
      globalProperties.$message.success(res.message);
    }
    emit('complete');
    loading.value = false;
    dialogClose();
  })
}

const saveFormAndComplete = () => {
  refForm.value.validate(valid => {
    if (valid) {
      loading.value = true;
      complete();
    }
  })
}

const onSaveOrUpdate = (flag) => {
  if (enums.formType.add === openType.value && flag) {
    saveFormAndComplete();
  } else if (enums.formType.add === openType.value && !flag) {
    saveForm();
  } else if (enums.formType.edit === openType.value) {
    updateForm();
  }
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