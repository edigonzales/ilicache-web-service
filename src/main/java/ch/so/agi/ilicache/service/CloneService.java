package ch.so.agi.ilicache.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import ch.interlis.ili2c.CheckReposIlis;
import ch.interlis.ili2c.CloneRepos;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.xtf.XtfWriter;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxWriter;
import ch.so.agi.ilicache.config.UserConfig;
import ch.so.agi.ilicache.config.UserConfig.IliSite;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

@Service
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class CloneService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserConfig userConfig;
    
//    @Autowired
//    ObjectContext objectContext;
    
    @Autowired
    @Qualifier("ilisite")
    TransferDescription tdIliSite;
    
    @Value("${app.stageRepoCloneDirectoryName}")
    private String stageRepoCloneDirectoryName;

    @Value("${app.liveRepoCloneDirectoryName}")
    private String liveRepoCloneDirectoryName;
            
//    public CloneService() {
//        super();
//        throw new NullPointerException();
//    }
    
    // TODO: check cron syntax. where? Oder was passiert? Kann man mit der Exception was anfangen?
    // Siehe Konstruktor. Aber wie transportiert man das dem Benutzer sinnvoll? 
    // Ah -> das wäre was für einen Health Endpoint.
    @Scheduled(cron = "${user.cloneCronExpression}")
    public void cloneRepositories() {
        log.info("cloning repositories");
        String cloneRepositories = userConfig.getCloneRepositories();
//        for (String repository : cloneRepositories.split(",")) {
//            cloneRepository(repository);
//        }
        cloneRepository(cloneRepositories);
    }
    
    // TODO: names (methode und parameter)
    private void cloneRepository(String repositoryUrl) {
        log.debug("cloning: " + repositoryUrl);
        
        try {
//            URI uri = new URI(repositoryUrl);
//            String repository = uri.getPath();
//            if (repository.endsWith("/")) {
//                repository = repository.substring(0, repository.length()-1);
//            }
            
            String rootCloneDirectory = userConfig.getCloneDirectory();
            log.debug("rootCloneDirectory: " + rootCloneDirectory);
    
            String stageRepoCloneDirectory = Paths.get(rootCloneDirectory, stageRepoCloneDirectoryName, "fubar").toFile().getAbsolutePath();
            String liveRepoCloneDirectory = Paths.get(rootCloneDirectory, liveRepoCloneDirectoryName, "fubar").toFile().getAbsolutePath();
                      
            LocalDateTime now = LocalDateTime.now();

            FileUtils.deleteDirectory(new File(stageRepoCloneDirectory));
            
            UserSettings settings = new UserSettings();
            Configuration config = new Configuration();    
            for (String repository : repositoryUrl.split(",")) {
                FileEntry file = new FileEntry(repository, FileEntryKind.ILIMODELFILE);
                config.addFileEntry(file);
            }
            // CloneRepos-Klasse erstellt Verzeichnis, falls es nicht vorhanden ist.
            config.setOutputFile(new File(stageRepoCloneDirectory).getAbsolutePath());
            boolean failed = new CloneRepos().cloneRepos(config, settings);
            
            if (!failed) {
                if (new File(liveRepoCloneDirectory).exists()) {
                    log.debug("cleaning live directory: " + liveRepoCloneDirectory);
                    FileUtils.cleanDirectory(new File(liveRepoCloneDirectory));
                } else {
                    boolean created = new File(liveRepoCloneDirectory).mkdirs();
                    if (!created) {
                        throw new IOException("could not create live directory for model " + repositoryUrl);
                    }
                }
                
                String ILI_TOPIC="IliSite09.SiteMetadata";
                String BID="IliSite09.SiteMetadata";

                TransferDescription td = tdIliSite;

                File outputFile = Paths.get(stageRepoCloneDirectory, "ilisite.xml").toFile();
                IoxWriter ioxWriter = new XtfWriter(outputFile, td);
                ioxWriter.write(new ch.interlis.iox_j.StartTransferEvent("ilicache-web-service", "", null));
                ioxWriter.write(new ch.interlis.iox_j.StartBasketEvent(ILI_TOPIC,BID));

                Iom_jObject iomRootObj = new Iom_jObject(ILI_TOPIC+".Site", String.valueOf(1));
                String repoName = "Mirror of " + repositoryUrl;
                if (repoName.length() >= 50) repoName = repoName.substring(0, 50);
                iomRootObj.setattrvalue("Name", repoName);

                // TODO: use shortDescription for listing all clone repos
                
                ioxWriter.write(new ch.interlis.iox_j.ObjectEvent(iomRootObj));   

                ioxWriter.write(new ch.interlis.iox_j.EndBasketEvent());
                ioxWriter.write(new ch.interlis.iox_j.EndTransferEvent());
                ioxWriter.flush();
                ioxWriter.close();                
                
                log.debug("copying from stage to live");
                FileUtils.copyDirectory(new File(stageRepoCloneDirectory), new File(liveRepoCloneDirectory));
                
                // TODO: In ein (Memory)-Object schreiben
//                repository.setLastsuccessfulrun(now);
            }            
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (IoxException e) {
            e.printStackTrace();
            log.error(e.getMessage());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            log.error(e.getMessage());
        } finally {
            // TODO: In ein (Memory)-Object schreiben
//            repository.setLastrun(now);
//            objectContext.commitChanges();
        }
    }
}
