package com.example.michel.mycalendar2.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddTreatmentFragment extends Fragment {

    private View view;
    public AddTreatmentFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.add_pill_view, container, false);

        return view;
    }
}
