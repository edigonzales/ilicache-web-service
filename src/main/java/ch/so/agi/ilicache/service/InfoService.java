package ch.so.agi.ilicache.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class InfoService {
    private LocalDateTime lastRun;
    private LocalDateTime lastSuccessfulRun;
    private Long cloneTimeElapsedSeconds;
    
    public LocalDateTime getLastRun() {
        return lastRun;
    }
    public void setLastRun(LocalDateTime lastRun) {
        this.lastRun = lastRun;
    }
    public LocalDateTime getLastSuccessfulRun() {
        return lastSuccessfulRun;
    }
    public void setLastSuccessfulRun(LocalDateTime lastSuccessfulRun) {
        this.lastSuccessfulRun = lastSuccessfulRun;
    }
    public Long getCloneTimeElapsedSeconds() {
        return cloneTimeElapsedSeconds;
    }
    public void setCloneTimeElapsedSeconds(Long cloneTimeElapsedSeconds) {
        this.cloneTimeElapsedSeconds = cloneTimeElapsedSeconds;
    }
}
