<template id="tweet">
  <v-app>
    <v-row justify="space-between" align="center" dense no-gutters class="flex-grow-0 primary">
        <v-col cols="1" md="2" sm="3" align-self="center" class="justify-center">
          <v-hover v-if="!user" v-slot:default="{ hover }">
            <a href="/login" target="_blank" class="ml-4 white--text text-decoration-none"
             :class="{'text-decoration-underline': hover}">LOGIN</a>
          </v-hover>
          <v-menu 
            v-else
            :close-on-content-click="false"
            offset-y
            >

            <template v-slot:activator="{ on, attrs }">
            <v-btn
                color="primary"
                v-bind="attrs"
                v-on="on"
                tile
                depressed
                v-ripple="false"
                class="white--text user-menu-button"
            >
              <img width="30" class="mr-1" src="/user-placeholder.png"/>
              USERNAME
              <v-icon color="white">mdi-chevron-down</v-icon>
            </v-btn>
          </template>

          <v-list color="primary" class="white--text">
            <v-list-group
                color="white"
                class="white--text">
              <template v-slot:activator>
                <v-list-item-icon>
                  <v-icon class="white--text">mdi-account-circle-outline</v-icon>
                </v-list-item-icon>
                <v-list-item-title class="white--text">Profile</v-list-item-title>
              </template>
              <template v-slot:appendIcon>
                <v-icon class="white--text">mdi-chevron-down</v-icon>
              </template>
              <v-list color="primary" class="ml-8">
                <v-list-item-content>
                  <v-list-item-title>
                    <a :href=`/users/${user.id}/profile` class="white--text text-decoration-none">Profile</a>
                  </v-list-item-title>
                </v-list-item-content>
              </v-list>
            </v-list-group>
            <v-list-item>
              <v-list-item-icon>
                <v-icon class="white--text">mdi-logout</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title class="white--text">
                  <a :href=`/logout` class="white--text text-decoration-none">Logout</a>
                </v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list>
          </v-menu>
        </v-col>
      
        <v-col>
          <v-tabs
            v-model="tab"
            @change="handleChange"
            background-color="#102338"
            color="#e49617"
            centered
            dark
            height="56px"
        >
          <v-tab exact exact-path href="/" class="tab-navigation">
            HOMEPAGE
          </v-tab>
          <v-tab exact exact-path href="/equipments" class="tab-navigation">
            NOTIFICATIONS
          </v-tab>
        </v-tabs>
        </v-col>
     </v-row>
    </v-app>
</template>
<script>
Vue.component("app-layout", {
  template: "#app-layout",
  data() {
    return {
      tab: null,
      user: null
    }
  },
  created() {
    this.tab = sessionStorage.getItem('twitter-main-navigation-selected-tab');
    this.user = new LoadableData(`/api/user/${this.$javalin.state.userDetails.user_id}`).data
  },
  methods: {
    handleChange(index) {
      sessionStorage.setItem('witter-main-navigation-selected-tab', index);
    }
  }
});
</script>


<style scoped>
.tab-navigation {
  width: 163.67px;
  height: 56px;
}

.user-menu-button::before {
  background-color: transparent !important;
}

.profile-icon {
  color: white !important;
}

.v-tab {
  text-transform: none !important;
}

</style>