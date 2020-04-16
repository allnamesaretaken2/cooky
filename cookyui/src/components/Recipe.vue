<template>
    <div>
        <div class="row mt-2">
            <div class="col-10 col-xl-8">
                <h3>
                    <input v-if="editMode" v-model="recipe.name" class="form-control">
                    <span v-else>{{ recipe.name }}</span>
                </h3>
            </div>
            <div class="col-2 ml-auto text-right">
                <h4 v-if="editMode">
                    <button class="btn btn-secondary fa fa-undo" title="Abbrechen" @click="getRecipe()" />
                    <button class="btn btn-secondary fa fa-save" title="Rezepte speichern" @click="saveRecipe()" />
                </h4>
                <h4 v-else><i class="fa fa-edit" @click="editMode=true" /></h4>
            </div>
        </div>
        <div class="row">
            <label class="col-12 col-sm-3 col-xl-2" :class="{'col-form-label': editMode}">
                Personen:
            </label>
            <div class="col-12 col-sm-7 col-md-2">
                <input v-if="editMode" v-model="recipe.persons" class="form-control">
                <span v-else>{{ recipe.persons }}</span>
            </div>
        </div>
        <div v-if="editMode" class="row">
            <div class="col">
                Zutaten:
                <table class="table table-hover">
                    <thead style="background-color:#AFBC6C">
                        <th class="py-2">Name</th><th />
                    </thead>
                    <tbody>
                        <tr v-for="(ingredient,key) in recipe.ingredients" :key="key">
                            <input v-model="ingredient.amount" type="number">
                            <input v-model="ingredient.unit" type="text">
                            <input v-model="ingredient.ingredient.name" type="text">

                            <td class="text-right"><button type="button" class="btn btn-secondary fa fa-trash" @click="deleteIngredient(ingredient, key)" /></td>
                        </tr>
                    </tbody>
                </table>
                <div class="text-right">
                    <button class="btn btn-secondary fa fa-trash" @click="addIngredient()">Zutat hinzufügen</button>
                </div>
            </div>
        </div>
        <div v-else class="row">
            <div class="col">
                <ul>
                    <li v-for="(ingredient, key) in recipe.ingredients" :key="key">
                        <span v-if="ingredient.amount">{{ ingredient.amount }}</span>
                        <span v-if="ingredient.unit"> {{ ingredient.unit }}</span>
                        {{ ingredient.ingredient.name }}
                    </li>
                </ul>
            </div>
        </div>
        <div class="row">
            <label class="col-12 col-sm-3 col-xl-2" :class="{'col-form-label': editMode}">
                Zubereitung:
            </label>
            <div class="col-12 col-sm-10 col-xl-8">
                <textarea v-if="editMode" v-model="recipe.description" rows="7" class="form-control" />
                <span v-else style="white-space: pre-line;">{{ recipe.description }}</span>
            </div>
        </div>
        <div class="row mt-3">
            <label class="col-12 col-sm-3 col-xl-2" :class="{'col-form-label': editMode}">
                Quelle:
            </label>
            <div class="col-12 col-sm-7 col-xl-6">
                <input v-if="editMode" v-model="recipe.source" class="form-control">
                <span v-else>{{ recipe.source }}</span>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Recipe',
    data: function () {
        return {
            recipe: {},
            editMode: false,
        }
    },
    mounted: function () {
        this.getRecipe()
    },
    methods: {
        async getRecipe () {
            this.editMode = false
            const recipeID = this.$route.params.id
            try {
                const response = await fetch('/rest/recipes/' + recipeID, { method: 'GET', headers: { 'Content-Type': 'application/json' } })
                const json = await response.json()
                this.recipe = json
            } catch (error) {
                console.log('Error: ', error)
            }
        },

        async saveRecipe () {
            try {
                await fetch('/rest/recipes/' + this.recipe.id, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(this.recipe) })
            } catch (error) {
                console.log('Error: ', error)
            }
            this.editMode = false
        },

        deleteIngredient (ingredient, key) {
            this.recipe.ingredients.splice(key, 1)
        },

        addIngredient () {
            this.recipe.ingredients.push({
                amount: 1,
                unit: '',
                ingredient: {
                    name: '',
                },
            })
        },
    },
}
</script>
