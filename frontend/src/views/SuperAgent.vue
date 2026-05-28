<template>
  <main class="agent-page">
    <header class="page-header">
      <button class="back-button" @click="goBack">
        <ArrowLeftIcon class="icon" />
        返回
      </button>
      <div>
        <p>云絮工具智能体</p>
        <h1>AI 超级智能体</h1>
      </div>
      <span class="status" :class="connectionStatus">{{ statusText }}</span>
    </header>

    <ChatRoom
      class="chat-panel"
      :messages="messages"
      :connection-status="connectionStatus"
      ai-type="super"
      @send-message="sendMessage"
    />
  </main>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeftIcon } from '@heroicons/vue/24/outline'
import ChatRoom, { type ChatRoomMessage } from '../components/ChatRoom.vue'
import { chatWithManus } from '../services/agentApi'

const router = useRouter()
const messages = ref<ChatRoomMessage[]>([])
const connectionStatus = ref<'disconnected' | 'connecting' | 'error'>('disconnected')
let eventSource: EventSource | null = null

const statusText = computed(() => {
  if (connectionStatus.value === 'connecting') return '执行中'
  if (connectionStatus.value === 'error') return '连接异常'
  return '就绪'
})

const addMessage = (content: string, isUser: boolean, type = '') => {
  messages.value.push({ content, isUser, type, time: Date.now() })
}

const sendMessage = (message: string) => {
  addMessage(message, true, 'user-question')
  eventSource?.close()

  const aiMessageIndex = messages.value.length
  addMessage('', false, 'agent-progress')
  connectionStatus.value = 'connecting'
  eventSource = chatWithManus(message)

  eventSource.onmessage = (event) => {
    if (event.data === '[DONE]') {
      connectionStatus.value = 'disconnected'
      eventSource?.close()
      return
    }
    messages.value[aiMessageIndex].content += `${event.data}\n`
  }

  eventSource.onerror = () => {
    connectionStatus.value = 'error'
    if (!messages.value[aiMessageIndex].content) {
      messages.value[aiMessageIndex].content = '智能体连接暂时不稳定，请稍后重试。'
    }
    eventSource?.close()
  }
}

const goBack = () => router.push('/')

onMounted(() => {
  addMessage('你好，我是云絮超级智能体。你可以让我拆解任务、调用工具、查询课程或整理资料。终端类工具默认受限，需要配置开启。', false)
})

onBeforeUnmount(() => {
  eventSource?.close()
})
</script>

<style scoped>
.agent-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 24px;
  background:
    linear-gradient(135deg, rgba(0, 124, 240, 0.14), rgba(0, 223, 216, 0.16)),
    var(--bg-color);
}

.page-header {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  gap: 16px;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
}

.page-header p,
.page-header h1 {
  margin: 0;
  text-align: center;
}

.page-header p {
  color: #667085;
}

.page-header h1 {
  margin-top: 4px;
  font-size: 32px;
  color: #172033;
}

.back-button,
.status {
  border-radius: 12px;
  padding: 10px 14px;
  background: rgba(255, 255, 255, 0.84);
}

.back-button {
  justify-self: start;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border: none;
  color: #172033;
  cursor: pointer;
}

.status {
  justify-self: end;
  color: #007cf0;
  font-size: 13px;
  font-weight: 700;
}

.status.error {
  color: #dc2626;
}

.icon {
  width: 18px;
  height: 18px;
}

.chat-panel {
  max-width: 1200px;
  width: 100%;
  min-height: 0;
  flex: 1;
  margin: 0 auto;
}

@media (max-width: 768px) {
  .agent-page {
    padding: 12px;
  }

  .page-header {
    grid-template-columns: 1fr auto;
  }

  .page-header > div {
    justify-self: end;
  }

  .page-header h1 {
    font-size: 22px;
  }

  .status {
    display: none;
  }
}
</style>
