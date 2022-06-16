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

@SpringBootApplication
public class IlicacheWebServiceApplication {    
    @Autowired
    UserConfig userConfig;
    
    @Autowired 
    CloneService cloneService;

	public static void main(String[] args) {
		SpringApplication.run(IlicacheWebServiceApplication.class, args);
	}
	
	// PostConstruct: Anwendung weder live noch ready, d.h. nicht fertig hochgefahren und nicht erreichbar.
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
