<template id="search-bar">
  <v-toolbar color="orange accent-1">
    <v-toolbar-title class="title mr-6 ">Add friends</v-toolbar-title>
    <v-autocomplete
        v-model="model"
        :items="items"
        :loading="isLoading"
        :search-input.sync="search"
        chips
        clearable
        hide-details
        hide-selected
        item-text="name"
        item-value="symbol"
        label="Search for a user..."
        solo
    >
      <template >
        <v-list-item>
          <v-list-item-title>
            Search for users
          </v-list-item-title>
        </v-list-item>
      </template>
      <template v-slot:selection="{ attr, on, item, selected }">
        <v-chip
            v-bind="attr"
            :input-value="selected"
            color="blue-grey"
            class="white--text"
            v-on="on"
        >
          <v-icon left>mdi-coin</v-icon>
          <span v-text="item.name"></span>
        </v-chip>
      </template>
      <template v-slot:item="{ item }">
        <v-list-item-avatar
            color="indigo"
            class="headline font-weight-light white--text"
        >
          {{ item.name.charAt(0) }}
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title v-text="item.name"></v-list-item-title>
<!--          <v-btn-->
<!--          @click="$emit('add',item.id)"-->
<!--          >Add Friend</v-btn>-->
          <v-btn
          @click="$emit('view',item.id)"
          >View profile</v-btn>
        </v-list-item-content>
        <v-list-item-action>
          <v-icon>mdi-coin</v-icon>
        </v-list-item-action>
      </template>
    </v-autocomplete>
  </v-toolbar>
</template>
<script>
Vue.component("search-bar", {
  template: "#search-bar",
  data: () => ({
    isLoading: false,
    items: [],
    model: null,
    search: null,
    tab: null,
  }),
  watch: {
    model(val) {
      if (val != null) this.tab = 0
      else this.tab = null
    },
    search(val) {
      if (this.items.length > 0) return

      this.isLoading = true

      fetch('/api/user')
          .then(res => res.clone().json())
          .then(res => {
            this.items = res.filter(item => item.id !== this.$javalin.state.userDetails.user_id)
          })
          .catch(err => {
            console.log(err)
          })
          .finally(() => (this.isLoading = false))
    },
  },
  methods:{

  }
});
</script>
<style>
#search-bar{
  height: 10px;
}
</style>