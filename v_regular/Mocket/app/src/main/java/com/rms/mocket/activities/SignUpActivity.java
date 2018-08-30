package com.rms.mocket.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rms.mocket.R;
import com.rms.mocket.common.Checker;
import com.rms.mocket.common.Utils;
import com.rms.mocket.object.User;

public class SignUpActivity extends AppCompatActivity {
    private final int RESULT_LOAD_IMAGE = 1;

    public boolean verified = false;
    String password;

    ImageView imageView_profile;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.SIGNUP_toolbar);
        this.setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imageView_profile = (ImageView) findViewById(R.id.SIGNUP_imageView_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

    }

    /* Load profile image from local device and set on the ImageView. */
    public void addProfileImage(View v) {
        Intent profileIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(profileIntent, RESULT_LOAD_IMAGE);
    }


    public void check(View v) {
        /* Generate random 6 digits number */
        String email = ((EditText) findViewById(R.id.SIGNUP_editText_email)).getText().toString();
        ProgressDialog progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Checking email address...");
        progressDialog.show();

        if (Checker.checkEmailValidation(email)) {
//            //TODO: Hide verification code after debugging.
//            String message = "Verification code has been sent." + verificationNumber;
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//            Utils.sendEmail(email, message);
            mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    boolean check = !task.getResult().getProviders().isEmpty();
                    if(!check){
                        Toast.makeText(SignUpActivity.this, "You can use this email.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(SignUpActivity.this, "The email is already registered.", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }
            });

        } else {
            String temp_message = "Invalid Email address.";
            Toast.makeText(this, temp_message, Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
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
            String email = ((EditText) findViewById(R.id.SIGNUP_editText_email)).getText().toString();
            this.signUp(email, password, firstName, lastName, Utils.getEncodedImage(imageView_profile));
        }

    }

    public void signUp(String email, String password, String first_name, String last_name, String profile_image) {
        /* Signup on Firebase */
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering, please Wait...");
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String user_id = mAuth.getCurrentUser().getUid();

                            User user = new User(first_name, last_name, profile_image);
                            mDatabase.child(User.REFERENCE_USERS).child(user_id).setValue(user);

                            Toast.makeText(getApplicationContext(), "Welcome to Mocket!",
                                    Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        } else {
                            progressDialog.dismiss();

                            Utils.log(task.getException().toString());
                            String err_message = task.getException().toString();
                            String message = "";
                            if(err_message.contains("The email address is already in use")){
                                message = "The email is already registered.";
                            }else if(err_message.contains("The email address is badly formatted")){
                                message = "The email address is badly formatted.";

                            }
                            Toast.makeText(getApplicationContext(), message,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });


    }
    public void goPreviousActivity(View v){
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null){
            Uri selectedImage = data.getData();

//            Glide.with(this).load(new File(String.valueOf(selectedImage))).into(imageView_profile);
            imageView_profile.setImageURI(selectedImage);
        }
    }
}
