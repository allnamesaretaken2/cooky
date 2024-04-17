<template>
    <div class="row">
        <div class="col-12 col-md-6">

            <h4 class="mt-2">Rezepte</h4>

            <div>
                <input v-model="searchText" type="text" class="col-4">
                <button class="btn btn-secondary " @click="getRecipeList()">Suchen</button>
                <button type="button" class="btn btn-secondary" @click="addRecipe()">Hinzufügen</button>
                <button type="button" class="btn btn-secondary" @click="openImportModal()">Importieren</button>
            </div>

            <table class="table table-hover">
                <thead style="background-color:#AFBC6C">
                    <th class="py-2">Name</th>
                    <th class="py-2">Min.</th>
                    <th class="py-2">Actions</th>
                </thead>
                <tbody>
                    <tr v-for="(recipe,key) in recipes" :key="key">
                        <td @click="openRecipe(recipe.name)">
                            <h5>{{ recipe.name }}</h5>
                        </td>
                        <td @click="openRecipe(recipe.name)">
                            <h5>{{recipe.durationInMinutes }}</h5>
                        </td>
                        <td class="text-right" style="width: 120px;">
                            <button type="button" class="btn btn-secondary fa fa-arrow-circle-right mr-2" @click="setSelection(recipe, true)"
                                    data-mdb-tooltip-init title="Zum kochen auswählen" />
                            <button type="button" class="btn btn-secondary fa fa-trash" @click="openDeleteRecipePopup(recipe)"
                                    data-mdb-tooltip-init title="weg damit" />
                        </td>
                    </tr>
                </tbody>
            </table>

            <div v-show="showLoadingSpinner" class="row mt-4">
                <div class="spinner-border mx-auto" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>

            <modal-import-recipe ref="modalImportRecipe" :callback="getRecipeList" />
            <modal-yes-cancel ref="modalYesCancel" />
        </div>

        <div class="col-12 col-md-6">

            <h4 class="mt-2">Das wollen wir demnächst kochen</h4>

            <div>
                <button class="btn btn-secondary" @click="unselectAll()">Leeren</button>
            </div>

            <table class="table table-hover">
                <thead style="background-color:#AFBC6C">
                    <th class="py-2">Name</th><th />
                </thead>
                <tbody>
                    <tr v-for="(recipe,key) in selectedEntries" :key="key" :class="{'selected':recipe.temporarySelected === true, 'unselected':recipe.temporarySelected === false}"
                        draggable="true" @drop="finishDrag" @dragenter="changeOrder(recipe)" @dragstart="startDrag(recipe, key)">
                        <td @click="openRecipe(recipe.name)">
                            <h5>{{ recipe.name }}</h5>
                            <span v-if="recipe.comment" class="commentColor">{{recipe.comment}}</span>
                        </td>
                        <td class="text-right">
                            <button type="button" class="btn btn-secondary fa fa-pencil-square-o" @click="setComment(recipe)" />
                            <button type="button" class="btn btn-secondary fa fa-arrow-circle-left" @click="setSelection(recipe, false)" />
                        </td>
                    </tr>
                </tbody>
            </table>
            <modal-change-entry-comment ref="changeCommentModal"/>

            <div v-show="showLoadingSpinnerSelected" class="row mt-4">
                <div class="spinner-border mx-auto" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
            </div>

            <button type="button" class="btn btn-secondary" @click="addToShoppingList()">speichern und Zutaten in EK-Liste übernehmen</button>
        </div>
    </div>

</template>

<script>
import ModalImportRecipe from './ModalImportRecipe.vue'
import ModalChangeEntryComment from './ModalChangeEntryComment.vue'

