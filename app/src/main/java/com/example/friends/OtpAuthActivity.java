package com.example.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpAuthActivity extends AppCompatActivity {

    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enterdotp;
    String phoneNumber;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarotpauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        getWindow().setStatusBarColor(ContextCompat.getColor(OtpAuthActivity.this,R.color.white));// set status background white


        init();

        mprogressbarotpauth.setVisibility(View.INVISIBLE);
        firebaseAuth=FirebaseAuth.getInstance();
        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtpAuthActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        
        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterdotp=mgetotp.getText().toString();
                if(enterdotp.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter Your OTP First", Toast.LENGTH_SHORT).show();

                }
                else{


                    mprogressbarotpauth.setVisibility(View.VISIBLE);
                    Intent intent=getIntent();
                    String coderecieved=intent.getExtras().getString("otp");
                    phoneNumber=intent.getExtras().getString("number");

                    Log.d("otp",coderecieved);
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(coderecieved,enterdotp);

                    signInWithPhoneCredentials(credential);

                }
            }
        });



    }

    private void signInWithPhoneCredentials(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d("tag","ko");
                    mprogressbarotpauth.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(OtpAuthActivity.this,SetProfile.class);
                    intent.putExtra("number",phoneNumber);
                    startActivity(intent);
                    finish();
                }
                else{
                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            mprogressbarotpauth.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                        }
                }
            }
        });
    }

    private void init()
    {
        mchangenumber=findViewById(R.id.resend);
        mverifyotp=findViewById(R.id.verify_button);
        mgetotp=findViewById(R.id.otp);
        mprogressbarotpauth=findViewById(R.id.pg_bar_otp_auth);
    }
}