package org.DAD.application.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class LoggedOutTokenService {
    
    private final Cache<String, LoggedOutTokenInfo> loggedOutTokens;
    
    public LoggedOutTokenService() {
        this.loggedOutTokens = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofDays(30))
                .maximumSize(100_000)
                .build();
    }
    
    public void addLoggedOutToken(String token, UUID userId, String ipAddress, String userAgent) {
        LoggedOutTokenInfo tokenInfo = new LoggedOutTokenInfo(userId, ipAddress, userAgent);
        loggedOutTokens.put(token, tokenInfo);
        log.info("Token logged out for user: {}, IP: {}", userId, ipAddress);
    }
    
    public boolean isTokenLoggedOut(String token) {
        return loggedOutTokens.getIfPresent(token) != null;
    }
    
    public LoggedOutTokenInfo getTokenInfo(String token) {
        return loggedOutTokens.getIfPresent(token);
    }
    
    public void clearExpiredTokens() {
        loggedOutTokens.cleanUp();
        log.info("Cleaned up expired logged out tokens");
    }
    
    public long getLoggedOutTokensCount() {
        return loggedOutTokens.estimatedSize();
    }

    @Getter
    public static class LoggedOutTokenInfo {
        private final UUID userId;
        private final String ipAddress;
        private final String userAgent;
        private final long logoutTime;
        
        public LoggedOutTokenInfo(UUID userId, String ipAddress, String userAgent) {
            this.userId = userId;
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
            this.logoutTime = System.currentTimeMillis();
        }

    }
} 