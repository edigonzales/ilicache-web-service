package ch.so.agi.ilicache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class AppProperties {
    private String ilicachedb;

    public String getIlicachedb() {
        return ilicachedb;
    }

    public void setIlicachedb(String ilicachedb) {
        this.ilicachedb = ilicachedb;
    }
}
