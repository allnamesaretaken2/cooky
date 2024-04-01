<template>
    <div>
        <template v-if="editMode"><EditRecipe :recipeID="recipeID" @toggleEditMode="toggleEditMode()"/></template>
        <template v-else><ShowRecipe :recipeID="recipeID" @toggleEditMode="toggleEditMode()"/></template>
    </div>
</template>

<script>
import EditRecipe from './EditRecipe.vue'
import ShowRecipe from './ShowRecipe.vue'

export default {
    name: 'Recipe',
    components: {
        EditRecipe,
        ShowRecipe,
    },
    data: function () {
        return {
            recipeID: this.$route.params.id,
            editMode: false,
        }
    },
    methods: {
        toggleEditMode () {
            this.editMode = !this.editMode
        },
    },
}
</script>
