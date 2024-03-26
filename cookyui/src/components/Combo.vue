<template>
    <div class="dropdown">
        <input v-model="newValue" @input="filterOptions" placeholder="Type to filter">
        <button type="button" class="btn btn-secondary fa fa-toggle-down" @click="showDropdownEvent" />
        <ul v-if="showDropdown" class="dropdown-style">
            <li v-for="(option, index) in filteredOptions" :key="index" @click="selectOption(option)" @mouseover="markElementHovered" @mouseleave="markElementNotHovered">
                {{ option }}
            </li>
        </ul>
    </div>
</template>

<script>
export default {

    props: ['modelValue', 'comboValues'],
    emits: ['update:modelValue'],

    data () {
        return {
            showDropdown: false,
            newValue: this.modelValue,
            filteredOptions: [],
        }
    },
    watch: {
        modelValue (changedValue) {
            this.newValue = changedValue
        },
    },

    methods: {
        filterOptions () {
            this.filteredOptions = this.comboValues.filter(option =>
                option.toLowerCase().includes(this.newValue.toLowerCase()))

            this.showDropdown = true
            this.$emit('update:modelValue', this.newValue)
        },
        selectOption (selectedValue) {
            this.showDropdown = false
            this.newValue = selectedValue
            this.$emit('update:modelValue', selectedValue)
            // You can emit an event, update a value, etc.

            this.filteredOptions = this.comboValues
        },
        showDropdownEvent () {
            this.filteredOptions = this.comboValues
            this.showDropdown = !this.showDropdown
        },
        markElementHovered (event) {
            event.target.classList.add('dropdown-listentry-hovered')
        },
        markElementNotHovered (event) {
            event.target.classList.remove('dropdown-listentry-hovered')
        },
    },
}
</script>

<style scoped>

.dropdown-style{
    width : 100%;
    z-index : 1;
    position: absolute;
    background-color: white;
    border: 1px solid black;
}

.dropdown-listentry-hovered{
    background-color: aquamarine;
}
</style>
