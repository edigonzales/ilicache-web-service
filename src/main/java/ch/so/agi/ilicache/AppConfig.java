package ch.so.agi.ilicache;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class AppConfig {
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
    
    @Bean
    public ObjectContext objectContext() {
        ServerRuntime cayenneRuntime = ServerRuntime.builder()
                .url("jdbc:h2:/Users/stefan/tmp/ilicache/ilicachedb")
                .jdbcDriver("org.h2.Driver")
                .addConfig("cayenne/cayenne-project.xml")
                .build();
        ObjectContext context = cayenneRuntime.newContext();
        
        return context;
    }
}
