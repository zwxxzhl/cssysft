<template>
  <div>
    <zwx-form-mu
      ref="refFormMu"
      :form="form"
      :form-row="formRow"
      :rules="rules">
    </zwx-form-mu>

    <el-affix position="bottom" :offset="-400">
      <el-button type="primary">Offset bottom 20px</el-button>
    </el-affix>

    <!--todo 固定按钮间隔；部门多级处理-->
    <!--<el-row class="dialog-bottom" type="flex" justify="center">
      <el-col :span="4">
        <el-button
          :size="comCfg.buttonSize"
          :type="comCfg.elButton.close"
          :icon="comCfg.elButton.closeIcon"
          @click="onPageClose">
          关闭
        </el-button>
      </el-col>
      <el-col :span="4" v-if="dialogOpenType !== enums.formType.detail">
        <el-button
          :size="comCfg.buttonSize"
          :type="comCfg.elButton.confirm"
          :icon="comCfg.elButton.confirmIcon"
          :loading="loading"
          @click="onSaveOrUpdate">
          保存
        </el-button>
      </el-col>
    </el-row>-->
  </div>
</template>

<script setup>
import ZwxFormMu from "../../../../components/base-com/com-parts/zwx-form-mu.vue";

import comCfg from "../../../../components/base-com/com-config/com-config";
import enums from "../../../../utils/enums";

import useForm from "../js/useForm";

import {
  getCurrentInstance, onMounted
} from "vue";

const gp = getCurrentInstance().appContext.config.globalProperties;
const emit = defineEmits(['after-save']);

const {
  refFormMu, form, formRow, rules, loading, initData, onSaveOrUpdate,
  dialogOpenType, onPageClose
} = useForm(emit);

onMounted(() => {

});

defineExpose({
  initData
});
</script>

<style scoped>

</style>
