<template>
  <section class="chat-room">
    <div ref="messagesContainer" class="messages">
      <div
        v-for="(message, index) in messages"
        :key="`${message.time}-${index}`"
        class="message"
        :class="{ user: message.isUser }"
      >
        <div v-if="!message.isUser" class="avatar">
          <AiAvatarFallback :type="aiType" />
        </div>
        <div class="bubble">
          <p>{{ message.content }}</p>
          <span
            v-if="connectionStatus === 'connecting' && index === messages.length - 1 && !message.isUser"
            class="cursor"
          >|</span>
          <time>{{ formatTime(message.time) }}</time>
        </div>
        <div v-if="message.isUser" class="avatar user-avatar">我</div>
      </div>
    </div>

    <form class="input-bar" @submit.prevent="submitMessage">
      <textarea
        v-model="inputMessage"
        :disabled="connectionStatus === 'connecting'"
        placeholder="输入消息..."
        rows="1"
        @keydown.enter.exact.prevent="submitMessage"
      />
      <button :disabled="connectionStatus === 'connecting' || !inputMessage.trim()" type="submit">
        <PaperAirplaneIcon class="send-icon" />
      </button>
    </form>
  </section>
</template>

<script setup lang="ts">
import { nextTick, ref, watch } from 'vue'
import { PaperAirplaneIcon } from '@heroicons/vue/24/outline'
import AiAvatarFallback from './AiAvatarFallback.vue'

export interface ChatRoomMessage {
  content: string
  isUser: boolean
  time: number
  type?: string
}

const props = defineProps<{
  messages: ChatRoomMessage[]
  connectionStatus: 'disconnected' | 'connecting' | 'error'
  aiType: 'love' | 'super' | 'default'
}>()

const emit = defineEmits<{
  'send-message': [message: string]
}>()

const inputMessage = ref('')
const messagesContainer = ref<HTMLElement | null>(null)

const submitMessage = () => {
  const message = inputMessage.value.trim()
  if (!message || props.connectionStatus === 'connecting') return
  emit('send-message', message)
  inputMessage.value = ''
}

const formatTime = (timestamp: number) => {
  return new Date(timestamp).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

watch(() => props.messages.length, scrollToBottom)
watch(() => props.messages.map((message) => message.content).join(''), scrollToBottom)
</script>

<style scoped>
.chat-room {
  display: flex;
  flex-direction: column;
  min-height: 0;
  height: 100%;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.94);
  border: 1px solid rgba(0, 124, 240, 0.12);
  border-radius: 16px;
  box-shadow: 0 20px 50px rgba(19, 41, 75, 0.08);
}

.messages {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  padding: 24px;
}

.message {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 18px;
  max-width: 78%;
}

.message.user {
  margin-left: auto;
  justify-content: flex-end;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  flex: 0 0 auto;
}

.user-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  background: #007cf0;
  font-weight: 700;
}

.bubble {
  position: relative;
  padding: 12px 14px 10px;
  border-radius: 14px;
  color: #1f2937;
  background: #f3f7fb;
  white-space: pre-wrap;
  word-break: break-word;
}

.user .bubble {
  color: #fff;
  background: linear-gradient(135deg, #007cf0, #00a8ff);
}

.bubble p {
  margin: 0;
  line-height: 1.65;
}

.bubble time {
  display: block;
  margin-top: 6px;
  font-size: 12px;
  opacity: 0.65;
  text-align: right;
}

.cursor {
  animation: blink 0.8s infinite;
}

.input-bar {
  display: flex;
  gap: 12px;
  padding: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  background: #fff;
}

textarea {
  flex: 1;
  min-height: 44px;
  max-height: 120px;
  resize: vertical;
  border: 1px solid rgba(0, 124, 240, 0.18);
  border-radius: 12px;
  padding: 12px 14px;
  font: inherit;
  outline: none;
}

textarea:focus {
  border-color: #007cf0;
}

button {
  width: 44px;
  height: 44px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 12px;
  color: #fff;
  background: #007cf0;
  cursor: pointer;
}

button:disabled {
  background: #b8c7d8;
  cursor: not-allowed;
}

.send-icon {
  width: 20px;
  height: 20px;
}

@keyframes blink {
  0%, 100% { opacity: 0; }
  50% { opacity: 1; }
}

@media (max-width: 768px) {
  .message {
    max-width: 92%;
  }

  .messages {
    padding: 16px;
  }
}
</style>
