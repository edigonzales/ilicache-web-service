package ch.so.agi.ilicache;

import java.io.File;

import javax.annotation.PreDestroy;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {
    @Autowired
    UserProperties userProperties;
    
//    @Autowired
//    MyTomcatWebServerCustomizer tomcatServerCustomizer;
    
    ServerRuntime cayenneRuntime;
    
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
    
    @Bean
    public ObjectContext objectContext() {
        cayenneRuntime = ServerRuntime.builder()
                .url("jdbc:h2:/Users/stefan/tmp/ilicache/ilicachedb")
                .jdbcDriver("org.h2.Driver")
                .addConfig("cayenne/cayenne-project.xml")
                .build();
        
        ObjectContext context = cayenneRuntime.newContext();
        
        return context;
    }
    
    @PreDestroy
    public void shutdownCayenne() {
        cayenneRuntime.shutdown();
    }
    
    // Directory listing funktioniert so nicht. Ist es überhaupt notwendig?
    // Umweg via MyTomcatWebServerCustomizer
//    @Override 
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        System.out.println(tmpdir);
//        registry.addResourceHandler("/clone/**").addResourceLocations("file:"+userProperties.getCloneDirectory()+File.separator+"live"+File.separator);
//    }
    
}
