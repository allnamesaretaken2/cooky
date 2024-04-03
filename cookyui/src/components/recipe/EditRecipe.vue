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
            <input v-model="recipe.persons" class="form-control col-12 col-sm-3">
        </div>
        <div class="row">
            <b class="col-12 col-sm-3 col-xl-2 col-form-label">
                Dauer [min]:
            </b>
            <input v-model="recipe.durationInMinutes" class="form-control col-12 col-sm-3">
        </div>
        <div v-for="(recipePart, recipePartIndex) in recipe.recipeParts" :key="'recipePart'+recipePart.id">
            <div class="row mt-4">
                <div class="col-12 col-sm-3 col-xl-2 col-form-label">
                    <b>Abschnitt:</b>
                </div>
                <input v-model="recipePart.name" class="form-control col-12 col-sm-7">
                <button class="btn btn-secondary col-6 col-sm-1" @click="addPart()">Abschnitt hinzufügen</button>
                <button class="btn btn-secondary col-6 col-sm-1" @click="deletePart(recipePartIndex)">Abschnitt löschen</button>
            </div>
            <div class="row">
                <div class="col-8">
                    <table class="table table-hover">
                        <thead style="background-color:#AFBC6C">
                            <th class="py-2">Menge</th><th>Zutat</th><th />
                        </thead>
                        <tbody ref="ingredientList">
                            <tr v-for="(ingredient,ingredientIndex) in recipePart.ingredients" :key="'ingredient'+ingredientIndex" draggable="true"
                                @dragstart="startDrag(ingredient)" @drop="finishDrag" @dragenter="changeOrder(ingredient)">
                                <td>
                                    <b>{{ ingredient.order }}</b>
                                    <input v-model="ingredient.amount" type="number" class="form-control">
                                    <input v-model="ingredient.unit" type="text" class="form-control">
                                </td>
                                <td>
                                    <Combobox v-model="ingredient.ingredient.name" style="display: inline-block" :comboValues="existingIngredients"/>
                                </td>
                                <td class="text-right"><button type="button" class="btn btn-secondary fa fa-trash" @click="deleteIngredient(recipePart.id, ingredientIndex)" /></td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="row">
                        <textarea class="form-control col-8" :id="'recipePartTextfield' + recipePartIndex" />
                        <button class="btn btn-secondary col-4" @click="addIngredient(recipePart, 'recipePartTextfield' + recipePartIndex)">Zutat hinzufügen</button>
                    </div>
                </div>
                <div class="col-4"><textarea v-model="recipePart.description" rows="7" class="form-control" /></div>
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
        addPart () {
            this.recipe.recipeParts.push({
                name: '',
                description: '',
                ingredients: [],
            })
        },
        deletePart (recipePartIndex) {
            this.recipe.recipeParts.splice(recipePartIndex, 1)
        },
        deleteIngredient (recipePartID, key) {
            this.recipe.recipeParts.find(part => part.id === recipePartID).ingredients.splice(key, 1)
        },

        addIngredient (recipePart, inputFieldId) {
            const valuesAsText = document.getElementById(inputFieldId).value

            const ingredients = this.createIngredientsAndAddValuesIfGiven(recipePart, valuesAsText)

            recipePart.ingredients.push(...ingredients)
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
