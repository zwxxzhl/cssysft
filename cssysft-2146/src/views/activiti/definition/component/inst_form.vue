<template>
  <div class="app-container">
    <el-form ref="refForm" :model="form" :rules="rules" label-width="80px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="内容" prop="content">
            <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="form.content"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :style="{ position: 'absolute', top: bottomBtnH+'px' }">
        <el-col :span="24" class="flex-row-center">
          <el-button size="medium" type="info" icon="el-icon-close" @click="onPageClose">关闭</el-button>
          <el-button size="medium" type="primary" icon="el-icon-check" :loading="loading" @click="onSaveOrUpdate">保存</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup>
import {ref, useContext, defineEmit, inject, getCurrentInstance, onMounted} from "vue";

const vm = getCurrentInstance();
const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const {expose} = useContext();
const emit = defineEmit(['page-close']);

const dialogdata = inject('dialogdata');
const dialogheight = inject('dialogheight');
const bottomBtnH = ref(0);

const form = ref({});
const loading = ref(false);
const rules = ref({
  title: [{required: true, trigger: 'blur', message: '用户名必须输入'}],
  content: [{required: true, trigger: 'blur', message: '用户名必须输入'}],
});

onMounted(() => {debugger
  globalProperties.$nextTick
  vm.$nextTick
  bottomBtnH.value = dialogheight.value - 36;
});

const onSaveOrUpdate = () => {
  this.$refs.refForm.validate(valid => {
    if (valid) {
      loading.value = true;

    }
  })
}

const onPageClose = () => {
  debugger
  dialogheight.value
  dialogdata.value
}

expose({
  open
});
</script>


<style scoped>
  .ceshi{
    float: top;
  }
</style>