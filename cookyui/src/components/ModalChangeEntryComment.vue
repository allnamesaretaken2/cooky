<template>
    <baseModal ref="baseModal">
        <template #header>
            Kommentar
        </template>

        <template #body>
            <div class="form-row">
                <textarea ref="commentField" v-model="comment" rows="3" class="form-control" />
            </div>
        </template>

        <template #footer>
            <button type="button" class="btn btn-primary" data-dismiss="modal" @click="saveComment()">Speichern</button>
            <button type="button" class="btn btn-secondary" data-dismiss="modal" @click="close()">Abbrechen</button>
        </template>
    </baseModal>
</template>

<script>
import baseModal from './Modal.vue'

export default {
    name: 'ModalChangeEntryComment',

    components: { baseModal },

    data: function () {
        return {
            selectedEntryId: { type: Number, required: true },
            comment: { type: String, required: false },
            callback: { type: Function, required: false },
        }
    },
    methods: {
        show (selectedEntry, callback) {
            this.selectedEntryId = selectedEntry.id
            this.comment = selectedEntry.comment
            this.$refs.baseModal.show()
            this.callback = callback
        },
        close () {
            this.$refs.baseModal.close()
        },
        async saveComment () {
            window.cookyFetch('/rest/selectedentry/setComment/' + this.selectedEntryId, 'PUT', this.comment, 'text/plain')
            if (this.callback) {
                this.callback(this.comment)
            }
            this.close()
        },
    },
}
</script>
