import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'
import 'font-awesome/css/font-awesome.css'

/**
 * @param {String} url
 * @param {String} restMethod
 * @param {*} body optional
 * @param {string} contentType optional
 * @returns the response promise.
 * @throws an Error which should be covered by our global error handling
 */
window.cookyFetch = async (url, restMethod, body, contentType) => {
    if (!contentType) {
        contentType = 'application/json'
    }
    const result = await fetch(url, { method: restMethod, headers: { 'Content-Type': contentType }, body: body })
    if (!result.ok) {
        const error = await result.json()
        throw new Error(error.errorMsg)
    }
    return result
}

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
