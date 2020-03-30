
<template>
  <div id="app">
    <div class="container">
      <h1 class="text-right mt-3" style="color:#005B4C">cooky</h1>
      <div class="row border-top border-bottom py-2" style="border-color:#005B4C !important;">
        <div class="col">
          <div class="row py-2" style="background-color:#005B4C; color:#FBFAF0;">
            <div class="col-2"><router-link to='/'>Alle Rezepte</router-link></div>
            <div class="col-7">Kalender</div>
            <div class="col-3 text-right"><input type="text" class="w-100" style="background-color:#fbfaf0"></div>
          </div>
        </div>
      </div> <!-- header -->
      <router-view></router-view>

    </div>
  </div>
</template>

<script>

export default {
  name: 'app',
}
</script>


<style lang="scss">
@import './styles/custom-bootstrap.scss';
@import '../node_modules/bootstrap/scss/bootstrap.scss';
body {background-color:#FBFAF0}
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}
</style>