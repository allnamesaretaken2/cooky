import RecipeList from './components/RecipeList.vue'
import Recipe from './components/Recipe.vue'
import ShoppingList from './components/shoppinglist/ShoppingList'

const routes = [
    { path: '/', component: RecipeList },
    { path: '/shoppinglist', component: ShoppingList },
    { path: '/recipe/:id', component: Recipe },
]

export default routes
