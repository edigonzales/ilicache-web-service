package ch.so.agi.ilicache;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

import ch.so.agi.ilicache.config.UserConfig;
import ch.so.agi.ilicache.service.CloneService;

//@TypeHint(types = org.apache.catalina.servlets.DefaultServlet.class, 
//                   access= {TypeAccess.DECLARED_METHODS, 
//                           TypeAccess.DECLARED_FIELDS, 
//                           TypeAccess.DECLARED_CONSTRUCTORS, 
//                           TypeAccess.PUBLIC_METHODS,
//                           TypeAccess.PUBLIC_FIELDS,
//                           TypeAccess.PUBLIC_CONSTRUCTORS,
//                           //TypeAccess.JNI,
//                           TypeAccess.QUERY_DECLARED_CONSTRUCTORS,
//                           TypeAccess.QUERY_DECLARED_METHODS,
//                           TypeAccess.QUERY_PUBLIC_CONSTRUCTORS,
//                           TypeAccess.QUERY_PUBLIC_METHODS}               
//    )
@TypeHint(
        types = {org.eclipse.jetty.security.ConstraintSecurityHandler.class, org.eclipse.jetty.servlet.DefaultServlet.class, java.lang.Byte.class, java.lang.Double.class, java.lang.Float.class},
        typeNames = {"org.eclipse.jetty.webapp.ClassMatcher$ByPackageOrName", "org.eclipse.jetty.webapp.ClassMatcher$ByLocationOrModule"}, 
        access= {TypeAccess.DECLARED_METHODS, 
              TypeAccess.DECLARED_FIELDS, 
              TypeAccess.DECLARED_CONSTRUCTORS, 
              TypeAccess.PUBLIC_METHODS,
              TypeAccess.PUBLIC_FIELDS,
              TypeAccess.PUBLIC_CONSTRUCTORS,
              //TypeAccess.JNI,
              TypeAccess.QUERY_DECLARED_CONSTRUCTORS,
              TypeAccess.QUERY_DECLARED_METHODS,
              TypeAccess.QUERY_PUBLIC_CONSTRUCTORS,
              TypeAccess.QUERY_PUBLIC_METHODS}               
)
@SpringBootApplication
public class IlicacheWebServiceApplication {    
    @Autowired
    UserConfig userConfig;
    
    @Autowired 
    CloneService cloneService;

	public static void main(String[] args) {
		SpringApplication.run(IlicacheWebServiceApplication.class, args);
	}
	
	// PostConstruct: Anwendung nicht "live live", d.h. nicht fertig hochgefahren und nicht erreichbar.
	// CommandLineRunner: Anwendung live aber nicht ready.
    @Bean
    public CommandLineRunner init() {
        return args -> {                        
            if (userConfig.isCloneOnStartup()) {
                cloneService.cloneRepositories();
            } 
            
            String LISTING_XSL = "listing.xsl";
            File listingXslFile = Paths.get(userConfig.getCloneDirectory(), LISTING_XSL).toFile();
            InputStream listingXslResource = new ClassPathResource(LISTING_XSL).getInputStream();
            Files.copy(listingXslResource, listingXslFile.toPath(), StandardCopyOption.REPLACE_EXISTING);            
        };
    }
}
