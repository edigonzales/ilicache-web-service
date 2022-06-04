package ch.so.agi.ilicache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
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
