<template> 
<transition name="modal">
 <div class="modal modal-mask" style="display: block" v-show="showModal">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">Rezept wirklich löschen?</h4>
      </div>

      <div class="modal-body">Rezept "{{recipe.name}}" wirklich löschen?
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-outline-info" @click="showModal = false">Nein</button>
        <button type="button" class="btn btn-primary" data-dismiss="modal" @click="deleteRecipe()">Ja</button>
      </div>
    </div>
  </div>
</div>
</transition> 
</template>

<script>
export default {
    name: 'modal-delete-recipe',
    data: function() {
        return {
            recipe: {name: ''},
            showModal: false,
        }
    },
    props: {
        callback: {type: Function, required: true}
    },
    methods: {
        show(recipe) {
            this.recipe = recipe
            this.showModal = true
        },
        async deleteRecipe() {
            try {
              await fetch('/rest/recipes/'+this.recipe.id, {method: 'DELETE', headers:{'Content-Type': 'application/json'}})
            } catch (error) {
              console.log("Error: ", error)
            }
            this.callback()
            this.showModal = false
        },
    },
}
</script>

<style>

</style>