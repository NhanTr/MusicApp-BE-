package nhantr.musicapp.service;

public interface RedisService {

    void blacklistToken(String token, long ttlInMs);

    boolean isTokenBlacklisted(String token);
}
