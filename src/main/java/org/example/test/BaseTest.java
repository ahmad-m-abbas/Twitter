package org.example.test;


import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.TestCase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.example.infra.App;
import org.example.provider.AccessManagerProvider;

/**
 *
 * @param <T> the Route Role class of the project
 */
public abstract class BaseTest<T extends RouteRole> {

    /**
     * Generate a new javalin app for use in this test
     *
     * @return
     */
    protected abstract App generateJavalinApp();

    /**
     * Insert a user with these roles into a database, if needed
     *
     * @param userId the id of the user
     * @param userEmail the email of the user
     * @param userRoles the roles of the user
     */
    protected abstract void insertUserIntoDatabase(String userId, String name,String userEmail);

    /**
     * Create a new instance of the application and injects a user into all
     * requests. To be used when testing equiptal portal
     *
     * @param testCase a Javalin test case, containing the server, client, and
     * asserts
     * @param userId the ID of the user to inject into the session
     * @param userEmail the email of the user to inject into the session
     * @param name
     */
    protected void startActor(TestCase testCase, String userId, String name,String userEmail) {
        // create the javalin app
        App app = generateJavalinApp();
        AccessManagerProvider.instance().accessManager(getAccessManager(userId, name,userEmail));
        app.init(0);
        insertUserIntoDatabase(userId, name,userEmail);
        // inject the user into the database
        JavalinTest.test(app.getJavalin(), (server, client) -> {
            // inject a memory-backed cookie jar for the current test
            client.setOkHttp(new OkHttpClient.Builder().cookieJar(new TestCookieJar()).build());
            // activate the access manager to inject any session parameters
            server.get("/verify-session", (ctx) -> ctx.status(200));
            client.get("/verify-session");
            // run the actual test case
            testCase.accept(server, client);
        });
    }

    /**
     * Parse an HTTP response into a specific class
     *
     * @param response the okHttp response
     * @param clazz the class to parse the JSON into
     * @return an instance of the class
     * @param <S> the class to parse into
     * @throws RuntimeException if any JSON parsing errors happen
     */
    protected <S> S parseBody(Response response, Class<S> clazz) {
        try {
            return JavalinJackson.Companion.defaultMapper().readValue(response.body().string(), clazz);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * A Factory method for an access manager that hard codes the user ID, for
     * testing
     */
    private AccessManager getAccessManager(String userId, String name,String userEmail) {
        return (Handler handler, Context ctx, Set<RouteRole> routeRoles) -> {
            ctx.sessionAttribute("user_id", userId);
            ctx.sessionAttribute("name", name);
            ctx.sessionAttribute("email", userEmail);
            handler.handle(ctx);
        };
    }

    /**
     * A Memory-backed cookie jar for testing
     */
    private class TestCookieJar implements CookieJar {

        private List<Cookie> cookies;

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            this.cookies = cookies;
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if (cookies != null) {
                return cookies;
            }
            return new ArrayList<>();
        }
    }

}
