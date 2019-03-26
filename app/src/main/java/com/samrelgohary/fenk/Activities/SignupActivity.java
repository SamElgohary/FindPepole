package com.samrelgohary.fenk.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.samrelgohary.fenk.Model.UserModel;
import com.samrelgohary.fenk.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;


public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;



    EditText mEmail, mPassword, mFName, mBirthDate, mLtName, mRePassword,mPhoneNumber;

    TextView mRegister;
    ImageView mUserImg;
    private Uri resultUri;

    RadioButton radioButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();


        mUserImg    = findViewById(R.id.user_img_profile);
        mFName      = findViewById(R.id.f_name);
        mLtName     = findViewById(R.id.l_name);
        mEmail      = findViewById(R.id.email);
        mPassword   = findViewById(R.id.password);
        mRePassword = findViewById(R.id.re_password);
        mBirthDate  = findViewById(R.id.birth_of_date);
        radioGroup  = findViewById(R.id.radioGrp);
        mPhoneNumber= findViewById(R.id.phone_number);

        mUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mRegister   = findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("email","__"+mEmail.getText().toString());
                Log.d("mPassword","__"+mPassword.getText().toString());
                signUpUser(mEmail.getText().toString(),mPassword.getText().toString());

            }
        });
    }

    private void signUpUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "sign up error please try again", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignupActivity.this, "sign up Secssful", Toast.LENGTH_SHORT).show();
                    saveUserData();
                }
            }
        });
    }

    public void saveUserData(){


        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);

        final UserModel userModel = new UserModel();

        userModel.setSocialId(mAuth.getCurrentUser().getUid());
        userModel.setEmail(mEmail.getText().toString());
        userModel.setDateOfBirth(mBirthDate.getText().toString());
        userModel.setGender(String.valueOf(radioButton.getText()));
        userModel.setPhone(mPhoneNumber.getText().toString());

        String fullName = mFName.getText().toString() + " " + mLtName.getText().toString();
        Log.d("fullName","__"+fullName);

        userModel.setFullName(fullName);

        if(resultUri != null) {

            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(mAuth.getCurrentUser().getUid());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        // Continue with the task to get the download URL
                        userModel.setImg(downloadUri.toString());
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference ref = firebaseDatabase.getReference();
                        ref.child("user").child((mAuth.getCurrentUser().getUid())).setValue(userModel);
                        Log.i("userImg",downloadUri.toString());
                        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Error Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }else{
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mUserImg.setImageURI(resultUri);
        }
    }

    //Getting read and write permission from External Storage in android
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted1");
                return true;
            } else {

                Log.v("TAG","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted2");
                return true;
            } else {

                Log.v("TAG","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted2");
            return true;
        }
    }
}
