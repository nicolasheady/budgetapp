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
    public static List<RecurringItem> recurringItems = new ArrayList<RecurringItem>();

    /**
     * A map of recurring transactions, by ID.
     */
    public static final Map<String, RecurringItem> ITEM_MAP = new HashMap<String, RecurringItem>();

    private static final int COUNT = 5;

    private int numCategories;
    private String categoryName;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createRecurringItem("Test transaction."));
        }
    }

    private static void addItem(RecurringItem item) {
        recurringItems.add(item);
        ITEM_MAP.put(item.categoryName, item);
    }

    public static RecurringItem createRecurringItem(String name) {
        return new RecurringItem(name);
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


        public RecurringItem(String name) {
            this.categoryName = name;
            this.totalAmount = 0.0;
        }

        // Will return total of all transactions

        public double getTotal() {
            return 5.0;
        }

        @Override
        public String toString() {
            return this.categoryName;
        }
    }
}
