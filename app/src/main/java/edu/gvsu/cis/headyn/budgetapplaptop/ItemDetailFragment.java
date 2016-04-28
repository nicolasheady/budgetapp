package edu.gvsu.cis.headyn.budgetapplaptop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    EditText item_detail;
    EditText item_amount;
    int position;
    public boolean isEditing;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private RecurringTransactions.RecurringItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isEditing = true;
        position = getArguments().getInt("ListPosition", 0);

        System.out.println("ITEM DETAIL FRAGMENT ListPosition added: " + position);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = RecurringTransactions.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            mItem = RecurringTransactions.recurringItems.get(position);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.categoryName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        item_detail = (EditText) rootView.findViewById(R.id.item_detail);
        item_amount = (EditText) rootView.findViewById(R.id.item_amount);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            item_detail.setText(mItem.categoryName);
            ((TextView) rootView.findViewById(R.id.item_dollar)).setText("$");
            item_amount.setText(Double.toString(mItem.totalAmount));
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isEditing) {
            String catName = item_detail.getText().toString();
            double catAmount = Double.parseDouble(item_amount.getText().toString());

            MainActivity.SaveUtility saver = new MainActivity.SaveUtility(getContext());
            saver.changeRecurring(position, catAmount, catName);
        }
    }
}