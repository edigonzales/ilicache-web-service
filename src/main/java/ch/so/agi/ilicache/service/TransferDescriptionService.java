package ch.so.agi.ilicache.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.metamodel.TransferDescription;

@Service
public class TransferDescriptionService {
    
    @Bean
    @Qualifier("ilisite")
    public TransferDescription getIliSiteTd() throws IOException, Ili2cFailure {
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
