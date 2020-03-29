<template>
<div>
    <h3 class="mt-2">{{recipe.name}}</h3>
    <p>Personen: {{recipe.persons}}<br/>Dauer: <br/><span v-if="recipe.source">Quelle: {{recipe.source}}</span></p>
    <ul>
        <li v-for="(ingredient, key) in recipe.ingredients" :key="key">{{ingredient}}</li>
    </ul>
    <span style="white-space: pre;">{{recipe.description}}</span>
</div>
</template>

<script>
export default {
    name: 'recipe',
    data: function () {
        return {
            recipe: null,
        }
    },
    methods: {
        getRecipe: async function(recipeID) {
            try {
                const response = await fetch('/rest/recipes/'+recipeID, {method: 'GET', headers:{'Content-Type': 'application/json'}})
                const json = await response.json()
                this.recipe = json
            } catch (error) {
                console.log("Error: ", error)
            }
        },
    },
    mounted: function() {
        this.getRecipe(this.$route.params.id)
  }
}
</script>
    