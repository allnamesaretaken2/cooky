import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import 'font-awesome/css/font-awesome.css'

const app = createApp(App)

const router = createRouter({
    history: createWebHistory(),
    routes,
})

// Global error handler
app.config.errorHandler = (err, instance, info) => {
    console.error('Global error:', err)
    console.log('Vue instance:', instance)
    console.log('Error info:', info)

    appmount.cookyNotifications.push(err)

    // Add code for UI notifications, reporting or other error handling logic
}

app.use(router)
const appmount = app.mount('#app')
