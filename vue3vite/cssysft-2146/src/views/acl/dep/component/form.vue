<template>
  <div>
    <zwx-form-mu
      ref="refFormMu"
      :form="form"
      :form-row="formRow"
      :rules="rules">
    </zwx-form-mu>

    <el-affix position="bottom" :offset="20">
      <el-button type="primary">Offset bottom 20px</el-button>
    </el-affix>

    <el-row class="dialog-bottom" type="flex" justify="center">
      <el-col :span="4">
        <el-button :size="comCfg.buttonSize" :type="comCfg.elButton.close" :icon="comCfg.elButton.closeIcon"
                   @click="onPageClose">关闭
        </el-button>
      </el-col>
      <el-col :span="4" v-if="openType !== enums.formType.detail">
        <el-button :size="comCfg.buttonSize" :type="comCfg.elButton.confirm" :icon="comCfg.elButton.confirmIcon"
                   :loading="loading" @click="onSaveOrUpdate">保存
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import ZwxFormMu from "../../../../components/base-com/com-parts/zwx-form-mu.vue";

import comCfg from "../../../../components/base-com/com-config/com-config";
import enums from "../../../../utils/enums";

import useForm from "../js/useForm";
import useDialog from "../../../../components/DialogCom/js/useDialogMu";

import {
  getCurrentInstance, onMounted
} from "vue";

const gp = getCurrentInstance().appContext.config.globalProperties;

const emit = defineEmits(['after-save']);

const {dialogData, openType, dialogClose} = useDialog();
const {
  refFormMu, form, formRow, rules, loading, initData, onSaveOrUpdate, onPageClose
} = useForm(dialogData, openType, dialogClose, emit);

onMounted(() => {
  initData();
});

defineExpose({
  initData
});
</script>

<style scoped>

</style>
