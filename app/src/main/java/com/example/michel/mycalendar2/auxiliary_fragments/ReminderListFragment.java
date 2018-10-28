package com.example.michel.mycalendar2.auxiliary_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.app_async_tasks.ReminderMedicineItemsCreationTask;

public class ReminderListFragment extends Fragment {
    private int fragmentType = 0;

    public static ReminderListFragment newInstance(int fragmentType) {
        ReminderListFragment rlf = new ReminderListFragment();
        rlf.setFragmentType(fragmentType);
        return rlf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView();
        view=inflater.inflate(R.layout.reminder_list, container, false);
        if (fragmentType==0){
            ReminderMedicineItemsCreationTask rmict = new ReminderMedicineItemsCreationTask(view);
            rmict.execute();
        }

        return view;
    }

    public int getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(int fragmentType) {
        this.fragmentType = fragmentType;
    }
}
