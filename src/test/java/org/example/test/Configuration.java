package org.example.test;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@NoArgsConstructor
@Log
public class Configuration {
    private static Configuration instance = null;
    private String mariaDBContainer;

    public static Configuration instance() {
        if (instance == null) {
            File file = new File("testconfig.yml");
            if (file.exists()) {
                ObjectMapper om = new ObjectMapper(new YAMLFactory());
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    instance = om.readValue(file, Configuration.class);
                } catch (IOException ex) {
                    Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                instance = new Configuration();
            }

            File overrideFile = new File("testconfig.override.yml");
            if (overrideFile.exists()) {
                ObjectMapper om = new ObjectMapper(new YAMLFactory());
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    Iterator<Map.Entry<String, JsonNode>> overriddenFields = om.readTree(overrideFile).fields();
                    while (overriddenFields.hasNext()) {
                        Map.Entry<String, JsonNode> field = overriddenFields.next();
                        PropertyDescriptor pd = new PropertyDescriptor(field.getKey(), Configuration.class);
                        pd.getWriteMethod().invoke(instance, field.getValue().toString());
                    }
                } catch (Exception ex) {
                    log.severe(String.format("%s: %s", Configuration.class.getName(), ex.getMessage()));
                }
            }
        }
        return instance;
    }
}
