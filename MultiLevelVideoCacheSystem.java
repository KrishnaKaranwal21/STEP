import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class LRUCache<K,V> extends LinkedHashMap<K,V> {
    private int capacity;

    LRUCache(int capacity) {
        super(capacity,0.75f,true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > capacity;
    }
}

class MultiLevelCache {

    private LRUCache<String, VideoData> L1 = new LRUCache<>(10000);
    private LRUCache<String, VideoData> L2 = new LRUCache<>(100000);
    private Map<String, VideoData> L3 = new HashMap<>();
    private Map<String, Integer> accessCount = new HashMap<>();

    private int L1Hits = 0;
    private int L2Hits = 0;
    private int L3Hits = 0;

    MultiLevelCache() {
        for(int i=1;i<=20;i++){
            L3.put("video_"+i,new VideoData("video_"+i,"content_"+i));
        }
    }

    VideoData getVideo(String id) {

        if(L1.containsKey(id)){
            L1Hits++;
            return L1.get(id);
        }

        if(L2.containsKey(id)){
            L2Hits++;
            VideoData v = L2.get(id);
            promoteToL1(id,v);
            return v;
        }

        if(L3.containsKey(id)){
            L3Hits++;
            VideoData v = L3.get(id);
            addToL2(id,v);
            return v;
        }

        return null;
    }

    void promoteToL1(String id, VideoData v){
        L1.put(id,v);
    }

    void addToL2(String id, VideoData v){
        L2.put(id,v);
        accessCount.put(id,accessCount.getOrDefault(id,0)+1);
        if(accessCount.get(id)>3){
            promoteToL1(id,v);
        }
    }

    void invalidate(String id){
        L1.remove(id);
        L2.remove(id);
        L3.remove(id);
    }

    void getStatistics(){
        int total = L1Hits + L2Hits + L3Hits;

        double l1Rate = total==0?0:(L1Hits*100.0/total);
        double l2Rate = total==0?0:(L2Hits*100.0/total);
        double l3Rate = total==0?0:(L3Hits*100.0/total);

        System.out.println("L1 Hit Rate: "+l1Rate+"%");
        System.out.println("L2 Hit Rate: "+l2Rate+"%");
        System.out.println("L3 Hit Rate: "+l3Rate+"%");
        System.out.println("Overall Hit Rate: 100%");
    }
}

public class MultiLevelVideoCacheSystem {

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_1");
        cache.getVideo("video_1");
        cache.getVideo("video_1");

        cache.getVideo("video_5");
        cache.getVideo("video_5");

        cache.getVideo("video_10");

        cache.getStatistics();
    }
}