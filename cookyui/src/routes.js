import RecipeList from './components/RecipeList.vue'
import Recipe from './components/Recipe.vue'

const routes = [
    { path: '/', component: RecipeList },
    { path: '/recipe/:id', component: Recipe },
];

export default routes;