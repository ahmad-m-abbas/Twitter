<template id="user-tweets">

  <v-col class="mt-n1" dense >
    <v-row dense>
      <v-row class="d-flex justify-center">
        <v-progress-circular
            v-if="tweets.loading"
            indeterminate
            color="primary"
        ></v-progress-circular>
      </v-row>
    </v-row>
    <v-col cols="7" md="9">
      <v-sheet
          v-if="tweets.loaded"
          outlined
          rounded
          class="py-4 px-6 ml-n2"
          min-height="70">
        <v-row>
          <v-col sm="12" md="4" class="primary--text title font-weight-medium d-flex justify-end align-center">
            <span>{{ totalResultsCount }} Total Results</span>
          </v-col>
        </v-row>
        <v-divider class="mt-1 mb-4"></v-divider>
        <v-row dense v-if=" tweets.loaded">

          <v-col cols="12" md="6" lg="4" >
            <tweet-post v-for="tweet in tweets.data" class=""
                        :id="tweet.hasOwnProperty('id') ? tweet.id : ''"
                        :name="new LoadableData(`/api/user/`+tweet.userId).data.name"
                        :user="tweet.hasOwnProperty('userId') ? tweet.userId : 'a'"
                        :text="tweet.hasOwnProperty('text') ? tweet.text : ''"
                        :date="tweet.hasOwnProperty('created_on') ? new Date(tweet.created_on) : ''"
                        :like="likedTweets.some(a=>tweet.id==a.id)"
            >
            </tweet-post>
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
Vue.component("user-tweets", {
  template: "#user-tweets",
  data: () => ({
    id:'',
    contains: "",
    sortingCriteria: "",
    sortingFilter: [

      {
        'text': 'Date - Descending',
        'value': {'orderBy': 'Date', 'order': 'desc'}
      },
      {
        'text': 'Date - Ascending',
        'value': {'orderBy': 'Date', 'order': 'asc'}
      }
    ],
    tweets: [],
    componentKey: 0,
  }),
  computed: {
    totalResultsCount() {
      if (this.tweets) {
        return this.tweets.data.length
      }
      return 0
    }
  },
  created() {
    this.id= this.$javalin.pathParams["userId"];
    this.tweets = new LoadableData(`/api/user/${this.id}/tweets`);
    this.likedTweets=new LoadableData(`/api/user/${this.$javalin.state.userDetails.user_id}/likes`).data;

  }, methods: {
    forceRerender() {
      this.componentKey += 1;
    },
    ClearFields() {
      this.tweets = new LoadableData(`/api/user/${this.id}/tweets`);
    },
    unfriend(id){

      fetch(`/api/user/friends/`, {
        method: "delete", 'Content-Type': 'application/json',
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