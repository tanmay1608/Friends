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

public class SpecificChat extends AppCompatActivity {

    private EditText getmessage;
    private ImageView sendbutton;
    private CardView sendmessagecardview;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView imageviewofspecifiuser;
    private TextView nameofspeificuser;

    private String enteredmessage;

    Intent intent;
    String receivername,sendername,receiverid,senderid;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
     String senderroom,receiverrrom;
    ImageButton backbuttonodspecificchat;

    private RecyclerView recyclerView;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;

    ProgressBar progressBar;
    LottieAnimationView lottieAnimationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);

        getmessage=findViewById(R.id.getmessage);

        sendbutton=findViewById(R.id.imageviewofsend);
        toolbar=findViewById(R.id.toolbarofspecificchat);
        nameofspeificuser=findViewById(R.id.nameofspecifcuserchat);
        imageviewofspecifiuser=findViewById(R.id.specificuserimage);
        backbuttonodspecificchat=findViewById(R.id.backbuttonofspecificchat);
        lottieAnimationView=findViewById(R.id.animationView);
       // progressBar=findViewById(R.id.trial);
       // progressBar.setVisibility(View.VISIBLE);
        messagesArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.rvofspecificchat);
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
        receiverid=getIntent().getStringExtra("receiveruid");
        receivername=getIntent().getStringExtra("name");
        senderroom=senderid+receiverid;
        receiverrrom=receiverid+senderid;

        DatabaseReference databaseReference=firebaseDatabase.getReference().child("Chats").child(senderroom).child("messages");
        messagesAdapter=new MessagesAdapter(SpecificChat.this,messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Messages messages=snapshot1.getValue(Messages.class);
                    messagesArrayList.add(messages);
                    messagesAdapter=new MessagesAdapter(SpecificChat.this,messagesArrayList);
                    recyclerView.setAdapter(messagesAdapter);


                }
                lottieAnimationView.setVisibility(View.INVISIBLE);


               // progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
            }
        });













        backbuttonodspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nameofspeificuser.setText(receivername);
        String uri=intent.getStringExtra("imageuri");
        if (uri.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "null received", Toast.LENGTH_SHORT).show();
        }
        else{
            Picasso.get().load(uri).into(imageviewofspecifiuser);
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

                    Messages messages=new Messages(enteredmessage,firebaseAuth.getUid(),date.getTime(),currentTime);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("Chats")
                            .child(senderroom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("Chats")
                                    .child(receiverrrom)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

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
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}