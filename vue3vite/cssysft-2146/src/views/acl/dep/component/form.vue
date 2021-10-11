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

    <el-row type="flex" justify="center">
      <el-col :span="12" style="display: flex; justify-content: center">
        <zwx-cols-mu
          :col-list="colList"
          @close-click="onPageClose"
          @save-click="onSaveOrUpdate">
        </zwx-cols-mu>
      </el-col>
    </el-row>

    <!--todo 固定按钮间隔；部门多级处理-->
    <el-row class="dialog-bottom" type="flex" justify="center">
      <el-col
        :span="4"
        :style="{flex: '0 0 89px'}">
        <el-button
          :size="comCfg.elButton.size"
          :type="comCfg.elButton.close"
          :icon="comCfg.elButton.closeIcon"
          @click="onPageClose">
          关闭
        </el-button>
      </el-col>
      <el-col
        :span="4"
        :style="{flex: '0 0 89px'}"
        v-if="dialogOpenType !== enums.formType.detail">
        <el-button
          :size="comCfg.elButton.size"
          :type="comCfg.elButton.confirm"
          :icon="comCfg.elButton.confirmIcon"
          :loading="loading"
          @click="onSaveOrUpdate">
          保存
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import ZwxFormMu from "../../../../components/base-com/com-parts/zwx-form-mu.vue";
import ZwxColsMu from "../../../../components/base-com/com-parts/zwx-cols-mu.vue";

import comCfg from "../../../../components/base-com/com-config/com-config";
import enums from "../../../../utils/enums";

import useForm from "../js/useForm";

import {
  getCurrentInstance, onMounted
} from "vue";

const gp = getCurrentInstance().appContext.config.globalProperties;
const emit = defineEmits(['after-save']);

const {
  refFormMu, form, formRow, rules, colList, loading, initData, onSaveOrUpdate,
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
