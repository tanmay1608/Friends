package com.example.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friends.Fragments.ChatFragment;
import com.example.friends.Fragments.GroupFragment;
import com.example.friends.Fragments.SearchFragment;
import com.example.friends.Fragments.SettingFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;


public class ChatActivity extends AppCompatActivity {




    //what is view binding
   // ActivityChatBinding binding;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private CardView userProfile;
    private ImageView imageView;
    private String imagetoken;
    private SetProfile setProfile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  binding=ActivityChatBinding.inflate(getLayoutInflater());

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark

        getWindow().setStatusBarColor(ContextCompat.getColor(ChatActivity.this,R.color.white));// set status background white
        setContentView(R.layout.activity_chat);


        bottomNavigationView=findViewById(R.id.bottom_navigation);
        userProfile=findViewById(R.id.user_id);
        imageView=findViewById(R.id.user_image);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseFirestore=FirebaseFirestore.getInstance();

//        setProfile=new SetProfile();
//        imagetoken=setProfile.ImageUriAccessToken;
//        Picasso.get().load(imagetoken).into(imageView);
        userProfile.setBackgroundResource(R.drawable.cardview_bg);

        replaceFragment(new ChatFragment());
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Picasso.get().load(value.getString("image")).into(imageView);
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(ChatActivity.this, UserOwnProfile.class);
                    startActivity(intent);
            }
        });




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment=null;
                switch (item.getItemId())
                {

                    case R.id.chat:
                        fragment=new ChatFragment();
                        break;
                    case R.id.group:
                        fragment=new GroupFragment();
                        break;
                    case R.id.search:
                        fragment=new SearchFragment();
                        break;
                    case R.id.setting:
                        fragment=new SettingFragment();
                        break;

                    default:
                        fragment=null;
                }

                replaceFragment(fragment);
                return true;
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        //replace fragment
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Now user is online", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "net nhi aara", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();

//        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
//        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(getApplicationContext(), "Now user is offline", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "net nhi aara", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}