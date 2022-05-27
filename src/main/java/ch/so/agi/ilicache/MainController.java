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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom_j.Iom_jObject;
import ch.interlis.iom_j.xtf.XtfWriter;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxWriter;
import ch.so.agi.ilicache.UserConfig.IliSite;
import ch.so.agi.ilicache.cayenne.Clonerepository;
import ch.so.agi.ilicache.cayenne.Peerrepository;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserConfig userConfig;
        
    @Autowired
    ObjectContext objectContext;
    
    @Autowired
    @Qualifier("ilisite")
    TransferDescription tdIliSite;

    @GetMapping("/ping")
    public ResponseEntity<?> ping()  {
        //return new ResponseEntity<String>("ilicache-web-service", HttpStatus.OK);
        return ResponseEntity.ok().body(userConfig);
    }
    
    @GetMapping("/config")
    public ResponseEntity<?> properties()  {
        System.out.println("hallo welt.");
        return ResponseEntity.ok().body(userConfig);
    }
    
    @GetMapping("ilisite.xml")
    public ResponseEntity<?> ilisite() throws IOException, IoxException {
        String ILI_TOPIC="IliSite09.SiteMetadata";
        String BID="IliSite09.SiteMetadata";

        TransferDescription td = tdIliSite;

        Path tempDirWithPrefix = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "ilicache-web-service");
        File outputFile = Paths.get(tempDirWithPrefix.toFile().getAbsolutePath(), "ilisite.xml").toFile();
        IoxWriter ioxWriter = new XtfWriter(outputFile, td);
        ioxWriter.write(new ch.interlis.iox_j.StartTransferEvent("ilicache-web-service", "", null));
        ioxWriter.write(new ch.interlis.iox_j.StartBasketEvent(ILI_TOPIC,BID));

        Iom_jObject iomRootObj = new Iom_jObject(ILI_TOPIC+".Site", String.valueOf(1));
        IliSite iliSite = userConfig.getIliSite();
        iomRootObj.setattrvalue("Name", iliSite.getName());
        iomRootObj.setattrvalue("Title", iliSite.getTitle());
        iomRootObj.setattrvalue("shortDescription", iliSite.getShortDescription());
        iomRootObj.setattrvalue("Owner", iliSite.getOwner());        
        iomRootObj.setattrvalue("technicalContact", iliSite.getTechnicalContact());        
        iomRootObj.setattrvalue("furtherInformation", iliSite.getFurtherInformation());        
        
        // TODO: Bedingung stimmt nicht. Wenn wir eine Restarten kann der Clone ja weg sein aber trotzdem
        // ein Datum vorhanden sein. Am ehesten gilt (zusätzlich) die Bedingung, dass das Verzeichnis 
        // nicht leer sein darf. D.h. z.B. ilisite.xml und ilimodels.xml
        List<Clonerepository> cloneRepositories = ObjectSelect.query(Clonerepository.class).where(Clonerepository.LASTSUCCESSFULRUN.isNotNull()).select(objectContext);
        for (Clonerepository repository : cloneRepositories) {
            Iom_jObject cloneSite = new Iom_jObject("IliSite09.RepositoryLocation_", null);
            String repositoryName = repository.getUrl().substring(repository.getUrl().indexOf("/")+2);
            String value = ServletUriComponentsBuilder.fromCurrentContextPath().pathSegment("clone").pathSegment(repositoryName).build().toUriString();
            cloneSite.setattrvalue("value",  value);
            iomRootObj.addattrobj("subsidiarySite", cloneSite);
        }
        
        // TODO eventuell parentSite bei uns löschen? Damit wird sicher nicht in den anderen gesucht.
        List<Peerrepository> peerRepositories = ObjectSelect.query(Peerrepository.class).select(objectContext);
        for (Peerrepository repository : peerRepositories) {
            Iom_jObject peerSite = new Iom_jObject("IliSite09.RepositoryLocation_", null);
            peerSite.setattrvalue("value",  repository.getUrl());
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
}
