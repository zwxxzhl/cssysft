<template>
  <!-- <bpmn-js></bpmn-js> -->

  <div class="dashboard-container">
<!--    <div class="dashboard-text">用户名:{{ name }}</div>-->
<!--    <div class="dashboard-text">-->
<!--      用户角色:<span v-for="role in roles" :key="role">{{ role }}</span>-->
<!--    </div>-->
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import BpmnJs from "@/components/BpmnJs/index.vue";

export default {
  name: "Dashboard",
  components: {
    BpmnJs,
  },
  computed: {
    ...mapGetters({
      name: "user/name",
      roles: "user/roles",
    }),
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
