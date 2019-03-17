
<template>
  <div id="app">
    <div class="container">
      <table class="table table-striped">
        <thead>
          <th>Name</th>
        </thead>
        <tbody>
          <tr v-for="(recipe,key) in recipes" :key="key"><td>{{recipe.name}}</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'app',
  data: function () {
    return {
      recipes: []
    }
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
    }
  },
  mounted: function() {
    this.getRecipeList()
  }
}
</script>


<style lang="scss">
@import './styles/custom-bootstrap.scss';
@import '../node_modules/bootstrap/scss/bootstrap.scss';
</style>