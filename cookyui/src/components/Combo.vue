<template>
    <div class="dropdown">
        <div class="input-group">
            <input v-model="newValue" @input="filterOptions" placeholder="Type to filter" class="form-control">
            <button type="button" class="btn btn-secondary fa fa-toggle-down input-group-append" @click="showDropdownEvent" />
        </div>
        <div v-if="showDropdown" class="dropdown-style">
            <a v-for="(option, index) in filteredOptions" :key="index" @click="selectOption(option)" class="dropdown-item">
                {{ option }}
            </a>
        </div>
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
    },
}
</script>

<style scoped>

.dropdown-style{
    width : 100%;
    z-index : 1;
    position: absolute;
    background-color: white;
    border: 1px solid #ced4da;
    border-radius: 0.25rem;
}
</style>
