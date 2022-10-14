package org.example;

import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpHeader;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TwitterAccessManager implements AccessManager {

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<RouteRole> routeRoles) throws Exception {

        String accessToken = ctx.sessionAttribute("accessToken");
        String idToken = ctx.sessionAttribute("idToken");
        if (StringUtils.isEmpty(ctx.header(HttpHeader.AUTHORIZATION.asString()))) {
            if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(idToken)) {
                if (!ctx.path().startsWith("/login") && !ctx.path().startsWith("/callback")) {
                    ctx.redirect("/login?redirectPath=" + ctx.path(), HttpCode.TEMPORARY_REDIRECT.getStatus());
                }
            }
        }

        handler.handle(ctx);
    }
}
