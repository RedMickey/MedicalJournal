package com.example.michel.mycalendar2.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReminderListFragment extends Fragment {
    public static ReminderListFragment newInstance() {
        return new ReminderListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView();

        view=inflater.inflate(R.layout.reminder_list, container, false);
        return view;
    }
}
