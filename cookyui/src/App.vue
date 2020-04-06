
<template>
    <div id="app">
        <div class="container">
            <h1 class="text-right mt-3" style="color:#005B4C">cooky</h1>
            <div class="row border-top border-bottom py-2" style="border-color:#005B4C !important;">
                <div class="col">
                    <div class="row py-2" style="background-color:#005B4C; color:#FBFAF0;">
                        <div class="col-2"><router-link to="/">Alle Rezepte</router-link></div>
                        <div class="col-10">Kalender</div>
                    </div>
                </div>
            </div> <!-- header -->
            <router-view />

        </div>
    </div>
</template>

<script>

export default {
    name: 'App',
}
</script>

<style lang="scss">
@import './styles/custom-bootstrap.scss';
@import '../node_modules/bootstrap/scss/bootstrap.scss';
body {background-color:#FBFAF0}

</style>
