import java.util.*;

class TransactionLog {
    String accountId;

    TransactionLog(String accountId) {
        this.accountId = accountId;
    }

    public String toString() {
        return accountId;
    }
}

class SearchService {

    void linearSearch(List<TransactionLog> list, String target) {
        int first = -1, last = -1;
        int comparisons = 0;

        for (int i = 0; i < list.size(); i++) {
            comparisons++;
            if (list.get(i).accountId.equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }

        System.out.println("First: " + first + ", Last: " + last);
        System.out.println("Comparisons: " + comparisons);
    }

    void binarySearch(List<TransactionLog> list, String target) {
        int low = 0, high = list.size() - 1;
        int comparisons = 0;
        int found = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            int cmp = list.get(mid).accountId.compareTo(target);

            if (cmp == 0) {
                found = mid;
                break;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        int count = 0;

        if (found != -1) {
            int i = found;
            while (i >= 0 && list.get(i).accountId.equals(target)) {
                count++;
                i--;
            }
            i = found + 1;
            while (i < list.size() && list.get(i).accountId.equals(target)) {
                count++;
                i++;
            }
        }

        System.out.println("Index: " + found + ", Count: " + count);
        System.out.println("Comparisons: " + comparisons);
    }
}

public class AccountIdSearchSystem {

    public static void main(String[] args) {

        List<TransactionLog> logs = new ArrayList<>();

        logs.add(new TransactionLog("accB"));
        logs.add(new TransactionLog("accA"));
        logs.add(new TransactionLog("accB"));
        logs.add(new TransactionLog("accC"));

        SearchService service = new SearchService();

        service.linearSearch(logs, "accB");

        logs.sort(Comparator.comparing(a -> a.accountId));

        service.binarySearch(logs, "accB");
    }
}