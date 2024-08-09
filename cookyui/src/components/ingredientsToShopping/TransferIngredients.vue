<template>
    <div>
        <div class="row">
            <div class="col">
                Select the ingredients you want to have as shopping items
            </div>
        </div>
        <div >
            <table class="table table-hover" >
                <thead style="background-color:#AFBC6C" class="stickyHeader">
                    <th >
                        <div class="row stickyHeader" style="top: 1;">
                            <div class="col-2" style="text-align: right;">
                                <button type="button" class="btn btn-primary fa fa-arrow-left" @click="selectPrevious()" />
                            </div>
                            <div class="col-4 " style="text-align: center;">
                                <h5 v-if="recipe" >{{ recipe.name }}</h5>
                            </div>
                            <div class="col-2 ">
                                <button type="button" class="btn btn-primary fa fa-arrow-right" @click="selectNext()" />
                            </div>
                            <div class="col-4" style="text-align: right;">
                                <button type="button" class="btn btn-primary" @click="createShoppingItems()" >
                                    Selektierte in EK-Liste übernehmen
                                </button>
                            </div>
                        </div>
                    </th>
                </thead>
                <tbody>
                    <tr v-for="(ingredient,key) in ingredients" :key="key" @click="selectIngredient(ingredient)">
                        <td :class="{ checkedbackground: ingredient.selectForShopping}">
                            <h5>{{ ingredient.ingredient.name }}</h5>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
    name: 'TransferIngredients',

    data () {
        return {
            selectedEntries: null,
            recipe: null,
            ingredients: null,
        }
    },

    mounted () {
        this.getSelectedEntries()
    },

    methods: {
        async getSelectedEntries () {
            const response = await window.cookyFetch('/rest/recipes/getSelected', 'GET')
            this.selectedEntries = await response.json()

            this.recipe = this.selectedEntries[0]
            this.getIngredientsFromParts()
        },
        selectPrevious () {
            var idx = this.selectedEntries.indexOf(this.recipe)
            if (idx === 0) {
                this.recipe = this.selectedEntries[this.selectedEntries.length - 1]
            } else {
                this.recipe = this.selectedEntries[idx - 1]
            }

            this.getIngredientsFromParts()
        },
        selectNext () {
            var idx = this.selectedEntries.indexOf(this.recipe)
            if (idx === this.selectedEntries.length - 1) {
                this.recipe = this.selectedEntries[0]
            } else {
                this.recipe = this.selectedEntries[idx + 1]
            }

            this.getIngredientsFromParts()
        },

        getIngredientsFromParts () {
            const newIngredients = []
            this.recipe.recipeParts.forEach(part => newIngredients.push(...part.ingredients))
            this.ingredients = newIngredients
        },

        /**
         * Set a selection flag so that the application knows which ingredients shall be converted to shopping items
         * @param {Object} ingredient
         */
        selectIngredient (ingredient) {
            ingredient.selectForShopping = !ingredient.selectForShopping
        },

        async createShoppingItems () {
            const selectedIngredientIds = []

            this.selectedEntries.forEach(recipe => {
                recipe.recipeParts.forEach(part => {
                    part.ingredients.forEach(ingr => {
                        if (ingr.selectForShopping) {
                            selectedIngredientIds.push(ingr.id)
                        }
                    })
                })
            })

            const response = await window.cookyFetch('/rest/shoppinglist/enhance', 'PUT', JSON.stringify(selectedIngredientIds))
            const newAndUpdated = await response.json()
            this.$root.addOkay('Created ' + newAndUpdated.new + ' items. Updated ' + newAndUpdated.updated + ' items')

            this.selectedEntries.forEach(recipe => {
                recipe.recipeParts.forEach(part => {
                    part.ingredients.forEach(ingr => {
                        if (ingr.selectForShopping) {
                            ingr.selectForShopping = null
                        }
                    })
                })
            })
        },
    },
}
</script>

<style scoped>
    .checkedbackground{
        background-color: rgba(175, 188, 108, 0.5);
    }

    .stickyHeader{
        position: sticky;
        top: 0;
    }
</style>
