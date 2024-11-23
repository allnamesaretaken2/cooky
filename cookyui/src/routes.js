import RecipeList from './components/RecipeList.vue'
import Recipe from './components/recipe/Recipe.vue'
import ShoppingList from './components/shoppinglist/ShoppingList'
import TransferIngredients from './components/ingredientsToShopping/TransferIngredients'
import Supermarket from './components/supermarket/Supermarket.vue'

const routes = [
    { path: '/', component: RecipeList },
    { path: '/shoppinglist', component: ShoppingList },
    { path: '/recipe', component: Recipe },
    { path: '/transferingredients', component: TransferIngredients },
    { path: '/supermarket', component: Supermarket },
]

export default routes
