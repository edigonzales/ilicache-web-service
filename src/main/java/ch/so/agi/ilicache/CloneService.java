package ch.so.agi.ilicache;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import ch.interlis.ili2c.CheckReposIlis;
import ch.interlis.ili2c.CloneRepos;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.gui.UserSettings;
import ch.so.agi.ilicache.cayenne.Clonerepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class CloneService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserProperties userProperties;
    
    @Autowired
    ObjectContext objectContext;
    
    // :/Users/stefan/tmp/ilicache/ java.io.tmpdir
    //String property = "java.io.tmpdir";
    //String tempDir = System.getProperty(property);
    // .iliclone?
    
    // stage. falls erfolgreich, kopieren nach live.
    // immer einzeln, da früher oder später man eventuell nicht alle gleichzeitig clonen will
    // ein originäres ilimodels.xml (resp. ilisite.xml) zeigt dann auf die cloned repos. Müssen die Klone auf das Mutter-Repo zeigen?
    // -> mich dünkt nicht zwingend. Nur wenn ich bei einem einzelnen Clone mit Suchen beginnen würde.
    // Ah, nicht sicher, ob ein ilisite.xml zwingend ein ilimodels benötigt. Falls nicht, ginge es
    // elegant. Es gäbe nur subsidiary sites oder peer sites. Plus noch weitere (z.B. unsesr live geo.so.ch/models).
    // notfalls ein leeres ilimodels.xml
    
    // Ausprobieren (nicht viel verloren)
    // 1. ilisite.xml herstellen per Code
    // 2. ein Repo clonen und files vom Filesystem verfügbar machen.
    
    // Wie Cronjob umsetzen?
    // -> entweder 1 mal täglich (oder einstellbar) alle refreshen oder on demand einzelner Job
    // -> refreshen einzelner Repos disable-bar
    // -> komplizierter wird es falls jedes Repo unterschiedlicher Refreshzyklus hat.
    // https://github.com/edigonzales/sdi-health-check/blob/main/src/main/java/ch/so/agi/healthcheck/SdiHealthCheckApplication.java
    
    //@PostConstruct
    public void init() {
        // TODO: PostConstruct wird vor dem CommandLineRunner ausgeführt. Somit macht es nix mehr (bei keiner oder leerer DB).
        log.info("* do something postconstruct");
        cloneRepositories();
    }

    public void cloneRepositories() {
        List<Clonerepository> cloneRepositories = ObjectSelect.query(Clonerepository.class).select(objectContext);
        for (Clonerepository repository : cloneRepositories) {
            cloneRepository(repository);
        }
    }
    
    public void cloneRepository(Clonerepository repository) {
        log.info("repository: " + repository);
       
        String rootCloneDirectory = userProperties.getCloneDirectory();
        String stageRepoCloneDirectory = Paths.get(rootCloneDirectory, "stage", repository.getAname()).toFile().getAbsolutePath();
        String liveRepoCloneDirectory = Paths.get(rootCloneDirectory, "live", repository.getAname()).toFile().getAbsolutePath();
        
        System.out.println(stageRepoCloneDirectory);
        
        try {
            FileUtils.deleteDirectory(new File(stageRepoCloneDirectory));
            
            UserSettings settings = new UserSettings();
            //settings.setIlidirs(UserSettings.DEFAULT_ILIDIRS);             
            Configuration config = new Configuration();        
            FileEntry file = new FileEntry(repository.getUrl(), FileEntryKind.ILIMODELFILE);
            config.addFileEntry(file);
            // CloneRepos-Klasse erstellt Verzeichnis, falls es nicht vorhanden ist.
            config.setOutputFile(new File(stageRepoCloneDirectory).getAbsolutePath());
            boolean failed = new CloneRepos().cloneRepos(config, settings);
            
            if (!failed) {
                if (new File(liveRepoCloneDirectory).exists()) {
                    FileUtils.cleanDirectory(new File(liveRepoCloneDirectory));
                } else {
                    boolean created = new File(liveRepoCloneDirectory).mkdirs();
                    if (!created) {
                        throw new IOException("could not create live directory for model " + repository.getUrl());
                    }
                }
                
                FileUtils.copyDirectory(new File(stageRepoCloneDirectory), new File(liveRepoCloneDirectory));

                
            }
            

            // TODO:
            // - update database
            // - write ilisite.xml
            
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        
        
    }
}
