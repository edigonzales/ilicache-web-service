package ch.so.agi.ilicache;

import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.so.agi.ilicache.cayenne.Clonerepository;
import ch.so.agi.ilicache.cayenne.Peerrepository;

@SpringBootApplication
public class IlicacheWebServiceApplication {

    @Autowired
    AppProperties appProperties;

    @Autowired
    ObjectContext objectContext;

	public static void main(String[] args) {
		SpringApplication.run(IlicacheWebServiceApplication.class, args);
	}

    @Bean
    public CommandLineRunner init() {
        return args -> {
            //TODO ilicachedb aus resources kopieren
            
            List<String> cloneRepositories = appProperties.getCloneRepositories();
            for (String repository : cloneRepositories) {
                Clonerepository cloneRepository = objectContext.newObject(Clonerepository.class);
                cloneRepository.setUrl(repository);
                cloneRepository.setAname(repository.substring(repository.indexOf("/")+2));
                objectContext.commitChanges();
            }
            
            List<String> peerRepositories = appProperties.getPeerRepositories();
            for (String repository : peerRepositories) {
                
                Peerrepository peerRepository = objectContext.newObject(Peerrepository.class);
                peerRepository.setUrl(repository);
                peerRepository.setAname(repository.substring(repository.indexOf("/")+2));
                objectContext.commitChanges();
            }
        };
    }
}
