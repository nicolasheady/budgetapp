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
public class RecurringTransactions {

    /**
     * An array of recurring transactions
     */
    public static List<RecurringItem> recurringItems = new ArrayList<RecurringItem>();

    /**
     * A map of recurring transactions, by ID.
     */
    public static final Map<String, RecurringItem> ITEM_MAP = new HashMap<String, RecurringItem>();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            createRecurringItem("Test recurring", 0.0);
        }
    }

    private static void addItem(RecurringItem item) {
        recurringItems.add(item);
        ITEM_MAP.put(item.categoryName, item);
    }

    public static void addToMap(List<RecurringItem> list) {
        for (RecurringItem item : list) {
            ITEM_MAP.put(item.categoryName, item);
        }
    }

    public static RecurringItem createRecurringItem(String name, double amount) {
        RecurringItem transaction = new RecurringItem(name, amount);
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
    public static class RecurringItem {
        public String categoryName;
        public double totalAmount;
        public List<DailyTransactions.DailyItem> dailyItems = new ArrayList<DailyTransactions.DailyItem>();


        public RecurringItem(String name, double amount) {
            this.categoryName = name;
            this.totalAmount = amount;

            for (int k = 0; k<3; k++) {
                dailyItems.add(new DailyTransactions.DailyItem("Test sub items", 0.0));
            }

            for (DailyTransactions.DailyItem item : dailyItems) {
                totalAmount += item.amount;
            }
        }

        public double getTotal() {
            if (dailyItems != null) {
                double total = 0.0;
                for (DailyTransactions.DailyItem k : dailyItems) {
                    total += k.amount;
                }
                return total;
            } else {
                return 0.0;
            }
        }

        @Override
        public String toString() {
            return this.categoryName;
        }
    }
}