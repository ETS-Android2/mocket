package com.rms.mocket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rms.mocket.R;
import com.rms.mocket.common.Checker;
import com.rms.mocket.common.Utils;


public class ForgotPasswordActivity extends AppCompatActivity {

    public int verificationNumber = 0;
    public CountDownTimer timer;
    public int timerCount;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.FORGOTPASSWORD_toolbar);
        this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        final TextView textView_verify = (TextView) findViewById(R.id.FORGOTPASSWORD_textView_timeLeft);

        timer = new CountDownTimer(300000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerCount = (int) (millisUntilFinished / 1000);
                textView_verify.setText("Time remaining: " + millisUntilFinished / 1000 + " sec");
            }

            public void onFinish() {
                timerCount = 0;
                textView_verify.setText("Verification code has been expired.");
            }
        };

    }


    /* OnClick: Send button is clicked. */
    public void sendVerificationCode(View v) {
        EditText editText_verifyEmail = (EditText) findViewById(R.id.FORGOTPASSWORD_editText_verifyEmail);
        EditText editText_verify = (EditText) findViewById(R.id.FORGOTPASSWORD_editText_verificationCode);
        Button button_verify = (Button) findViewById(R.id.FORGOTPASSWORD_button_verificationCode);
        email = editText_verifyEmail.getText().toString();
        final TextView textView_verify = (TextView) findViewById(R.id.FORGOTPASSWORD_textView_timeLeft);

        if (Checker.checkEmailValidation(email)) {
            LinearLayout sendLayout = (LinearLayout) findViewById(R.id.FORGOTPASSWORD_verifyLayout);
            sendLayout.setVisibility(LinearLayout.VISIBLE);

            this.sendVerficationCodeToEmail(email);

            /* Start countdown for 30 seconds */
            timer.cancel();
            timer.start();

        } else {
            String message = "Invalid Email address.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }


    /* Send the verification code to email */
    public void sendVerficationCodeToEmail(String email) {
        /* Generate random 6 digits number */
        verificationNumber = Utils.generateVerificationCode();

        //TODO: Hide verification code after debugging.
        String message = "Verification code has been sent." + verificationNumber;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        //TODO: Set what to send on the mail.
        String messageToEmail = "";

        Utils.sendEmail(email, messageToEmail);

    }


    /* OnClick: Verify button is clicked.  */
    public void verifyCode(View v) {
        /* When timer is over, invalid. */
        if (!(timerCount > 0)) {
            String message = "Expired verification code.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {

            EditText editText_verify = (EditText) findViewById(R.id.FORGOTPASSWORD_editText_verificationCode);
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
                /* Move to ChangePasswordActivity */
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);

            }

        }
    }
    public void goPreviousActivity(View v){
        onBackPressed();
    }

}
