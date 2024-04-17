<template>
    <div>
        <template v-if="editMode"><EditRecipe :recipeName="recipeName" @toggleEditMode="toggleEditMode()"/></template>
        <template v-else><ShowRecipe :recipeName="recipeName" @toggleEditMode="toggleEditMode()"/></template>
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
        const editModeAsstring = this.$route.query.editMode
        const editMode = editModeAsstring === 'true'

        return {
            recipeName: this.$route.query.name,
            editMode: editMode,
        }
    },
    methods: {
        toggleEditMode () {
            this.editMode = !this.editMode
        },
    },
}
</script>
