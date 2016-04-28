package edu.gvsu.cis.headyn.budgetapplaptop;

import android.app.FragmentManager;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

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
    SharedPreferences prefs;
    DailyTransactions dailyTraxs;
    RecurringTransactions recurringTraxs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dailyTraxs = new DailyTransactions();
        recurringTraxs = new RecurringTransactions();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAddItem();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        currentFragment = "Daily";

        if (prefs.contains("Daily_Exps")) {
            dailyTraxs.dailyItems = getDaily();
        }

        if (prefs.contains("Recurring_Exps")) {
            ArrayList<RecurringTransactions.RecurringItem> list = getRecurring();
            recurringTraxs.recurringItems = list;
            recurringTraxs.addToMap(list);
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

        // Opens daily expenses on app launch
        if (!prefs.contains("Recur_Edit_Position")) {
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

        System.out.println("PRINTINT ALL MAP ENTRIES::::::::");
        Map<String, RecurringTransactions.RecurringItem> map = recurringTraxs.ITEM_MAP;
        for (Map.Entry<String,RecurringTransactions.RecurringItem> entry : map.entrySet()) {
            String key = entry.getKey();
           // String value = entry.getValue();
            System.out.println(key);
        }

        System.out.println("onResume called!");

        if (prefs.contains("Recur_Edit_Position")) {

            System.out.println("onResume: Prefs DOES contain Recur_Edit_Position!");

            int position = prefs.getInt("Recur_Edit_Position", 0);
            String catName = prefs.getString("Recur_Edit_Name", "Category");
            double catAmount = Double.longBitsToDouble(prefs.getLong("Recur_Edit_Amount", 0));

            System.out.println("Name : " + catName);
            System.out.println("Amount : " + catAmount);
            System.out.println("List Position : " + position);

            changeRecurring(position, catAmount, catName);
        }

        SharedPreferences.Editor ped = prefs.edit();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 0xFACE) {

            String result = data.getStringExtra("transName");
            double amount = data.getDoubleExtra("amount", 0.0);
            FragmentManager fm = getFragmentManager();

            if (currentFragment.equals("Daily")) {
                dailyTraxs.createDailyItem(result, amount);
                fm.beginTransaction()
                        .replace(R.id.content_frame, new DailyFragment()).commit();

                saveDaily(dailyTraxs.dailyItems);

            } else if (currentFragment.equals("Recurring")) {
                recurringTraxs.createRecurringItem(result, amount);
                fm.beginTransaction()
                        .replace(R.id.content_frame, new RecurringFragment()).commit();

                saveRecurring(recurringTraxs.recurringItems);
            }

        // Overwrites a transaction if user was editing one
        } else if (resultCode == RESULT_OK && requestCode == 0xFACD) {

            String result = data.getStringExtra("transName");
            double amount = data.getDoubleExtra("amount", 0.0);
            FragmentManager fm = getFragmentManager();

            int position = data.getIntExtra("Position", 0);
            dailyTraxs.dailyItems.get(position).name = result;
            dailyTraxs.dailyItems.get(position).amount = amount;
            dailyTraxs.addToMap(dailyTraxs.dailyItems.get(position));

            fm.beginTransaction()
                    .replace(R.id.content_frame, new DailyFragment()).commit();

            saveDaily(dailyTraxs.dailyItems);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void launchAddItem() {
        Intent launchAddItem = new Intent(this, AddItemActivity.class);
        launchAddItem.putExtra("Previous Activity", "AddItem");
        startActivityForResult(launchAddItem, 0xFACE);
    }

    private void changeRecurring(int position, double amount, String name) {
        RecurringTransactions.RecurringItem itemToChange = recurringTraxs.recurringItems.get(position);
        String oldName = itemToChange.categoryName;

        if (itemToChange.categoryName != name || itemToChange.totalAmount != amount) {
            //change name and amount.
            itemToChange.categoryName = name;
            itemToChange.totalAmount = amount;
            recurringTraxs.recurringItems.set(position, itemToChange);

            //Remove old entry and create new for ITEM_MAP
            recurringTraxs.ITEM_MAP.remove(oldName);
            recurringTraxs.ITEM_MAP.put(name, itemToChange);

            saveRecurring(recurringTraxs.recurringItems);

            // Need to refresh view?
        }
    }

    private void saveDaily(List<DailyTransactions.DailyItem> items) {
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

    public ArrayList<DailyTransactions.DailyItem> getDaily() {
        int size = prefs.getInt("Daily_Exps", 0);
        ArrayList<DailyTransactions.DailyItem> dailyExps = new ArrayList<>();

        for (int k=0; k<size; k++) {
            String itemName = prefs.getString("daily_" + k, null);
            double itemAmount = Double.longBitsToDouble(prefs.getLong("daily_amount_" + k, 0));

            dailyTraxs.createDailyItem(itemName, itemAmount);
            //DailyTransactions.DailyItem item = new DailyTransactions.DailyItem(itemName, itemAmount);
            //dailyExps.add(item);
        }
        return dailyExps;
    }

    private void saveRecurring(List<RecurringTransactions.RecurringItem> items) {
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

    private ArrayList<RecurringTransactions.RecurringItem> getRecurring() {
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

    public static class SaveUtility {

        public RecurringTransactions recurringTraxs;

        public SaveUtility() {
            recurringTraxs = new RecurringTransactions();
        }

        public static void saveNewTransaction() {

        }
    }
}
