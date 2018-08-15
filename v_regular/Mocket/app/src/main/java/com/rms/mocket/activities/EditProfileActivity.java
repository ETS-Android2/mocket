package com.rms.mocket.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rms.mocket.R;
import com.rms.mocket.common.Checker;

public class EditProfileActivity extends AppCompatActivity {

    public int verificationNumber = 0;
    public boolean verified = false;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.EDITPROFILE_toolbar);
        this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /* Load profile image from local device and set on the ImageView. */
    public void addProfileImage(View v) {
        //TODO: load image from local device and set it on the ImageView.
        Toast.makeText(this, "Add image clicked!!", Toast.LENGTH_LONG).show();
    }


    /* SignUp with the given information and update to database. */
    public void saveProfile(View v) {
        String firstName = ((EditText) findViewById(R.id.EDITPROFILE_editText_firstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.EDITPROFILE_editText_lastName)).getText().toString();

        if ((!Checker.checkName(firstName)) || (!Checker.checkName(lastName))) {
            String message = "The given name contains non-letter.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        if (firstName.equals("") || lastName.equals("")) {
            String message = "The given name is empty.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        String password1 = ((EditText) findViewById(R.id.EDITPROFILE_editText_password1)).getText().toString();
        String password2 = ((EditText) findViewById(R.id.EDITPROFILE_editText_password2)).getText().toString();

        if (!password1.equals(password2)) {
            String message = "Passwords are not matched.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        } else {
            password = password1;
            if (Checker.checkWeakPassword(password)) {
                String message = "Passwords are too weak.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                return;
            }


            //TODO: Signup on Database. and upload profile image!!!
            this.saveProfile(email, password, firstName, lastName);
        }

    }

    public boolean saveProfile(String email, String password, String firstName, String lastName) {
        //TODO: Savev profile to Database
        return true;
    }

    public void changePassword(View v) {
        // TODO: Change password

    }
    public void goPreviousActivity(View v){
        onBackPressed();
    }
}
