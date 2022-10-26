package org.example.test;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.infra.App;
import org.example.provider.JdbiProvider;
import org.example.test.BaseTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;


public class TwitterTest extends BaseTest<Role> {
    protected static MariaDBContainer mariadbContainer;

    @BeforeClass
    public static void prepareEnvironment() {
        mariadbContainer = new MariaDBContainer<>(DockerImageName.parse(Configuration.instance().getMariaDBContainer()))
                .withUsername(RandomStringUtils.randomAlphabetic(15))
                .withPassword(RandomStringUtils.randomAlphabetic(15))
                .withDatabaseName("test");
        mariadbContainer.start();
        org.example.Configuration portalConfig = org.example.Configuration.instance();
        portalConfig.setDbUrl(mariadbContainer.getJdbcUrl());
        portalConfig.setDbUser(mariadbContainer.getUsername());
        portalConfig.setDbPassword(mariadbContainer.getPassword());
        portalConfig.setAuth0ClientSecret("idk");
        portalConfig.setAuth0ClientId("idk");
        portalConfig.setAuth0Domain("idk");
        portalConfig.setRequiredScope("idk");
    }

    @AfterClass
    public static void teardownEnvironment() {
        mariadbContainer.stop();
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
            h.execute("INSERT IGNORE INTO `user` (id, name,email) VALUES (?,?, ?)", userId, name,userEmail);
        });
    }

    @Override
    protected App generateJavalinApp() {
        return new TwitterApp();
    }


}
