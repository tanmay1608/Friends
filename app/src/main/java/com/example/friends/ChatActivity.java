package com.example.friends;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.friends.Fragments.ChatFragment;
import com.example.friends.Fragments.GroupFragment;
import com.example.friends.Fragments.ProfileFragment;
import com.example.friends.Fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class ChatActivity extends AppCompatActivity {




    //what is view binding
   // ActivityChatBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_chat);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        replaceFragment(new ChatFragment());


//    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                            Fragment fragment=null;
//                switch (item.getItemId())
//                {
//
//                    case R.id.chat:
//                        fragment=new ChatFragment();
//                        break;
//                    case R.id.group:
//                        fragment=new GroupFragment();
//                        break;
//                    case R.id.profile:
//                        fragment=new ProfileFragment();
//                        break;
//                    default:
//                        fragment=null;
//                }
//
//                replaceFragment(fragment);
//                return true;
//        }
//    });

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
                    case R.id.profile:
                        fragment=new ProfileFragment();
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
}