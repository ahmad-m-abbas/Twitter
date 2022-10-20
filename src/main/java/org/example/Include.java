package org.example;

import io.javalin.http.Context;
import io.javalin.plugin.rendering.vue.VueRenderer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Include extends VueRenderer {

    private static final String MAP_INCLUDES;
    private static final String COMPONENT_SETUP;

    static {
        String[] includes = {
                "<link rel=\"stylesheet\" href=\"//unpkg.com/leaflet/dist/leaflet.css\" />",
                "<script src=\"//unpkg.com/leaflet/dist/leaflet.js\"></script>",
                "<script src=\"//unpkg.com/vue2-leaflet\"></script>"};
        MAP_INCLUDES = String.join("\n", includes).concat("\n@componentRegistration");

    }

    static {
        String[] mapComponents = {
                "Vue.component('l-map', window.Vue2Leaflet.LMap);",
                "Vue.component('l-tile-layer', window.Vue2Leaflet.LTileLayer);",
                "Vue.component('l-marker', window.Vue2Leaflet.LMarker);",
                "Vue.component('l-polygon', window.Vue2Leaflet.LPolygon);",
                "Vue.component('l-popup', window.Vue2Leaflet.LPopup);"
        };
        COMPONENT_SETUP = "<body>\n<script>".concat(String.join("\n", mapComponents)).concat("</script>\n");
    }

    @Override
    public String preRender(String layout, Context ctx) {
        return layout.replace("@componentRegistration", MAP_INCLUDES)
                .replace("<body>", COMPONENT_SETUP);
    }
}
