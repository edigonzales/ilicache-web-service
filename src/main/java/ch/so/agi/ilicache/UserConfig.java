package ch.so.agi.ilicache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("user")
public class UserConfig {
    private IliSite iliSite;
    private String cloneDirectory;
    private String cloneCronExpression;
    private boolean cloneOnStartup;
    private String cloneRepositories;    
    private String peerRepositories;
    
    public IliSite getIliSite() {
        return iliSite;
    }

    public void setIliSite(IliSite iliSite) {
        this.iliSite = iliSite;
    }

    public String getCloneDirectory() {
        return cloneDirectory;
    }

    public void setCloneDirectory(String cloneDirectory) {
        this.cloneDirectory = cloneDirectory;
    }

    public String getCloneCronExpression() {
        return cloneCronExpression;
    }

    public void setCloneCronExpression(String cloneCronExpression) {
        this.cloneCronExpression = cloneCronExpression;
    }

    public boolean isCloneOnStartup() {
        return cloneOnStartup;
    }

    public void setCloneOnStartup(boolean cloneOnStartup) {
        this.cloneOnStartup = cloneOnStartup;
    }
    
    public String getCloneRepositories() {
        return cloneRepositories;
    }

    public void setCloneRepositories(String cloneRepositories) {
        this.cloneRepositories = cloneRepositories;
    }

    public String getPeerRepositories() {
        return peerRepositories;
    }

    public void setPeerRepositories(String peerRepositories) {
        this.peerRepositories = peerRepositories;
    }

    public static class IliSite {
        private String name;
        private String title;
        private String shortDescription;
        private String owner;
        private String technicalContact;
        private String furtherInformation;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getTechnicalContact() {
            return technicalContact;
        }

        public void setTechnicalContact(String technicalContact) {
            this.technicalContact = technicalContact;
        }

        public String getFurtherInformation() {
            return furtherInformation;
        }

        public void setFurtherInformation(String furtherInformation) {
            this.furtherInformation = furtherInformation;
        }
    }
}
