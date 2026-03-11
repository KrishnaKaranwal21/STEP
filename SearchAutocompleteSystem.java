import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd = false;
    String query;
}

class AutocompleteService {

    private TrieNode root = new TrieNode();
    private Map<String, Integer> frequency = new HashMap<>();

    void addQuery(String query) {

        frequency.put(query, frequency.getOrDefault(query, 0) + 1);

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isEnd = true;
        node.query = query;
    }

    List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();
        dfs(node, results);

        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequency.get(b) - frequency.get(a)
        );

        pq.addAll(results);

        List<String> top = new ArrayList<>();
        int k = 0;

        while (!pq.isEmpty() && k < 10) {
            top.add(pq.poll());
            k++;
        }

        return top;
    }

    void dfs(TrieNode node, List<String> results) {

        if (node.isEnd) {
            results.add(node.query);
        }

        for (TrieNode child : node.children.values()) {
            dfs(child, results);
        }
    }

    void updateFrequency(String query) {
        addQuery(query);
    }
}

public class SearchAutocompleteSystem {

    public static void main(String[] args) {

        AutocompleteService service = new AutocompleteService();

        service.addQuery("java tutorial");
        service.addQuery("javascript");
        service.addQuery("java download");
        service.addQuery("java tutorial");
        service.addQuery("java 21 features");

        System.out.println(service.search("jav"));

        service.updateFrequency("java 21 features");
        service.updateFrequency("java 21 features");

        System.out.println(service.search("java"));
    }
}