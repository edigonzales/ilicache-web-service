package ch.so.agi.ilicache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.fubar.Clonerepository;

import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.xtf.XtfWriter;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxWriter;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppProperties appProperties;
    
    @Autowired
    CloneService cloneService;
    
    @Autowired
    ObjectContext objectContext;

    @GetMapping("/ping")
    public ResponseEntity<String> ping()  {
        return new ResponseEntity<String>("ilicache-web-service", HttpStatus.OK);
    }
    
    @GetMapping("/properties")
    public ResponseEntity<?> properties()  {
        return ResponseEntity.ok().body(appProperties);
    }
    
    @GetMapping("ilisite.xml")
    public ResponseEntity<?> ilidata() throws IOException, IoxException {
        String ILI_TOPIC="IliSite09.SiteMetadata";
        String BID="IliSite09.SiteMetadata";

        TransferDescription td = null;
        try {
            td = getTransferDescription();
        } catch (Ili2cException e) {
            throw new IllegalStateException(e);
        }

        Path tempDirWithPrefix = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "ilicache-web-service");
        File outputFile = Paths.get(tempDirWithPrefix.toFile().getAbsolutePath(), "ilisite.xml").toFile();
        IoxWriter ioxWriter = new XtfWriter(outputFile, td);
        ioxWriter.write(new ch.interlis.iox_j.StartTransferEvent("ilicache-web-service", "", null));
        ioxWriter.write(new ch.interlis.iox_j.StartBasketEvent(ILI_TOPIC,BID));

        // TODO expose to AppProperties
        Iom_jObject iomRootObj = new Iom_jObject(ILI_TOPIC+".Site", String.valueOf(1));
        iomRootObj.setattrvalue("Name", "_ilicache-web-service_");
        iomRootObj.setattrvalue("Title", "_Title_");
        iomRootObj.setattrvalue("shortDescription", "_shortDescription_");
        iomRootObj.setattrvalue("Owner", "_https://www.example.com_");        
        iomRootObj.setattrvalue("technicalContact", "_mailto:agi@bd.so.ch_");        
        iomRootObj.setattrvalue("furtherInformation", "_furtherInformation_");        
        
        
        List<Clonerepository> repos = ObjectSelect.query(Clonerepository.class).select(objectContext);
        for (Clonerepository repo: repos) {
            log.info("****"+repo.getUrl());
        }
        
        List<String> repositories = appProperties.getCloneRepositories();
        for (String repository : repositories) {
            Iom_jObject cacheSite = new Iom_jObject("IliSite09.RepositoryLocation_", null);
            String repositoryName = repository.substring(repository.indexOf("/")+2);
            String value = ServletUriComponentsBuilder.fromCurrentContextPath().pathSegment(repositoryName).build().toUriString();
            cacheSite.setattrvalue("value",  value);
            iomRootObj.addattrobj("subsidiarySite", cacheSite);
        }
        
        // TODO eventuell parentSite bei uns löschen? Damit wird sicher nicht in den anderen gesucht.
        List<String> peerRepositories = appProperties.getPeerRepositories();
        for (String repository : peerRepositories) {
            Iom_jObject peerSite = new Iom_jObject("IliSite09.RepositoryLocation_", null);
            peerSite.setattrvalue("value",  repository);
            // TODO peerSite scheint nicht zu funktionieren.
            // https://github.com/claeis/ili2c/issues/63
            iomRootObj.addattrobj("subsidiarySite", peerSite);
        }
        
        ioxWriter.write(new ch.interlis.iox_j.ObjectEvent(iomRootObj));   

        ioxWriter.write(new ch.interlis.iox_j.EndBasketEvent());
        ioxWriter.write(new ch.interlis.iox_j.EndTransferEvent());
        ioxWriter.flush();
        ioxWriter.close();

        InputStream is = new FileInputStream(outputFile);
        return ResponseEntity.ok().header("Content-Type", "application/xml; charset=utf-8")
                .contentLength(outputFile.length())
                .body(new InputStreamResource(is));
    }
    
    // TODO move to AppConfig?
    private TransferDescription getTransferDescription() throws Ili2cException, IOException {
        String ILISITE09 = "IliSite09-20091119.ili";
        
        String tmpdir = System.getProperty("java.io.tmpdir");
        File iliSiteFile = Paths.get(tmpdir, ILISITE09).toFile();
        InputStream resource = new ClassPathResource(ILISITE09).getInputStream();
        Files.copy(resource, iliSiteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        ArrayList<String> filev = new ArrayList<>(List.of(iliSiteFile.getAbsolutePath())); 
        ch.interlis.ili2c.metamodel.TransferDescription td = Ili2c.compileIliFiles(filev, null);

        if (td == null) {
            throw new IllegalArgumentException("INTERLIS compiler failed"); 
        }

        return td;
    }

}
