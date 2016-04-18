package edu.gvsu.cis.headyn.budgetapplaptop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DailyTransactions {

    /**
     * An array of recurring transactions
     */
    public static List<DailyItem> dailyItems = new ArrayList<DailyItem>();

    /**
     * A map of recurring transactions, by ID.
     */
    public static final Map<String, DailyItem> ITEM_MAP = new HashMap<String, DailyItem>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 0; i < COUNT; i++) {
            createDailyItem("Test daily.", 0.0);
        }
    }

    private static void addItem(DailyItem item) {
        dailyItems.add(item);
        ITEM_MAP.put(item.name, item);
    }

    public static void addToMap(DailyItem item) {
        ITEM_MAP.put(item.name, item);
    }

    public static DailyItem createDailyItem(String name, double amount) {
        DailyItem transaction = new DailyItem(name, amount);
        addItem(transaction);
        return transaction;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A recurring transaction object.
     */
    public static class DailyItem {
        public String name;
        public double amount;

        public DailyItem(String name, double amount) {
            this.name = name;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
