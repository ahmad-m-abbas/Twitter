<template id="user-info">
  <v-row class="mt-n1" dense>
    <v-col cols="12">
      <v-sheet
          outlined
          rounded
          class="py-4 px-6 ml-n2"
          min-height="70">
        <v-form
            ref="form"
            v-model="valid">
          <div class="pa-5 rounded-tl-0 ml-n2">
            <v-row class="mt-2 d-flex flex-row">
              <v-text-field
                  :readonly="id!==this.$javalin.state.userDetails.user_id"
                  class="px-2"
                  label="User Name"
                  :rules="validationRules.nameRules"
                  required
                  outlined
                  v-model="userItem.name">
              </v-text-field>

              <v-text-field
                  :readonly="id!==this.$javalin.state.userDetails.user_id"
                  class="px-2"
                  label="Email"
                  :rules="validationRules.emailRules"
                  required
                  outlined
                  v-model="userItem.email">
              </v-text-field>
            </v-row>
          </div>
          <v-row
              v-if="id===this.$javalin.state.userDetails.user_id"
              dense
              mx-auto
              align="center"
              class="border-save-btn">
            <v-btn
                outlined
                class="save-btn"
                color="primary"
                @click="UpdateUser"
                large>
              SAVE
            </v-btn>
          </v-row>
          <v-row
          v-else
          dense
          mx-auto
          align="center"
          class="border-save-btn"
          >
            <v-btn
                v-if="!friends.some(f => f.id===this.$javalin.state.userDetails.user_id)"
                outlined
                class="save-btn"
                color="primary"
                @click="add(id)"
                large>
                ADD
            </v-btn>
          </v-row>
        </v-form>
      </v-sheet>
    </v-col>
  </v-row>
</template>
<script>
Vue.component("user-info", {
  template: "#user-info",
  data: () => ({
    valid: true,
    userItem:{
      id:'',
      name:'',
      email:''
    },
    validationRules: {
      nameRules: [
        v => !!v || 'Name is required',
      ],
      emailRules: [
        v => !!v || 'E-mail is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
      ]
    },
    user:{},
    friends: {},
    tweets: {},
    likes: {}
  }),
  created() {
    this.id = this.$javalin.pathParams["userId"];
    this.user = new LoadableData(`/api/user/${this.id}`).data;
    this.friends = new LoadableData(`/api/friends/${this.id}`).data;
    this.tweets = new LoadableData(`/api/user/${this.id}/tweets`).data;
    this.likes = new LoadableData(`/api/user/${this.id}/likes`).data;
    this.userItem.name = this.user.hasOwnProperty('name') ? this.user.name : "";
    this.userItem.email = this.user.hasOwnProperty('email') ? this.user.email : null;

  },
  methods: {

    save() {
        this.UpdateUser();

    },
    add(id){

      fetch(`/api/friends/`, {
        method: "post", 'Content-Type': 'application/json',
        body: JSON.stringify({
          "firstUser": id,
          "secondUser": this.$javalin.state.userDetails.user_id
        })
      }).then(
      this.friends.refresh()
      );
    },
    UpdateUser() {
      fetch(`/api/user/${this.$javalin.state.userDetails.user_id}`, {
        method: "PUT", 'Content-Type': 'application/json',
        body: JSON.stringify(this.userItem)
      }).then(() => {
        this.user = new LoadableData(`/api/user/${this.$javalin.state.userDetails.user_id}`).data;
      });
    }
  }
});
</script>
<style>
.border-save-btn {
  position: sticky;
  width: auto;
  z-index: 1;
  background: rgb(255, 255, 255);
  border: 1px solid rgba(0, 0, 0, 0.12);
  box-shadow: rgb(0 0 0 / 5%) 0px -4px 7px;
  border-radius: 0px 0px 5px 5px;
  height: 76px;
  bottom: 0;
  padding-right: 12px;
  justify-content: end;
}

.save-btn {
  position: relative;
  padding-right: 10px;
}
</style>