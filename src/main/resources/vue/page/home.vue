<template id="home">
  <app-layout>
    <v-overlay absolute class="main-content" color="#000000" opacity="0.5">
      <v-container class="main-content-container">
        <h1 class="main-title">Twitter</h1>
      </v-container>
    </v-overlay>
  </app-layout>
</template>
<script>
Vue.component("home", {
  template: "#home",
  data() {
    return {
      searchQuery: '',
    }
  }
});
</script>

<style scoped>
.main-content {
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}

.main-title {
  font-size: 6rem !important;
  font-weight: 500;
  line-height: 6rem;
  font-family: "Roboto", sans-serif !important;
  letter-spacing: 18px !important;
}

.description {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-size: 26px;
  line-height: 32px;
  letter-spacing: 0.01em;
  color: #FFFFFF;
  text-shadow: 0 2px 4px rgb(0 0 0 / 25%);
}

.main-content-container {
  max-width: 600px;
}

.popular-text {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-size: 16px;
  line-height: 24px;
  display: flex;
  align-items: center;
  letter-spacing: 0.5px;
}

.most-popular-hover {
  color: rgba(0, 0, 0, 0.87) !important;
  background-color: #FFFFFF;
  border-color: transparent;
}

.most-popular {
  color: #FFFFFF !important;
}

</style>