<template>
<div>
    Rezept {{$route.params.id}}
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
    