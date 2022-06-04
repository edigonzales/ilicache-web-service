package ch.so.agi.ilicache.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PreDestroy;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Autowired
    private UserConfig userConfig;

    private ServerRuntime cayenneRuntime;
    
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
        
    @Bean
    public ObjectContext objectContext() throws IOException {
        File dbFile = Paths.get(userConfig.getIlicachedb() + ".mv.db").toFile();
        InputStream resource = new ClassPathResource("ilicachedb.mv.db").getInputStream();
        Files.copy(resource, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);   
        
        ServerRuntime cayenneRuntime = ServerRuntime.builder()
                .url("jdbc:h2:"+new File(userConfig.getIlicachedb()).getAbsolutePath())
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
   
    // WARUM????
    // Directory listing funktioniert so nicht. Ist es überhaupt notwendig?
    // Umweg via MyTomcatWebServerCustomizer
//    @Override 
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        System.out.println(tmpdir);
//        registry.addResourceHandler("/clone/**").addResourceLocations("file:"+userProperties.getCloneDirectory()+File.separator+"live"+File.separator);
//    }
    
}
