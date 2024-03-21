<template>
    <div class="row">
        <div class="col-12 col-md-6">

            <h4 class="mt-2">Liste aller Rezepte</h4>

            <div>
                <input v-model="searchText" type="text" class="col-4">
                <button class="btn btn-secondary " @click="getRecipeList()">Suchen</button>
                <button type="button" class="btn btn-secondary" @click="addRecipe()">Hinzufügen</button>
            </div>

            <table class="table table-hover">
                <thead style="background-color:#AFBC6C">
                    <th class="py-2">Name</th><th />
                </thead>
                <tbody>
                    <tr v-for="(recipe,key) in recipes" :key="key">
                        <td><router-link :to="`/recipe/${recipe.id}`">{{ recipe.name }}</router-link></td>
                        <td class="text-right">
                            <button type="button" class="btn btn-secondary fa fa-arrow-circle-right mr-2" @click="setSelection(recipe, true)" />
                            <button type="button" class="btn btn-secondary fa fa-trash" @click="deleteRecipe(recipe)" />
                        </td>
                    </tr>
                </tbody>
            </table>

            <div v-show="showLoadingSpinner" class="row mt-4">
                <div class="spinner-border mx-auto" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>

            <modal-add-recipe ref="modalAddRecipe" :callback="getRecipeList" />
            <modal-delete-recipe ref="modalDeleteRecipe" :callback="getRecipeList" />
        </div>

        <div class="col-12 col-md-6">

            <h4 class="mt-2">Ausgewählte Rezepte</h4>

            <div>
                <button class="btn btn-secondary" @click="unselectAll()">Leeren</button>
            </div>

            <table class="table table-hover">
                <thead style="background-color:#AFBC6C">
                    <th class="py-2">Name</th><th />
                </thead>
                <tbody>
                    <tr v-for="(recipe,key) in selectedRecipes" :key="key" :class="{'selected':recipe.temporarySelected === true, 'unselected':recipe.temporarySelected === false}">
                        <td><router-link :to="`/recipe/${recipe.id}`">{{ recipe.name }}</router-link></td>
                        <td class="text-right"><button type="button" class="btn btn-secondary fa fa-arrow-circle-left" @click="setSelection(recipe, false)" /></td>
                    </tr>
                </tbody>
            </table>

            <div v-show="showLoadingSpinnerSelected" class="row mt-4">
                <div class="spinner-border mx-auto" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>

            <button type="button" class="btn btn-secondary" @click="addToShoppingList()">speichern und Zutaten in EK-Liste übernehmen</button>
        </div>

    </div>

</template>

<script>
import ModalAddRecipe from './ModalAddRecipe.vue'
import ModalDeleteRecipe from './ModalDeleteRecipe.vue'

export default {
    name: 'RecipeList',
    components: {
        ModalAddRecipe,
        ModalDeleteRecipe,
    },
    data: function () {
        return {
            recipes: [],
            selectedRecipes: [],
            searchText: null,
            showLoadingSpinner: false,
            showLoadingSpinnerSelected: false,
        }
    },
    mounted: function () {
        this.getRecipeList()
        this.getSelectedRecipes()
    },
    methods: {
        async getRecipeList () {
            this.recipes = []

            this.showLoadingSpinner = true

            try {
                let url = '/rest/recipes/'

                if (this.searchText) {
                    url += '?name=' + this.searchText
                }

                const response = await fetch(url, { method: 'GET', headers: { 'Content-Type': 'application/json' } })
                const json = await response.json()
                this.recipes = json
            } catch (error) {
                console.log('Error: ', error)
            }

            this.showLoadingSpinner = false
        },

        async getSelectedRecipes () {
            this.selectedRecipes = []

            this.showLoadingSpinnerSelected = true

            try {
                const response = await fetch('/rest/recipes/selected', { method: 'GET', headers: { 'Content-Type': 'application/json' } })
                const json = await response.json()
                this.selectedRecipes = json
            } catch (error) {
                console.log('Error: ', error)
            }

            this.showLoadingSpinnerSelected = false
        },

        addRecipe () {
            this.$refs.modalAddRecipe.show()
        },
        deleteRecipe (recipe) {
            this.$refs.modalDeleteRecipe.show(recipe)
        },
        setSelection (recipe, select) {
            if (recipe.temporarySelected === true && select === false) {
                const idx = this.selectedRecipes.indexOf(recipe)
                this.selectedRecipes.pop(idx)
            }

            recipe.temporarySelected = select

            if (select === false) {
                this.$forceUpdate()
            }

            if (select) {
                this.selectedRecipes.push(recipe)
            }
        },

        async saveTemporarySelection () {
            const newlySelectedRecipes = this.selectedRecipes.filter(recipe => recipe.temporarySelected === true || recipe.temporarySelected === false)

            if (newlySelectedRecipes.length === 0) {
                return
            }

            this.showLoadingSpinnerSelected = true

            const body = {}

            newlySelectedRecipes.forEach(recipe => { body[recipe.id] = recipe.temporarySelected })

            const response = await fetch('/rest/recipes/select/', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })

            if (response.ok) {
                this.getSelectedRecipes()
            } else {
                this.showLoadingSpinnerSelected = false
            }
        },

        async addToShoppingList () {
            const recipeKeys = []

            this.selectedRecipes.filter(recipe => {
                if (recipe.temporarySelected === true) {
                    recipeKeys.push(recipe.id)
                }
                return undefined
            })

            this.saveTemporarySelection()

            this.$router.push({ path: '/shoppingList' })
        },
    },
}
</script>

<style scoped>

.selected{
    background-color: lightgreen;
}

.unselected{
    background-color: pink;
}

</style>
