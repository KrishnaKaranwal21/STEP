import java.util.*;

class InventoryManager {

    private Map<String, Integer> stockMap = new HashMap<>();
    private Map<String, Queue<Integer>> waitingList = new HashMap<>();

    void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    int checkStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    synchronized String purchaseItem(String productId, int userId) {

        int stock = stockMap.getOrDefault(productId, 0);

        if (stock > 0) {
            stockMap.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        } else {
            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);
            return "Added to waiting list, position #" + queue.size();
        }
    }

    int getWaitingListPosition(String productId, int userId) {

        Queue<Integer> queue = waitingList.get(productId);

        int position = 1;
        for (int id : queue) {
            if (id == userId) {
                return position;
            }
            position++;
        }

        return -1;
    }
}

public class FlashSaleInventoryManager {

    public static void main(String[] args) {

        InventoryManager manager = new InventoryManager();

        manager.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}