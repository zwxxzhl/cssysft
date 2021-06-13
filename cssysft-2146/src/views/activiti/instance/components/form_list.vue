<template>
  <el-table
    ref="refElTable"
    style="width: 100%"
    stripe
    v-loading="listLoading"
    :data="list"
    row-key="id"
    @row-click="onRowClick"
    :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
  >
    <el-table-column prop="userId" label="用户名" :formatter="userIdFormatter"/>
    <el-table-column prop="title" label="标题"/>
    <el-table-column prop="content" label="内容"/>
    <el-table-column prop="gmtCreate" label="时间"/>
  </el-table>

  <el-row class="dialog-bottom" type="flex" justify="center">
    <el-col :span="4">
      <el-button size="medium" type="info" icon="el-icon-close"
                 @click="onPageClose">关闭
      </el-button>
    </el-col>
  </el-row>
</template>

<script setup>
import enums from "../../../../utils/enums";
import busTaskFormApi from "../../../../api/acl/busTaskForm";
import userApi from "../../../../api/acl/user";

import {ref, onMounted, reactive, getCurrentInstance, inject, defineEmit, useContext} from "vue";

const {expose} = useContext();
const globalProperties = getCurrentInstance().appContext.config.globalProperties;

let emit = defineEmit(['close']);

const dialogData = inject('dialogData');
const openType = inject('openType');
const dialogClose = inject('dialogClose');

const listLoading = ref(true);
const list = ref([]);

const comObj = ref({});

const refElTable = ref(null);

const fetchData = () => {
  busTaskFormApi
    .selectTree(dialogData.value.id)
    .then((res) => {
      list.value = res.data.items;
      listLoading.value = false;
    });
}

const getUserList = () => {
  userApi
    .select()
    .then((res) => {
      comObj.value.userList = res.data.items;
    })
}

const onRowClick = (row) => {
  if (row.children) {
    refElTable.value.toggleRowExpansion(row);
  }
}

const userIdFormatter = (row, column) => {
  let data = row[column.property];
  let ret = data;
  if (comObj.value.userList) {
    let obj = comObj.value.userList.filter(f => f.id === data);
    obj.length > 0 && (ret = obj[0].username);
  }
  return ret;
}

const onPageClose = () => {
  dialogClose();
}

const initData = () => {
  if (enums.formType.detail === openType.value) {
    fetchData();
    getUserList();
  }
}

onMounted(() => {
  initData();
})

expose({
  initData
});

</script>

<style scoped>

</style>
