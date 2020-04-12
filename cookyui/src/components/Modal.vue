<template>
    <div v-if="isVisible" class="modal modal-mask">

        <div class="modal-dialog modal-dialog-centered modal-lg" role="dialog">
            <div class="modal-content">

                <!-- headline -->
                <div class="modal-header">
                    <h4 class="modal-title">
                        <slot name="header">
                            Default header
                        </slot>
                    </h4>
                </div>

                <!-- content -->
                <div class="modal-body">
                    <slot name="body">
                        <p>default body</p>
                    </slot>
                </div>

                <!--footer -->
                <div class="modal-footer">
                    <slot name="footer">
                        <p>default footer</p>
                    </slot>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'Window',
    data: function () {
        return {
            isVisible: false,
        }
    },

    methods: {

        close: function () {
            this.isVisible = false
        },

        show: function () {
            this.isVisible = true
        },
    },
}
</script>

<style scoped>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}
</style>
