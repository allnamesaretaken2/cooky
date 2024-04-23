<template>
    <div class="row">
        <div class="col-12 col-md-6">
            <h4 class="mt-2">Das wollen wir demnächst kochen</h4>

            <div>
                <button class="btn btn-secondary" @click="unselectAll()">Leeren</button>
            </div>

            <table class="table table-hover">
                <thead style="background-color:#AFBC6C">
                    <th class="py-2">Name</th>
                    <th class="py-2">Actions</th>
                </thead>
                <tbody>
                    <tr v-for="(recipe,key) in selectedEntries" :key="key" draggable="true" @dragend="finishDrag"
                        @dragenter="changeOrder(recipe)" @dragstart="startDrag(recipe, key)">
                        <td @click="openRecipe(recipe.idRecipe)">
                            <h5>{{ recipe.frontendText }}</h5>
                            <span v-if="recipe.comment" class="commentColor">{{recipe.comment}}</span>
                        </td>
                        <td class="text-right" style="width: 110px">
                            <button type="button" class="btn btn-secondary fa fa-pencil-square-o mr-1" @click="setComment(recipe)" />
                            <button type="button" class="btn btn-secondary fa fa-arrow-circle-right" @click="deleteSelection(recipe)" />
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

            <button type="button" class="btn btn-secondary" @click="addToShoppingList()">Zutaten in EK-Liste übernehmen</button>
        </div>

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
                    <tr v-for="(recipe,key) in filteredRecipes" :key="key" >
                        <td @click="openRecipe(recipe.id)" v-if="textContains(recipe, searchText)">
                            <h5>{{ recipe.name }}</h5>
                        </td>
                        <td @click="openRecipe(recipe.id)">
                            <h5>{{recipe.durationInMinutes }}</h5>
                        </td>
                        <td class="text-right" style="width: 110px;">
                            <button type="button" class="btn btn-secondary fa fa-arrow-circle-left mr-1" @click="setSelection(recipe)"
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
    computed: {
        filteredRecipes: function () {
            if (!this.searchText) {
                return this.recipes
            }

            const filterText = this.searchText.toLowerCase()

            return this.recipes.filter(recipe => {
                return recipe.name.toLowerCase().indexOf(filterText) !== -1
            })
        },
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

            const response = await window.cookyFetch('/rest/selectedentry', 'GET')
            const json = await response.json()
            this.selectedEntries = json

            this.showLoadingSpinnerSelected = false
        },

        openRecipe (id) {
            this.$router.push('/recipe?idRecipe=' + id + '&editMode=false')
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

        async deleteSelection (entry, select) {
            await window.cookyFetch('/rest/selectedentry/' + entry.id, 'DELETE')
            this.getSelectedEntries()
        },
        async setSelection (recipe) {
            await window.cookyFetch('/rest/selectedentry/' + recipe.id, 'POST')
            this.getSelectedEntries()
        },

        async saveTemporarySelection () {
            this.showLoadingSpinnerSelected = true

            const body = []
            this.selectedEntries.forEach(entry => { body.push(entry.id) })

            await window.cookyFetch('/rest/selectedentry/setorder', 'PUT', JSON.stringify(body))

            this.showLoadingSpinnerSelected = false
        },

        async addToShoppingList () {
            const recipeIds = []
            this.selectedEntries.forEach(entry => { if (entry.idRecipe) { recipeIds.push(entry.idRecipe) } })
            await window.cookyFetch('/rest/shoppinglist/enhance', 'PUT', JSON.stringify(recipeIds))
        },

        startDrag (item) {
            this.draggingItem = item
        },
        finishDrag () {
            this.draggingItem = null
            this.saveTemporarySelection()
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
        textContains (recipe, filterText) {
            if (!filterText) {
                return true
            }
            return recipe.name.toLowerCase().indexOf(filterText.toLowerCase()) !== -1
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
