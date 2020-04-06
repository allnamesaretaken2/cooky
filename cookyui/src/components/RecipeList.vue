<template>
    <div>
        <div class="row mt-4">
            <h4 class="mt-4 col-10">Liste aller Rezepte</h4>
            <div class="col-2"><button type="button" class="btn btn-primary" @click="addRecipe()">Hinzufügen</button></div>
        </div> <!-- row -->
        <input v-model="searchText" type="text" class="w-30">
        <button class="btn btn-primary" @click="getRecipeList()">Suchen</button>
        <table class="table table-hover">
            <thead style="background-color:#AFBC6C">
                <th class="py-2">Name</th><th />
            </thead>
            <tbody>
                <tr v-for="(recipe,key) in recipes" :key="key">
                    <td><router-link :to="`/recipe/${recipe.id}`">{{ recipe.name }}</router-link></td>
                    <td class="text-right"><i class="fa fa-trash" @click="deleteRecipe(recipe)" /></td>
                </tr>
            </tbody>
        </table>
        <modal-add-recipe ref="modalAddRecipe" :callback="getRecipeList" />
        <modal-delete-recipe ref="modalDeleteRecipe" :callback="getRecipeList" />
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
            searchText: null,
        }
    },
    mounted: function () {
        this.getRecipeList()
    },
    methods: {
        async getRecipeList () {
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
        },
        addRecipe () {
            this.$refs.modalAddRecipe.show()
        },
        deleteRecipe (recipe) {
            this.$refs.modalDeleteRecipe.show(recipe)
        },
    },
}
</script>
