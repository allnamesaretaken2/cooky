<template>
    <div>
        <div class="row mt-1">
            <div class="col-12 text-right">
                <button type="button" class="btn btn-secondary m-1" @click="addNewItem()">Hinzufügen</button>
                <button type="button" class="btn btn-secondary" @click="chooseRecipes()">Rezepte auswählen</button>
            </div>
        </div>

        <table class="table table-hover">
            <thead style="background-color:#AFBC6C">
                <th class="py-2">Name</th>
                <th class="py-2">Menge</th>
                <th class="py-2">Einheit</th>
                <th />
            </thead>
            <tbody>
                <tr v-for="(item,key) in items" :key="key" draggable="true" @drop="changeOrder(item, key)" @dragover="allowDrop" @dragstart="ondragstart(item, key)">
                    <td>{{ item.name }}</td>
                    <td><input v-model="item.amount" type="number"></td>
                    <td><input v-model="item.unit" type="text"></td>
                    <td><button type="button" class="btn btn-secondary fa fa-trash" @click="removeItem(item, key)" /></td>
                </tr>
            </tbody>
        </table>

        <div class="row">
            <div class="col-12 text-right">
                <button type="button" class="btn btn-secondary m-1" @click="saveList()">Liste speichern</button>
                <button type="button" class="btn btn-secondary" @click="copyList()">Liste kopieren</button>
            </div>
        </div>

        <textarea id="copyField" hidden="true" />

        <!-- Recipe selection popup -->
        <baseModal ref="modalRecipeSelection">
            <template v-slot:header>
                Rezept auswählen
            </template>

            <template v-slot:body>
                <div class="form-row">
                    <table class="table table-hover">
                        <thead style="background-color:#AFBC6C">
                            <th class="py-2">Name</th><th />
                        </thead>
                        <tbody>
                            <tr v-for="(recipe,key) in recipes" :key="key">
                                <td>{{ recipe.name }}</td>
                                <input v-model="recipe.checked" type="checkbox" class="form-check-input">
                            </tr>
                        </tbody>
                    </table>
                </div>
            </template>

            <template v-slot:footer>
                <button type="button" class="col-3 btn btn-outline-primary" @click="closeRecipePopup()">okay</button>
            </template>
        </baseModal>

        <!-- shopping item edit popup -->
        <baseModal ref="modalItemEdit">
            <template v-slot:header>
                Neuer Eintrag
            </template>

            <template v-slot:body>
                <label class="m-1">Name</label>
                <input v-model="itemEdit.name" type="text" class="m-1">

                <label class="m-1">Menge</label>
                <input v-model="itemEdit.amount" type="number" class="m-1">

                <label class="m-1">Einheit</label>
                <input v-model="itemEdit.unit" type="text" class="m-1">
            </template>

            <template v-slot:footer>
                <button type="button" class="col-3 btn btn-outline-primary" @click="cancelModalItemEdit()">cancel</button>
                <button type="button" class="col-3 btn btn-outline-primary" @click="closeModalItemEdit()">okay</button>
            </template>
        </baseModal>
    </div>
</template>

<script>

import baseModal from '.././Modal.vue'

export default {
    name: 'RecipeList',

    components: { baseModal },

    data () {
        return {
            items: [],
            recipes: [],
            draggingItem: null,
            draggingItemIndex: null,
            // the item that is currently edited in the edit-popup
            itemEdit: null,
        }
    },

    mounted: function () {
        this.getShoppingItems()
    },

    methods: {

        /*
         * copy the item list to the clopboard by creating a string representation and stuff that one into an hidden text area.
         * Then call the copy function from there and hope that it works
         */
        copyList () {
            let itemText = ''
            this.items.forEach(item => {
                itemText += item.name

                if (item.amount) {
                    itemText += ' ' + item.amount
                }

                if (item.unit) {
                    itemText += ' ' + item.unit
                }

                itemText += '\n'
            })

            const copyField = document.querySelector('#copyField')

            copyField.hidden = false
            copyField.value = itemText

            copyField.select()

            try {
                var successful = document.execCommand('copy')

                if (!successful) { alert('Oops, unable to copy') }
            } catch (err) {
                alert('Oops, unable to copy')
            }

            /* unselect the range */
            copyField.hidden = true
            window.getSelection().removeAllRanges()
        },

        addNewItem () {
            this.itemEdit = {
                name: null,
                amount: null,
                unit: null,
            }

            this.$refs.modalItemEdit.show()
        },

        cancelModalItemEdit () {
            this.$refs.modalItemEdit.close()
        },

        closeModalItemEdit () {
            this.$refs.modalItemEdit.close()

            const foundItems = this.items.filter(item => item.name === this.itemEdit.name)
            if (foundItems.length > 0) {
                foundItems[0].amount += this.itemEdit.amount
            } else {
                this.items.push(this.itemEdit)
            }
        },

        async removeItem (item, key) {
            if (item.id) {
                try {
                    await fetch('/rest/shoppinglist/' + item.id, { method: 'DELETE', headers: { 'Content-Type': 'application/json' } })

                    this.items.splice(key, 1)
                } catch (error) {
                    console.log('Error: ', error)
                }
            } else {
                this.items.splice(key, 1)
            }
        },

        async chooseRecipes () {
            const response = await fetch('/rest/recipes/', { method: 'GET', headers: { 'Content-Type': 'application/json' } })
            const json = await response.json()
            this.recipes = json

            this.$refs.modalRecipeSelection.show()
        },

        allowDrop (ev) {
            ev.preventDefault()
        },

        changeOrder (item, key) {
            var blorp = this.items

            this.items = []

            if (this.draggingItemIndex < key) {
                for (let currentIdx = this.draggingItemIndex; currentIdx <= key; currentIdx++) {
                    blorp[currentIdx] = blorp[currentIdx + 1]
                }
                blorp[key] = this.draggingItem
            } else if (this.draggingItemIndex > key) {
                for (let currentIdx = this.draggingItemIndex; currentIdx >= key; currentIdx--) {
                    blorp[currentIdx] = blorp[currentIdx - 1]
                }
                blorp[key] = this.draggingItem
            }

            this.items = blorp
        },

        ondragstart (item, key) {
            this.draggingItem = item
            this.draggingItemIndex = key
        },

        closeRecipePopup () {
            this.$refs.modalRecipeSelection.close()
            this.recipes.forEach(recipe => {
                if (!recipe.checked) {
                    return
                }

                recipe.ingredients.forEach(ingredient => {
                    const foundItemArray = this.items.filter(item => {
                        return item.name === ingredient.ingredient.name
                    })

                    if (foundItemArray.length === 0) {
                        this.items.push({
                            name: ingredient.ingredient.name,
                            amount: ingredient.amount,
                        })
                    } else {
                        const item = foundItemArray[0]
                        item.amount += ingredient.amount
                    }
                })
            })
        },

        async saveList () {
            try {
                const response = await fetch('/rest/shoppinglist/', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(this.items) })

                const json = await response.json()

                this.items = json
            } catch (error) {
                console.log('Error: ', error)
            }
        },

        async getShoppingItems () {
            try {
                const response = await fetch('/rest/shoppinglist', { method: 'GET', headers: { 'Content-Type': 'application/json' } })

                const json = await response.json()

                this.items = json
            } catch (error) {
                console.log('Error: ', error)
            }
        },
    },
}
</script>
