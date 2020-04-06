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
                <h4 v-if="editMode"><i class="fa fa-save" @click="saveRecipe()" /></h4>
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
        <div class="row">
            <div class="col">
                Zutaten:
                <ul>
                    <li v-for="(ingredient, key) in recipe.ingredients" :key="key">{{ ingredient }}</li>
                </ul>
            </div>
        </div>
        <div class="row">
            <label class="col-12 col-sm-3 col-xl-2" :class="{'col-form-label': editMode}">
                Zubereitung:
            </label>
            <div class="col-12 col-sm-10 col-xl-8">
                <textarea v-if="editMode" v-model="recipe.description" rows="7" class="form-control" />
                <span v-else style="white-space: pre;">{{ recipe.description }}</span>
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
            recipe: null,
            editMode: false,
        }
    },
    mounted: function () {
        this.getRecipe(this.$route.params.id)
    },
    methods: {
        getRecipe: async function (recipeID) {
            try {
                const response = await fetch('/rest/recipes/' + recipeID, { method: 'GET', headers: { 'Content-Type': 'application/json' } })
                const json = await response.json()
                this.recipe = json
            } catch (error) {
                console.log('Error: ', error)
            }
        },
        saveRecipe: async function () {
            try {
                await fetch('/rest/recipes/' + this.recipe.id, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(this.recipe) })
            } catch (error) {
                console.log('Error: ', error)
            }
            this.editMode = false
        },
    },
}
</script>
