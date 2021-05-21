<template>
  <div>
    <h1>{{ msg }}</h1>

    <p>
      <a href="https://vitejs.dev/guide/features.html" target="_blank">Vite Documentation</a> |
      <a href="https://v3.vuejs.org/" target="_blank">Vue 3 Documentation</a>
    </p>

    <button @click="state.count++">count is: {{ state.count }}</button>
    <p>
      Edit
      <code>components/HelloWorld.vue</code> to test hot module replacement.
    </p>
  </div>
</template>

<script setup>
import { defineProps, reactive, useContext } from 'vue';
const { expose } = useContext();

defineProps({
  msg: String
})

const state = reactive({ count: 0 })

const ceshi = () => {
  console.log("进入子组件方法")
}

expose({
  ceshi
});

</script>

<style scoped>
a {
  color: #42b983;
}
</style>