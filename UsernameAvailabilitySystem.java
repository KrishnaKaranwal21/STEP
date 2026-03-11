import java.util.*;

class UsernameService {

    private Map<String, Integer> userMap = new HashMap<>();
    private Map<String, Integer> attemptFrequency = new HashMap<>();

    boolean checkAvailability(String username) {
        attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);
        return !userMap.containsKey(username);
    }

    void registerUser(String username, int userId) {
        userMap.put(username, userId);
    }

    List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));
        suggestions.add(username + "_official");

        return suggestions;
    }

    String getMostAttempted() {
        String result = "";
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                result = entry.getKey();
            }
        }

        return result;
    }
}

public class UsernameAvailabilitySystem {

    public static void main(String[] args) {

        UsernameService service = new UsernameService();

        service.registerUser("john_doe", 101);
        service.registerUser("admin", 1);

        System.out.println(service.checkAvailability("john_doe"));
        System.out.println(service.checkAvailability("jane_smith"));

        System.out.println(service.suggestAlternatives("john_doe"));

        for(int i=0;i<5;i++) service.checkAvailability("admin");

        System.out.println(service.getMostAttempted());
    }
}