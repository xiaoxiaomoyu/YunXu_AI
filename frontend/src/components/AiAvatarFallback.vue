<template>
  <div class="ai-avatar-fallback" :class="type">
    <HeartIcon v-if="type === 'love'" class="icon" />
    <CpuChipIcon v-else class="icon" />
  </div>
</template>

<script setup lang="ts">
import { CpuChipIcon, HeartIcon } from '@heroicons/vue/24/outline'

defineProps<{
  type?: 'love' | 'super' | 'default'
}>()
</script>

<style scoped>
.ai-avatar-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  background: linear-gradient(135deg, #007cf0, #00dfd8);
}

.ai-avatar-fallback.love {
  background: linear-gradient(135deg, #ff5d8f, #ff9f7f);
}

.icon {
  width: 20px;
  height: 20px;
}
</style>
