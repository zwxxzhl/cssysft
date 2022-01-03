<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.username" placeholder="用户名" />
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 工具条 -->
    <div>
      <el-button type="danger" size="small" @click="addUser()" v-if="hasPerm('user.add')">添加</el-button>
      <el-button type="danger" size="small" @click="removeRows()" v-if="hasPerm('user.remove')">批量删除</el-button>
    </div>

    <!-- 讲师列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange">

      <el-table-column type="selection" width="55" />

      <el-table-column label="序号" width="70" align="center">
        <template #default="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="depId" label="部门" :formatter="depIdFormatter" />

      <el-table-column prop="username" label="账号" />

      <el-table-column prop="nickName" label="昵称" />

      <el-table-column prop="userNo" label="工号" />

      <el-table-column prop="gmtCreate" label="创建时间" />

      <el-table-column label="操作" width="230" align="center">
        <template #default="scope">
          <router-link :to="'/dispatchweb/acl/user/role/' + scope.row.id">
            <el-button type="info" size="small" icon="el-icon-info" v-if="hasPerm('user.assgin')"/>
          </router-link>
          <router-link :to="'/dispatchweb/acl/user/update/' + scope.row.id">
            <el-button type="primary" size="small" icon="el-icon-edit" v-if="hasPerm('user.update')"/>
          </router-link>
          <el-button type="warning" size="small" icon="el-icon-edit-outline" @click="onEditPassword(scope.row)" v-if="hasPerm('user.update')"/>
          <el-button type="danger" size="small" icon="el-icon-delete" @click="removeDataById(scope.row.id)" v-if="hasPerm('user.remove')"/>
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
      @size-change="changeSize"/>

    <!-- 修改密码 -->
    <el-dialog v-model="dialogFormVisible" title="修改密码">
      <el-form :model="form">
        <el-form-item prop="password" label="旧密码" :label-width="formLabelWidth">
          <el-input v-model="form.password" @change="onPasswordChange" type="password"/>
        </el-form-item>
        <el-divider></el-divider>
        <el-form-item prop="pwdOne" label="新密码" :label-width="formLabelWidth">
          <el-input v-model="form.pwdOne" @change="onPwdChange" type="password"/>
        </el-form-item>
        <el-form-item prop="pwdTwo" label="确认密码" :label-width="formLabelWidth">
          <el-input v-model="form.pwdTwo" @change="onPwdChange" type="password"/>
          <el-alert :title="alertTitle" :type="alertType" :closable="false" show-icon style="margin-top: 10px"> </el-alert>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="onSavePassword">修改</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import userApi from "../../../api/acl/user";
import depApi from "../../../api/acl/dep";

export default {
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: null, // 讲师列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象
      multipleSelection: [], // 批量选择中选择的记录列表
      depList: [],

      form: {},
      formLabelWidth: '100px',
      dialogFormVisible: false,
      alertType: 'info',
      alertTitle: '请输入旧密码'
    };
  },
  created() {
    this.fetchData();
    this.getDepList();
  },
  mounted() {},
  methods: {
    onEditPassword(row) {
      this.dialogFormVisible = true;
      this.form = {
        id: '',
        password: '',
        pwdOne: '',
        pwdTwo: ''
      };
      this.form.id = row.id;
      this.alertType = 'info';
      this.alertTitle = '请输入旧密码';
    },
    onPasswordChange(val) {
      if (val) {
        userApi.updatePwdVlid(this.form).then(res => {
          if (res.success) {
            this.onPwdChange();
          } else {
            this.alertType = 'error';
            this.alertTitle = res.message;
          }
        })
      }
    },
    onPwdChange() {
      if ('' === this.form.password.trim()) {
        this.alertType = 'info';
        this.alertTitle = '请输入旧密码';
      } else if ('' === this.form.pwdOne.trim() && '' === this.form.pwdTwo.trim()) {
        this.alertType = 'warning';
        this.alertTitle = '请输入新密码';
      } else if ('' !== this.form.pwdOne.trim() && '' === this.form.pwdTwo.trim()) {
        this.alertType = 'warning';
        this.alertTitle = '再次输入新密码';
      } else if (this.form.pwdOne.trim() !== this.form.pwdTwo.trim()) {
        this.alertType = 'error';
        this.alertTitle = '新密码输入不匹配';
      } else {
        this.alertType = 'success';
        this.alertTitle = '新密码输入匹配成功';
      }
    },
    onSavePassword() {
      if (this.alertType === 'success') {
        userApi.updatePwd(this.form).then(res => {
          if (res.success) {
            this.$message({
              type: "success",
              message: "修改密码成功!",
            });
            this.dialogFormVisible = false;
            this.fetchData();
          } else {
            this.alertType = 'error';
            this.alertTitle = res.message;
          }
        })
      }
    },
    changeSize(size) {
      this.limit = size;
      this.fetchData(1);
    },
    addUser() {
      this.$router.push({ path: "/dispatchweb/acl/user/add" });
    },
    getDepList() {
      depApi.select({}).then(res => {
        this.depList = res.data.items;
      })
    },
    depIdFormatter(row, column) {
      let val = row[column.property];
      if (this.depList.length > 0) {
        let obj = this.depList.find(f => f.id === val);
        if (obj) return obj.depName;
      } else {
        return '';
      }
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


