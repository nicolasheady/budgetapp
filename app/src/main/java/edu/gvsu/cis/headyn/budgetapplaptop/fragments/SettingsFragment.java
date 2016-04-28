package edu.gvsu.cis.headyn.budgetapplaptop.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.gvsu.cis.headyn.budgetapplaptop.GoogleSignIn;
import edu.gvsu.cis.headyn.budgetapplaptop.R;

/**
 * Created by crazy on 4/1/2016.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button googleButton;

    public SettingsFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_settings, container, false);

        googleButton = (Button) myView.findViewById(R.id.googleButton);
        googleButton.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v == googleButton) {
            Context context = v.getContext();
            Intent intent = new Intent(context, GoogleSignIn.class);
            startActivity(intent);
        }
    }
}
