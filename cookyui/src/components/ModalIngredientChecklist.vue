<template>
    <baseModal ref="baseModal">
        <template #header>
            Zutaten abhaken
        </template>

        <template #body>
            <div v-show="showLoadingSpinner" class="spinner-border mx-auto" role="status">
                <span class="sr-only">Loading...</span>
            </div>

            <div class="container overflow-y-scroll height : 100px" >
                <div v-for="(ingredient, key) in ingredients" :key="key" class="row"
                     :class="{ checkedbackground: ingredient.found}" @click="setFound(ingredient)">
                    <label>{{ingredient.amount}} {{ingredient.unit}} {{ingredient.ingredient.name}}</label>
                </div>
            </div>
        </template>

        <template #footer>
            <button type="button" class="btn btn-primary" @click="$refs.baseModal.close()">Schließen</button>
        </template>
    </baseModal>
</template>

<script>

import baseModal from './Modal.vue'

export default {
    name: 'ModalIngredientChecklist',

    components: { baseModal },

    data: function () {
        return {
            ingredients: [],
            showLoadingSpinner: true,
        }
    },
    props: ['recipe'],
    methods: {

        show () {
            this.$refs.baseModal.show()
            this.showLoadingSpinner = true
            this.load()
        },
        async load () {
            const response = await window.cookyFetch('/rest/ingredients/getByRecipe/' + this.recipe.name, 'GET')
            const json = await response.json()

            this.showLoadingSpinner = false
            this.ingredients = json
        },
        setFound (ingredient) {
            ingredient.found = !ingredient.found
        },
    },
}
</script>

<style scoped>
.checkedbackground{
    background-color: lightgreen;
}

</style>
