<template>
  <main class="agent-page love">
    <header class="page-header">
      <button class="back-button" @click="goBack">
        <ArrowLeftIcon class="icon" />
        返回
      </button>
      <div>
        <p>云絮情感陪伴</p>
        <h1>AI 恋爱大师</h1>
      </div>
      <span class="chat-id">{{ chatId }}</span>
    </header>

    <ChatRoom
      class="chat-panel"
      :messages="messages"
      :connection-status="connectionStatus"
      ai-type="love"
      @send-message="sendMessage"
    />
  </main>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeftIcon } from '@heroicons/vue/24/outline'
import ChatRoom, { type ChatRoomMessage } from '../components/ChatRoom.vue'
import { chatWithLoveApp } from '../services/agentApi'

const router = useRouter()
const messages = ref<ChatRoomMessage[]>([])
const chatId = ref('')
const connectionStatus = ref<'disconnected' | 'connecting' | 'error'>('disconnected')
let eventSource: EventSource | null = null

const generateChatId = () => `love_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`

const addMessage = (content: string, isUser: boolean) => {
  messages.value.push({ content, isUser, time: Date.now() })
}

const sendMessage = (message: string) => {
  addMessage(message, true)
  eventSource?.close()

  const aiMessageIndex = messages.value.length
  addMessage('', false)
  connectionStatus.value = 'connecting'
  eventSource = chatWithLoveApp(message, chatId.value)

  eventSource.onmessage = (event) => {
    if (event.data === '[DONE]') {
      connectionStatus.value = 'disconnected'
      eventSource?.close()
      return
    }
    messages.value[aiMessageIndex].content += event.data
  }

  eventSource.onerror = () => {
    connectionStatus.value = 'error'
    if (!messages.value[aiMessageIndex].content) {
      messages.value[aiMessageIndex].content = '连接暂时不稳定，请稍后再试。'
    }
    eventSource?.close()
  }
}

const goBack = () => router.push('/')

onMounted(() => {
  chatId.value = generateChatId()
  addMessage('你好呀，我是云絮的恋爱大师。你可以把关系里的困惑、委屈或小小心动都告诉我，我会陪你一起理清。', false)
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
    linear-gradient(135deg, rgba(255, 93, 143, 0.14), rgba(0, 223, 216, 0.12)),
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

.back-button {
  justify-self: start;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border: none;
  border-radius: 12px;
  padding: 10px 14px;
  color: #172033;
  background: rgba(255, 255, 255, 0.84);
  cursor: pointer;
}

.icon {
  width: 18px;
  height: 18px;
}

.chat-id {
  justify-self: end;
  color: #667085;
  font-size: 13px;
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

  .chat-id {
    display: none;
  }
}
</style>
