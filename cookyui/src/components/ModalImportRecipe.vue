<template>
    <baseModal ref="baseModal">
        <template #header>
            Rezept Importieren
        </template>

        <template #body>
            <div class="form-row">
                <label class="col-3">Chefkoch-Import</label>
                <input v-model="chefkochImport.url" class="col-9 form-control">
            </div>
        </template>

        <template #footer>
            <button type="button" class="btn btn-primary" data-dismiss="modal" @click="chefkoch()">Importieren</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal" @click="close()">Abbrechen</button>
        </template>
    </baseModal>
</template>

<script>
import baseModal from './Modal.vue'

export default {
    name: 'ModalImportRecipe',

    components: { baseModal },

    props: {
        callback: { type: Function, required: true },
    },
    data: function () {
        return {
            chefkochImport: { url: null },
        }
    },
    methods: {
        show () {
            this.$refs.baseModal.show()
        },
        close () {
            this.$refs.baseModal.close()
        },
        async chefkoch () {
            const response = await window.cookyFetch('/rest/recipes/importFromChefkoch', 'POST', JSON.stringify(this.chefkochImport))
            const json = await response.json()
            this.callback(json)
            this.$refs.baseModal.close()
        },
    },
}
</script>
