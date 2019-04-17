<template>
<div>
      <div class="row mt-4">
        <h4 class="mt-4 col-10">Liste aller Rezepte</h4>
        <div class="col-2"><button type="button" class="btn btn-primary" @click="openModal()">Hinzufügen</button></div>
      </div> <!-- row -->
      <table class="table table-hover">
        <thead style="background-color:#AFBC6C">
          <th class="py-2">Name</th><th></th>
        </thead>
        <tbody>
          <tr v-for="(recipe,key) in recipes" :key="key"><td><router-link :to="`/recipe/${recipe.id}`">{{recipe.name}}</router-link></td><td class="text-right"><i class="fa fa-edit"></i><i class="fa fa-trash"></i></td></tr>
        </tbody>
      </table>
            <modal-add-recipe v-if="showModal" @close="closeModal()"></modal-add-recipe>
</div>
</template>

<script>
import ModalAddRecipe from './ModalAddRecipe.vue'

export default {
  name: 'recipe-list',
  data: function () {
    return {
      recipes: [],
      showModal: false
    }
  },
  components: {
    ModalAddRecipe
  },
  methods: {
    getRecipeList: async function() {
      try {
        const response = await fetch('/rest/recipes/', {method: 'GET', headers:{'Content-Type': 'application/json'}});
        const json = await response.json();
        this.recipes = json
      } catch (error) {
        console.log("Error: ", error);
      }
    },
    openModal() {
        this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.getRecipeList()
    },
  },
  mounted: function() {
    this.getRecipeList()
  }
}
</script>