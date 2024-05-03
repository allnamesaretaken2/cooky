<template>
    <div>
        <div class="row mt-1">
            <div class="col-12 text-right">
                <button type="button" class="btn btn-secondary m-1" @click="addNewItem()">Hinzufügen</button>
            </div>
        </div>
        <div class="row">
            <div class="col">
                We added the ingredients of the following recipes:
            </div>
        </div>
        <div class="row">
            <ul>
                <li v-for="(recipeName, key) in recipeNames" :key="key" >{{ recipeName }}</li>
            </ul>
        </div>
        <table class="table table-hover">
            <thead style="background-color:#AFBC6C">
                <th class="py-2">Name</th>
                <th class="py-2" width="80px">Menge</th>
                <th class="py-2" width="100px">Einheit</th>
                <th width="110px" />
            </thead>
            <tbody>
                <tr v-for="(item,key) in items" :key="key" draggable="true"
                    @drop="finishDrag" @dragenter="changeOrder(item)" @dragstart="startDrag(item, key)">
                    <td class="p-2" ref="shoppingItemName" @click="changeEditMode(item, key, 'Name')">
                        <input v-if="item.isEditable" v-model="item.name" type="text" style="width: 100%;">
                        <div v-else>{{item.name}}</div>
                    </td>
                    <td class="p-2" ref="shoppingItemAmount" @click="changeEditMode(item, key, 'Amount')">
                        <input v-if="item.isEditable" v-model="item.amount" type="number" style="width: 100%;">
                        <div v-else>{{item.amount}}</div>
                    </td>
                    <td class="p-2" ref="shoppingItemUnit" @click="changeEditMode(item, key, 'Unit')">
                        <input v-if="item.isEditable" v-model="item.unit" type="text" style="width: 100%;">
                        <div v-else>{{item.unit}}</div>
                    </td>
                    <td class="p-2 " >
                        <button v-if="item.isEditable" type="button" class="btn btn-secondary fa fa-check" @click="removeEditMode(item)" />
                        <button type="button" class="btn btn-secondary fa fa-trash" @click="removeItem(item, key)" />
                    </td>
                </tr>
            </tbody>
        </table>
        <div class="row">
            <div class="col-12 text-right">
                <button type="button" class="btn btn-secondary m-1" @click="clearList()">Alles neu</button>
                <button type="button" class="btn btn-secondary m-1" @click="saveList()">Speichern</button>
                <button type="button" class="btn btn-secondary" @click="copyList()">Kopieren</button>
            </div>
        </div>

        <textarea id="copyField" hidden="true" />

        <!-- shopping item edit popup -->
        <baseModal ref="modalItemEdit">
            <template #header>
                Neuer Eintrag
            </template>

            <template #body>
                <label class="m-1">Name</label>
                <input v-model="itemEdit.name" type="text" class="m-1">

                <label class="m-1">Menge</label>
                <input v-model="itemEdit.amount" type="number" class="m-1">

                <label class="m-1">Einheit</label>
                <input v-model="itemEdit.unit" type="text" class="m-1">
            </template>

            <template #footer>
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
            recipeNames: [],
            draggingItem: null,
            // the item that is currently edited in the edit-popup
            itemEdit: null,
            previousEditedItem: null,
        }
    },

    mounted: function () {
        this.getShoppingItems()
        this.getRecipeNamesWeAlreadyAddedToTheShoppingList()
    },

    methods: {
        removeEditMode (shoppingItem) {
            shoppingItem.isEditable = false
        },

        changeEditMode (shoppingItem, key, refKey) {
            if (this.previousEditedItem) {
                this.previousEditedItem.isEditable = false
            }
            shoppingItem.isEditable = true
            this.previousEditedItem = shoppingItem

            this.$nextTick(() => {
                this.$refs['shoppingItem' + refKey][key].getElementsByTagName('input')[0].focus()
            })
        },

        /*
         * copy the item list to the clipboard by creating a string representation and stuff that one into an hidden text area.
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
                const successful = document.execCommand('copy')

                if (!successful) { alert('Oops, unable to copy') }
            } catch (err) {
                alert('Oops, unable to copy')
            }

            /* unselect the range */
            copyField.hidden = true
            window.getSelection().removeAllRanges()
        },

        async clearList () {
            await window.cookyFetch('/rest/shoppinglist/', 'DELETE')
            this.items = []
            this.recipeNames = []
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
        startDrag (item, key) {
            this.draggingItem = item
        },
        finishDrag () {
            this.draggingItem = null
        },
        changeOrder (item) {
            const newIndex = this.items.indexOf(item)
            const oldIndex = this.items.indexOf(this.draggingItem)

            this.items[newIndex] = this.draggingItem
            this.items[oldIndex] = item
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
            const response = await window.cookyFetch('/rest/shoppinglist', 'GET')
            const json = await response.json()

            this.items = json
        },
        async getRecipeNamesWeAlreadyAddedToTheShoppingList () {
            const response = await window.cookyFetch('/rest/shoppinglist/getRecipeNamesWeAlreadyAddedToTheShoppingList', 'GET')
            const json = await response.json()

            this.recipeNames = json
        },
    },
}
</script>
