<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.name" placeholder="流程名称" />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()"
        >查询</el-button
      >
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 工具条 -->
    <div>
      <el-button
        type="danger"
        size="mini"
        @click="addUser()"
        v-if="hasPerm('user.add')"
        >添加</el-button
      >
      <el-button
        type="danger"
        size="mini"
        @click="removeRows()"
        v-if="hasPerm('user.remove')"
        >批量删除</el-button
      >
    </div>

    <!-- 讲师列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />

      <el-table-column label="序号" width="70" align="center">
        <template #default="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="用户名" width="150" />

      <el-table-column prop="nickName" label="用户昵称" />

      <el-table-column prop="gmtCreate" label="创建时间" width="180" />

      <el-table-column label="操作" width="230" align="center">
        <template #default="scope">
          <router-link :to="'/acl/user/role/' + scope.row.id">
            <el-button
              type="info"
              size="mini"
              icon="el-icon-info"
              v-if="hasPerm('user.assgin')"
            ></el-button>
          </router-link>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-timer"
            @click="onAddTask()"
            v-if="hasPerm('user.add')"
          ></el-button>
          <router-link :to="'/acl/user/update/' + scope.row.id">
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
              v-if="hasPerm('user.update')"
            ></el-button>
          </router-link>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeDataById(scope.row.id)"
            v-if="hasPerm('user.remove')"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: center"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="fetchData"
      @size-change="changeSize"
    />

    <!-- 添加功能的窗口 -->
    <el-dialog
      v-model="taskVisible"
      title="添加任务"
      width="80%"
      top="1vh"
      @opened="onOpenedTask"
    >
      <div ref="refDialogDiv" class="dialog-layout">
        <bpmn-js ref="refBpmnJs"></bpmn-js>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="onExportImg">导出图片</el-button>
          <el-button @click="onExportBpmn">导出Bpmn</el-button>
          <el-button @click="onForward">前进</el-button>
          <el-button @click="onRetreat">撤销</el-button>
          <el-button @click="onCancel">取消</el-button>
          <el-button type="primary" @click="onDeploy">
            部署
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import userApi from "@/api/acl/user";
import BpmnJs from "@/components/BpmnJs/index.vue";

export default {
  components: {
    BpmnJs,
  },
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: null, // 讲师列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象
      multipleSelection: [], // 批量选择中选择的记录列表

      taskVisible: false,
    };
  },
  computed: {
    ...mapGetters({
      bpmnModeler: "app/bpmnModeler",
    }),
  },
  created() {
    this.fetchData();
  },
  mounted() {},
  methods: {
    onExportImg() {
      this.$refs.refBpmnJs.exportImg();
    },
    onExportBpmn() {
      this.$refs.refBpmnJs.exportBpmn();
    },
    onForward() {
      this.$refs.refBpmnJs.forward();
    },
    onRetreat() {
      this.$refs.refBpmnJs.retreat();
    },
    onOpenedTask() {
      this.$refs.refDialogDiv.style.height = parent.innerHeight * 0.7 + "px";
    },
    onCancel(){
      this.taskVisible = false;
    },
    onAddTask() {
      this.taskVisible = true;
    },
    onDeploy(){
      this.$refs.refBpmnJs.deploy();
    },
    changeSize(size) {
      this.limit = size;
      this.fetchData(1);
    },
    addUser() {
      this.$router.push({ path: "/acl/user/add" });
    },
    // 加载讲师列表数据
    fetchData(page = 1) {
      // 异步获取远程数据（ajax）
      this.page = page;

      userApi
        .getPageList(this.page, this.limit, this.searchObj)
        .then((response) => {
          this.list = response.data.items;
          this.total = response.data.total;

          // 数据加载并绑定成功
          this.listLoading = false;
        });
    },

    // 重置查询表单
    resetData() {
      this.searchObj = {};
      this.fetchData();
    },

    // 根据id删除数据
    removeDataById(id) {
      // debugger
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // promise
          // 点击确定，远程调用ajax
          return userApi.removeById(id);
        })
        .then((response) => {
          this.fetchData(this.page);
          if (response.success) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },
    removeRows() {
      console.log("removeRows......");

      if (this.multipleSelection.length === 0) {
        this.$message({
          type: "warning",
          message: "请选择要删除的记录!",
        });
        return;
      }

      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 遍历selection，将id取出放入id列表
          var idList = [];
          this.multipleSelection.forEach((item) => {
            idList.push(item.id);
            // console.log(idList)
          });
          // 调用api
          return userApi.removeRows(idList);
        })
        .then((response) => {
          this.fetchData(this.page);
          if (response.success) {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    // 执行搜索
    // queryString：文本框中输入的值
    // cb：一个函数
    querySearch(queryString, cb) {
      console.log(queryString);
      console.log(cb);

      // teacher.selectNameByKey(queryString).then(response => {
      //   // 调用 callback 返回建议列表的数据
      //   cb(response.data.items)
      // })
    },
  },
};
</script>

<style scoped>
.dialog-layout {
  overflow: auto;
}
</style>


