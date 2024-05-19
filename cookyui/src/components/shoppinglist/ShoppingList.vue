<template>
    <div>
        <div class="row">
            <div class="col">
                We added the ingredients of the following recipes:
            </div>
        </div>
        <div class="row m-1 stickybuttons">
            <button type="button" class="btn btn-secondary m-1" @click="clearList()">Leeren</button>
            <button type="button" class="btn btn-secondary m-1" @click="saveList()">Speichern</button>
            <button type="button" class="btn btn-secondary m-1" @click="copyList()">Kopieren</button>
            <button type="button" class="btn btn-secondary m-1" @click="addNewItem()">Hinzufügen</button>
        </div>
        <table class="table table-hover">
            <thead style="background-color:#AFBC6C">
                <th class="py-2">Name</th>
                <th class="py-2" width="80px">Menge</th>
                <th class="py-2" width="100px">Einheit</th>
                <th width="110px" />
            </thead>
            <tbody>
                <tr v-for="(item,key) in items" :key="key" draggable="true" @mousemove="setPositionOfAddBar" @dragend="finishDrag"
                    @dragenter="changeOrder(item)" @dragstart="startDrag(item)">
                    <td class="p-2" ref="shoppingItemName" @click="setEditMode(item, key, 'Name')">
                        <input v-if="item.isEditable" v-model="item.name" type="text" style="width: 100%;" @keypress="onEnterKey(item, $event)">
                        <div v-else>{{item.name}}</div>
                    </td>
                    <td class="p-2" ref="shoppingItemAmount" @click="setEditMode(item, key, 'Amount')">
                        <input v-if="item.isEditable" v-model="item.amount" type="number" style="width: 100%;" @keypress="onEnterKey(item, $event)">
                        <div v-else>{{item.amount}}</div>
                    </td>
                    <td class="p-2" ref="shoppingItemUnit" @click="setEditMode(item, key, 'Unit')">
                        <input v-if="item.isEditable" v-model="item.unit" type="text" style="width: 100%;" @keypress="onEnterKey(item, $event)">
                        <div v-else>{{item.unit}}</div>
                    </td>
                    <td class="p-2 " >
                        <button v-if="item.isEditable" type="button" class="btn btn-secondary fa fa-check" @click="removeEditMode(item, key)" />
                        <button type="button" class="btn btn-secondary fa fa-trash" @click="removeItem(item, key)" />
                    </td>
                </tr>
            </tbody>
        </table>
        <textarea id="copyField" hidden="true" />

        <div class="hugo" ref="hugo" />
    </div>
</template>

<script>
export default {
    name: 'RecipeList',

    data () {
        return {
            items: [],
            draggingItem: null,
            // the item that is currently edited in the edit-popup
            itemEdit: null,
            previousEditedItem: null,
            addItemEventActive: false,

            // used to reset the values of a shopping item if the input is cancelled
            oldInputValues: null,
        }
    },

    mounted: function () {
        this.getShoppingItems()
    },

    methods: {
        finishDrag () {
            this.saveList()
        },
        setPositionOfAddBar (event) {
            var rect = event.currentTarget.getBoundingClientRect()
            const hugosStyle = this.$refs.hugo.style

            let addAreaReached = false

            if (event.clientY - rect.top < 5) {
                hugosStyle.top = (rect.top - 8) + 'px'
                addAreaReached = 0
            } else if (event.clientY > rect.bottom - 5) {
                hugosStyle.top = (rect.bottom - 8) + 'px'
                addAreaReached = 1
            }

            if (addAreaReached !== false) {
                hugosStyle.left = rect.x + 'px'
                hugosStyle.width = rect.width + 'px'
                if (hugosStyle.animation !== 'fadeIn 0.3s') {
                    hugosStyle.animation = 'fadeIn 0.3s'
                    hugosStyle.opacity = '100%'
                    this.addItemEventActive = addAreaReached
                }
            } else {
                if (hugosStyle.animation !== 'fadeOut 0.3s') {
                    hugosStyle.animation = 'fadeOut 0.3s'
                    hugosStyle.opacity = '0%'
                    this.addItemEventActive = false
                }
            }
        },
        removeEditMode (shoppingItem, key) {
            shoppingItem.isEditable = false

            if (shoppingItem.id === undefined && !shoppingItem.name) {
                // if nothing was written in this item we can remove it imediately
                this.items.splice(key, 1)
                return
            }

            if (shoppingItem.id === undefined ||
                 shoppingItem.name !== this.oldInputValues.name ||
                 shoppingItem.unit !== this.oldInputValues.unit ||
                 shoppingItem.amount !== this.oldInputValues.amount) {
                // only save the item if it is new or any of the important values has changed
                this.saveList()
            }
        },
        setEditMode (shoppingItem, key, refKey) {
            if (this.addItemEventActive !== false) {
                key += this.addItemEventActive
                this.items.splice(key, 0, { isEditable: true })
                refKey = 'Name'
            } else {
                if (this.previousEditedItem) {
                    this.previousEditedItem.isEditable = false
                }
                shoppingItem.isEditable = true
                this.oldInputValues = {
                    name: shoppingItem.name,
                    amount: shoppingItem.amount,
                    unit: shoppingItem.unit,
                }
                this.previousEditedItem = shoppingItem
            }

            this.$nextTick(() => {
                this.$refs['shoppingItem' + refKey][key].getElementsByTagName('input')[0].focus()
            })
        },
        async onEnterKey (shoppingItem, event) {
            if (event.key === 'Enter') {
                shoppingItem.isEditable = false
                await this.saveList()
                this.addNewItem()
            }
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
        },
        addNewItem () {
            this.itemEdit = {
                name: null,
                amount: null,
                unit: null,
                isEditable: true,
            }
            const arrayLength = this.items.push(this.itemEdit)
            this.$nextTick(() => {
                this.$refs.shoppingItemName[arrayLength - 1].getElementsByTagName('input')[0].focus()
            })
        },
        async removeItem (item, key) {
            if (item.id) {
                await window.cookyFetch('/rest/shoppinglist/' + item.id, 'DELETE')
                this.$root.addOkay('gelöscht')
            }
            this.items.splice(key, 1)
        },
        startDrag (item) {
            this.draggingItem = item
        },
        changeOrder (item) {
            const newIndex = this.items.indexOf(item)
            const oldIndex = this.items.indexOf(this.draggingItem)

            this.items[newIndex] = this.draggingItem
            this.items[oldIndex] = item
        },
        async saveList () {
            const response = await window.cookyFetch('/rest/shoppinglist/', 'POST', JSON.stringify(this.items))
            const json = await response.json()
            this.items = json.entities
            this.$root.addOkay('gespeichert in ' + json.duration + ' ms')
        },
        async getShoppingItems () {
            const response = await window.cookyFetch('/rest/shoppinglist', 'GET')
            const json = await response.json()

            this.items = json
        },
    },
}
</script>

<style scoped>
.hugo {
    position: fixed;
    height: 16px;
    background-color: rgb(175, 188, 108);
    top: 10px;
    left: 10px;
    pointer-events: none;
}
.stickybuttons {
    position: sticky;
    background-color: rgba(175, 188, 108, 0.5);
    top: 0;
    float: right
}

@keyframes fadeIn {
  0% { opacity: 0; }
  100% { opacity: 1; }
}

@keyframes fadeOut {
  0% { opacity: 1; }
  100% { opacity: 0; }
}
</style>
