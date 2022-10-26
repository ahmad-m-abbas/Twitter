<template id="tweet-post">
  <div id="blog-post">
    <h2>{{ name }}</h2>
    <h3>{{ date.toLocaleDateString('en-US') }}</h3>
    <v-bt
        v-if="user==this.$javalin.state.userDetails.user_id"
        icon
        @click="enabel"
    >
      <v-icon
      >mdi-book-edit
      </v-icon>
    </v-bt>
    <v-bt
        v-if="user==this.$javalin.state.userDetails.user_id"
        icon
        @click="deleteTweet"
    >
      <v-icon
      >mdi-delete
      </v-icon>
    </v-bt>
    <v-text-field
        :readonly="!edit"
        v-model="text"
        outlined
        style="border-style: none;"
    ></v-text-field>
    <v-btn
        icon
        @click="addLike(id)"
    >
      <v-icon
          :color="liked ? 'red': ''"
      >mdi-heart
      </v-icon>
    </v-btn>
  </div>
</template>
<script>
Vue.component("tweet-post", {
  template: "#tweet-post",
  props: {
    id: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    },
    text: {
      type: String,
      required: true
    },
    date: {
      type: Date,
      required: true
    },
    like: {
      type: Boolean,
      required: true
    },
    user: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      liked: false,
      edit: false
    }
  },
  created() {

    this.liked = this.like;
  },
  methods: {
    deleteTweet() {
      fetch(`/api/tweet/${this.id}`, {
        method: "delete", 'Content-Type': 'application/json',
        body: JSON.stringify({})
      });
    },
    enabel() {
      this.edit = !this.edit;
      fetch(`/api/tweet/${this.id}`, {
        method: "put", 'Content-Type': 'application/json',
        body: JSON.stringify({
          "text": this.text,
          "id": this.id
        })
      });
    },
    addLike(id) {
      if (this.liked) {
        fetch(`/api/tweet/unlike`, {
          method: "delete", 'Content-Type': 'application/json',
          body: JSON.stringify({
            "tweetId": this.id,
            "userId": this.$javalin.state.userDetails.user_id
          })
        });
      } else {
        fetch(`/api/tweet/like`, {
          method: "post", 'Content-Type': 'application/json',
          body: JSON.stringify({
            "tweetId": this.id,
            "userId": this.$javalin.state.userDetails.user_id
          })
        });
      }
      this.liked = !this.liked;
    }
  }
});
</script>


<style scoped>
#blog-post {
  border: 5px solid #102338;
  text-align: center;
  width: 100%;
  overflow: auto;
}

text-field {
  text-align: start;
  color: black;
  font-size: 20px;
}

h1 {
  color: black;
}

h2 {
  color: black;
}

h3 {
  color: black;
}
</style>