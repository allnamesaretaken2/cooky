<template>
    <div>
        <div class="row mt-2">
            <div class="col-8 col-xl-8">
                <h3>{{ recipe.name }}</h3>
            </div>
            <div class="col-4 ml-auto text-right">
                <button type="button" class="btn btn-secondary fa fa-check-square-o mr-1" @click="showStuff()" >Zutaten abhaken</button>
                <button type="button" class="btn btn-secondary fa fa-edit" @click="$emit('toggleEditMode')" />
            </div>
        </div>
        <div class="row">
            <div class="col-6 col-sm-3 col-xl-2">
                <b>Personen:</b>
            </div>
            <div class="col-6 col-sm-7 col-md-2">
                <span>{{ recipe.persons }}</span>
            </div>
        </div>
        <div class="row">
            <div class="col-6 col-sm-3 col-xl-2">
                <b>Dauer:</b>
            </div>
            <div class="col-6 col-sm-7 col-md-2">
                <span>{{ recipe.durationInMinutes }} Minuten</span>
            </div>
        </div>
        <ModalIngredientChecklist ref="ModalIngredientChecklist" :recipe="recipe" />
        <div v-for="(recipePart, key) in recipe.recipeParts" :key="key">
            <div class="row mt-3"><div class="col"><b>{{recipePart.name}}</b></div></div>
            <div class="row">
                <div class="col-5 col-sm-3 col-xl-4 offset-sm-1">
                    <ul class="no-bullets-for-list">
                        <li v-for="(ingredient, key) in recipePart.ingredients" :key="key" >
                            <span v-if="ingredient.amount" style="margin-right: 5px;">{{ ingredient.amount }}</span>
                            <span v-if="ingredient.unit" style="margin-right: 5px;">{{ ingredient.unit }}</span>
                            <span>{{ ingredient.ingredient.name }}</span>
                        </li>
                    </ul>
                </div>
                <div class="col-6 col-sm-8 col-xl-7" style="white-space: pre-line;">{{recipePart.description}}</div>
            </div>
        </div>
        <div class="row">
            <div class="col mt-3">
                <span style="white-space: pre-line;">{{ recipe.description }}</span>
            </div>
        </div>
        <div class="row mt-3">
            <b class="col-12 col-sm-3 col-xl-2">
                Quelle:
            </b>
            <div class="col-12 col-sm-7 col-xl-6">
                <span>{{ recipe.source }}</span>
            </div>
        </div>
    </div>
</template>

<script>
import ModalIngredientChecklist from '../ModalIngredientChecklist.vue'

export default {
    name: 'ShowRecipe',
    components: {
        ModalIngredientChecklist,
    },
    data: function () {
        return {
            recipe: {},
        }
    },
    props: ['idRecipe'],
    mounted () {
        this.getRecipe()
    },
    methods: {
        async getRecipe () {
            const response = await window.cookyFetch('/rest/recipes/' + this.idRecipe, 'GET')
            const json = await response.json()
            this.recipe = json
        },
        showStuff () {
            this.$refs.ModalIngredientChecklist.show()
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
