package ch.so.agi.ilicache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
public class AppProperties {
    private String cloneDirectory;
    
    private List<String> cloneRepositories = new ArrayList<String>();
    
    private List<String> peerRepositories = new ArrayList<String>();

    public String getCloneDirectory() {
        return cloneDirectory;
    }

    public void setCloneDirectory(String cloneDirectory) {
        this.cloneDirectory = cloneDirectory;
    }

    public List<String> getCloneRepositories() {
        return cloneRepositories;
    }

    public void setCloneRepositories(List<String> cloneRepositories) {
        this.cloneRepositories = cloneRepositories;
    }

    public List<String> getPeerRepositories() {
        return peerRepositories;
    }

    public void setPeerRepositories(List<String> peerRepositories) {
        this.peerRepositories = peerRepositories;
    }
}
