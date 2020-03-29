<template>
<div>
      <div class="row mt-4">
        <h4 class="mt-4 col-10">Liste aller Rezepte</h4>
        <div class="col-2"><button type="button" class="btn btn-primary" @click="addRecipe()">Hinzufügen</button></div>
      </div> <!-- row -->
      <table class="table table-hover">
        <thead style="background-color:#AFBC6C">
          <th class="py-2">Name</th><th></th>
        </thead>
        <tbody>
          <tr v-for="(recipe,key) in recipes" :key="key">
            <td><router-link :to="`/recipe/${recipe.id}`">{{recipe.name}}</router-link></td>
            <td class="text-right"><i class="fa fa-edit"></i><i class="fa fa-trash" @click="deleteRecipe(recipe)"></i></td>

          </tr>
        </tbody>
      </table>
            <modal-add-recipe ref="modalAddRecipe" :callback="getRecipeList"></modal-add-recipe>
            <modal-delete-recipe ref="modalDeleteRecipe" :callback="getRecipeList"/>
</div>
</template>

<script>
import ModalAddRecipe from './ModalAddRecipe.vue'
import ModalDeleteRecipe from './ModalDeleteRecipe.vue'

export default {
  name: 'recipe-list',
  data: function () {
    return {
      recipes: [],
    }
  },
  components: {
    ModalAddRecipe,
    ModalDeleteRecipe,
  },
  methods: {
    async getRecipeList() {
      try {
        const response = await fetch('/rest/recipes/', {method: 'GET', headers:{'Content-Type': 'application/json'}})
        const json = await response.json()
        this.recipes = json
      } catch (error) {
        console.log("Error: ", error)
      }
    },
    addRecipe() {
      this.$refs['modalAddRecipe'].show()
    },
    deleteRecipe(recipe) {
      this.$refs['modalDeleteRecipe'].show(recipe)
    },
  },
  mounted: function() {
    this.getRecipeList()
  }
}
</script>