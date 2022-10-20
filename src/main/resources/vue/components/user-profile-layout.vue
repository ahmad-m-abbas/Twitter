<template id="user-profile-layout">
  <app-layout>
    <v-container fluid>
      <v-row dense class="d-flex align-center justify-center" align-content="center">
        <side-navigation :menu-links="menuLinks">
          <template #default="props" >
            <user-tweets v-if="props.selectedItem.component === 'user-tweets'"> </user-tweets>
            <user-info v-if="props.selectedItem.component === 'user-info'"></user-info>
            <user-friends v-if="props.selectedItem.component === 'my-friends'"></user-friends>
          </template>
        </side-navigation>
      </v-row>
    </v-container>
  </app-layout>
</template>
<script>
Vue.component("user-profile-layout", {
  template: "#user-profile-layout",

  data() {
    return {
      userid: "",
      menuLinks: []
    }
  },

  created() {
    this.userid = this.$javalin.pathParams["userId"];

    this.menuLinks = [
      {
        text: 'User Information',
        icon: 'mdi-domain ',
        path: `/users/${this.userid}/info`,
        component: 'user-info',
      },
      {
        text: 'My Friends',
        icon: 'mdi-tractor-variant',
        path: `/users/${this.userid}/friends`,
        component: 'my-friends'
      },
      {
        text: 'Tweets',
        icon: 'mdi-domain -variant',
        path: `/users/${this.userid}/tweets`,
        component: 'user-tweets'
      }
    ]
  }
});
</script>