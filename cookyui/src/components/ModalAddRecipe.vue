<template> 
<transition name="modal">
 <div class="modal modal-mask" style="display: block">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">
          Rezept hinzufügen
        </h4>
      </div>
      
      <div class="modal-body">
          <label class="col-4">Name</label>
          <input class="col-8" v-model="newRecipe.name">
      </div>
      
      <div class="modal-footer">
             <button type="button" class="btn btn-outline-info" @click="close()"> Close </button>
   <button type="button" class="btn btn-primary" data-dismiss="modal" @click="submitAndClose()">
     Submit
   </button>
      </div>
    </div>
  </div>
</div>
</transition> 
</template>

<script>
export default {
    name: 'modal-add-recipe',
    data: function() {
        return {
            newRecipe: {}
        }
    },
    methods: {
        close() {
            this.$emit('close')
        },
        submitAndClose: async function(){
            try {
                await fetch('/rest/recipes/', {method: 'POST', headers:{'Content-Type': 'application/json'}, body: JSON.stringify(this.newRecipe)});
            } catch (error) {
                console.log("Error: ", error);
            }
            this.close()
        }
    },
}
</script>

<style>
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
