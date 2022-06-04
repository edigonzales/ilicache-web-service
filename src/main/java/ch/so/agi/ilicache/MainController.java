package ch.so.agi.ilicache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ch.interlis.iox.IoxException;
import ch.so.agi.ilicache.config.UserConfig;
import ch.so.agi.ilicache.service.IlisiteService;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserConfig userConfig;

    @Autowired
    IlisiteService ilisiteService;
    
    private File ilisiteFile;
    
//    @PostConstruct
//    private void createIlisiteXml() {
//        ilisiteFile = ilisiteService.createIlisiteXml();
//    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping()  {
        return new ResponseEntity<String>("ilicache-web-service", HttpStatus.OK);
    }
    
    @GetMapping("/config")
    public ResponseEntity<?> properties()  {
        return ResponseEntity.ok().body(userConfig);
    }
    
    @GetMapping("ilisite.xml")
    public ResponseEntity<?> ilisite() throws IOException, IoxException {
        // Nein: Respektive man muss die geklonten Repos ja hier in das ilisite verlinken.
        // Aber so hat es wohl auch noch Verbesserungspotential. Man muss es nur ändern,
        // wenn die Repos, die geklont werden sollen, ändern.
        // 
//        if (ilisiteFile == null) {
//            ilisiteFile = ilisiteService.createIlisiteXml();
//        }
        File ilisiteFile = ilisiteService.createIlisiteXml();
        InputStream is = new FileInputStream(ilisiteFile);
        return ResponseEntity.ok().header("Content-Type", "application/xml; charset=utf-8")
                .contentLength(ilisiteFile.length())
                .body(new InputStreamResource(is));
    }
    
    @GetMapping("/status")
    public ResponseEntity<?> status() throws SQLException {
//        var cloneRepositories = ObjectSelect.query(Clonerepository.class).select(objectContext);        
//        var cloneReposList = new ArrayList<HashMap<String,String>>();
//        for (var cloneRepo : cloneRepositories) {
//            var cloneRepoMap = new HashMap<String,String>();
//            cloneRepoMap.put("name", cloneRepo.getAname());
//            cloneRepoMap.put("url", cloneRepo.getUrl());
//            if (cloneRepo.getLastrun()!=null) cloneRepoMap.put("lastRun", cloneRepo.getLastrun().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            if (cloneRepo.getLastsuccessfulrun()!=null) cloneRepoMap.put("lastSuccessfulRun", cloneRepo.getLastsuccessfulrun().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            cloneReposList.add(cloneRepoMap);
//        }
        
        return ResponseEntity.ok().body("foo");
    } 
}
