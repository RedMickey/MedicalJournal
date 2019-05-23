package com.example.michel.mycalendar2.activities;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michel.mycalendar2.controllers.app_async_tasks.MeasurementNotificationsCreationTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.PillNotificationsCreationTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.UserGlobalUpdateTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.UserLocalUpdateTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.synchronization.DownloadUserDataTask;
import com.example.michel.mycalendar2.services.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.models.User;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Spinner birthdayYearSpinner;
    private Spinner gendersSpinner;
    private EditText usernameRegEt;
    private EditText userSurnameRegEt;
    private EditText emailEt;
    private Button changePasswordButton;
    private String password = "";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Профиль");

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        password ="";
        birthdayYearSpinner = (Spinner) findViewById(R.id.birthday_year_spinner);
        ArrayList<String> years = new ArrayList<String>();
        int endYear = Calendar.getInstance().get(Calendar.YEAR)-10;
        int startYear = endYear-110;

        for (int i = endYear; i > startYear; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        birthdayYearSpinner.setAdapter(adapter);

        gendersSpinner = (Spinner) findViewById(R.id.genders_spinner);

        usernameRegEt = (EditText) findViewById(R.id.username_reg_et);

        userSurnameRegEt = (EditText) findViewById(R.id.usersurname_reg_et);

        emailEt = (EditText) findViewById(R.id.email_et);

        gendersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ((CircleImageView)findViewById(R.id.user_profile_icon))
                                .setImageResource(R.drawable.avatar2);
                        break;
                    case 1:
                        ((CircleImageView)findViewById(R.id.user_profile_icon))
                                .setImageResource(R.drawable.boy_avatar);
                        break;
                    case 2:
                        ((CircleImageView)findViewById(R.id.user_profile_icon))
                                .setImageResource(R.drawable.girl_avatar);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        usernameRegEt.setText(AccountGeneralUtils.curUser.getName());
        userSurnameRegEt.setText(AccountGeneralUtils.curUser.getSurname());
        emailEt.setText(AccountGeneralUtils.curUser.getEmail());
        gendersSpinner.setSelection(AccountGeneralUtils.curUser.getGenderId()-1);
        int spinnerPosition = ((ArrayAdapter) birthdayYearSpinner.getAdapter())
                .getPosition(String.valueOf(AccountGeneralUtils.curUser.getBirthdayYear()));
        birthdayYearSpinner.setSelection(spinnerPosition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.log_out:
                AccountGeneralUtils.curUser.setIsCurrent(0);
                UserLocalUpdateTask userUpdateTask = new UserLocalUpdateTask(1);
                userUpdateTask.execute(AccountGeneralUtils.curUser);
                // cancel previous user notifications
                MeasurementNotificationsCreationTask preMnct = new MeasurementNotificationsCreationTask(
                        1, AccountGeneralUtils.curUser.getId());
                preMnct.execute(this);
                PillNotificationsCreationTask prePnct = new PillNotificationsCreationTask(
                        1, AccountGeneralUtils.curUser.getId());
                prePnct.execute(this);
                AccountManager accountManager = AccountManager.get(this);
                accountManager.removeAccountExplicitly(AccountGeneralUtils.curAccount);
                AccountGeneralUtils.curUser = new User();
                AccountGeneralUtils.curToken = null;
                AccountGeneralUtils.curAccount = null;
                // set up notification for default user
                MeasurementNotificationsCreationTask newMnct = new MeasurementNotificationsCreationTask(2);
                newMnct.execute(this);
                PillNotificationsCreationTask newPnct = new PillNotificationsCreationTask(2);
                newPnct.execute(this);
                Intent data = new Intent();
                data.putExtra("is_log_out",true);
                setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSaveConfigButtonClick(View view) {
        User updatedUser = new User();
        updatedUser.setIsCurrent(1);

        boolean isCorrect = validateInputForm(updatedUser);
        if (isCorrect) {
            UserGlobalUpdateTask userGlobalUpdateTask = new UserGlobalUpdateTask(getBaseContext());
            userGlobalUpdateTask.execute(updatedUser);
        }
    }

    private boolean validateInputForm(User updatedUser){
        boolean isCorrect = true;
        View focusView = null;

        // Reset errors.
        usernameRegEt.setError(null);
        userSurnameRegEt.setError(null);
        emailEt.setError(null);

        updatedUser.setId(AccountGeneralUtils.curUser.getId());
        updatedUser.setName(usernameRegEt.getText().toString());
        updatedUser.setSurname(userSurnameRegEt.getText().toString());
        updatedUser.setGenderId(gendersSpinner.getSelectedItemPosition()+1);
        updatedUser.setBirthdayYear(Integer.parseInt((String) birthdayYearSpinner.getSelectedItem()));
        updatedUser.setEmail(emailEt.getText().toString());

        if (password.equals(""))
            password = AccountManager.get(getBaseContext()).getPassword(AccountGeneralUtils.curAccount);

        updatedUser.setPassword(password);

        if (TextUtils.isEmpty(updatedUser.getName())) {
            usernameRegEt.setError(getString(R.string.error_field_required));
            focusView = usernameRegEt;
            isCorrect = false;
        }
        if (TextUtils.isEmpty(updatedUser.getSurname())) {
            userSurnameRegEt.setError(getString(R.string.error_field_required));
            focusView = userSurnameRegEt;
            isCorrect = false;
        }
        if (TextUtils.isEmpty(updatedUser.getEmail())) {
            emailEt.setError(getString(R.string.error_field_required));
            focusView = emailEt;
            isCorrect = false;
        } else if (!updatedUser.getEmail().contains("@")) {
            emailEt.setError(getString(R.string.error_invalid_email));
            focusView = emailEt;
            isCorrect = false;
        }

        if (!isCorrect){
            focusView.requestFocus();
        }

        return isCorrect;
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("is_log_out",false);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }

    public void onChangePasswordButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.change_password_dialog_layout, null, false);

        final EditText passwordEt = (EditText) dialogView.findViewById(R.id.password_et);
        final EditText secondPasswordEt = (EditText) dialogView.findViewById(R.id.second_password_et);

        builder.setView(dialogView)
                .setTitle("Изменить пароль")
                .setCancelable(false)
                .setPositiveButton("Изменить", null)
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordBuf = passwordEt.getText().toString();
                String secondPasswordBuf = secondPasswordEt.getText().toString();
                boolean isCorrect = true;
                View focusView = null;

                if (passwordBuf.equals("") || passwordBuf.length()<4) {
                    passwordEt.setError(getString(R.string.error_invalid_password));
                    focusView = passwordEt;
                    isCorrect = false;
                }
                if (secondPasswordBuf.equals("") || secondPasswordBuf.length()<4) {
                    secondPasswordEt.setError(getString(R.string.error_invalid_password));
                    focusView = secondPasswordEt;
                    isCorrect = false;
                }

                if (!isCorrect){
                    focusView.requestFocus();
                }

                if (!passwordBuf.equals(secondPasswordBuf)){
                    isCorrect = false;
                    Toast.makeText(v.getContext(),"Введённые пароли не совпадают", Toast.LENGTH_SHORT).show();
                }

                if (isCorrect){
                    password = passwordBuf;
                    dialog.dismiss();
                }
            }
        });
    }

    public Spinner getBirthdayYearSpinner() {
        return birthdayYearSpinner;
    }

    public Spinner getGendersSpinner() {
        return gendersSpinner;
    }

    public EditText getEmailEt() {
        return emailEt;
    }

    public EditText getUsernameRegEt() {
        return usernameRegEt;
    }

    public EditText getUserSurnameRegEt() {
        return userSurnameRegEt;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public void onRefresh() {
        DownloadUserDataTask downloadUserDataTask = new DownloadUserDataTask(this);
        downloadUserDataTask.execute();
    }
}
