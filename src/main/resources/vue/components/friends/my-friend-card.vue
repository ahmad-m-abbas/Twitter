<template id="my-friend-card">
  <div>
    <v-card
        class="mx-auto"
        outlined>

      <v-row class="d-flex justify-space-between align-center my-2 ml-4 mr-3">
        <v-col>
          <h6 class="title">{{ name }}</h6>
        </v-col>
        <v-col>
          <v-btn
              v-if="this.$javalin.pathParams['userId']===this.$javalin.state.userDetails.user_id"
              @click="$emit('unfriend',id)"
              class="text-uppercase pr-0" color="grey"
          >
            Unfriend
          </v-btn>
        </v-col>
      </v-row>
      <v-row class="d-flex justify-start align-center my-2 ml-4 mr-3">
        <template>
          <v-chip  v-if="email"
                   class="body-2  mr-2 mb-1"
                   color="blue"
                   label
                   small
                   outlined>
            {{ email }}
          </v-chip>
        </template>
      </v-row>

    </v-card>
  </div>
</template>
<script>
Vue.component("my-friend-card", {
  template: "#my-friend-card",
  props: {
    id:{
      type: String,
      required : true
    },
    name: {
      type: String,
      required: true
    },
    email:{
      type: String ,
      required: true
    }
  },
  data(){
    return{
    }
  }

});
</script>