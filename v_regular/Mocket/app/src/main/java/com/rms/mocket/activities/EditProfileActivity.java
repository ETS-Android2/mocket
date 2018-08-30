package com.rms.mocket.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rms.mocket.R;
import com.rms.mocket.common.Checker;
import com.rms.mocket.common.Utils;
import com.rms.mocket.object.User;

public class EditProfileActivity extends AppCompatActivity {
    private final int RESULT_LOAD_IMAGE = 1;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    public boolean verified = false;

    ImageView imageView_profile;

    EditText editText_firstName;
    EditText editText_lastName;

    EditText editText_email;
    EditText editText_newPassword1;
    EditText editText_newPassword2;

    String user_id;
    User user;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.EDITPROFILE_toolbar);
        imageView_profile = (ImageView) findViewById(R.id.EDITPROFILE_imageView_profile);
        editText_email = (EditText) findViewById(R.id.EDITPROFILE_editText_email);

        editText_newPassword1 = (EditText) findViewById(R.id.EDITPROFILE_editText_password1);
        editText_newPassword2 = (EditText) findViewById(R.id.EDITPROFILE_editText_password2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        editText_firstName = (EditText) findViewById(R.id.EDITPROFILE_editText_firstName);
        editText_lastName = (EditText) findViewById(R.id.EDITPROFILE_editText_lastName);

        this.setSupportActionBar(myToolbar);
        this.setInitialSetting();
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if(!user.isEmailVerified()){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        } else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    private void setInitialSetting(){

        user_id = mAuth.getCurrentUser().getUid();


        mDatabase.child(User.REFERENCE_USERS).child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mAuth.getCurrentUser() == null) return;

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                String full_name = "";
                String first_name = "";
                String last_name = "";
                String profile_image = "";

                String setting_game = "";
                String setting_notification = "";
                String setting_vibration = "";
                String setting_gesture = "";


                for(DataSnapshot child: children) {
                    String key = child.getKey();
                    String value = child.getValue(String.class);
                    switch (key) {
                        case "first_name":
                            first_name = value;
                            full_name = value + full_name;
                            break;
                        case "last_name":
                            last_name = value;
                            full_name = full_name + value;
                            break;
                        case "profile_image":
                            /* Set profile image */
                            profile_image = value;
                            Utils.setEncodedImage(imageView_profile, value);

                            break;

                        case "setting_game":
                            setting_game = value;
                            break;

                        case "setting_gesture":
                            setting_gesture = value;
                            break;

                        case "setting_notification":
                            setting_notification = value;
                            break;

                        case "setting_vibration":
                            setting_vibration = value;
                            break;

                    }
                }

                user = new User(first_name, last_name, profile_image);
                user.setting_notification = setting_notification;
                user.setting_vibration = setting_vibration;
                user.setting_gesture = setting_gesture;
                user.setting_game = setting_game;


                /* Initialize name and email */
                email = mAuth.getCurrentUser().getEmail();
                editText_firstName.setText(user.first_name);
                editText_lastName.setText(user.last_name);
                editText_email.setText(email);

                return;

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    /* Load profile image from local device and set on the ImageView. */
    public void addProfileImage(View v) {
        Intent profileIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(profileIntent, RESULT_LOAD_IMAGE);
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

        //TODO: Sync with server database
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving profile...");
        progressDialog.show();

        user.first_name = editText_firstName.getText().toString();
        user.last_name = editText_lastName.getText().toString();
        user.profile_image = Utils.getEncodedImage(imageView_profile);
        mDatabase.child(User.REFERENCE_USERS).child(user_id).setValue(user);

        progressDialog.dismiss();

        String message = "Profile has been updated.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void changePassword(View v) {
        String password1 = editText_newPassword1.getText().toString();
        String password2 = editText_newPassword2.getText().toString();

        if (!password1.equals(password2)) {
            String message = "New Passwords are not matched.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating password...");
        progressDialog.show();

        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        final String email = u.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,"randomPassword");

        u.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                u.updatePassword(password1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            String message = "Failed to update password."+task.getException().toString();
                            Toast.makeText(v.getContext(), message, Toast.LENGTH_LONG).show();
                        }else {
                            String message = "Password has been updated.";
                            Toast.makeText(v.getContext(), message, Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

            }
        });
    }




    public void goPreviousActivity(View v){
        onBackPressed();
    }

    public void clickLogout(View v){
        FirebaseAuth.getInstance().signOut();

        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void clickDelete(View v){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.delete_account_dialogue, null);


        /* Initialize the blanks. */
        EditText editText_email = (EditText) mView.findViewById(R.id.DELETE_editText_email);

        Button button_cancel = (Button) mView.findViewById(R.id.DELETE_button_cancel);
        Button button_delete = (Button) mView.findViewById(R.id.DELETE_button_delete);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String user_id = user.getUid();
                String entered_email = editText_email.getText().toString();

                if(!email.equals(entered_email)){
                    String message = "Email address is not correct.";
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                    return;
                }


                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, "randomPassword");


                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mDatabase.child(User.REFERENCE_USERS).child(user_id).removeValue();

                                                    String message = "Your account has been deleted.";
                                                    Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();

                                                    FirebaseAuth.getInstance().signOut();

                                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);

                                                }
                                                else{
                                                    Utils.log(task.getException().toString());
                                                }
                                            }
                                        });

                            }
                        });

            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });




        dialog.show();
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
