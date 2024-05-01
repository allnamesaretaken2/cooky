<template>
    <div>
        <template v-if="editMode"><EditRecipe :idRecipe="idRecipe" @toggleEditMode="toggleEditMode"/></template>
        <template v-else><ShowRecipe :idRecipe="idRecipe" @toggleEditMode="toggleEditMode"/></template>
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
            idRecipe: this.$route.query.idRecipe,
            editMode: editMode,
        }
    },
    methods: {
        toggleEditMode (idRecipe) {
            if (idRecipe) {
                this.idRecipe = idRecipe
            }
            this.editMode = !this.editMode
        },
    },
}
</script>
