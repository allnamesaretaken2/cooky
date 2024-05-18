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
        <div class="cookyAlertContainer" id="alertContainer" />
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
            count: 0,
        }
    },
    methods: {
        addError (msg) {
            this.addAlertToDiv('alert-danger', msg)
        },
        addOkay (msgText) {
            const alert = this.addAlertToDiv('alert-success', msgText)

            setTimeout(function () {
                const alertContainer = document.getElementById('alertContainer')
                alertContainer.removeChild(alert)
            }, 3000)
        },
        addAlertToDiv (styleClass, text) {
            const alertContainer = document.getElementById('alertContainer')
            // Create the div element
            const alert = document.createElement('div')
            alert.classList.add('alert', styleClass, 'alert-dismissible')
            if (styleClass === 'alert-success') {
                alert.classList.add('cookyFadeoutAlert')
            }

            // Add the text
            alert.textContent = text

            // Create the close button
            const closeButton = document.createElement('button')
            closeButton.setAttribute('type', 'button')
            closeButton.classList.add('close')
            closeButton.onclick = () => {
                alert.remove()
            }

            // Create the span element for the "x" icon
            const spanIcon = document.createElement('span')
            spanIcon.setAttribute('aria-hidden', 'true')
            spanIcon.innerHTML = '&times;'

            // Append the span icon to the close button
            closeButton.appendChild(spanIcon)

            // Append the close button to the alert div
            alert.appendChild(closeButton)

            // Append the alert div to the parent div
            if (alertContainer.children.length > 0) {
                alertContainer.insertBefore(alert, alertContainer.children[0])
            } else {
                alertContainer.appendChild(alert)
            }

            return alert
        },
        removeNotification (notification, key) {
            // todo currently broken
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
body {background-color:var(--background)}

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
