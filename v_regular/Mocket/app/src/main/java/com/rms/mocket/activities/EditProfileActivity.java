package com.rms.mocket.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rms.mocket.R;
import com.rms.mocket.common.Checker;
import com.rms.mocket.common.Utils;
import com.rms.mocket.database.DatabaseHandlerUser;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    private final int RESULT_LOAD_IMAGE = 1;
    DatabaseHandlerUser db_user;

    public int verificationNumber = 0;
    public boolean verified = false;
    String email;
    String password;

    ImageView imageView_profile;

    EditText editText_firstName;
    EditText editText_lastName;

    EditText editText_email;
    EditText editText_currentPassword;
    EditText editText_newPassword1;
    EditText editText_newPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.EDITPROFILE_toolbar);
        imageView_profile = (ImageView) findViewById(R.id.EDITPROFILE_imageView_profile);
        editText_email = (EditText) findViewById(R.id.EDITPROFILE_editText_email);
        editText_currentPassword = (EditText) findViewById(R.id.EDITPROFILE_editText_currentPassword);
        editText_newPassword1 = (EditText) findViewById(R.id.EDITPROFILE_editText_password1);
        editText_newPassword2 = (EditText) findViewById(R.id.EDITPROFILE_editText_password2);

        editText_firstName = (EditText) findViewById(R.id.EDITPROFILE_editText_firstName);
        editText_lastName = (EditText) findViewById(R.id.EDITPROFILE_editText_lastName);


        db_user = new DatabaseHandlerUser(this);

        this.setSupportActionBar(myToolbar);
        this.setInitialSetting();
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    private void setInitialSetting(){
        /* Set profile image */
        byte[] byte_profile_image = db_user.getUserProfileImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(byte_profile_image, 0, byte_profile_image.length);
        imageView_profile.setImageBitmap(bmp);

        /* Show current email, firstname, lastname */
        HashMap<String, String> user_data = db_user.getUserData();
        editText_firstName.setText(user_data.get(DatabaseHandlerUser.COLUMN_FIRST_NAME));
        editText_lastName.setText(user_data.get(DatabaseHandlerUser.COLUMN_LAST_NAME));
        editText_email.setText(user_data.get(DatabaseHandlerUser.COLUMN_EMAIL));



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

        db_user.saveUserData(firstName, lastName);
        db_user.saveProfileImage(Utils.getByteImage(imageView_profile));
        String message = "Profile has been updated.";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();



    }

    public void changePassword(View v) {
        String currentPassword = editText_currentPassword.getText().toString();
        String password1 = editText_newPassword1.getText().toString();
        String password2 = editText_newPassword2.getText().toString();

        if(!currentPassword.equals(db_user.getPassword())){
            String message = "Current password is not correct.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }


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

            db_user.savePassword(password1);

            String message = "Password has been updated.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        }
        // TODO: Change password

    }
    public void goPreviousActivity(View v){
        onBackPressed();
    }

    public void clickLogout(View v){
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

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> user_data = db_user.getUserData();
                String email = user_data.get(DatabaseHandlerUser.COLUMN_EMAIL);
                String entered_email = editText_email.getText().toString();
                
                if(!email.equals(entered_email)){
                    String message = "Email address is not correct.";
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();
                    return;
                }

                String message = "Account has been deleted.";
                Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                //TODO: Delete account from server and local.

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
