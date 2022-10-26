<template id="user-friends">
  <v-col class="mt-n1" dense >
    <v-row dense>
      <v-row class="d-flex justify-center">
        <v-progress-circular
            v-if="MyFriends.loading"
            indeterminate
            color="primary"
        ></v-progress-circular>
      </v-row>
    </v-row>
    <v-col cols="7" md="9">
      <v-sheet
          v-if="MyFriends.loaded"
          outlined
          rounded
          class="py-4 px-6 ml-n2"
          min-height="70">
        <v-row>
          <v-col sm="12" md="6">
            <v-text-field
                label="Search.."
                @change="filterMyFriends"
                v-model="nameFilter"
                prepend-icon="mdi-magnify"
            ></v-text-field>
          </v-col>z
          <v-col md="2" class="mt-4">
            <v-select
                v-model="sortingCriteria"
                :items="sortingFilter"
                @change="filterMyFriends"
                label="Sort"
                dense
                max-width="200px"
                outlined
            ></v-select>
          </v-col>
          <v-col sm="12" md="4" class="primary--text title font-weight-medium d-flex justify-end align-center">
            <span>{{ totalResultsCount }} Total Results</span>
          </v-col>
        </v-row>
        <v-divider class="mt-1 mb-4"></v-divider>
        <v-row dense v-if=" MyFriends.loaded">

          <v-col cols="12" md="6" lg="4" >

            <my-friend-card v-for="friend in MyFriends.data" class="" :key="friend.id"
                :name="friend.name"
                :email="friend.email"
                :id="friend.id"
                @unfriend="unfriend"
            ></my-friend-card>
          </v-col>
        </v-row>
        <v-row class="py-16 d-flex flex-column align-center justify-center" v-else>
          <img class="mx-auto" style="width:20%" src="/no_data.svg"/>
          <p class="pt-4 body-2">No results found!</p>
        </v-row>

      </v-sheet>
    </v-col>
  </v-col>
</template>

<script>
Vue.component("user-friends", {
  template: "#user-friends",
  data: () => ({
    id: '',
    nameFilter: "",
    sortingCriteria: "",
    sortingFilter: [

      {
        'text': 'Name - Descending',
        'value': {'orderBy': 'name', 'order': 'desc'}
      },
      {
        'text': 'Name - Ascending',
        'value': {'orderBy': 'name', 'order': 'asc'}
      }
    ],
    MyFriends: [],
    componentKey: 0,
  }),
  computed: {
    totalResultsCount() {
      if (this.MyFriends) {
        return this.MyFriends.length
      }
      return 0
    }
  },
  created() {
    this.id= this.$javalin.pathParams["userId"];
    this.MyFriends = new LoadableData(`/api/user/${this.id}/friends`);
  }, methods: {
    forceRerender() {
      this.componentKey += 1;
    },
    ClearFields() {
      this.MyFriends = new LoadableData(`/api/user/${this.id}/friends`);
    },
    filterMyFriends() {
      let query = []
      if (this.nameFilter) {
        query.push(`name=${this.nameFilter}`)
      }

      if (this.sortingCriteria) {
        query.push(`orderBy=${this.sortingCriteria.orderBy}`)
        query.push(`order=${this.sortingCriteria.order}`)
      }

      this.MyFriends = new LoadableData(`/api/user/${this.id}/friends/search?${query.join('&')}`);
    },
    unfriend(id){

      fetch(`/api/user/friends/`, {
        method: "delete", 'Content-Type': 'application/json',
        body: JSON.stringify({
          "firstUser": id,
          "secondUser": this.id
        })
      }).then(
          this.ClearFields()
    );
    }
  }
});
</script>