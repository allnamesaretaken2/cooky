<template>
    <div class="row">
        <div class="col-12 col-md-6">
            <div class="row px-3">
                <h4 class="mt-2">Das wollen wir demnächst kochen</h4>
                <div style="flex: auto;" />
                <button class="btn btn-secondary" @click="unselectAll()">Leeren</button>
            </div>
            <table class="table table-hover">
                <thead style="background-color:#AFBC6C">
                    <th class="py-2">Name</th>
                    <th class="py-2">Actions</th>
                </thead>
                <tbody >
                    <tr v-for="(recipe,key) in selectedEntries" :key="key" draggable="true" @dragend="finishDrag"
                        @dragstart="startDrag(recipe, key)" @dragenter="changeOrder(recipe)">
                        <td @click="openRecipe(recipe.idRecipe)">
                            <h5>{{ recipe.frontendText }}</h5>
                            <span v-if="recipe.comment" class="commentColor">{{recipe.comment}}</span>
                        </td>
                        <td class="text-right" style="width: 160px">
                            <button type="button" class="btn btn-secondary fa fa-arrows mr-1" :itemId="recipe.id"
                                    @touchstart="startDrag(recipe, key)" @touchmove="movetouch" @touchend="finishDrag" />
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
        </div>

        <div class="col-12 col-md-6">

            <div class="row px-3">
                <h4 class="mt-2">Rezepte</h4>
                <div style="flex: auto;" />
                <button type="button" class="btn btn-secondary fa fa-search-plus " @click="showSearchAreaFn" />
            </div>
            <div class="row border border-success py-1" v-if="showSearchArea" style="background-color: rgba(175, 188, 108, 0.5); margin: 1px">
                <h5 class="px-2">Suche nach...</h5>
                <div class="col-12 d-flex">
                    <span style="width:170px">Zutaten</span>
                    <input v-model="searchTextIngredient" type="text" style="width: 100%;">
                </div>
                <div class="col-12 d-flex py-1">
                    <span style="width:170px">Rezept</span>
                    <input v-model="searchText" type="text" style="width: 100%;">
                </div>
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

            <modal-yes-cancel ref="modalYesCancel" />
        </div>
    </div>

</template>

<script>
import ModalChangeEntryComment from './ModalChangeEntryComment.vue'

export default {
    name: 'RecipeList',
    components: {
        ModalChangeEntryComment,
    },
    data: function () {
        return {
            recipes: [],
            selectedEntries: [],
            searchText: null,
            searchTextIngredient: null,
            showLoadingSpinner: false,
            showLoadingSpinnerSelected: false,
            draggingItem: null,
            showSearchArea: false,
        }
    },
    mounted: function () {
        this.getRecipeList()
        this.getSelectedEntries()
    },
    computed: {
        filteredRecipes: function () {
            let result = this.recipes

            if (this.searchText) {
                const filterText = this.searchText.toLowerCase()
                result = result.filter(recipe => {
                    return recipe.name.toLowerCase().indexOf(filterText) !== -1
                })
            }

            if (this.searchTextIngredient) {
                const textIngredients = this.searchTextIngredient.toLowerCase()
                result = result.filter(recipe => {
                    if (!recipe.recipeParts) {
                        return false
                    }
                    const foundParts = recipe.recipeParts.filter(part => {
                        if (!part.ingredients) {
                            return false
                        }
                        const foundingredients = part.ingredients.filter(ing => {
                            return ing.ingredient.name.toLowerCase().indexOf(textIngredients) !== -1
                        })
                        return foundingredients.length > 0
                    })
                    return foundParts.length > 0
                })
            }

            return result
        },
    },
    methods: {
        showSearchAreaFn () {
            this.showSearchArea = !this.showSearchArea
            if (!this.showSearchArea) {
                this.searchTextIngredient = null
                this.searchText = null
            }
        },
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
            this.$root.addOkay('Order changed')
        },

        async addToShoppingList () {
            const recipeIds = []
            this.selectedEntries.forEach(entry => { if (entry.idRecipe) { recipeIds.push(entry.idRecipe) } })
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

        movetouch (event) {
            const x = event.touches[0].clientX
            const y = event.touches[0].clientY
            const element = document.elementFromPoint(x, y)

            const itemIdAsString = element.getAttribute('itemId')
            if (itemIdAsString) {
                const itemId = parseInt(itemIdAsString)
                const item = this.selectedEntries.find(item => {
                    return item.id === itemId
                })
                if (item) {
                    this.changeOrder(item)
                }
            }

            if (event) {
                event.stopPropagation()
                event.preventDefault()
            }
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
