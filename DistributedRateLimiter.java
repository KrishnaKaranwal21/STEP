import java.util.*;
import java.util.concurrent.*;

class TokenBucket {
    long tokens;
    long maxTokens;
    long refillRate;
    long lastRefillTime;

    TokenBucket(long maxTokens, long refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    synchronized boolean allowRequest() {
        refill();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime;
        long refillTokens = (elapsed * refillRate) / 3600000;
        if (refillTokens > 0) {
            tokens = Math.min(maxTokens, tokens + refillTokens);
            lastRefillTime = now;
        }
    }

    long getRemainingTokens() {
        refill();
        return tokens;
    }
}

class RateLimiterService {

    private Map<String, TokenBucket> clientBuckets = new ConcurrentHashMap<>();
    private final long limit = 1000;

    boolean checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId, new TokenBucket(limit, limit));

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket.allowRequest()) {
            System.out.println("Allowed (" + bucket.getRemainingTokens() + " requests remaining)");
            return true;
        } else {
            System.out.println("Denied (0 requests remaining)");
            return false;
        }
    }

    void getRateLimitStatus(String clientId) {

        TokenBucket bucket = clientBuckets.get(clientId);

        if (bucket == null) return;

        long remaining = bucket.getRemainingTokens();
        long used = limit - remaining;

        System.out.println("{used: " + used + ", limit: " + limit + "}");
    }
}

public class DistributedRateLimiter {

    public static void main(String[] args) {

        RateLimiterService service = new RateLimiterService();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {
            service.checkRateLimit(client);
        }

        service.getRateLimitStatus(client);
    }
}