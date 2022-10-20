<template id="tweet-post">
  <div  id="blog-post">
    <h1>{{id}}</h1>
    <h2>{{name}}</h2>
    <h3>{{date.toLocaleDateString('en-US')}}</h3>
    <p >{{text}}</p>
    <br>
    <br>
    <v-btn
    icon
    @click="like(id)"
    >
      <v-icon
      :color="liked ? 'red': ''"
      >mdi-heart</v-icon>
    </v-btn>
  </div>
</template>
<script>
Vue.component("tweet-post", {
  template: "#tweet-post",
  props: {
    id:{
      type: String,
      required : true
    },
    name: {
      type: String,
      required: true
    },
    text:{
      type: String,
      required: true
    },
    date:{
      type: Date,
      required: true
    }
  },
  data() {
    return {
      liked:false
    }
  },
  created(){
    liked = this.$parent.likedTweets.filter(tweet => tweet.id===id);
  },
  methods:{
    like(id){
      if(this.liked){
        this.liked=false
      }
      else{
        this.liked=true
      }

    }
  }
});
</script>


<style scoped>
#blog-post{
  border: 5px solid #102338;
  text-align: center;
  width: 100%;

}
p{
  color: black;
}
h1{
  color: black;
}
h2{
  color: black;
}
h3{
  color: black;
}
</style>