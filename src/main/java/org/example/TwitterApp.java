package org.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.example.provider.JdbiProvider;
import org.flywaydb.core.Flyway;

import java.util.Map;

import static java.rmi.server.LogStream.log;


public class TwitterApp implements App{
    Javalin javalin;
    TwitterAccessManager instance = null;
    public TwitterApp(){
        instance=new TwitterAccessManager();
    }

    @Override
    public void init() {
        init(8080);

    }
    public void JavalinConfig(int port){
        this.javalin = Javalin.create(config -> {
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
}
