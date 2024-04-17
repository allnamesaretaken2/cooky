<template>
    <baseModal ref="baseModal">
        <template #header>
            {{title}}
        </template>

        <template #body>
            {{text}}
        </template>

        <template #footer>
            <button type="button" class="btn btn-outline-info" @click="$refs.baseModal.close()">Nein</button>
            <button type="button" class="btn btn-primary" data-dismiss="modal" @click="ok">Ja</button>
        </template>
    </baseModal>
</template>

<script>
import baseModal from './Modal.vue'

export default {
    name: 'ModalYesNo',

    components: { baseModal },

    data () {
        return {
            callback: { type: Function, required: false },
            title: { type: String, required: false },
            text: { type: String, required: false },
        }
    },
    methods: {
        show (callback, title, text) {
            this.callback = callback
            this.title = title
            this.text = text
            this.$refs.baseModal.show()
        },
        async ok () {
            this.callback()
            this.$refs.baseModal.close()
        },
    },
}
</script>
