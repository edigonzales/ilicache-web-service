package ch.so.agi.ilicache.actuator;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import ch.so.agi.ilicache.config.UserConfig;

@Component
public class DatabaseHealthIndicator extends AbstractHealthIndicator {
    @Autowired
    private UserConfig userConfig;

    @Override
    protected void doHealthCheck(Builder builder) throws Exception {
        // TODO: Connection von ServerRuntime (Cayenne) anfordern. Das funktioniert bei mir jedoch nicht.
        // Momentan findet hier im Health-Check kein Connection Pooling statt.
        // Was ich noch nicht verstehe: Warum funktioniert es immer noch, wenn ich die H2-Datenbank auf
        // dem Filesystem lösche? Es wird sogar eine neue Datei erstellt.
        try (Connection conn = DriverManager.getConnection("jdbc:h2:"+new File(userConfig.getIlicachedb()).getAbsolutePath());
                Statement stmt = conn.createStatement()){
            stmt.execute("SELECT 1;");
            builder.up().build();
          } catch (SQLException e) {
              builder.down(e).build();
          }        
    }
}
