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
        <div class="form-row">
          <label class="col-3">Chefkoch-Import</label>
          <input class="col-6 form-control" v-model="chefkochImport.url">
          <button type="button" class="col-3 btn btn-outline-primary" @click="chefkoch()">Importieren</button>
        </div>
        <div class="form-row">
          <label class="col-3">Name</label>
          <input class="col-9 form-control" v-model="newRecipe.name">
        </div>
        <div class="form-row">
          <label class="col-3">Portionen</label>
          <input class="col-9 form-control" v-model="newRecipe.persons">
        </div>

        <div class="form-row">
          <label class="col-3">Beschreibung</label>
          <textarea class="col-9 form-control" rows="8" v-model="newRecipe.description" />
        </div>
        <div class="form-row">
          <label class="col-3">Quelle</label>
          <input class="col-9 form-control" v-model="newRecipe.source">
        </div>
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
            newRecipe: {},
            chefkochImport: {url: null},
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
        },
        async chefkoch() {
            try {
                const response = await fetch('/rest/recipes/importFromChefkoch', {method: 'POST', headers:{'Content-Type': 'application/json'}, body: JSON.stringify(this.chefkochImport)})
                const json = await response.json()
                this.newRecipe = json
                this.chefkochImport.url = null
            } catch (error) {
                console.log("Error: ", error)
            }
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
