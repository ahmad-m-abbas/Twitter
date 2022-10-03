package org.example.provider;


import org.example.Configuration;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class JdbiProvider {

    private static JdbiProvider instance = instance();
    private Jdbi jdbi = null;

    private JdbiProvider() {
    }

    public void init() {
        if (jdbi != null) {
            jdbi = null;
        }
        jdbi = Jdbi.create(
                Configuration.instance().getDbUrl(),
                Configuration.instance().getDbUser(),
                Configuration.instance().getDbPassword()
        );
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    public static JdbiProvider instance() {
        if (instance == null) {
            instance = new JdbiProvider();
        }
        return instance;
    }

    public Jdbi jdbi() {
        return jdbi;
    }
}
