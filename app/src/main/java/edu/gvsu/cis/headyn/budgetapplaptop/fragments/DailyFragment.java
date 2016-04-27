package edu.gvsu.cis.headyn.budgetapplaptop.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.gvsu.cis.headyn.budgetapplaptop.AddItemActivity;
import edu.gvsu.cis.headyn.budgetapplaptop.DailyTransactions;
import edu.gvsu.cis.headyn.budgetapplaptop.ItemDetailActivity;
import edu.gvsu.cis.headyn.budgetapplaptop.ItemDetailFragment;
import edu.gvsu.cis.headyn.budgetapplaptop.MainActivity;
import edu.gvsu.cis.headyn.budgetapplaptop.R;
import edu.gvsu.cis.headyn.budgetapplaptop.RecurringTransactions;

/**
 * Created by crazy_000 on 4/1/2016.
 */

public class DailyFragment extends Fragment {

    private boolean mTwoPane;

    public DailyFragment(){}

    public View recyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_daily, container, false);

        recyclerView = myView.findViewById(R.id.daily_item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (myView.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        return myView;
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DailyTransactions.dailyItems));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DailyTransactions.DailyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DailyTransactions.DailyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).name);
            double total = mValues.get(position).amount;
            holder.mContentView.setText(String.format("%1$,.2f", total));

            final String itemName = mValues.get(position).name;
            final String itemAmount = String.format("%1$,.2f", total);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.name);
                        Fragment fragment = new Fragment();
                        fragment.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, AddItemActivity.class);
                        intent.putExtra("Previous Activity", "DailyFragment");
                        intent.putExtra("Name", itemName);
                        intent.putExtra("Amount", itemAmount);
                        intent.putExtra("Position", position);

                        //getActivity().startActivityForResult(intent, 0xFACD);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DailyTransactions.DailyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
