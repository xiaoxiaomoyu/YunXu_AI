import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('../views/Home.vue'),
    },
    {
      path: '/ai-chat',
      name: 'AIChat',
      component: () => import('../views/AIChat.vue'),
    },
    {
      path: '/comfort-simulator',
      name: 'ComfortSimulator',
      component: () => import('../views/ComfortSimulator.vue'),
    },
    {
      path: '/customer-service',
      name: 'CustomerService',
      component: () => import('../views/CustomerService.vue'),
    },
    {
      path: '/game',
      name: 'Game',
      component: () => import('../views/GameChat.vue'),
    },
  ],
})

export default router
