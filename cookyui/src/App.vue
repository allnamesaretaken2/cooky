
<template>
  <div id="app">
    <div class="container" style="width:960px">
      <h1 class="text-right mt-3" style="color:#005B4C">cooky</h1>
      <div class="row border-top border-bottom py-2" style="border-color:#005B4C !important;">
        <div class="col">
          <div class="row py-2" style="background-color:#005B4C; color:#FBFAF0;">
            <div class="col-2">Alle Rezepte</div>
            <div class="col-7">Kalender</div>
            <div class="col-3 text-right"><input type="text" class="w-100" style="background-color:#fbfaf0"></div>
          </div>
        </div>
      </div> <!-- header -->
      <div class="row mt-4">
        <h4 class="mt-4 col-10">Liste aller Rezepte</h4>
        <div class="col-2"><button type="button" class="btn btn-primary" @click="openModal()">Hinzufügen</button></div>
      </div> <!-- row -->
      <table class="table table-hover">
        <thead style="background-color:#AFBC6C">
          <th class="py-2">Name</th><th></th>
        </thead>
        <tbody>
          <tr v-for="(recipe,key) in recipes" :key="key"><td>{{recipe.name}}</td><td class="text-right"><i class="fa fa-edit"></i><i class="fa fa-trash"></i></td></tr>
        </tbody>
      </table>
    </div>
    <modal-add-recipe v-if="showModal" @close="closeModal()"></modal-add-recipe>
  </div>
</template>

<script>
import ModalAddRecipe from './components/ModalAddRecipe.vue'
export default {
  name: 'app',
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


<style lang="scss">
@import './styles/custom-bootstrap.scss';
@import '../node_modules/bootstrap/scss/bootstrap.scss';
body {background-color:#FBFAF0}
</style>