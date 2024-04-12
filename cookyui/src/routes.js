import RecipeList from './components/RecipeList.vue'
import Recipe from './components/recipe/Recipe.vue'
import ShoppingList from './components/shoppinglist/ShoppingList'

const routes = [
    { path: '/', component: RecipeList },
    { path: '/shoppinglist', component: ShoppingList },
    { path: '/recipe', component: Recipe },
]

export default routes
