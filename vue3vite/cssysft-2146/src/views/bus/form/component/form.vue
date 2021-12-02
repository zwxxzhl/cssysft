<template>
  <div>
    <zwx-form-mu
      ref="refFormMu"
      :form="form"
      :form-row="formRow"
      :rules="rules">
      <template #mid-divider>
        <el-divider content-position="center">选择接收者</el-divider>
      </template>
    </zwx-form-mu>
  </div>
</template>

<script setup>
import ZwxFormMu from "../../../../components/base-com/com-parts/zwx-form-mu.vue";

import useForm from "../js/useForm";

import {getCurrentInstance, onMounted} from "vue";

const gp = getCurrentInstance().appContext.config.globalProperties;
const emit = defineEmits(['after-save']);

const {
  refFormMu, form, formRow, rules, colList, loading, dialogOpenType,
  initData, onSaveOrUpdate, onAfterFormSave, onPageClose
} = useForm(emit);

onMounted(() => {

});

defineExpose({
  initData, colList, onSaveOrUpdate, onAfterFormSave, onPageClose
});
</script>

<style scoped>

</style>
