package com.rms.mocket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rms.mocket.R;
import com.rms.mocket.common.Checker;

public class ChangePasswordActivity extends AppCompatActivity {

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.CHANGEPASSWORD_toolbar);
        this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        email = getIntent().getStringExtra("email");


    }

    public void changePassword(View v) {
        EditText editText_password1 = (EditText) findViewById(R.id.CHANGEPASSWORD_editText_password1);
        EditText editText_password2 = (EditText) findViewById(R.id.CHANGEPASSWORD_editText_password2);
        String password1 = editText_password1.getText().toString();
        String password2 = editText_password2.getText().toString();

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
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                builder.setTitle("Change password");
                builder.setMessage("Password has been changed. \nLogin with new password.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //TODO: Save the email and the password to the database.

                        /* Move to LoginActivity */
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    public void goPreviousActivity(View v){
        onBackPressed();
    }
}
