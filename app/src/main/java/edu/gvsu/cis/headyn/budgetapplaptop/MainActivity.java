package edu.gvsu.cis.headyn.budgetapplaptop;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gvsu.cis.headyn.budgetapplaptop.fragments.DailyFragment;
import edu.gvsu.cis.headyn.budgetapplaptop.fragments.RecurringFragment;
import edu.gvsu.cis.headyn.budgetapplaptop.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String currentFragment;
    SaveUtility saver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saver = new SaveUtility(this);
        currentFragment = "Daily";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddItem();
            }
        });

        if (saver.prefs.contains("Daily_Exps")) {
            saver.dailyTraxs.dailyItems = saver.getDaily();
        }

        if (saver.prefs.contains("Recurring_Exps")) {
            ArrayList<RecurringTransactions.RecurringItem> list = saver.getRecurring();
            saver.recurringTraxs.recurringItems = list;
            saver.recurringTraxs.addToMap(list);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        Firebase myFirebaseRef = new Firebase("https://popping-fire-7900.firebaseio.com/");
        myFirebaseRef.child("message").setValue(23);
        myFirebaseRef.child("message2").setValue(45);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Firebase Event Listener
        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());  //prints "Test Message"
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }

        });
    }

    @Override
    protected void onResume() {

        System.out.println("::::PRINTING ALL MAP ENTRIES::::");

        Map<String, RecurringTransactions.RecurringItem> map = saver.recurringTraxs.ITEM_MAP;
        for (Map.Entry<String,RecurringTransactions.RecurringItem> entry : map.entrySet()) {
            String key = entry.getKey();
            // String value = entry.getValue();
            System.out.println(key);
        }

        if (currentFragment.equals("Daily")) {
            Fragment fragment = new DailyFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        } else {
            Fragment fragment = new RecurringFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }


        // If user was editing a category, we save it
        if (saver.prefs.contains("Recur_Edit_Position")) {

            int position = saver.prefs.getInt("Recur_Edit_Position", 0);
            String catName = saver.prefs.getString("Recur_Edit_Name", "Category");
            double catAmount = Double.longBitsToDouble(saver.prefs.getLong("Recur_Edit_Amount", 0));

            System.out.println("Name : " + catName);
            System.out.println("Amount : " + catAmount);
            System.out.println("List Position : " + position);

            saver.changeRecurring(position, catAmount, catName);
        }

        // Removes these "temp" saved items so they don't appear when they don't need to
        SharedPreferences.Editor ped = saver.prefs.edit();
        ped.remove("Recur_Edit_Position");
        ped.remove("Recur_Edit_Name");
        ped.remove("Recur_Edit_Amount");
        ped.commit();

        super.onResume();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.daily) {
            currentFragment = "Daily";
            fragment = new DailyFragment();
        } else if (id == R.id.recurring) {
            currentFragment = "Recurring";
            fragment = new RecurringFragment();
        } else if (id == R.id.settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();

            /* update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            */

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

        return true;
    }

    private void launchAddItem() {
        Intent launchAddItem = new Intent(this, AddItemActivity.class);
        if (currentFragment.equals("Daily")) {
            launchAddItem.putExtra("Previous Activity", "AddDaily");
        } else {
            launchAddItem.putExtra("Previous Activity", "AddRecurring");
        }
        startActivity(launchAddItem);
    }


    public static class SaveUtility {

        public RecurringTransactions recurringTraxs;
        public DailyTransactions dailyTraxs;
        public SharedPreferences prefs;


        public SaveUtility(Context context) {
            dailyTraxs = new DailyTransactions();
            recurringTraxs = new RecurringTransactions();
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        public ArrayList<DailyTransactions.DailyItem> getDaily() {
            int size = prefs.getInt("Daily_Exps", 0);
            ArrayList<DailyTransactions.DailyItem> dailyExps = new ArrayList<>();

            for (int k=0; k<size; k++) {
                String itemName = prefs.getString("daily_" + k, null);
                double itemAmount = Double.longBitsToDouble(prefs.getLong("daily_amount_" + k, 0));

                DailyTransactions.DailyItem newItem = new DailyTransactions.DailyItem(itemName, itemAmount);
                dailyExps.add(newItem);
            }
            return dailyExps;
        }

        public void saveDaily() {
            List<DailyTransactions.DailyItem> items = dailyTraxs.dailyItems;

            SharedPreferences.Editor ped = prefs.edit();
            ped.putInt("Daily_Exps", items.size());
            for (int k=0; k < items.size(); k++) {
                String itemName = items.get(k).name;
                double itemAmount = items.get(k).amount;
                ped.putString("daily_" + k, itemName);
                ped.putLong("daily_amount_" + k, Double.doubleToRawLongBits(itemAmount));
            }
            ped.commit();
        }

        public void changeDaily(int position, double amount, String name) {
            dailyTraxs.dailyItems.get(position).name = name;
            dailyTraxs.dailyItems.get(position).amount = amount;
            dailyTraxs.addToMap(dailyTraxs.dailyItems.get(position));
            saveDaily();
        }

        public void addDaily(double amount, String name) {
            dailyTraxs.createDailyItem(name, amount);
            saveDaily();
        }

        public void saveRecurring() {
            List<RecurringTransactions.RecurringItem> items = recurringTraxs.recurringItems;

            SharedPreferences.Editor ped = prefs.edit();
            int numOfCategories = items.size();
            ped.putInt("Recurring_Exps", numOfCategories);
            for (int k=0; k < numOfCategories; k++) {
                RecurringTransactions.RecurringItem item = items.get(k);
                int numOfTransInCategory = item.dailyItems.size();

                String itemName = item.categoryName;
                double itemAmount = item.totalAmount;

                ped.putString("recur_" + k, itemName);
                ped.putLong("recur_amount_" + k, Double.doubleToRawLongBits(itemAmount));

                ped.putInt("Recurring_Exps_" + k, numOfTransInCategory);

                for (int m=0; m<numOfTransInCategory; m++) {
                    DailyTransactions.DailyItem subItem = item.dailyItems.get(m);

                    String subItemName = subItem.name;
                    double subItemAmount = subItem.amount;
                    ped.putString("sub_name_" + k + "_" + m, subItemName);
                    ped.putLong("sub_amount_" + k + "_" + m, Double.doubleToRawLongBits(subItemAmount));
                }
            }
            ped.commit();
        }

        public ArrayList<RecurringTransactions.RecurringItem> getRecurring() {
            int size = prefs.getInt("Recurring_Exps", 0);
            ArrayList<RecurringTransactions.RecurringItem> recurringExps = new ArrayList<>();

            for (int k=0; k<size; k++) {
                String categoryName = prefs.getString("recur_" + k, null);
                double totalAmount = Double.longBitsToDouble(prefs.getLong("recur_amount_" + k, 0));
                RecurringTransactions.RecurringItem categoryItem = new RecurringTransactions.RecurringItem(categoryName, totalAmount);

                // Pulls transactions in the category
                ArrayList<DailyTransactions.DailyItem> subItems = new ArrayList<>();
                int numOfSubItems = prefs.getInt("Recurring_Exps_" + k, 0);

                for (int m=0; m<numOfSubItems; m++) {
                    String subItemName = prefs.getString("sub_name_" + k + "_" + m, null);
                    double subItemAmount = Double.longBitsToDouble(prefs.getLong("sub_amount_" + k + "_" + m, 0));
                    DailyTransactions.DailyItem subItem = new DailyTransactions.DailyItem(subItemName, subItemAmount);
                    subItems.add(subItem);
                }
                // Add the array list of transactions to the recurring item
                categoryItem.dailyItems = subItems;

                // Add the recurring item to the array list of recurring items
                recurringExps.add(categoryItem);
            }

            return recurringExps;
        }

        public void changeRecurring(int position, double amount, String name) {
            RecurringTransactions.RecurringItem itemToChange = recurringTraxs.recurringItems.get(position);
            String oldName = itemToChange.categoryName;

            if (oldName != name || Double.compare(itemToChange.totalAmount, amount) != 0) {
                //change name and amount.
                itemToChange.categoryName = name;
                itemToChange.totalAmount = amount;
                recurringTraxs.recurringItems.set(position, itemToChange);

                //Remove old entry and create new for ITEM_MAP
                recurringTraxs.ITEM_MAP.remove(oldName);
                recurringTraxs.ITEM_MAP.put(name, itemToChange);

                saveRecurring();
            }
        }

        public void addRecurring(double amount, String name) {
            recurringTraxs.createRecurringItem(name, amount);
            saveRecurring();
        }

        public void addSubItem(int catPosition, double amount, String name) {
            DailyTransactions.DailyItem itemToAdd = new DailyTransactions.DailyItem(name, amount);

            recurringTraxs.recurringItems.get(catPosition).dailyItems.add(itemToAdd);
            saveRecurring();

            int size = recurringTraxs.recurringItems.get(catPosition).dailyItems.size();
            System.out.println("From addSubItem, item added: name: " + recurringTraxs.recurringItems.get(catPosition).dailyItems.get(size-1).name);
        }

        public void changeSubItem(int catPosition, int itemPosition, double amount, String name) {
            DailyTransactions.DailyItem itemToChange = recurringTraxs.recurringItems.get(catPosition).dailyItems.get(itemPosition);
            String oldName = itemToChange.name;

            if (oldName != name || Double.compare(itemToChange.amount, amount) != 0 ) {
                // change name and amount of sub item
                itemToChange.name = name;
                itemToChange.amount = amount;
                recurringTraxs.recurringItems.get(catPosition).dailyItems.set(itemPosition, itemToChange);

                //Remove old entry and create new for ITEM_MAP ???

                saveRecurring();
            }
        }

        public void deleteSubItem(int position, int subPosition) {
            recurringTraxs.recurringItems.get(position).dailyItems.remove(subPosition);
            saveRecurring();
        }

        public void deleteDaily(int position) {
            dailyTraxs.dailyItems.remove(position);
            saveDaily();
        }

        public void deleteRecurring(int position) {

        }
    }
}