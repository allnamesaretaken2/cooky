<template>

    <baseModal ref="baseModal">
        <template #header>
            Rezept hinzufügen
        </template>

        <template #body>
            <div class="form-row">
                <label class="col-3">Chefkoch-Import</label>
                <input v-model="chefkochImport.url" class="col-6 form-control">
                <button type="button" class="col-3 btn btn-outline-primary" @click="chefkoch()">Importieren</button>
            </div>
            <div class="form-row">
                <label class="col-3">Name</label>
                <input v-model="newRecipe.name" class="col-9 form-control">
            </div>
            <div class="form-row">
                <label class="col-3">Portionen</label>
                <input v-model="newRecipe.persons" class="col-9 form-control">
            </div>
            <div class="form-row">
                <label class="col-3">Zutaten</label>
                <textarea v-model="ingredientsAsString" class="col-9 form-control" rows="8" />
            </div>
            <div class="form-row">
                <label class="col-3">Beschreibung</label>
                <textarea v-model="newRecipe.description" class="col-9 form-control" rows="8" />
            </div>
            <div class="form-row">
                <label class="col-3">Quelle</label>
                <input v-model="newRecipe.source" class="col-9 form-control">
            </div>
        </template>

        <template #footer>
            <button type="button" class="btn btn-outline-info" @click="$refs.baseModal.close()">Schließen</button>
            <button type="button" class="btn btn-primary" data-dismiss="modal" @click="submitAndClose()">Abschicken</button>
        </template>
    </baseModal>
</template>

<script>

import baseModal from './Modal.vue'

export default {
    name: 'ModalAddRecipe',

    components: { baseModal },

    props: {
        callback: { type: Function, required: true },
    },
    data: function () {
        return {
            newRecipe: {},
            ingredientsAsString: '',
            chefkochImport: { url: null },
        }
    },
    methods: {
        show () {
            this.newRecipe = {}
            this.chefkochImport = { url: null }
            this.ingredientsAsString = ''
            this.$refs.baseModal.show()
        },

        async submitAndClose () {
            try {
                await fetch('/rest/recipes/', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(this.newRecipe) })
                const recipeName = this.newRecipe.name
                await fetch('/rest/ingredients/insertFromString/' + recipeName,
                    { method: 'POST', headers: { 'Content-Type': 'application/text' }, body: this.ingredientsAsString.replaceAll('\n', ';') })
            } catch (error) {
                console.log('Error: ', error)
            }
            this.callback()
            this.$refs.baseModal.close()
        },

        async chefkoch () {
            try {
                const response = await fetch('/rest/recipes/importFromChefkoch', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(this.chefkochImport) })
                const json = await response.json()
                this.newRecipe = json
                this.chefkochImport.url = null
            } catch (error) {
                console.log('Error: ', error)
            }
        },
    },
}
</script>
