package ch.so.agi.ilicache.config;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Autowired
    private UserConfig userConfig;

    @Value("${app.liveRepoCloneDirectoryName}")
    private String liveRepoCloneDirectoryName;

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
   
    @Override 
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String mirrorResourceLocation = Paths.get(userConfig.getCloneDirectory(), liveRepoCloneDirectoryName).toFile().getAbsolutePath();
        // File.separator ist notwendig, sonst funktioniert es nicht.
        // Es ist wohl sinnvoll dem Repo einen Kontext zu geben, also z.B. "repo". Sonst gibt es
        // ein Durcheinander mit vielem anderem.
        registry.addResourceHandler("/repo/**").addResourceLocations("file:"+mirrorResourceLocation + File.separator).setCachePeriod(3000);
    }    
}
