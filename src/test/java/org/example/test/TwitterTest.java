package org.example.test;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Role;
import org.example.TwitterApp;
import org.example.infra.App;
import org.example.provider.JdbiProvider;
import org.example.Configuration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY;


public class TwitterTest extends BaseTest<Role> {

    protected static DB mariadb;

    @BeforeClass
    public static void prepareEnvironment() throws IOException, ManagedProcessException {
        System.setProperty(DEFAULT_LOG_LEVEL_KEY, "ERROR");
        if (mariadb == null) {

            mariadb = DB.newEmbeddedDB(DBConfigurationBuilder.newBuilder()
                    .setPort(0)
                    .setBaseDir(String.format("/tmp/%s", RandomStringUtils.randomAlphabetic(16)))
                    .build());
            mariadb.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        mariadb.stop();
                    } catch (ManagedProcessException ex) {
                        Logger.getLogger(TwitterTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    mariadb = null;
                }
            });
        }
        Configuration marketplaceConfig = Configuration.instance();
        marketplaceConfig.setDbUrl("jdbc:mariadb://127.0.0.1:" + mariadb.getConfiguration().getPort() + "/test");
        marketplaceConfig.setDbUser("root");
        marketplaceConfig.setDbPassword("");
        marketplaceConfig.setAuth0ClientSecret("idk");
        marketplaceConfig.setAuth0ClientId("idk");
        marketplaceConfig.setAuth0Domain("idk");
        marketplaceConfig.setRequiredScope("idk");
    }

    @AfterClass
    public static void teardownEnvironment() {
    }

    @Before
    public void clearDatabase() {
        JdbiProvider.instance().init();
        JdbiProvider.instance().jdbi().useHandle(h -> {
            h.execute("DROP DATABASE test");
            h.execute("CREATE DATABASE test");
        });
    }

    @Override
    protected void insertUserIntoDatabase(String userId, String name,String userEmail) {
        JdbiProvider.instance().jdbi().useHandle(h -> {
            h.execute("INSERT IGNORE INTO `user` (id,name, email) VALUES (?, ?, ?)", userId, name,userEmail);
        });
    }

    @Override
    protected App generateJavalinApp() {
        return new TwitterApp();
    }

}
