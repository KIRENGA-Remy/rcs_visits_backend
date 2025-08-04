package gitoli.java.projects.com.rcs_visits_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitingService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean isAllowed(String key, int maxAttempts, long timeoutMinutes) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String attempts = ops.get(key);

        if (attempts != null && Integer.parseInt(attempts) >= maxAttempts) {
            return false;
        }

        ops.increment(key);
        if (attempts == null) {
            redisTemplate.expire(key, timeoutMinutes, TimeUnit.MINUTES);
        }

        return true;
    }
}
