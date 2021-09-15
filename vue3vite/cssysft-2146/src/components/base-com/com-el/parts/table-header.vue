<template>
  <div>
    <template v-if="!config.headerSlot">

      <span
        v-if="config.headerAk"
        class="red-ak"
        @click="e => config.headerEvent && $emit(config.headerEvent, 'headerAk', e)">
        *
      </span>

      <span
        v-if="config.headerIconLeft"
        :class="config.headerIconLeft"
        @click="e => config.headerEvent && $emit(config.headerEvent, 'headerIconLeft', e)"
        style="padding-top: 5px;">
      </span>

      <span @click="e => config.headerEvent && $emit(config.headerEvent, config.prop, e)">
        {{ scope.column.label }}
      </span>

      <span
        v-if="config.headerIconRight"
        :class="config.headerIconRight"
        @click="e => config.headerEvent && $emit(config.headerEvent, 'headerIconRight', e)"
        style="padding-top: 5px;">
      </span>
    </template>

    <template v-else>

      <slot :name="config.headerSlotName" v-bind="scope"></slot>

    </template>
  </div>
</template>

<script setup>

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
