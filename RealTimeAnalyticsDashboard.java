import java.util.*;
import java.util.concurrent.*;

class AnalyticsEngine {

    private Map<String, Integer> pageViews = new HashMap<>();
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();
    private Map<String, Integer> sourceCount = new HashMap<>();

    void processEvent(String url, String userId, String source) {

        pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);

        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        sourceCount.put(source, sourceCount.getOrDefault(source, 0) + 1);
    }

    void getDashboard() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        int count = 0;

        System.out.println("Top Pages:");

        while (!pq.isEmpty() && count < 10) {
            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((count + 1) + ". " + url + " - " + views + " views (" + unique + " unique)");

            count++;
        }

        System.out.println("Traffic Sources:");

        for (Map.Entry<String, Integer> e : sourceCount.entrySet()) {
            System.out.println(e.getKey() + " - " + e.getValue());
        }
    }
}

public class RealTimeAnalyticsDashboard {

    public static void main(String[] args) throws Exception {

        AnalyticsEngine engine = new AnalyticsEngine();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            engine.getDashboard();
            System.out.println("-----");
        }, 5, 5, TimeUnit.SECONDS);

        engine.processEvent("/article/breaking-news", "user_123", "google");
        engine.processEvent("/article/breaking-news", "user_456", "facebook");
        engine.processEvent("/sports/championship", "user_789", "google");
        engine.processEvent("/sports/championship", "user_999", "direct");
        engine.processEvent("/article/breaking-news", "user_111", "google");

        Thread.sleep(15000);
        scheduler.shutdown();
    }
}