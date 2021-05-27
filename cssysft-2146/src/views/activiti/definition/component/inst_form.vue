<template>
  <div class="app-container">
    <el-form ref="refForm" :model="form" :rules="rules" label-width="60px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="标 题" prop="title">
            <el-input v-model="form.title"></el-input>
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
<!--      <el-row type="flex" justify="center" class="dialog-bottom-btn" :style="{ top: dialogheight+'px' }">
        <el-col :span="6">
          <el-button size="medium" type="info" icon="el-icon-close" @click="onPageClose">关闭</el-button>
        </el-col>
        <el-col :span="6">
          <el-button size="medium" type="primary" icon="el-icon-check" :loading="loading" @click="onSaveOrUpdate">保存</el-button>
        </el-col>
      </el-row>-->
    </el-form>
  </div>
</template>

<script setup>
import {ref, useContext, defineEmit, inject, getCurrentInstance} from "vue";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const {expose} = useContext();
const emit = defineEmit(['page-close']);

const dialogdata = inject('dialogdata');
const dialogheight = inject('dialogheight');

const form = ref({});
const loading = ref(false);
const refForm = ref(null);
const rules = ref({
  title: [{required: true, trigger: 'blur', message: '用户名必须输入'}],
  content: [{required: true, trigger: 'blur', message: '用户名必须输入'}],
});

const initData = () => {
  console.log('initData');
}

const onSaveOrUpdate = () => {
  refForm.value.validate(valid => {
    if (valid) {
      loading.value = true;
      console.log(this.form);
    }
  })
}

const onPageClose = () => {

}

expose({
  initData
});
</script>


<style scoped>

</style>