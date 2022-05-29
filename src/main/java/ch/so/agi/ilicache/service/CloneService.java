package ch.so.agi.ilicache.service;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
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
import ch.so.agi.ilicache.cayenne.Clonerepository;
import ch.so.agi.ilicache.config.UserConfig;
import ch.so.agi.ilicache.config.UserConfig.IliSite;

import java.io.File;
import java.io.IOException;
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
    UserConfig userProperties;
    
    @Autowired
    ObjectContext objectContext;
    
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
        List<Clonerepository> cloneRepositories = ObjectSelect.query(Clonerepository.class).select(objectContext);
        for (Clonerepository repository : cloneRepositories) {
            cloneRepository(repository);
        }
    }
    
    private void cloneRepository(Clonerepository repository) {
        log.debug("cloning: " + repository.getUrl());
        
        String rootCloneDirectory = userProperties.getCloneDirectory();
        log.debug("rootCloneDirectory: " + rootCloneDirectory);

        String stageRepoCloneDirectory = Paths.get(rootCloneDirectory, "stage", repository.getAname()).toFile().getAbsolutePath();
        String liveRepoCloneDirectory = Paths.get(rootCloneDirectory, liveRepoCloneDirectoryName, repository.getAname()).toFile().getAbsolutePath();
                  
        LocalDateTime now = LocalDateTime.now();
        
        try {
            FileUtils.deleteDirectory(new File(stageRepoCloneDirectory));
            
            UserSettings settings = new UserSettings();
            Configuration config = new Configuration();        
            FileEntry file = new FileEntry(repository.getUrl(), FileEntryKind.ILIMODELFILE);
            config.addFileEntry(file);
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
                        throw new IOException("could not create live directory for model " + repository.getUrl());
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
                String repoName = "Mirror of " + repository.getUrl();
                if (repoName.length() >= 50) repoName = repoName.substring(0, 50);
                iomRootObj.setattrvalue("Name", repoName);

                ioxWriter.write(new ch.interlis.iox_j.ObjectEvent(iomRootObj));   

                ioxWriter.write(new ch.interlis.iox_j.EndBasketEvent());
                ioxWriter.write(new ch.interlis.iox_j.EndTransferEvent());
                ioxWriter.flush();
                ioxWriter.close();                
                
                log.debug("copying from stage to live");
                FileUtils.copyDirectory(new File(stageRepoCloneDirectory), new File(liveRepoCloneDirectory));
                
                repository.setLastsuccessfulrun(now);
            }            
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (IoxException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            repository.setLastrun(now);
            objectContext.commitChanges();
        }
    }
}
