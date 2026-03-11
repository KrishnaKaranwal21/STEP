import java.util.*;

class PlagiarismDetector {

    private Map<String, Set<String>> ngramIndex = new HashMap<>();
    private Map<String, List<String>> documentNgrams = new HashMap<>();
    private int n = 5;

    void addDocument(String docId, String text) {
        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {
            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    List<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }

        return grams;
    }

    void analyzeDocument(String docId) {
        List<String> grams = documentNgrams.get(docId);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {
            Set<String> docs = ngramIndex.getOrDefault(gram, new HashSet<>());
            for (String d : docs) {
                if (!d.equals(docId)) {
                    matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {
            String otherDoc = entry.getKey();
            int matches = entry.getValue();
            double similarity = (matches * 100.0) / grams.size();

            System.out.println("Match with " + otherDoc + ": " + matches + " n-grams");
            System.out.println("Similarity: " + similarity + "%");
        }
    }
}

public class PlagiarismDetectionSystem {

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector();

        String essay1 = "machine learning is a field of artificial intelligence that focuses on data";
        String essay2 = "machine learning is a field of artificial intelligence used in many systems";
        String essay3 = "the history of art and painting is very interesting for students";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay3);

        detector.analyzeDocument("essay_092.txt");
    }
}