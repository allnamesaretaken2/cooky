<template>
    <div>
        <div class="row mt-2">
            <div class="col-10 col-xl-8">
                <h3>
                    <input v-model="recipe.name" class="form-control">
                </h3>
            </div>
            <div class="col-2 ml-auto text-right">
                <h4>
                    <button class="btn btn-secondary fa fa-undo" title="Abbrechen" @click="$emit('toggleEditMode')" />
                    <button class="btn btn-secondary fa fa-save" title="Rezepte speichern" @click="saveRecipe()" />
                </h4>
            </div>
        </div>
        <div class="row">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Personen:
            </b>
            <div class="col-12 col-sm-7 col-md-2">
                <input v-model="recipe.persons" class="form-control">
            </div>
        </div>
        <div class="row">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Dauer:
            </b>
            <div class="col-12 col-sm-7 col-md-2">
                <input v-model="recipe.durationInMinutes" class="form-control">
            </div>
            <span class="col-12 col-sm-3 col-xl-2 col-form-label">Minuten</span>
        </div>
        <div class="row">
            <b class="col-12 col-sm-3 col-xl-2" >
                Zutaten:
            </b>
            <div class="col">
                <table class="table table-hover">
                    <thead style="background-color:#AFBC6C">
                        <th class="py-2">Name</th><th />
                    </thead>
                    <tbody ref="ingredientList">
                        <tr v-for="(ingredient,key) in recipe.ingredients" :key="key" draggable="true"
                            @dragstart="startDrag(ingredient)" @drop="finishDrag" @dragenter="changeOrder(ingredient)">
                            <b>{{ ingredient.order }}</b>
                            <input v-model="ingredient.amount" type="number">
                            <input v-model="ingredient.unit" type="text">
                            <Combobox v-model="ingredient.ingredient.name" style="display: inline-block" :comboValues="existingIngredients"/>

                            <td class="text-right"><button type="button" class="btn btn-secondary fa fa-trash" @click="deleteIngredient(ingredient, key)" /></td>
                        </tr>
                    </tbody>
                </table>
                <div class="text-right">
                    <button class="btn btn-secondary fa fa-trash" @click="addIngredient()">Zutat hinzufügen</button>
                </div>
            </div>
        </div>
        <div class="row">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Zubereitung:
            </b>
            <div class="col-12 col-sm-10 col-xl-8">
                <textarea v-model="recipe.description" rows="7" class="form-control" />
            </div>
        </div>
        <div class="row mt-3">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Quelle:
            </b>
            <div class="col-12 col-sm-7 col-xl-6">
                <input v-model="recipe.source" class="form-control">
            </div>
        </div>
    </div>
</template>

<script>

import Combobox from '../Combo.vue'

export default {
    name: 'EditRecipe',
    components: {
        Combobox,
    },
    data: function () {
        return {
            recipe: {},
            existingIngredients: [],
            draggedIngredient: null,
        }
    },
    props: ['recipeID'],
    mounted () {
        this.getRecipe()
        this.getIngredients()
    },
    methods: {
        async getRecipe () {
            const response = await fetch('/rest/recipes/' + this.recipeID, { method: 'GET', headers: { 'Content-Type': 'application/json' } })
            const json = await response.json()
            this.recipe = json
        },

        async getIngredients () {
            const response = await fetch('/rest/ingredients/', { method: 'GET', headers: { 'Content-Type': 'application/json' } })
            const json = await response.json()
            this.existingIngredients = json.map(ing => ing.name)
        },

        async saveRecipe () {
            await fetch('/rest/recipes/' + this.recipe.id, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(this.recipe) })
            this.$emit('toggleEditMode')
        },

        deleteIngredient (ingredient, key) {
            this.recipe.ingredients.splice(key, 1)
        },

        addIngredient () {
            this.recipe.ingredients.push({
                amount: 1,
                unit: '',
                order: this.recipe.ingredients.length,
                ingredient: {
                    name: '',
                },
            })
        },
        startDrag (ingredient) {
            this.draggedIngredient = ingredient
        },
        finishDrag () {
            this.draggedIngredient = null
        },
        changeOrder (ingredient) {
            const ings = this.recipe.ingredients

            const newIndex = ings.indexOf(ingredient)
            const oldIndex = ings.indexOf(this.draggedIngredient)
            ings[newIndex] = this.draggedIngredient
            ings[oldIndex] = ingredient

            var order = this.draggedIngredient.order
            this.draggedIngredient.order = ingredient.order
            ingredient.order = order
        },
    },
}
</script>

<style>
.no-bullets-for-list {
  list-style-type: none; /* Remove bullets */
  padding: 0; /* Remove padding */
  margin: 0; /* Remove margins */
}
</style>
