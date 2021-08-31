<template>
  <div>
    <zwx-form-mu
      ref="refFormMu"
      :form="form"
      :form-row="formRow"
      :rules="rules">
    </zwx-form-mu>

    <el-row class="dialog-bottom" type="flex" justify="center">
      <el-col :span="4">
        <el-button :size="comCfg.buttonSize" :type="comCfg.closeBtnType" :icon="comCfg.closeBtnIcon"
                   @click="onPageClose">关闭
        </el-button>
      </el-col>
      <el-col :span="4" v-if="openType !== enums.formType.detail">
        <el-button :size="comCfg.buttonSize" :type="comCfg.confirmBtnType" :icon="comCfg.confirmBtnIcon"
                   :loading="loading" @click="onSaveOrUpdate">保存
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import ZwxFormMu from "../../../../components/base-com/com-parts/zwx-form-mu.vue";

import comCfg from "../../../../components/base-com/com-config/com-config.js";
import depApi from "../../../../api/acl/dep.js";
import enums from "../../../../utils/enums.js";

import {
  ref, useContext, defineEmit, inject, reactive, defineComponent,
  getCurrentInstance, onMounted
} from "vue";
import de from "element-plus/packages/locale/lang/de";

defineComponent({
  inheritAttrs: false
});

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

const {expose} = useContext();
const emit = defineEmit(['page-close']);

const dialogData = inject('dialogData');
const openType = inject('openType');
const dialogClose = inject('dialogClose');

const refFormMu = ref(null);
const form = ref({});
const formRow = reactive([
  [{ span: 24, dom: 'input', type: 'text', label: '部门名称：', model: 'depName' }],
  [{ span: 24, dom: 'input', type: 'text ', label: '部门编码：', model: 'depNo', change: 'dep-no-change' }],
  [{ span: 24, dom: 'input', type: 'text ', label: '顺序：', model: 'sequence' }]
]);
const rules = ref({
  depName: [{required: true, trigger: 'blur', message: '名称必须输入'}],
  depNo: [{required: true, trigger: 'blur', message: '编码必须输入'}]
});
const loading = ref(false);



const initData = () => {
  loading.value = false;
  refFormMu.value.refForm.clearValidate();
  form.value = {};
  if (enums.formType.add !== openType.value) {
    depApi.select({id: dialogData.value.id}).then(res => {
      form.value = res.data;
    })
  }
}

onMounted(() => {
  initData();
});

const updateForm = () => {
  loading.value = true;
  depApi.update(form.value).then(res => {
    if (res.success) {
      globalProperties.$message.success(res.message);
    } else {
      globalProperties.$message.error(res.message);
    }
    loading.value = false;
    dialogClose();
  })
}

const saveForm = () => {
  loading.value = true;
  depApi.save(form.value).then(res => {
    if (res.success) {
      globalProperties.$message.success(res.message);
    } else {
      globalProperties.$message.error(res.message);
    }
    loading.value = false;
    dialogClose();
  })
}

const onSaveOrUpdate = () => {
  refFormMu.value.refForm.validate(valid => {
    if (valid) {
      if (enums.formType.add === openType.value) {
        saveForm();
      } else if (enums.formType.edit === openType.value) {
        updateForm();
      }
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
