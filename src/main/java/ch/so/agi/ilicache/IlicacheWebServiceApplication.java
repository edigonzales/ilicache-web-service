package ch.so.agi.ilicache;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.cayenne.ObjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import ch.so.agi.ilicache.cayenne.Clonerepository;
import ch.so.agi.ilicache.cayenne.Peerrepository;
import ch.so.agi.ilicache.config.UserConfig;
import ch.so.agi.ilicache.service.CloneService;

@SpringBootApplication
public class IlicacheWebServiceApplication {    
    @Autowired
    UserConfig userConfig;

    @Autowired
    ObjectContext objectContext;
    
    @Autowired 
    CloneService cloneService;

	public static void main(String[] args) {
		SpringApplication.run(IlicacheWebServiceApplication.class, args);
	}
	
    @Bean
    public CommandLineRunner init() {
        return args -> {
            // Datenbank wird in AppConfig beim Erstellen des ObjectContexts
            // in das definierte Verzeichnis kopiert.
            
            String cloneRepositories = userConfig.getCloneRepositories();
            for (String repository : cloneRepositories.split(",")) {
                Clonerepository cloneRepository = objectContext.newObject(Clonerepository.class);
                cloneRepository.setUrl(repository);
                cloneRepository.setAname(repository.substring(repository.indexOf("/")+2));
                objectContext.commitChanges();
            }
            
            // Peer Repositories funktionieren mit ilitools irgendwie nicht.
            // https://github.com/claeis/ili2c/issues/63
            // Als subsidiarySite?
//            List<String> peerRepositories = userProperties.getPeerRepositories();
//            for (String repository : peerRepositories) {
//                Peerrepository peerRepository = objectContext.newObject(Peerrepository.class);
//                peerRepository.setUrl(repository);
//                peerRepository.setAname(repository.substring(repository.indexOf("/")+2));
//                objectContext.commitChanges();
//            }
            
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
