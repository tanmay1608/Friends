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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class InfoGroup extends AppCompatActivity {

    private CardView mgetgroupimage;
    private ImageView mgetgroupImageinimageview;
    private static int PICK_IMAGE=123;
    private Uri imagepath;

    private EditText mgetgroupname;

    private Button savegroup;

    private FirebaseAuth firebaseAuth;
    private  String name;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public String ImageUriAccessToken;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar mprrogressbarsetGroupe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        //It makes status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            firebaseAuth=FirebaseAuth.getInstance();
            firebaseStorage=FirebaseStorage.getInstance();
            storageReference=firebaseStorage.getReference();
            firebaseFirestore=FirebaseFirestore.getInstance();

            mgetgroupname=findViewById(R.id.getGroupName);
            mgetgroupimage=findViewById(R.id.getGroupImage);
            mgetgroupImageinimageview=findViewById(R.id.getimageinimageviewofGroup);
            savegroup=findViewById(R.id.save_button_grp);
            mprrogressbarsetGroupe=findViewById(R.id.pg_bar_grp);
            mprrogressbarsetGroupe.setVisibility(View.INVISIBLE);

//            Intent prevIntent;
//            prevIntent=getIntent();
//            mgetgroupname.setText(prevIntent.getStringExtra("groupname").toString());

            mgetgroupimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(intent,PICK_IMAGE);
                }
            });

            savegroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name=mgetgroupname.getText().toString();

                    if (name.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Name is Empty", Toast.LENGTH_SHORT).show();
                    } else if (imagepath==null) {
                        Toast.makeText(getApplicationContext(), "Image is Empty", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mprrogressbarsetGroupe.setVisibility(View.VISIBLE);
                        sendDataForNewUser();
                        mprrogressbarsetGroupe.setVisibility(View.INVISIBLE);
                        Intent intent=new Intent(InfoGroup.this, ChatActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
    private void sendDataForNewUser() {
        sendDataToRealTimeDatabase();
    }

    private void sendDataToRealTimeDatabase() {

        name=mgetgroupname.getText().toString();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference();


        databaseReference.child("Groups").child(name).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), name+"created successfully ", Toast.LENGTH_SHORT).show();

//                    sendDataToCloudFireStore(groupName);
                    sendImagetoStorage();
                }

            }
        });


    }

    private void sendImagetoStorage()
    {
        StorageReference imgref= storageReference.child("Images").child("Groups").child(name);

        //Image compression

        Bitmap bitmap=null;

        try {
            bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();


        //putting image to storage
        UploadTask uploadTask=imgref.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAccessToken=uri.toString();
                        Toast.makeText(getApplicationContext(), "URI getSuccess", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFireStore();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "URI get failed", Toast.LENGTH_SHORT).show();
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

        DocumentReference documentReference=firebaseFirestore.collection("Groups").document(name);
        Map<String,Object> groupdata=new HashMap<>();
        groupdata.put("groupName",name);
        groupdata.put("groupImage",ImageUriAccessToken);

        documentReference.set(groupdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Group data send successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            mgetgroupImageinimageview.setImageURI(imagepath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}