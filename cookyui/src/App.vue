<template>
    <div id="app">
        <div class="container">
            <h1 class="text-right mt-3" style="color:#005B4C">cooky</h1>
            <div class="row border-top border-bottom py-2" style="border-color:#005B4C !important;">
                <div class="col">
                    <div class="row py-2" style="background-color:#005B4C; color:#FBFAF0;">
                        <div class="col-1"><router-link to="/" class="badge badge-secondary">Alle Rezepte</router-link></div>
                        <div class="col-1"><router-link to="/shoppinglist" class="badge badge-secondary">Einkaufsliste</router-link></div>
                    </div>
                </div>
            </div> <!-- header -->
            <router-view />
            <div class="cookyAlertContainer">
                <div v-for="(notification,key) in cookyNotifications" :key="key"
                     class="alert alert-danger alert-dismissible">
                    {{ notification }}
                    <button type="button" class="close" @click="removeNotification(notification, key)">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'App',
    data () {
        return {
            cookyNotifications: [],
        }
    },
    methods: {
        removeNotification (notification, key) {
            this.cookyNotifications.pop(notification)
        },
    },
}
</script>

<style lang="scss">
@import './styles/custom-bootstrap.scss';
@import '../node_modules/bootstrap/scss/bootstrap.scss';
body {background-color:#FBFAF0}

.cookyAlertContainer {
    position: fixed;
    margin : auto;
    top: 0;
}
</style>
