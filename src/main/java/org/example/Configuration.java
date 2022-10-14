package org.example;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Configuration {

    private static Configuration instance = null;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private String auth0ClientId;
    private String auth0ClientSecret;
    private String auth0Domain;
    private String requiredScope;


    public static Configuration instance() {
        if (instance == null) {
            File file = new File("appconfig.yml");
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
        }
        return instance;
    }
}
