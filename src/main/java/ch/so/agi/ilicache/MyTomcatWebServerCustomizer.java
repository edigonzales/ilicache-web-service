package ch.so.agi.ilicache;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import ch.so.agi.ilicache.config.UserConfig;

@Component
public class MyTomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>  {
    
    @Autowired
    UserConfig userConfig;
    
    @Value("${app.liveRepoCloneDirectoryName}")
    private String liveRepoCloneDirectoryName;

    /*
     * Achtung: Low level Tomcat Konfiguration, damit das Directory-Listing
     * verwendet werden kann. 
     * Andere statische (Spring Boot) Ressourcen funktionieren immer
     * noch (z.B. "src/main/resources/static").
     */
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        TomcatContextCustomizer tomcatContextCustomizer = new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                String childFolder = liveRepoCloneDirectoryName;
                context.setDocBase(userConfig.getCloneDirectory());                
                Wrapper defServlet = (Wrapper) context.findChild("default");
                defServlet.addInitParameter("listings", "true");
                defServlet.addInitParameter("sortListings", "true");
                defServlet.addInitParameter("sortDirectoriesFirst", "true");
                defServlet.addInitParameter("readOnly", "true");
                defServlet.addInitParameter("contextXsltFile", "/listing.xsl");
                defServlet.addMapping("/"+childFolder+"/*");                
            }
        };
        factory.addContextCustomizers(tomcatContextCustomizer);        
    }
}
