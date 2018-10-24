package com.example.michel.mycalendar2.auxiliary_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michel.mycalendar2.activities.R;

public class ReminderMedicineListFragment extends Fragment {
    public static ReminderMedicineListFragment newInstance() {
        return new ReminderMedicineListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView();
        view=inflater.inflate(R.layout.reminder_list, container, false);

        return view;
    }
}
