package ch.so.agi.ilicache;

import java.sql.SQLException;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ch.so.agi.ilicache.config.UserConfig;
import ch.so.agi.ilicache.service.InfoService;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserConfig userConfig;
    
    @Autowired
    InfoService infoService;
    
//    @PostConstruct
//    private void init() {
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
        
    @GetMapping("/ping")
    public ResponseEntity<?> ping()  {
        return new ResponseEntity<String>("ilimirror-web-service", HttpStatus.OK);
    }
    
    @GetMapping("/config")
    public ResponseEntity<?> properties()  {
        return ResponseEntity.ok().body(userConfig);
    }
        
    @GetMapping("/info")
    public ResponseEntity<?> info() throws SQLException {
        var infoMap = new HashMap<String,Object>();
        infoMap.put("repositories", userConfig.getCloneRepositories().split(","));
        infoMap.put("cloneStatus", infoService);
        
        System.out.println("fuba2222r");
        
        return ResponseEntity.ok().body(infoMap);
    } 
}
