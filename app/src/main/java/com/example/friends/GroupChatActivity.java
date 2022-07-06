package com.example.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friends.Models.GroupMessages;
import com.example.friends.Models.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    private EditText getmessage;
    private ImageView sendbutton;
    private CardView sendmessagecardview;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView imageviewofspecifigroup;
    private TextView nameofspeificgroup;

    private String enteredmessage;

    Intent intent;
    String receivername,sendername,receiverid,senderid;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    String senderroom,receiverrrom;
    ImageButton backbuttonodspecificgroup;

    private RecyclerView recyclerView;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    GroupsMessageAdapter  groupsMessageAdapter;
    ArrayList<Messages> messagesArrayList;
    ArrayList<GroupMessages> groupMessagesArrayList=new ArrayList<>();

    ProgressBar progressBar;
    LottieAnimationView lottieAnimationView;
    private String groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        getmessage=findViewById(R.id.get_group_message);
        sendbutton=findViewById(R.id.imageviewof_group_send);
        toolbar=findViewById(R.id.toolbarofspecific_group_chat);
        imageviewofspecifigroup=findViewById(R.id.specific_group_image);
        backbuttonodspecificgroup=findViewById(R.id.backbuttonofspecific_group_chat);
        lottieAnimationView=findViewById(R.id.animationView);
        nameofspeificgroup=findViewById(R.id.nameofspecifc_group_chat);
        messagesArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.rvofspecific_group_chat);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent=getIntent();

        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Toolbar is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        senderid=firebaseAuth.getUid();
        groupName=getIntent().getStringExtra("groupName");
        receiverid=getIntent().getStringExtra("receiveruid");


        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference dr=firebaseDatabase.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid().toString());
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile=snapshot.getValue(UserProfile.class);
                sendername=userProfile.getUsername();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Groups").child(groupName).child("messages");
        groupsMessageAdapter=new GroupsMessageAdapter(GroupChatActivity.this,groupMessagesArrayList);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupMessagesArrayList.clear();

                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                  //  Messages messages=snapshot1.getValue(Messages.class);
                    GroupMessages messages=snapshot1.getValue(GroupMessages.class);
                    groupMessagesArrayList.add(messages);
                   // messagesArrayList.add(messages);

                    groupsMessageAdapter=new GroupsMessageAdapter(GroupChatActivity.this,groupMessagesArrayList);
                    recyclerView.setAdapter(groupsMessageAdapter);
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                }
                lottieAnimationView.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });

        backbuttonodspecificgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nameofspeificgroup.setText(groupName);
        String uri=intent.getStringExtra("groupImage");
        if (uri.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "null received", Toast.LENGTH_SHORT).show();
        }
        else{
            Picasso.get().load(uri).into(imageviewofspecifigroup);
        }

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage=getmessage.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "please enter message first", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("message", enteredmessage);
                    Date date=new Date();
                    currentTime=simpleDateFormat.format(calendar.getTime());

                    GroupMessages messages=new GroupMessages(enteredmessage,firebaseAuth.getUid(),sendername,date.getTime(),currentTime);

                    firebaseDatabase.getReference().child("Groups")
                            .child(groupName)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    getmessage.setText(null);
                }







            }
        });







    }

    @Override
    public void onStart() {
        super.onStart();
        groupsMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(groupsMessageAdapter!=null)
        {
            groupsMessageAdapter.notifyDataSetChanged();
        }
    }
}