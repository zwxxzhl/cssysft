<template>
  <div>
    <zwx-form-mu
      ref="refFormMu"
      :form="form"
      :form-row="formRow"
      :rules="rules">
      <template #mid-divider>
        <el-divider content-position="center">选择人员</el-divider>
      </template>
      <template #user-select>
        <el-select v-model="form.userIds" multiple placeholder="Select">

          <el-cascader
            v-model="depCr"
            :options="depList"
            :props="{ expandTrigger: 'hover', checkStrictly: true, value: 'id', label: 'depName' }"
            @change="depCrChange">
          </el-cascader>

          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.username"
            :value="item.id">
          </el-option>
        </el-select>
      </template>
    </zwx-form-mu>
  </div>
</template>

<script setup>
import ZwxFormMu from "../../../../components/base-com/com-parts/zwx-form-mu.vue";

import useForm from "../js/useForm";

import {getCurrentInstance, onMounted, ref} from "vue";
import depApi from "../../../../api/acl/dep";
import userApi from "../../../../api/acl/user";

const gp = getCurrentInstance().appContext.config.globalProperties;
const emit = defineEmits(['after-save']);

// 获取部门树
let depList = ref([]);
const depTree = () => {
  depApi.selectTree({}).then(res => {
    depList.value = res.data.items;
  })
}
depTree();

// 获取用户
let userList = ref([]);
const getUserList = () => {
  userApi.select().then(res => {
    userList.value = res.data.items;
  })
}
getUserList();

let depCr = ref([]);
const depCrChange = (val) => {

}

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
