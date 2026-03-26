import java.util.*;

class Asset {
    String name;
    double returnRate;
    double volatility;

    Asset(String name, double returnRate, double volatility) {
        this.name = name;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }

    public String toString() {
        return name + ":" + returnRate;
    }
}

class PortfolioSorter {

    void mergeSort(List<Asset> list, int left, int right) {
        if (left >= right) return;

        int mid = (left + right) / 2;

        mergeSort(list, left, mid);
        mergeSort(list, mid + 1, right);

        merge(list, left, mid, right);
    }

    void merge(List<Asset> list, int left, int mid, int right) {
        List<Asset> temp = new ArrayList<>();

        int i = left, j = mid + 1;

        while (i <= mid && j <= right) {
            if (list.get(i).returnRate <= list.get(j).returnRate) {
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

    void quickSort(List<Asset> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    int partition(List<Asset> list, int low, int high) {
        int pivotIndex = medianOfThree(list, low, high);
        Collections.swap(list, pivotIndex, high);

        Asset pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).returnRate > pivot.returnRate ||
                    (list.get(j).returnRate == pivot.returnRate &&
                            list.get(j).volatility < pivot.volatility)) {

                i++;
                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    int medianOfThree(List<Asset> list, int low, int high) {
        int mid = (low + high) / 2;

        Asset a = list.get(low);
        Asset b = list.get(mid);
        Asset c = list.get(high);

        if ((a.returnRate - b.returnRate) * (c.returnRate - a.returnRate) >= 0) return low;
        else if ((b.returnRate - a.returnRate) * (c.returnRate - b.returnRate) >= 0) return mid;
        else return high;
    }
}

public class PortfolioReturnSorter {

    public static void main(String[] args) {

        List<Asset> assets = new ArrayList<>();

        assets.add(new Asset("AAPL", 12, 5));
        assets.add(new Asset("TSLA", 8, 7));
        assets.add(new Asset("GOOG", 15, 4));

        PortfolioSorter sorter = new PortfolioSorter();

        List<Asset> mergeSorted = new ArrayList<>(assets);
        sorter.mergeSort(mergeSorted, 0, mergeSorted.size() - 1);
        System.out.println(mergeSorted);

        List<Asset> quickSorted = new ArrayList<>(assets);
        sorter.quickSort(quickSorted, 0, quickSorted.size() - 1);
        System.out.println(quickSorted);
    }
}