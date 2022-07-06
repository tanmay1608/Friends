package com.example.friends;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.friends.Models.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetProfile extends AppCompatActivity {


        private CardView mgetuserimage;
        private ImageView mgetuserImageinimageview;
        private static int PISCK_IMAGE=123;
        private Uri imagepath;
        private String phoneNumber;
        private EditText mgetlastName,mgetpetname;

        private EditText mgetusername;

        private Button saveprofile;

        private FirebaseAuth firebaseAuth;
        private  String firstname,lastname,petname;

        private FirebaseStorage firebaseStorage;
        private StorageReference storageReference;

        public String ImageUriAccessToken;
        private FirebaseFirestore firebaseFirestore;
        ProgressBar mprrogressbarsetProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        //It makes status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Intent intent=getIntent();

        phoneNumber=intent.getExtras().getString("number");

        mgetusername=findViewById(R.id.getUserfirstName);
        mgetlastName=findViewById(R.id.getUserlastName);
        mgetpetname=findViewById(R.id.getUserpetName);
        mgetuserimage=findViewById(R.id.getUserImage);
        mgetuserImageinimageview=findViewById(R.id.getimageinimageview);
        saveprofile=findViewById(R.id.save_button);
        mprrogressbarsetProfile=findViewById(R.id.pg_bar_pro);
        mprrogressbarsetProfile.setVisibility(View.INVISIBLE);

        mgetuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PISCK_IMAGE);
            }
        });

        saveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname=mgetusername.getText().toString();
                lastname=mgetlastName.getText().toString();
                petname=mgetpetname.getText().toString();


                if (firstname.isEmpty() || lastname.isEmpty()||petname.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Names are Empty", Toast.LENGTH_SHORT).show();
                } else if (imagepath==null) {
                    Toast.makeText(getApplicationContext(), "Image is Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                  mprrogressbarsetProfile.setVisibility(View.VISIBLE);
                  sendDataForNewUser();
                  mprrogressbarsetProfile.setVisibility(View.INVISIBLE);

                  Intent intent=new Intent(SetProfile.this,ChatActivity.class);
                  startActivity(intent);
                  finish();
                }
            }
        });

    }

    private void sendDataForNewUser() {
        sendDataToRealTimeDatabase();
    }

    private void sendDataToRealTimeDatabase() {

        firstname=mgetusername.getText().toString();
        lastname=mgetlastName.getText().toString();
        petname=mgetpetname.getText().toString();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference();





//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
//            if (!TextUtils.isEmpty(token)) {
//
//                Log.d("tag1", "retrieve token successful : " + token);
//            } else{
//                Log.w("tag2", "token should not be null...");
//            }
//        }).addOnFailureListener(e -> {
//            //handle e
//        }).addOnCanceledListener(() -> {
//            //handle cancel
//        }).addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                String str=task.getResult();
//                databaseReference.child("Users").child(firebaseAuth.getUid()).child("deviceToken").setValue(str);
//
//            }
//        });
//


        UserProfile userProfile=new UserProfile(firstname,firebaseAuth.getUid(),lastname,petname);
        databaseReference.child("Users").child(firebaseAuth.getUid()).setValue(userProfile);

        Toast.makeText(getApplicationContext(), "User Profile Added Successfully", Toast.LENGTH_SHORT).show();

        sendImagetostorage();

    }

    private void sendImagetostorage() {
        StorageReference imageref=storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic");

        //Image compression

        Bitmap bitmap=null;

        try {
            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();


        //putting image to storage

        UploadTask uploadTask=imageref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAccessToken=uri.toString();
                        Toast.makeText(getApplicationContext(), "URI get success", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFireStore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "URI get Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image Not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });






    }

    private void sendDataToCloudFireStore() {


        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String,Object> userdata=new HashMap<>();
        userdata.put("name",firstname);
        userdata.put("image",ImageUriAccessToken);
        userdata.put("uid",firebaseAuth.getUid());
        userdata.put("status","Online");
        userdata.put("number",phoneNumber);



        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Data on Cloud Firestore send success", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==PISCK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            mgetuserImageinimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}