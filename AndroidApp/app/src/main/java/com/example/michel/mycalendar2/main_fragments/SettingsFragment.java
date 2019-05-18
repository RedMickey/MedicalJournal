package com.example.michel.mycalendar2.main_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.LoginActivity;
import com.example.michel.mycalendar2.activities.MainActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.activities.UserActivity;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.utils.GFitUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.HealthDataTypes;

public class SettingsFragment extends Fragment {
    private static final int REQUEST_OAUTH_REQUEST_CODE = 5541;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private Switch switchGFitEnabled;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = getView();
        mView=inflater.inflate(R.layout.settings_fragment, container, false);

        ((RelativeLayout)mView.findViewById(R.id.account_profile_set)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountGeneralUtils.curUser.getId() == 1){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, MainActivity.getRESULT_SIGN_IN_CODE());
                }
                else {
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    getActivity().startActivityForResult(intent, MainActivity.getRESULT_USER_CONFIG_CODE());
                }
            }
        });

        switchGFitEnabled = (Switch)mView.findViewById(R.id.switch_gf_enabled);

        if (GFitUtils.checkSignedIn(getContext())){
            switchGFitEnabled.setChecked(true);
        }

        switchGFitEnabled.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            loginGFit();
                        }
                        else {
                            logoutGFit();
                        }
                    }
                }
        );

        return mView;
    }

    public void loginGFit(){
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)
                .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ)
                .build();
        GoogleSignIn.requestPermissions(
                this,
                REQUEST_OAUTH_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this.getContext()),
                fitnessOptions);

    }

    public void logoutGFit(){
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this.getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignInClient.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                Log.i("GFIT", "Successful login");
            }
        }
        else
            switchGFitEnabled.setChecked(false);
    }
}
