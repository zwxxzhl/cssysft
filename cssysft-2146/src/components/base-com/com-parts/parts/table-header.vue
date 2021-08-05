<template>
  <div>

    <!--  @click="e => $emit('header-click-cus', e, 'hAk')"-->
    <template v-if="!config.hSlot">

      <span
        v-if="config.hAk"
        class="red-ak"
        @click="onclickCeshi"
      >
        *
      </span>

      <span
        v-if="config.hIconL"
        :class="config.hIconLC"
        @click="e => $emit('header-click-cus', e, 'hIconL')"
        style="padding-top: 5px;"
      >
      </span>

      <span
        @click="e => $emit('header-click-cus', e, scope.column.label)"
      >
      {{ scope.column.label }}
      </span>

      <span
        v-if="config.hIconR"
        :class="config.hIconRC"
        @click="e => $emit('header-click-cus', e, 'hIconR')"
        style="padding-top: 5px;"
      >
      </span>
    </template>

    <template v-else>

      <slot :name="config.hSlotName" v-bind="this"></slot>

    </template>
  </div>

</template>

<script setup>
import {
  defineEmit,
  defineProps
} from "vue";

const props = defineProps({
  config: {
    type: Object,
    required: true
  },
  scope: {
    type: Object,
    required: true,
  }
});

const emits = defineEmit(['header-click-cus', 'click-ceshi']);

const onclickCeshi = (val) => {
  console.log(val);
  debugger
  emits('click-ceshi', 'ceshi');
}

</script>

<style scoped>

.red-ak {
  color: #F56C6C;
  font-size: 12px;
  margin-right: 4px;
}

</style>
