package com.example.michel.mycalendar2.adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.michel.mycalendar2.activities.R;

import java.util.Calendar;
import java.util.List;

public class TimesOfTakingMedicineAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;
    private int layout;
    private List<String> mTime;

    public TimesOfTakingMedicineAdapter(Context context, int resource, List<String> time) {
        super(context, resource, time);
        this.mTime = time;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View viewMain=inflater.inflate(this.layout, parent, false);

        final EditText editText = (EditText) viewMain.findViewById(R.id.reminder_time);

        final TextInputLayout textInputLayout = (TextInputLayout) viewMain.findViewById(R.id.reminder_time_InputLayout);

       /* editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
                public void onFocusChange(View view, boolean b) {*/

        editText.setOnClickListener(new View.OnClickListener() {
            //textInputLayout.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
                //if (b){
                    Calendar cal = Calendar.getInstance();

                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            editText.setText(String.format("%02d:%02d", i, i1));
                            editText.clearFocus();
                        }
                    }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                    //view.clearFocus();
                //}
            }
        });

        String time = this.mTime.get(position);

        editText.setText(time);

        return viewMain;
    }
}
