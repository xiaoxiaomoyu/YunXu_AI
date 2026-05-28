const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export type AgentType = 'love' | 'manus'

function connectAgentSse(path: string, params: Record<string, string>) {
  const url = new URL(`${BASE_URL}${path}`)
  Object.entries(params).forEach(([key, value]) => {
    url.searchParams.set(key, value)
  })
  return new EventSource(url)
}

export function chatWithLoveApp(message: string, chatId: string) {
  return connectAgentSse('/ai/love-app/chat/sse', { message, chatId })
}

export function chatWithManus(message: string) {
  return connectAgentSse('/ai/manus/chat', { message })
}
