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
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

@Service
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class CloneService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserConfig userConfig;
    
    @Autowired
    @Qualifier("ilisite")
    TransferDescription tdIliSite;
    
    @Autowired
    InfoService infoService;
    
    @Value("${app.stageRepoCloneDirectoryName}")
    private String stageRepoCloneDirectoryName;

    @Value("${app.liveRepoCloneDirectoryName}")
    private String liveRepoCloneDirectoryName;

    // Anwendung startet nicht, falls CronExpression 
    // syntaktisch falsch ist.
    @Scheduled(cron = "${user.cloneCronExpression}")
    public void cloneRepositories() {
        log.info("cloning repositories");
        String cloneRepositories = userConfig.getCloneRepositories();
        cloneRepository(cloneRepositories);
    }
    
    private void cloneRepository(String repositories) {
        log.debug("cloning: " + repositories);
        
        LocalDateTime now = LocalDateTime.now();

        try {            
            String rootCloneDirectory = userConfig.getCloneDirectory();
            log.debug("rootCloneDirectory: " + rootCloneDirectory);
    
            String stageRepoCloneDirectory = Paths.get(rootCloneDirectory, stageRepoCloneDirectoryName).toFile().getAbsolutePath();
            String liveRepoCloneDirectory = Paths.get(rootCloneDirectory, liveRepoCloneDirectoryName).toFile().getAbsolutePath();

            FileUtils.deleteDirectory(new File(stageRepoCloneDirectory));
            
            UserSettings settings = new UserSettings();
            Configuration config = new Configuration();    
            for (String repository : repositories.split(",")) {
                FileEntry file = new FileEntry(repository, FileEntryKind.ILIMODELFILE);
                config.addFileEntry(file);
            }
            // CloneRepos-Klasse erstellt Verzeichnis, falls es nicht vorhanden ist.
            config.setOutputFile(new File(stageRepoCloneDirectory).getAbsolutePath());
            log.info("start cloning...");
            long start = System.currentTimeMillis();
            boolean failed = new CloneRepos().cloneRepos(config, settings);
            log.info("cloning finished.");
            long finish = System.currentTimeMillis();
            infoService.setCloneTimeElapsedSeconds((finish - start) / 1000);

            if (!failed) {
                if (new File(liveRepoCloneDirectory).exists()) {
                    log.debug("cleaning live directory: " + liveRepoCloneDirectory);
                    FileUtils.cleanDirectory(new File(liveRepoCloneDirectory));
                } else {
                    boolean created = new File(liveRepoCloneDirectory).mkdirs();
                    if (!created) {
                        throw new IOException("could not create live directory for model " + repositories);
                    }
                }
                
                // Create ilisite.xml, which is not create by ili2c cloning method.
                String ILI_TOPIC="IliSite09.SiteMetadata";
                String BID="IliSite09.SiteMetadata";

                TransferDescription td = tdIliSite;

                File outputFile = Paths.get(stageRepoCloneDirectory, "ilisite.xml").toFile();
                IoxWriter ioxWriter = new XtfWriter(outputFile, td);
                ioxWriter.write(new ch.interlis.iox_j.StartTransferEvent("ilicache-web-service", "", null));
                ioxWriter.write(new ch.interlis.iox_j.StartBasketEvent(ILI_TOPIC,BID));

                Iom_jObject iomRootObj = new Iom_jObject(ILI_TOPIC+".Site", String.valueOf(1));
                iomRootObj.setattrvalue("Title", userConfig.getIliSite().getTitle());
                iomRootObj.setattrvalue("Name", userConfig.getIliSite().getName());
                
                String shortDescription = String.join("\n", Arrays.asList(repositories.split(",")));
                iomRootObj.setattrvalue("shortDescription", shortDescription);
                
                iomRootObj.setattrvalue("Owner", userConfig.getIliSite().getOwner());        
                iomRootObj.setattrvalue("technicalContact", userConfig.getIliSite().getTechnicalContact());        
                iomRootObj.setattrvalue("furtherInformation", userConfig.getIliSite().getFurtherInformation());        

                ioxWriter.write(new ch.interlis.iox_j.ObjectEvent(iomRootObj));   

                ioxWriter.write(new ch.interlis.iox_j.EndBasketEvent());
                ioxWriter.write(new ch.interlis.iox_j.EndTransferEvent());
                ioxWriter.flush();
                ioxWriter.close();                
                
                // Copy from stage to live directory
                log.debug("copying from stage to live");
                FileUtils.copyDirectory(new File(stageRepoCloneDirectory), new File(liveRepoCloneDirectory));
                
                infoService.setLastSuccessfulRun(now);
            }            
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (IoxException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            infoService.setLastRun(now);
        }
    }
}
