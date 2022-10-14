package auth.infra;
import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.example.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static auth.infra.Sentry.useSentry;

@Log
public class Auth0Plugin implements Plugin {

    private static Auth0Plugin instance = null;
    private AuthenticationController authenticationController;
    private String callbackPath;
    private String defaultRedirectPath;
    private String loginPath;
    private String logoutPath;
    private String scopes;
    private String domain;
    private String clientId;
    private BiConsumer<Tokens, Claims> auth0Callback;
    private BiConsumer<IdentityVerificationException, Context> identityVerificationExceptionCallback;
    private io.jsonwebtoken.JwtParser parser;
    public static final void useAuth0Plugin(JavalinConfig javalinConfig, Consumer<Auth0Config> consumer) {
        Auth0Config config = new Auth0Config();
        consumer.accept(config);
        instance().init(config.getDomain(), config.getClientId(), config.getClientSecret());
        instance().callbackPath = config.getCallbackPath();
        instance().defaultRedirectPath = config.getDefaultRedirectPath();
        instance().auth0Callback = config.getAuth0Callback();
        instance().loginPath = config.getLoginPath();
        instance().logoutPath = config.getLogoutPath();
        instance().scopes = config.getScopes();
        instance().domain = config.getDomain();
        instance().clientId = config.getClientId();
        instance().identityVerificationExceptionCallback = config.identityVerificationExceptionCallback;
        instance().parser = Jwts.parserBuilder()
                .setSigningKey(config.getClientSecret().getBytes())
                .build();
        javalinConfig.registerPlugin(instance);
    }


    private Jws<Claims> parseClaimsJws(String idToken) {
        return parser.parseClaimsJws(idToken);
    }

    @Override
    public void apply(Javalin app) {
        app.post(callbackPath, this::handleAuth0Callback);
        app.get(callbackPath, this::handleAuth0Callback);
        app.get(loginPath, this::handleLoginRedirect);
        app.get(logoutPath, this::handleLogoutRedirect);
    }

    private void handleLoginRedirect(@NotNull Context ctx) {
        String redirectUri = new StringBuilder()
                .append(StringUtils.contains(ctx.host(), "localhost") ? ctx.scheme() : "https")
                .append("://")
                .append(ctx.host())
                .append("/callback")
                .append("?redirectPath=")
                .append(ctx.queryParam("redirectPath"))
                .toString();

        String authorizeUrl = this.buildAuthorizeUrl(ctx, redirectUri);
        ctx.redirect(authorizeUrl, HttpCode.TEMPORARY_REDIRECT.getStatus());
    }

    private void handleLogoutRedirect(Context ctx) {
        ctx.req.getSession().invalidate();

        String logoutUrl = this.buildLogoutUrl(ctx);
        ctx.redirect(logoutUrl, HttpCode.TEMPORARY_REDIRECT.getStatus());
    }

    private void handleAuth0Callback(Context ctx) {
        Auth0Plugin auth0Handler = Auth0Plugin.instance();

        String redirectPath = ctx.queryParam("redirectPath");
        try {
            Tokens tokens = auth0Handler.handle(ctx);
            Claims jwsClaims = parseClaimsJws(tokens.getIdToken()).getBody();
            System.out.println(jwsClaims);
            auth0Callback.accept(tokens, jwsClaims);
            ctx.sessionAttribute("accessToken", tokens.getAccessToken());
            ctx.sessionAttribute("idToken", tokens.getIdToken());
            ctx.sessionAttribute("user_id", jwsClaims.getSubject());
            ctx.sessionAttribute("name", jwsClaims.get("name",String.class));
            ctx.sessionAttribute("email", jwsClaims.get("email", String.class));
            if (StringUtils.isEmpty(redirectPath)) {
                redirectPath = defaultRedirectPath;
            }
            ctx.redirect(redirectPath, HttpCode.TEMPORARY_REDIRECT.getStatus());
        } catch (IdentityVerificationException e) {
            log.warning(String.format("Auth0 Identity Verification Error: %s", e.getMessage()));
            ctx.redirect("/login?redirectPath=" + redirectPath, HttpCode.TEMPORARY_REDIRECT.getStatus());
            identityVerificationExceptionCallback.accept(e, ctx);
        } catch (Exception e) {
            log.severe(String.format("Failed to handle auth0 callback : %s", e.getMessage()));
            useSentry(sentry -> sentry.sendException(e));
        }
    }

    @Data
    @NoArgsConstructor
    public static class Auth0Config {

        private String domain = "random";
        private String clientId = "junk";
        private String clientSecret = "test";
        private String callbackPath = "/callback";
        private String defaultRedirectPath = "/";
        private String loginPath = "/login";
        private String logoutPath = "/logout";
        private String scopes = "";
        private BiConsumer<Tokens, Claims> auth0Callback = (token, claims) -> {
        };
        private BiConsumer<IdentityVerificationException, Context> identityVerificationExceptionCallback = (ex, ctx) -> {
        };
    }

    private Auth0Plugin() {

    }

    private void init(String domain, String clientId, String clientSecret) {
        authenticationController = AuthenticationController
                .newBuilder(domain, clientId, clientSecret)
                .build();
    }

    public static Auth0Plugin instance() {
        if (instance == null) {
            instance = new Auth0Plugin();
        }

        return instance;
    }

    public String buildAuthorizeUrl(Context ctx, String redirectUri) {
        return authenticationController.buildAuthorizeUrl(ctx.req, ctx.res, redirectUri)
                .withScope(this.scopes)
                .build();
    }

    public String buildLogoutUrl(Context ctx) {
        String returnUrl = String.format("%s://%s", ctx.req.getScheme(), ctx.req.getServerName());
        int port = ctx.req.getServerPort();
        String scheme = ctx.req.getScheme();
        if (("http".equals(scheme) && port != 80) || ("https".equals(scheme) && port != 443)) {
            returnUrl += ":" + port;
        }
        returnUrl += "/";
        String logoutUrl = String.format(
                "https://%s/v2/logout?client_id=%s&returnTo=%s",
                this.domain,
                this.clientId,
                returnUrl
        );
        return logoutUrl;
    }

    public Tokens handle(Context ctx) throws IdentityVerificationException {
        return authenticationController.handle(ctx.req, ctx.res);
    }
}
