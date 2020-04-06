<template>

    <baseModal ref="baseModal">
        <template v-slot:header>
            Rezept löschen
        </template>

        <template v-slot:body>
            Rezept "{{ recipe.name }}" wirklich löschen?
        </template>

        <template v-slot:footer>
            <button type="button" class="btn btn-outline-info" @click="$refs.baseModal.close()">Nein</button>
            <button type="button" class="btn btn-primary" data-dismiss="modal" @click="deleteRecipe()">Ja</button>
        </template>
    </baseModal>

</template>

<script>

import baseModal from './Modal.vue'

export default {
    name: 'ModalDeleteRecipe',

    components: { baseModal },

    props: {
        callback: { type: Function, required: true },
    },
    data: function () {
        return {
            recipe: { name: '' },
        }
    },
    methods: {
        show (recipe) {
            this.recipe = recipe
            this.$refs.baseModal.show()
        },
        async deleteRecipe () {
            try {
                await fetch('/rest/recipes/' + this.recipe.id, { method: 'DELETE', headers: { 'Content-Type': 'application/json' } })
            } catch (error) {
                console.log('Error: ', error)
            }
            this.callback()
            this.$refs.baseModal.close()
        },
    },
}
</script>
