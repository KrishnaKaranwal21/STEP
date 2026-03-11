import java.util.*;
import java.util.concurrent.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long timestamp;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.timestamp = System.currentTimeMillis();
        this.expiryTime = this.timestamp + ttlSeconds * 1000;
    }

    boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private final int capacity;
    private final Map<String, DNSEntry> cache;
    private long hits = 0;
    private long misses = 0;

    DNSCache(int capacity) {
        this.capacity = capacity;

        this.cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.capacity;
            }
        };

        startCleanupThread();
    }

    synchronized String resolve(String domain) {
        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            return entry.ipAddress;
        }

        misses++;

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
        }

        String ip = queryUpstreamDNS(domain);
        cache.put(domain, new DNSEntry(domain, ip, 300));
        return ip;
    }

    private String queryUpstreamDNS(String domain) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Random r = new Random();
        return "172.217." + r.nextInt(255) + "." + r.nextInt(255);
    }

    private void startCleanupThread() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            synchronized (this) {
                Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, DNSEntry> entry = iterator.next();
                    if (entry.getValue().isExpired()) {
                        iterator.remove();
                    }
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    synchronized void getCacheStats() {
        long total = hits + misses;
        double hitRate = total == 0 ? 0 : ((double) hits / total) * 100;

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }
}

public class DNSCacheSystem {

    public static void main(String[] args) throws Exception {

        DNSCache cache = new DNSCache(5);

        System.out.println(cache.resolve("google.com"));
        System.out.println(cache.resolve("google.com"));

        Thread.sleep(301000);

        System.out.println(cache.resolve("google.com"));

        cache.getCacheStats();
    }
}