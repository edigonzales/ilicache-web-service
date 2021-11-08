package ch.so.agi.ilicache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.interlis.ili2c.CheckReposIlis;
import ch.interlis.ili2c.CloneRepos;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.gui.UserSettings;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class CloneService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppProperties appProperties;
    
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
    
    @PostConstruct
    public void init() {
        log.info("* do something postconstruct");
        cloneRepositories();
    }

    public void cloneRepositories() {
        List<String> cloneRepositories = appProperties.getCloneRepositories();
        for (String repository : cloneRepositories) {
            cloneRepository(repository);
        }
    }
    
    public void cloneRepository(String repository) {
        log.info("repository: " + repository);
        
        UserSettings settings = new UserSettings();
        //settings.setIlidirs(UserSettings.DEFAULT_ILIDIRS);

        // TODO: pro Repo ein Verzeichnis.
        // TODO: stage und live. falls failed=false, kopieren von stage nach live. Sonst im GUI zeigen, dass Probleme
        
        Configuration config = new Configuration();        
        FileEntry file = new FileEntry(repository, FileEntryKind.ILIMODELFILE);
        config.addFileEntry(file);
        config.setOutputFile(new File("/Users/stefan/tmp/ilicache/").getAbsolutePath());
        boolean failed = new CloneRepos().cloneRepos(config, settings);
        System.out.println(failed);
    }
}
