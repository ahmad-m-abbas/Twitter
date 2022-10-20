<template id="app-layout">
  <v-app>
    <topbar @change="change()" :name="name">
      <template v-slot:appbar-extension>
        <slot name="appbar">
        </slot>
      </template>
    </topbar>
    <v-row dense no-gutters class="flex-grow-0 primary">
      <v-col cols="1" sm="4" align-self="center" class="justify-center">
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
              {{ user.data.name }}
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

                  <v-list-item-title class="white--text">
                    <a :href=`/users/${$javalin.state.userDetails.user_id}/profile`
                       class="white--text text-decoration-none">Profile</a>
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
      <v-col cols="1" sm="4" align-self="center" class="mr-12 d-flex justify-around">
        <v-tabs
            v-model="tab"
            @change="handleChange"
            background-color="#102338"
            color="#e49617"
            centered
            dark
            height="100%"
        >
          <v-tab exact exact-path href="/" class="tab-navigation">
            HOMEPAGE
          </v-tab>
        </v-tabs>
      </v-col>
      <v-col cols="1" sm="3" class="ml-12 d-flex justify-end" style="height: 100%;">
        <search-bar
            @add="add"
            @view="view"
        >
        </search-bar>
      </v-col>
    </v-row>
    <v-row class="d-flex justify-center">
      <h1 class="main-title">Twitter</h1>
    </v-row>
    <v-main width="900px">
      <div class="d-flex justify-center align-baseline fill-height">
        <slot></slot>
      </div>
    </v-main>
  </v-app>
</template>

<script>
Vue.component("app-layout", {
  template: "#app-layout",
  props: ["name"],
  data() {
    return {
      tab: null,
      user: null,
      drawer: false
    }
  },
  created() {
    this.tab = sessionStorage.getItem('twitter-main-navigation-selected-tab');
    this.user = new LoadableData(`/api/user/${this.$javalin.state.userDetails.user_id}`);
  },
  methods: {
    handleChange(index) {
      sessionStorage.setItem('twitter-main-navigation-selected-tab', index);
    },

    change: function () {
      this.drawer = !this.drawer
    },
    view(id) {
      window.location = `/users/${id}/profile`;
    },
    add(id) {

      fetch(`/api/friends/`, {
        method: "post", 'Content-Type': 'application/json',
        body: JSON.stringify({
          "firstUser": id,
          "secondUser": this.$javalin.state.userDetails.user_id
        })
      }).then(
          this.MyFriends.refresh()
      );
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

* p {
  color: black;
}
</style>