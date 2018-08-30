package com.rms.mocket.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rms.mocket.R;
import com.rms.mocket.common.Checker;


public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.FORGOTPASSWORD_toolbar);
        this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    /* OnClick: Send button is clicked. */
    public void sendResetLink(View v) {
        EditText editText_resetEmail = (EditText) findViewById(R.id.FORGOTPASSWORD_editText_resetEmail);
        String email = editText_resetEmail.getText().toString();

        ProgressDialog progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Checking email address...");
        progressDialog.show();

        if (Checker.checkEmailValidation(email)) {

            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                String message = "Reset link has been sent.";
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                            else{
                                progressDialog.dismiss();
                                String err_message = task.getException().toString();
                                if(err_message.contains("There is no user record")){
                                    String message = "A user with the email address does not exist.";
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

        } else {
            progressDialog.dismiss();
            String message = "Invalid Email address.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }


    public void goPreviousActivity(View v){
        onBackPressed();
    }

}
