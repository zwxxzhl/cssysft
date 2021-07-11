<template>
  <div class="app-container">
    <bpmn-js></bpmn-js>

    <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">
      保存
    </el-button>
  </div>
</template>

<script>
import userApi from "@/api/acl/user";
import BpmnJs from "@/components/BpmnJs/index.vue";

export default {
  components: {
    BpmnJs,
  },
  data() {
    return {
      user: {},
      saveBtnDisabled: false,
    };
  },
  watch: {
    $route(to, from) {
      this.init();
    },
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id;
        this.fetchDataById(id);
      } else {
        this.user = {};
      }
    },

    saveOrUpdate() {
      this.$refs.user.validate((valid) => {
        if (valid) {
          this.saveBtnDisabled = true; // 防止表单重复提交
          if (!this.user.id) {
            this.saveData();
          } else {
            this.updateData();
          }
        }
      });
    },

    // 新增讲师
    saveData() {
      userApi.save(this.user).then((response) => {
        // debugger
        if (response.success) {
          this.$message({
            type: "success",
            message: response.message,
          });
          this.$router.push({ path: "/dispatchweb/acl/user/list" });
        }
      });
    },

    // 根据id更新记录
    updateData() {
      // teacher数据的获取
      userApi.updateById(this.user).then((response) => {
        if (response.success) {
          this.$message({
            type: "success",
            message: response.message,
          });
          this.$router.push({ path: "/dispatchweb/acl/user/list" });
        }
      });
    },

    // 根据id查询记录
    fetchDataById(id) {
      userApi.getById(id).then((response) => {
        this.user = response.data;
      });
    },
  },
};
</script>
