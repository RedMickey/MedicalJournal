package com.example.michel.mycalendar2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.michel.mycalendar2.app_async_tasks.UserSignUpTask;
import com.example.michel.mycalendar2.models.User;

import java.util.ArrayList;
import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    private Spinner birthdayYearSpinner;
    private Spinner gendersSpinner;
    private ToggleButton passwordVisibilityButton;
    private EditText passwordEt;
    private EditText usernameRegEt;
    private EditText userSurnameRegEt;
    private EditText emailEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        birthdayYearSpinner = (Spinner) findViewById(R.id.birthday_year_spinner);
        ArrayList<String> years = new ArrayList<String>();
        int endYear = Calendar.getInstance().get(Calendar.YEAR)-10;
        int startYear = endYear-100;

        for (int i = endYear; i > startYear; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        birthdayYearSpinner.setAdapter(adapter);

        passwordVisibilityButton = (ToggleButton) findViewById(R.id.password_visibility_button);

        passwordEt = (EditText) findViewById(R.id.password_et);

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);

        gendersSpinner = (Spinner) findViewById(R.id.genders_spinner);

        usernameRegEt = (EditText) findViewById(R.id.username_reg_et);

        userSurnameRegEt = (EditText) findViewById(R.id.usersurname_reg_et);

        emailEt = (EditText) findViewById(R.id.email_et);

        passwordVisibilityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndSendSignUpRequest();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submit) {
            createAndSendSignUpRequest();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAndSendSignUpRequest(){
        User newUser = new User();
        boolean isCorrect = validateInputForm(newUser);
        if (isCorrect) {
            UserSignUpTask userSignUpTask = new UserSignUpTask(this);
            userSignUpTask.execute(newUser);
        }

    }

    private boolean validateInputForm(User newUser){
        boolean isCorrect = true;
        View focusView = null;

        // Reset errors.
        passwordEt.setError(null);
        usernameRegEt.setError(null);
        userSurnameRegEt.setError(null);
        emailEt.setError(null);

        newUser.setName(usernameRegEt.getText().toString());
        newUser.setSurname(userSurnameRegEt.getText().toString());
        newUser.setGenderId(gendersSpinner.getSelectedItemPosition()+1);
        newUser.setBirthdayYear(Integer.parseInt((String) birthdayYearSpinner.getSelectedItem()));
        newUser.setEmail(emailEt.getText().toString());
        newUser.setPassword(passwordEt.getText().toString());

        if (TextUtils.isEmpty(newUser.getName())) {
            usernameRegEt.setError(getString(R.string.error_field_required));
            focusView = usernameRegEt;
            isCorrect = false;
        }
        if (TextUtils.isEmpty(newUser.getSurname())) {
            userSurnameRegEt.setError(getString(R.string.error_field_required));
            focusView = userSurnameRegEt;
            isCorrect = false;
        }
        if (TextUtils.isEmpty(newUser.getEmail())) {
            emailEt.setError(getString(R.string.error_field_required));
            focusView = emailEt;
            isCorrect = false;
        } else if (!newUser.getEmail().contains("@")) {
            emailEt.setError(getString(R.string.error_invalid_email));
            focusView = emailEt;
            isCorrect = false;
        }
        if (newUser.getPassword().equals("") || newUser.getPassword().length()<4) {
            passwordEt.setError(getString(R.string.error_invalid_password));
            focusView = passwordEt;
            isCorrect = false;
        }

        if (!isCorrect){
            focusView.requestFocus();
        }

        return isCorrect;
    }
}
