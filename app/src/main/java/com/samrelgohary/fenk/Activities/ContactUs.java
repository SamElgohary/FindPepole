package com.samrelgohary.fenk.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samrelgohary.fenk.Model.CircleModel;
import com.samrelgohary.fenk.Model.ContactUsModel;
import com.samrelgohary.fenk.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ContactUs extends AppCompatActivity {

    EditText mName,mEmail,mPhone,mMessage;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        mName = findViewById(R.id.cu_name);
        mEmail = findViewById(R.id.cu_email);
        mPhone = findViewById(R.id.cu_phone);
        mMessage = findViewById(R.id.cu_message);
        mSubmit = findViewById(R.id.cu_submit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mName.getText().toString().isEmpty()){
                    mName.requestFocus();
                    mName.setError(getResources().getString(R.string.enter_name_please));
                    return;
                }
                if (mEmail.getText().toString().isEmpty()){
                    mEmail.requestFocus();
                    mEmail.setError(getResources().getString(R.string.enter_email_please));
                    return;
                }
                if (mPhone.getText().toString().isEmpty()){
                    mPhone.requestFocus();
                    mPhone.setError(getResources().getString(R.string.enter_phone_please));
                    return;
                }
                if (mMessage.getText().toString().isEmpty()){
                    mMessage.requestFocus();
                    mMessage.setError(getResources().getString(R.string.enter_message_please));
                    return;
                }

                submitMethod();

            }
        });
    }

    public void submitMethod(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("contactUs");

        ContactUsModel contactUsModel = new ContactUsModel();

        contactUsModel.setName(mName.getText().toString());
        contactUsModel.setEmail(mEmail.getText().toString());
        contactUsModel.setPhone(mPhone.getText().toString());
        contactUsModel.setMessage(mMessage.getText().toString());
        ref.push().setValue(contactUsModel);
        finish();
    }
}
