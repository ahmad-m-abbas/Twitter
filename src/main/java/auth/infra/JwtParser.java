package auth.infra;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.example.Configuration;

public class JwtParser {

    private static JwtParser instance = null;
    private final io.jsonwebtoken.JwtParser parser;

    private JwtParser() {
        parser = Jwts.parserBuilder()
                .setSigningKey(Configuration.instance().getAuth0ClientSecret().getBytes())
                .build();
    }

    public static JwtParser instance() {
        if (instance == null) {
            instance = new JwtParser();
        }

        return instance;
    }

    public Jws<Claims> parseClaimsJws(String idToken) {
        return parser.parseClaimsJws(idToken);
    }
}