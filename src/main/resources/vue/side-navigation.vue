<template id="side-navigation">
  <v-container fluid>
    <v-row
        dense
        class="d-flex"
        :class="{'flex-column': $vuetify.breakpoint.mdAndDown}"
    >
      <v-col
          cols="12"
          md="2"
          :class="{'pr-3': $vuetify.breakpoint.mdAndUp}">
        <v-card class="mx-auto" outlined>
          <v-list flat class="py-0">
            <v-list-item-group v-model="selectedItemIndex" color="primary">
              <v-list-item v-for="(item, i) in items" :key="i" @click="redirectTo([item.path].flat()[0], i)"
                           active-class="active-navigation" :class="activate(item, i)">
                <v-list-item-icon class="mr-5">
                  <slot name="icon" v-if="slotExists('icon')" :selected-item="item"></slot>
                  <v-icon v-else v-text="item.icon"></v-icon>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title v-text="item.text"></v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-card>
      </v-col>
      <v-col cols="12" md="10">
        <slot :selected-item="selectedItem"></slot>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
Vue.component("side-navigation", {
  template: "#side-navigation",
  props: {
    menuLinks: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      selectedItemIndex: null,
      selectedItem: null,
      items: this.menuLinks
    }
  },
  created() {
    let idx = this.items.findIndex(item => item.path === window.location.pathname);
    if (idx !== -1) {
      this.selectedItemIndex = idx;
      this.selectedItem = this.items[idx];
    } else {
      this.selectedItemIndex = 0;
      this.selectedItem = this.items[0];
    }
  },
  methods: {
    redirectTo(path, i) {
      window.location.assign(path);
    },
    activate(item, index) {
      if (window.location.pathname === encodeURI(item.path)) {
        this.selectedItemIndex = index
        this.selectedItem = item
        return 'active-navigation'
      }
      return 'inactive-navigation'
    },
    slotExists(slotName) {
      return !!this.$slots[slotName] || !!this.$scopedSlots[slotName]
    }
  }
});
</script>

<style>
.remove-border-right {
  border-right: none !important;
}

.active-navigation {
  border-left: 6px solid #102338 !important;
  background-color: rgba(16, 35, 56, 0.05);
}

.inactive-navigation {
  border-left: 6px solid transparent;
}
</style>