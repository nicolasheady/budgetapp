package edu.gvsu.cis.headyn.budgetapplaptop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
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

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

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
    private int position;
    private int subItemPosition;
    private String previousAct;

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
        this.deleteButton.setVisibility(View.GONE);
        this.deleteButton.setOnClickListener(this);
        this.setButton.setOnClickListener(this);

        Intent previous = getIntent();
        if (previous.getExtras().containsKey("Previous Activity")) {
            previousAct = previous.getStringExtra("Previous Activity");
        } else {
            previousAct = "None";
        }

        // Change titles and variables appropriately:
        if (!previousAct.equals("AddDaily") && !previousAct.equals("AddSubItem") && !previousAct.equals("AddRecurring")) {
            this.name.setText(previous.getStringExtra("Name"));
            this.amount.setText(previous.getStringExtra("Amount"));
            this.setButton.setText("Save");
            this.deleteButton.setVisibility(View.VISIBLE);
        }
        if (previous.getExtras().containsKey("RecurPosition")) {
            this.position = previous.getIntExtra("RecurPosition", 0);
        } else if (previous.getExtras().containsKey("Position")) {
            this.position = previous.getIntExtra("Position", 0);
        }
        if (previous.getExtras().containsKey("SubItemPosition")) {
            this.subItemPosition = previous.getIntExtra("SubItemPosition", 0);
        }
    }

    public void onClick(View v) {

        EditText nameView = (EditText) findViewById(R.id.name);
        EditText amountView = (EditText) findViewById(R.id.amount);

        String name = nameView.getText().toString();
        String amountStr = amountView.getText().toString();
        double amount = Double.parseDouble(amountStr);

        MainActivity.SaveUtility saver = new MainActivity.SaveUtility(this);

        if (v == this.setButton) {

            System.out.println("SET BUTTON PRESSED!");

            if (previousAct.equals("DailyFragment")) {
                saver.changeDaily(position, amount, name);
            } else if (previousAct.equals("AddDaily")) {
                saver.addDaily(amount, name);
            } else if (previousAct.equals("AddRecurring")) {
                saver.addRecurring(amount, name);
            }  else if (previousAct.equals("RecurringTransFragment")) {
                saver.changeSubItem(this.position, this.subItemPosition, amount, name);
            } else if (previousAct.equals("AddSubItem")) {
                saver.addSubItem(this.position, amount, name);
            }

        } else if (v == this.deleteButton) {

            System.out.println("DELETE BUTTON PRESSED!");

            if (previousAct.equals("DailyFragment")) {
                saver.deleteDaily(position);
            } else if (previousAct.equals("RecurringTransFragment")) {
                saver.deleteSubItem(position, subItemPosition);
            }
        }
        System.out.println("PPPPPPPPreviousAct : " + this.previousAct);

        finish();
    }
}