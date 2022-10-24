package org.example;

import org.example.exceptions.UsedEmailException;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.rendering.vue.JavalinVue;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.example.dto.UserDto;
import org.example.provider.JdbiProvider;
import org.example.service.UserService;
import org.flywaydb.core.Flyway;

import java.util.HashMap;
import java.util.Map;

import static org.example.infra.Auth0Plugin.useAuth0Plugin;


public class TwitterApp implements App {
    Javalin javalin;
    TwitterAccessManager instance = null;

    public TwitterApp() {
        instance = new TwitterAccessManager();
    }

    @Override
    public void init() {
        init(8080);

    }

    public void JavalinConfig(int port) {
        this.javalin = Javalin.create(config -> {

            config.jsonMapper(new JavalinJackson());
            config.addStaticFiles(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/";
                staticFiles.location = Location.CLASSPATH;
                staticFiles.precompress = false;
                staticFiles.aliasCheck = null;
                staticFiles.skipFileFunction = req -> false;
            });
            config.enableWebjars();

            config.server(() -> {
                Server server = new Server(port);
                HttpConfiguration httpConfig = new HttpConfiguration();
                httpConfig.addCustomizer(new org.eclipse.jetty.server.ForwardedRequestCustomizer());
                HttpConnectionFactory connectionFactory = new HttpConnectionFactory(httpConfig);
                ServerConnector connector = new ServerConnector(server, connectionFactory);
                connector.setPort(port);
                server.setConnectors(new ServerConnector[]{connector});
                return server;
            });
            config.accessManager(AccessManagerProvider.instance().accessManager());
            useAuth0Plugin(config, (auth0config) -> {
                auth0config.setClientId(Configuration.instance().getAuth0ClientId());
                auth0config.setClientSecret(Configuration.instance().getAuth0ClientSecret());
                auth0config.setDomain(Configuration.instance().getAuth0Domain());
                auth0config.setScopes(Configuration.instance().getRequiredScope());
                auth0config.setAuth0Callback((token, jwsClaims) -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(jwsClaims.getSubject());
                    userDto.setEmail(jwsClaims.get("email", String.class));
                    userDto.setName(jwsClaims.get("name", String.class));
                    if (UserService.instance().findUser(jwsClaims.getSubject()) == null) {
                        try {
                            UserService.instance().addUser(userDto);
                        } catch (UsedEmailException e) {
                            throw new RuntimeException(e);
                        }
                    }
//                    UserService.instance().updateUser(jwsClaims.getSubject(), userDto);
                });
            });
            config.accessManager(instance);
        });

        this.javalin.exception(Exception.class, (ex, ctx) -> {
            ex.printStackTrace();
            ctx.status(500);
        });

        this.javalin.routes(new Routes());
    }

    @Override
    public void init(int port) {
        setupFlyway();
        JavalinConfig(port);
        setupJdbi();
        setupJavalinVue();
    }

    private void setupJdbi() {
        JdbiProvider.instance().init();
    }

    private void setupFlyway() {
        Flyway flyway = Flyway.configure().dataSource(
                Configuration.instance().getDbUrl(),
                Configuration.instance().getDbUser(),
                Configuration.instance().getDbPassword()
        ).load();

        flyway.migrate();
    }

    @Override
    public void start() {
        javalin.start();
    }

    @Override
    public Javalin getJavalin() {
        return this.javalin;
    }

    private void setupJavalinVue() {
        if (!StringUtils.isEmpty(System.getProperty("production"))) {
            JavalinVue.rootDirectory(config -> {
                config.externalPath("vue");
            });
        }
        JavalinVue.stateFunction = ctx -> {
            HashMap<String, String> userDetails = new HashMap<>();
            userDetails.put("user_id", ctx.sessionAttribute("user_id"));
            userDetails.put("email", ctx.sessionAttribute("email"));

            return Map.of("userDetails", userDetails);
        };

    }
}