export default {
    name: 'RecipeList',
    components: {
        ModalImportRecipe,
        ModalChangeEntryComment,
    },
    data: function () {
        return {
            recipes: [],
            selectedEntries: [],
            searchText: null,
            showLoadingSpinner: false,
            showLoadingSpinnerSelected: false,
            draggingItem: null,
        }
    },
    mounted: function () {
        this.getRecipeList()
        this.getSelectedEntries()
    },
    methods: {
        async getRecipeList () {
            this.recipes = []

            this.showLoadingSpinner = true

            let url = '/rest/recipes/'

            if (this.searchText) {
                url += '?name=' + this.searchText
            }
            const response = await window.cookyFetch(url, 'GET')
            const json = await response.json()
            this.recipes = json

            this.showLoadingSpinner = false
        },

        async getSelectedEntries () {
            this.selectedEntries = []

            this.showLoadingSpinnerSelected = true

            try {
                const response = await window.cookyFetch('/rest/selectedentry', 'GET')
                const json = await response.json()
                this.selectedEntries = json
            } catch (error) {
                console.log('Error: ', error)
            }

            this.showLoadingSpinnerSelected = false
        },

        openRecipe (recipeName) {
            this.$router.push('/recipe?name=' + recipeName + '&editMode=false')
        },
        addRecipe () {
            this.$router.push('/recipe/?editMode=true')
        },
        openImportModal () {
            this.$refs.modalImportRecipe.show()
        },
        openDeleteRecipePopup (recipe) {
            const me = this
            const callback = function () {
                me.deleteRecipe(recipe)
            }
            this.$refs.modalYesCancel.show(callback, 'Rezept löschen', 'schmeckt nicht?')
        },
        async unselectAll () {
            const me = this
            const callback = async function () {
                await window.cookyFetch('/rest/selectedentry', 'DELETE')
                me.getSelectedEntries()
            }
            this.$refs.modalYesCancel.show(callback, 'Auswahl abräumen', 'Alles neu?')
        },
        setSelection (recipe, select) {
            const alreadySelected = this.selectedEntries.filter(selectedRecipe => selectedRecipe.name === recipe.name)

            if (recipe.temporarySelected === true && select === false) {
                const idx = this.selectedEntries.indexOf(recipe)
                this.selectedEntries.pop(idx)
            }

            recipe.temporarySelected = select

            if (select === false) {
                this.$forceUpdate()
            }

            if (select && alreadySelected.length === 0) {
                // only add the recipe if it wasn't already added
                this.selectedEntries.push(recipe)
            }
        },

        async saveTemporarySelection () {
            this.showLoadingSpinnerSelected = true

            const body = {}

            this.selectedEntries.forEach(recipe => { body[recipe.name] = recipe.temporarySelected === undefined ? null : recipe.temporarySelected })

            const response = await window.cookyFetch('/rest/selectedentry/select/', 'PUT', JSON.stringify(body))

            if (response.ok) {
                this.getSelectedEntries()
            } else {
                this.showLoadingSpinnerSelected = false
            }
        },

        async addToShoppingList () {
            await this.saveTemporarySelection()
        },

        startDrag (item) {
            this.draggingItem = item
        },
        finishDrag () {
            this.draggingItem = null
        },
        changeOrder (item) {
            const newIndex = this.selectedEntries.indexOf(item)
            const oldIndex = this.selectedEntries.indexOf(this.draggingItem)

            this.selectedEntries[newIndex] = this.draggingItem
            this.selectedEntries[oldIndex] = item
        },
        async deleteRecipe (recipe) {
            try {
                await fetch('/rest/recipes/' + recipe.id, { method: 'DELETE', headers: { 'Content-Type': 'application/json' } })
            } catch (error) {
                console.log('Error: ', error)
            }
            this.getRecipeList()
        },
        setComment (selectedEntry) {
            const callback = (newComment) => {
                selectedEntry.comment = newComment
            }

            this.$refs.changeCommentModal.show(selectedEntry, callback)
        },
    },
}
</script>

<style scoped>

.selected{
    background-color: lightgreen;
}

.unselected{
    background-color: pink;
}
.commentColor{
    color: gray;
}

</style>
