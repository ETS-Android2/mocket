package com.rms.mocket.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rms.mocket.R;
import com.rms.mocket.common.Checker;
import com.rms.mocket.common.Utils;

public class SignUpActivity extends AppCompatActivity {

    public int verificationNumber = 0;
    public boolean verified = false;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.SIGNUP_toolbar);
        this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /* Load profile image from local device and set on the ImageView. */
    public void addProfileImage(View v) {
        //TODO: load image from local device and set it on the ImageView.
        Toast.makeText(this, "Add image clicked!!", Toast.LENGTH_LONG).show();
    }


    /* Send verification code. */
    public void send(View v) {
        /* Generate random 6 digits number */
        verificationNumber = Utils.generateVerificationCode();
        email = ((EditText) findViewById(R.id.SIGNUP_editText_email)).getText().toString();

        if (Checker.checkEmailValidation(email)) {
            //TODO: Hide verification code after debugging.
            String message = "Verification code has been sent." + verificationNumber;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            Utils.sendEmail(email, message);
        } else {
            String temp_message = "Invalid Email address.";
            Toast.makeText(this, temp_message, Toast.LENGTH_LONG).show();
        }
    }


    /* Verify if the given code is correct. */
    public void verify(View v) {

        EditText editText_verify = (EditText) findViewById(R.id.SIGNUP_editText_verificationCode);
        String str_verificationNumberGiven = editText_verify.getText().toString();

        if (!Checker.checkIfNumber(str_verificationNumberGiven)) {
            String message = "Invalid verification code.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        int verificationNumberGiven = Integer.parseInt(str_verificationNumberGiven);

        if (verificationNumberGiven != verificationNumber) {
            String message = "Invalid verification code.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        } else {
            /* Code has been verified */
            verified = true;

            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setTitle("Verification");
            builder.setMessage("The Email has been verified.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            /* Set Email address and verification code not editable.  */
            EditText editText_email = (EditText) findViewById(R.id.SIGNUP_editText_email);
            Button button_send = (Button) findViewById(R.id.SIGNUP_button_send);
            Button button_verify = (Button) findViewById(R.id.SIGNUP_button_verify);
            editText_email.setEnabled(false);
            editText_verify.setEnabled(false);
            button_send.setText("Sent");
            button_verify.setText("Verified");
            button_send.setClickable(false);
            button_verify.setClickable(false);

        }
    }


    /* SignUp with the given information and update to database. */
    public void signUp(View v) {
        String firstName = ((EditText) findViewById(R.id.SIGNUP_editText_firstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.SIGNUP_editText_lastName)).getText().toString();

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

        String password1 = ((EditText) findViewById(R.id.SIGNUP_editText_password1)).getText().toString();
        String password2 = ((EditText) findViewById(R.id.SIGNUP_editText_password2)).getText().toString();

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
            this.signUp(email, password, firstName, lastName);
        }

    }

    public boolean signUp(String email, String password, String firstName, String lastName) {
        //TODO: Signup to Database
        return true;
    }
}
