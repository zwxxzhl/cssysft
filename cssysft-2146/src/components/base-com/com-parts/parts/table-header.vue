<template>

  <template v-if="!config.hSlot">

    <span
      v-if="config.hAk"
      class="red-ak"
      @click="$emit('header-click', $event, 'hAk')"
    >
      *
    </span>

    <span
      v-if="config.hIconL"
      :class="config.hIconLC"
      @click="$emit('header-click', $event, 'hIconL')"
      style="padding-top: 5px;"
    >
    </span>

    <span
      @click="$emit('header-click', $event, scope.column.label)"
    >
      {{ scope.column.label }}
    </span>

    <span
      v-if="config.hIconR"
      :class="config.hIconRC"
      @click="$emit('header-click', $event, 'hIconR')"
      style="padding-top: 5px;"
    >
    </span>
  </template>

  <template v-else>

    <slot :name="config.hSlotName" v-bind="this"></slot>

  </template>

</template>

<script setup>
import {
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

</script>

<style scoped>

.red-ak {
  color: #F56C6C;
  font-size: 12px;
  margin-right: 4px;
}

</style>