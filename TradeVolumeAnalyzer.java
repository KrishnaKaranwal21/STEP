import java.util.*;

class Trade {
    String id;
    int volume;

    Trade(String id, int volume) {
        this.id = id;
        this.volume = volume;
    }

    public String toString() {
        return id + ":" + volume;
    }
}

class TradeAnalyzer {

    void mergeSort(List<Trade> list, int left, int right) {
        if (left >= right) return;

        int mid = (left + right) / 2;

        mergeSort(list, left, mid);
        mergeSort(list, mid + 1, right);

        merge(list, left, mid, right);
    }

    void merge(List<Trade> list, int left, int mid, int right) {
        List<Trade> temp = new ArrayList<>();

        int i = left, j = mid + 1;

        while (i <= mid && j <= right) {
            if (list.get(i).volume <= list.get(j).volume) {
                temp.add(list.get(i++));
            } else {
                temp.add(list.get(j++));
            }
        }

        while (i <= mid) temp.add(list.get(i++));
        while (j <= right) temp.add(list.get(j++));

        for (int k = 0; k < temp.size(); k++) {
            list.set(left + k, temp.get(k));
        }
    }

    void quickSort(List<Trade> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    int partition(List<Trade> list, int low, int high) {
        int pivot = list.get(high).volume;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).volume > pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    List<Trade> mergeLists(List<Trade> a, List<Trade> b) {
        List<Trade> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < a.size() && j < b.size()) {
            if (a.get(i).volume <= b.get(j).volume) {
                result.add(a.get(i++));
            } else {
                result.add(b.get(j++));
            }
        }

        while (i < a.size()) result.add(a.get(i++));
        while (j < b.size()) result.add(b.get(j++));

        return result;
    }

    int totalVolume(List<Trade> list) {
        int sum = 0;
        for (Trade t : list) sum += t.volume;
        return sum;
    }
}

public class TradeVolumeAnalyzer {

    public static void main(String[] args) {

        List<Trade> trades = new ArrayList<>();

        trades.add(new Trade("trade3", 500));
        trades.add(new Trade("trade1", 100));
        trades.add(new Trade("trade2", 300));

        TradeAnalyzer analyzer = new TradeAnalyzer();

        List<Trade> mergeSorted = new ArrayList<>(trades);
        analyzer.mergeSort(mergeSorted, 0, mergeSorted.size() - 1);
        System.out.println(mergeSorted);

        List<Trade> quickSorted = new ArrayList<>(trades);
        analyzer.quickSort(quickSorted, 0, quickSorted.size() - 1);
        System.out.println(quickSorted);

        List<Trade> merged = analyzer.mergeLists(mergeSorted, quickSorted);
        System.out.println(merged);

        System.out.println("Total Volume: " + analyzer.totalVolume(trades));
    }
}