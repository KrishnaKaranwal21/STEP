import java.util.*;

class RiskSearchService {

    void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                found = true;
                System.out.println("Found at index: " + i);
                break;
            }
        }

        if (!found) {
            System.out.println("Not found");
        }

        System.out.println("Comparisons: " + comparisons);
    }

    void binarySearchFloorCeil(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int comparisons = 0;
        int floor = -1, ceil = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] == target) {
                floor = arr[mid];
                ceil = arr[mid];
                break;
            } else if (arr[mid] < target) {
                floor = arr[mid];
                low = mid + 1;
            } else {
                ceil = arr[mid];
                high = mid - 1;
            }
        }

        System.out.println("Floor: " + floor + ", Ceiling: " + ceil);
        System.out.println("Comparisons: " + comparisons);
    }

    void findInsertionPoint(int[] arr, int target) {
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = (low + high) / 2;

            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        System.out.println("Insertion Index: " + low);
    }
}

public class RiskThresholdSearch {

    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100};

        RiskSearchService service = new RiskSearchService();

        service.linearSearch(risks, 30);

        service.binarySearchFloorCeil(risks, 30);

        service.findInsertionPoint(risks, 30);
    }
}