<template>
    <div id="app" class="container">
        <h1 class="text-right mt-3" style="color:#005B4C">cooky</h1>
        <div class="row border-top border-bottom py-2" style="border-color:#005B4C !important;">
            <div class="col">
                <div class="nav py-2" style="background-color:#005B4C; color:#FBFAF0;">
                    <router-link to="/" class="mx-1 px-1 nav-item-style">Rezepte</router-link>
                    <router-link to="/shoppinglist" class="mx-1 px-1 nav-item-style" >Einkaufsliste</router-link>
                    <a class="mx-1 px-1 nav-item-style" @click="openImportModal()">Import</a>
                    <a class="mx-1 px-1 nav-item-style" @click="addRecipe()">Neu</a>
                </div>
            </div>
        </div> <!-- header -->
        <router-view />
        <div class="cookyAlertContainer">
            <div v-for="(notification,key) in cookyNotifications" :key="key" :class="'alert alert-dismissible ' + notification.class" >
                {{ notification.msg }}
                <button type="button" class="close" @click="removeNotification(notification, key)">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </div>
        <modal-import-recipe ref="modalImportRecipe" :callback="openRecipeInEditMode" />
    </div>
</template>

<script>
import ModalImportRecipe from './components/ModalImportRecipe.vue'

export default {
    name: 'App',
    components: {
        ModalImportRecipe,
    },
    data () {
        return {
            cookyNotifications: [],
        }
    },
    methods: {
        addError (msg) {
            this.cookyNotifications.push({
                msg: msg,
                class: 'alert-danger',
            })
        },
        addOkay (msgText) {
            const msg = {
                msg: msgText,
                class: 'alert-success cookyFadeoutAlert',
            }

            this.cookyNotifications.push(msg)
            var me = this
            setTimeout(function () {
                const idx = me.cookyNotifications.indexOf(msg)
                if (idx !== -1) {
                    me.cookyNotifications.splice(idx, 1)
                }
            }, 3000)
        },
        removeNotification (notification, key) {
            this.cookyNotifications.pop(notification)
        },
        openImportModal () {
            this.$refs.modalImportRecipe.show()
        },
        openRecipeInEditMode (recipe) {
            this.$router.push('/recipe?idRecipe=' + recipe.id + '&editMode=true')
        },
        addRecipe () {
            this.$router.push('/recipe/?editMode=true')
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

.cookyFadeoutAlert {
    animation: alertFadeOut 3s;
}

@keyframes alertFadeOut {
  0% { opacity: 1; }
  50% { opacity: 1; }
  100% { opacity: 0; }
}

a.nav-item-style:hover {
    color: rgb(175, 188, 108);
}
.nav-item-style {
    color: #FBFAF0;
    font-size: 20px;
    /* border: solid; */
    /* border-width: 1px; */
    background-color: #aaaaaa44;
}
</style>
