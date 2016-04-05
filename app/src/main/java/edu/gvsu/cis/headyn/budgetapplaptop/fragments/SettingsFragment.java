package edu.gvsu.cis.headyn.budgetapplaptop.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.gvsu.cis.headyn.budgetapplaptop.R;

/**
 * Created by crazy on 4/1/2016.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_settings, container, false);


        return myView;
    }
}
