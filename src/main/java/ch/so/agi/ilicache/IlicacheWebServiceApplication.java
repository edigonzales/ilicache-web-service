package ch.so.agi.ilicache;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import ch.so.agi.ilicache.cayenne.Clonerepository;
import ch.so.agi.ilicache.cayenne.Peerrepository;

@SpringBootApplication
public class IlicacheWebServiceApplication {
    @Autowired
    AppProperties appProperties;
    
    @Autowired
    UserProperties userProperties;

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
            //TODO: Immer überschreiben?
            File dbFile = Paths.get(appProperties.getIlicachedb()).toFile();
            InputStream resource = new ClassPathResource("ilicachedb.mv.db").getInputStream();
            Files.copy(resource, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            List<String> cloneRepositories = userProperties.getCloneRepositories();
            for (String repository : cloneRepositories) {
                Clonerepository cloneRepository = objectContext.newObject(Clonerepository.class);
                cloneRepository.setUrl(repository);
                cloneRepository.setAname(repository.substring(repository.indexOf("/")+2));
                objectContext.commitChanges();
            }
            
            List<String> peerRepositories = userProperties.getPeerRepositories();
            for (String repository : peerRepositories) {
                Peerrepository peerRepository = objectContext.newObject(Peerrepository.class);
                peerRepository.setUrl(repository);
                peerRepository.setAname(repository.substring(repository.indexOf("/")+2));
                objectContext.commitChanges();
            }
            
            if (userProperties.isCloneOnStartup()) {
                cloneService.cloneRepositories();
            } 
        };
    }
}
