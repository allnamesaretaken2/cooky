<template> 
<transition name="modal">
 <div class="modal modal-mask" style="display: block" v-if="showModal">
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
        <button type="button" class="btn btn-outline-info" @click="showModal = false">Schließen</button>
        <button type="button" class="btn btn-primary" data-dismiss="modal" @click="submitAndClose()">Abschicken</button>
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
            showModal: false,
            newRecipe: {},
            chefkochImport: {url: null},
        }
    },
    props: {
      callback: {type: Function, required: true}
    },
    methods: {
        show() {
            this.newRecipe = {}
            this.chefkochImport = {url: null}
            this.showModal = true
        },
        async submitAndClose() {
            try {
                await fetch('/rest/recipes/', {method: 'POST', headers:{'Content-Type': 'application/json'}, body: JSON.stringify(this.newRecipe)})
            } catch (error) {
                console.log("Error: ", error)
            }
            this.callback()
            this.showModal = false
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

