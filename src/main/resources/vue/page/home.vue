<template id="home">
  <app-layout >

    <v-container absolute  class="main-content" color="#87CEFA" opacity="0.5" >
      <v-container class="main-content-container" >
        <tweet-post v-for="tweet in tweets" class=""
        :id="tweet.hasOwnProperty('id') ? tweet.id : ''"
        :name="new LoadableData(`/api/user/`+tweet.userId).data.name"
        :text="tweet.hasOwnProperty('text') ? tweet.text : ''"
        :date="tweet.hasOwnProperty('created_on') ? new Date(tweet.created_on) : ''"

        >
        </tweet-post>
        <h1>
          {{tweets}}
        </h1>
      </v-container>
    </v-container>
  </app-layout>
</template>
<script>
Vue.component("home", {
  template: "#home",
  data() {
    return {
      tweets: [],
      likedTweets:[]
    }
  },
  created(){
    this.tweets=new LoadableData(`/api/friends/${this.$javalin.state.userDetails.user_id}/tweets`).data;
    this.likedTweets=new LoadableData(`/api/user/${this.$javalin.state.userDetails.user_id}/likes`).data;
  },
  methods:{

  }
});
</script>

<style scoped>
.main-content {
   height: 1000px;
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
  max-width: 40%;
  max-height: 100%;

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
  color: #102338 !important;
}

</style>