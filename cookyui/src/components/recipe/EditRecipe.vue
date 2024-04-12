<template>
    <div>
        <div class="form-row mt-2">
            <div class="col-10">
                <input v-model="recipe.name" class="form-control">
            </div>
            <div class="col-2 col-form-group d-flex text-right">
                <button class="btn btn-secondary fa fa-undo form-control" title="Abbrechen" @click="$emit('toggleEditMode')" />
                <button class="btn btn-secondary fa fa-save form-control" title="Rezepte speichern" @click="saveRecipe()" />
            </div>
        </div>
        <div class="form-row">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Personen
            </b>
            <div class="col-12 col-sm-3">
                <input v-model="recipe.persons" class="form-control">
            </div>
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Dauer [min]
            </b>
            <div class=" col-12 col-sm-3">
                <input v-model="recipe.durationInMinutes" class="form-control">
            </div>
        </div>
        <button class="btn btn-secondary form-control fa fa-plus my-1" @click="addPart()" v-if="!anyPartsExisting"></button>
        <div v-for="(recipePart, recipePartIndex) in recipe.recipeParts" :key="'recipePart'+recipePart.id">
            <div class="form-row mt-4" style="background-color:#AFBC6C" >
                <div class="col-12 col-sm-3 col-xl-2 col-form-label">
                    <b>Schritt</b>
                </div>
                <div class="col-12 col-sm-7 col-xl-8">
                    <input v-model="recipePart.name" class="form-control my-1">
                </div>
                <div class="col-12 col-sm-2 col-form-group d-flex text-right">
                    <button class="btn btn-secondary form-control fa fa-plus my-1" @click="addPart(recipePartIndex)"></button>
                    <button class="btn btn-secondary form-control fa fa-trash my-1" @click="deletePart(recipePartIndex)"></button>
                </div>
            </div>
            <div class="row">
                <div class="col-12 col-md-8">
                    <table class="table table-hover">
                        <tbody ref="ingredientList">
                            <tr v-for="(ingredient,ingredientIndex) in recipePart.ingredients" :key="'ingredient'+ingredientIndex" draggable="true"
                                @dragstart="startDrag(recipePart, ingredient)" @drop="finishDrag" @dragenter="changeOrder(recipePart, ingredient)" class="form-row">
                                <td class="input-group col-3">
                                    <input v-model="ingredient.amount" type="number" class="form-control w-50 w-lg-75">
                                    <input v-model="ingredient.unit" type="text" class="form-control w-lg-25">
                                </td>
                                <td class="col-7">
                                    <Combobox class="w-100" v-model="ingredient.ingredient.name" :comboValues="existingIngredients"/>
                                </td>
                                <td class="col-2 text-right">
                                    <button type="button" class="btn btn-secondary fa fa-trash form-control" @click="deleteIngredient(recipePart, ingredientIndex)" />
                                </td>
                            </tr>
                            <tr class="form-row">
                                <td class="col-3"/>
                                <td class="col-7">
                                    <textarea class="form-control" :id="'recipePartTextfield' + recipePartIndex" />
                                </td>
                                <td class="col-2 text-right">
                                    <button class="btn btn-secondary fa fa-plus form-control" @click="addIngredient(recipePart, 'recipePartTextfield' + recipePartIndex)" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="col-12 col-md-4"><textarea v-model="recipePart.description" rows="5" class="form-control mt-2" /></div>
            </div>
        </div>
        <div class="form-row">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Zubereitung
            </b>
            <div class="col-12 col-sm-9 col-xl-10">
                <textarea v-model="recipe.description" rows="10" class="form-control" />
            </div>
        </div>
        <div class="form-row mt-3 mb-3">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Quelle
            </b>
            <div class="col-12 col-sm-9 col-xl-10">
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
    computed: {
        anyPartsExisting: function () {
            return this.recipe.recipeParts && this.recipe.recipeParts.length > 0
        },
    },
    props: ['recipeID'],
    mounted () {
        this.getRecipe()
        this.getIngredients()
    },
    methods: {
        async getRecipe () {
            if (this.recipeID) {
                const response = await fetch('/rest/recipes/' + this.recipeID, { method: 'GET', headers: { 'Content-Type': 'application/json' } })
                const json = await response.json()
                this.recipe = json
            } else {
                this.recipe = {}
            }
        },

        async getIngredients () {
            const response = await fetch('/rest/ingredients/', { method: 'GET', headers: { 'Content-Type': 'application/json' } })
            const json = await response.json()
            this.existingIngredients = json.map(ing => ing.name)
        },

        async saveRecipe () {
            if (this.recipe.id) {
                await window.cookyFetch('/rest/recipes/' + this.recipe.id, 'PUT', JSON.stringify(this.recipe))
                this.$emit('toggleEditMode')
            } else {
                await window.cookyFetch('/rest/recipes/', 'POST', JSON.stringify(this.recipe))
                this.$router.push('/')
            }
        },
        addPart (recipePartIndex) {
            if (!this.recipe.recipeParts) {
                this.recipe.recipeParts = []
            }

            if (recipePartIndex === undefined) {
                recipePartIndex = 0
            }

            const newPart = {
                name: '',
                description: '',
                ingredients: [],
            }
            this.recipe.recipeParts.splice(recipePartIndex + 1, 0, newPart)
            this.addIngredient(newPart)
        },
        deletePart (recipePartIndex) {
            this.recipe.recipeParts.splice(recipePartIndex, 1)
        },
        deleteIngredient (recipePart, key) {
            recipePart.ingredients.splice(key, 1)
        },
        addIngredient (recipePart, inputFieldId) {
            let valuesAsText = ''

            if (inputFieldId) {
                valuesAsText = document.getElementById(inputFieldId).value
            }

            const ingredients = this.createIngredientsAndAddValuesIfGiven(recipePart, valuesAsText)

            recipePart.ingredients.push(...ingredients)
        },
        startDrag (recipePart, ingredient) {
            this.draggedIngredient = ingredient
            this.draggedIngredientRecipePart = recipePart
        },
        finishDrag () {
            this.draggedIngredient = null
            this.draggedIngredientRecipePart = null
        },
        changeOrder (recipePart, ingredient) {
            if (recipePart === this.draggedIngredientRecipePart) {
                // dragging within a recipe part
                const newIndex = recipePart.ingredients.indexOf(ingredient)
                const oldIndex = recipePart.ingredients.indexOf(this.draggedIngredient)

                recipePart.ingredients[newIndex] = this.draggedIngredient
                recipePart.ingredients[oldIndex] = ingredient
            } else {
                // dragging across recipe parts
                const newIndex = recipePart.ingredients.indexOf(ingredient)
                const oldIndex = this.draggedIngredientRecipePart.ingredients.indexOf(this.draggedIngredient)
                // add ingredient to new recipe part
                recipePart.ingredients.splice(newIndex, 0, ingredient)
                // remove ingredient from old recipe part
                this.draggedIngredientRecipePart.ingredients.splice(oldIndex, 1)
                // draggedIngredient recipe part has now changed
                this.draggedIngredientRecipePart = recipePart
            }
        },
        createIngredientsAndAddValuesIfGiven (recipePart, valuesAsText) {
            const textArray = valuesAsText.split('\n')
            const ingredientArray = []
            let counter = recipePart.ingredients.length

            textArray.forEach(textForIngredient => {
                const ingredient = {
                    amount: 1,
                    unit: '',
                    order: counter++,
                    ingredient: {
                        name: '',
                    },
                }
                this.squeezeValuesIntoIngredient(ingredient, textForIngredient)
                ingredientArray.push(ingredient)
            })

            return ingredientArray
        },
        squeezeValuesIntoIngredient (ingredient, valuesAsText) {
            if (valuesAsText.length !== 0) {
                const ingredientParts = valuesAsText.split(' ')

                if (ingredientParts.length === 1) {
                    ingredient.ingredient.name = ingredientParts[0]
                } else if (ingredientParts.length === 2) {
                    ingredient.amount = parseInt(ingredientParts[0])
                    ingredient.ingredient.name = ingredientParts[1]
                } else if (ingredientParts.length === 3) {
                    ingredient.amount = parseInt(ingredientParts[0])
                    ingredient.unit = ingredientParts[1]
                    ingredient.ingredient.name = ingredientParts[2]
                }
            }
        },
    },
}
</script>
<style scoped>
/* Hide annoyingly wide up and down arrows on number input fields */
/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type=number] {
  -moz-appearance: textfield;
}
</style>
