package com.rms.mocket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rms.mocket.R;
import com.rms.mocket.common.Checker;
import com.rms.mocket.database.DatabaseHandlerTerms;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_emailAddress;
    private EditText editText_password;
    private TextView textView_forgot;
    private Button button_signIn;
    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logo);

        editText_emailAddress = (EditText) findViewById(R.id.LOGIN_editText_emailAddress);
        editText_password = (EditText) findViewById(R.id.LOGIN_editText_password);
        button_register = (Button) findViewById(R.id.button_signUp);

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                //set the new Content of your activity
                LoginActivity.this.setContentView(R.layout.activity_login);
                button_signIn = (Button) findViewById(R.id.LOGIN_button_signIn);
//                button_signIn.getBackground().setColorFilter(230744, PorterDuff.Mode.MULTIPLY);
            }
        }.start();


    }


    /* Check if email address and password are correct */
    public void checkAccount(View v) {
        editText_emailAddress = (EditText) findViewById(R.id.LOGIN_editText_emailAddress);
        editText_password = (EditText) findViewById(R.id.LOGIN_editText_password);
        String emailAddress = editText_emailAddress.getText().toString();
        String password = editText_password.getText().toString();
        String message = "Invalid email address or password.";

        /* Check if email address or password is empty */
        if (!Checker.checkEmailValidation(emailAddress) || password.equals("")) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            //TODO: check with server database.
            if (DatabaseHandlerTerms.checkEmailAndPassword(emailAddress, password)) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("email", emailAddress);
                intent.putExtra("password", password);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }


    public void moveToForgotPasswordActivity(View v) {
        Intent i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i);
    }


    public void moveToRegisterActivity(View v) {
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }
}
