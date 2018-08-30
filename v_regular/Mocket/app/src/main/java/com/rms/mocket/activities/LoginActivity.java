package com.rms.mocket.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rms.mocket.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_emailAddress;
    private EditText editText_password;
    private TextView textView_forgot;
    private Button button_signIn;
    private Button button_register;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logo);

        editText_emailAddress = (EditText) findViewById(R.id.LOGIN_editText_emailAddress);
        editText_password = (EditText) findViewById(R.id.LOGIN_editText_password);
        button_register = (Button) findViewById(R.id.button_signUp);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onResume(){
        super.onResume();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                /* When the user is logged in, move on to MainActivity. */
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    if(!user.isEmailVerified()){
                        //set the new Content of your activity
                        LoginActivity.this.setContentView(R.layout.activity_login);
                        button_signIn = (Button) findViewById(R.id.LOGIN_button_signIn);
                        FirebaseAuth.getInstance().signOut();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }else{
                    //set the new Content of your activity
                    LoginActivity.this.setContentView(R.layout.activity_login);
                    button_signIn = (Button) findViewById(R.id.LOGIN_button_signIn);
                }
            }
        }.start();
    }


    /* Check if email address and password are correct */
    public void checkAccount(View v) {
        editText_emailAddress = (EditText) findViewById(R.id.LOGIN_editText_emailAddress);
        editText_password = (EditText) findViewById(R.id.LOGIN_editText_password);
        String emailAddress = editText_emailAddress.getText().toString();
        String password = editText_password.getText().toString();

        if(emailAddress.equals("") || password.equals("")){
            String message = "Please enter valid email and password.";
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /* When the user is logged in, move on to MainActivity. */
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                progressDialog.dismiss();
                                if(!user.isEmailVerified()){
                                    user.sendEmailVerification();
                                    String message = "A verification link sent. Please check the email.";
                                    Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
                                    FirebaseAuth.getInstance().signOut();
                                }else{
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }

                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            String err_message = task.getException().toString();
                            String message="";
                            if(err_message.contains("The password is invalid")){
                                message = "The password is invalid.";
                            }else if(err_message.contains("The email address is badly formatted.")){
                                message = "The email address is badly formatted.";
                            }else if(err_message.contains("There is no user record")){
                                message = "There is no such user registered.";
                            }
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
