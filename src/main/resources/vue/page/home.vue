<template id="home">
  <app-layout >
    <v-container absolute  class="main-content" color="#87CEFA" opacity="0.5" >
      <h2>Hi <strong>{{new LoadableData(`/api/user/`+this.$javalin.state.userDetails.user_id).data.name}}</strong></h2>
      <h3>Share your thoughts here</h3>
      <v-textarea
          solo
          name="input-7-4"
          label="Post here..."
          v-model="text"

      ></v-textarea>
      <v-btn
      @click="post()"
      >
        Post the blog
      </v-btn>
      <v-container class="main-content-container" >
        <tweet-post v-for="tweet in tweets" class=""
        :id="tweet.hasOwnProperty('id') ? tweet.id : ''"
        :user="tweet.hasOwnProperty('id') ? tweet.userId : ''"
        :name="new LoadableData(`/api/user/`+tweet.userId).data.name"
        :text="tweet.hasOwnProperty('text') ? tweet.text : ''"
        :date="tweet.hasOwnProperty('created_on') ? new Date(tweet.created_on) : ''"
        :like="likedTweets.some(a=>tweet.id==a.id)"
        >
        </tweet-post>
      </v-container>
    </v-container>
  </app-layout>
</template>
<script>
Vue.component("home", {
  template: "#home",
  data() {
    return {
      text:'',
      tweets: [],
      likedTweets:[]
    }
  },
  created(){
    this.tweets=new LoadableData(`/api/friends/${this.$javalin.state.userDetails.user_id}/tweets`).data;
    this.likedTweets=new LoadableData(`/api/user/${this.$javalin.state.userDetails.user_id}/likes`).data;
  },
  methods:{
    post(){
      fetch(`/api/tweet/`, {
        method: "post", 'Content-Type': 'application/json',
        body: JSON.stringify({
          "userId": this.$javalin.state.userDetails.user_id,
          "text": this.text,
          "created_on": Date.now()
        })
      }).then(() => {
            this.text=''
      });
    }
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
  max-width: 100%;
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