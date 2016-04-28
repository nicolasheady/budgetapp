package edu.gvsu.cis.headyn.budgetapplaptop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import baileybr.simplebudget.database.DBHelper;

/*
    A dialog-like activity that prompts a user to enter an name and amount for
    a new line item.
 */

public class AddItemActivity extends ActionBarActivity {

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    //private DBHelper myDb;
    private EditText name;
    private EditText amount;
    private Button setButton;
    private Button deleteButton;
    private Button addToMap;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Add New Item");

        //toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //myDb = DBHelper.getInstance(this);

        this.name = (EditText) findViewById(R.id.name);
        this.amount = (EditText) findViewById(R.id.amount);
        this.setButton = (Button) findViewById(R.id.addButton);
        this.deleteButton = (Button) findViewById(R.id.deleteButton);
<<<<<<< HEAD
        this.addToMap = (Button) findViewById(R.id.mapButton);
        this.deleteButton.setVisibility(View.GONE);
        this.addToMap.setVisibility(View.GONE);
        this.deleteButton.setOnClickListener(this);
        this.setButton.setOnClickListener(this);
        this.addToMap.setOnClickListener(this);
=======
>>>>>>> 1d8792acf755b3d8c1c063bdfdfb04ae54568c0d

        Intent previous = getIntent();

        // If this activity was launch by clicking on a transaction, change titles appropriately:
        if (!previous.getStringExtra("Previous Activity").equals("AddItem")) {
            this.name.setText(previous.getStringExtra("Name"));
            this.amount.setText(previous.getStringExtra("Amount"));
            this.setButton.setText("Save");
<<<<<<< HEAD
            this.deleteButton.setVisibility(View.VISIBLE);
            this.addToMap.setVisibility(View.VISIBLE);
        }
        if (previous.getExtras().containsKey("RecurPosition")) {
            this.position = previous.getIntExtra("RecurPosition", 0);
        } else if (previous.getExtras().containsKey("Position")) {
=======
>>>>>>> 1d8792acf755b3d8c1c063bdfdfb04ae54568c0d
            this.position = previous.getIntExtra("Position", 0);
        }
    }

    public void onClick(View v) {

        if (v == setButton) {
            EditText nameView = (EditText) findViewById(R.id.name);
            EditText amountView = (EditText) findViewById(R.id.amount);

            String name = nameView.getText().toString();
            String amountStr = amountView.getText().toString();
            double amount = Double.parseDouble(amountStr);

            Intent answer = new Intent();
            answer.putExtra("transName", name);
            answer.putExtra("amount", amount);
            answer.putExtra("Position", position);
            setResult (RESULT_OK, answer); // pass the result to the caller of this activity

        } else if (v == deleteButton) {


        }

        //What if statement to put this under?
        MainActivity.SaveUtility saver = new MainActivity.SaveUtility();
        saver.saveNewTransaction();



        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;
        //int allocated = myDb.getTotalAllocated();

        //SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        //int curBudget = preferences.getInt("curBudget", 0);

        /*
        // basic input validation
        if(amountStr.equals("") || name.equals("")) {

            text = "Invalid input, please try again!";
            Toast.makeText(context, text, duration).show();

        } else if(!(name.replaceAll("\\s+", "")).matches("[a-zA-z]+")) {

            text = "Item name can only contain letters, please try again!";
            Toast.makeText(context, text, duration).show();

        } else {

            if(allocated == curBudget) {
                text = "Current budget has already been completely allocated";
                Toast.makeText(context, text, duration).show();
                finish();
            } else if((allocated + Integer.parseInt(amountStr)) > curBudget) {
                text = "Amount exceeds remaining allocatable budget, please try again!";
                Toast.makeText(context, text, duration).show();
            } else {

                if(myDb.checkNameExists(name)) {
                    text = "Item with that name already exists, please try again!";
                    Toast.makeText(context, text, duration).show();
                } else {
                    myDb.insertLineItem(name, Integer.parseInt(amountStr), 0);

                    text = "Item added. $" + Integer.toString(curBudget - myDb.getTotalAllocated()) + ".00 remaining to be allocated.";
                    duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, text, duration).show();
                    finish();
                }

            }
<<<<<<< HEAD

        } else if (v == this.addToMap) {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("AddMarker", name);
            startActivity(intent);
        }
=======

        }

        */
>>>>>>> 1d8792acf755b3d8c1c063bdfdfb04ae54568c0d
        finish();
    }

}
