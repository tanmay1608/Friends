package com.example.friends.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friends.Models.FirebaseModel;
import com.example.friends.R;
import com.example.friends.SpecificChat;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;


public class ChatFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private ImageView imageView;
    int len;
    private Set<String> contactSet=new HashSet<>();

    FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> ChatAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView=view.findViewById(R.id.recycler_view);


        //Log.d("devicetoken", deviceToken[0]);



        //Query query=firebaseFirestore.collection("Users");


        Query query=firebaseFirestore.collection("Users").whereNotEqualTo("uid",firebaseAuth.getUid());
        FirestoreRecyclerOptions<FirebaseModel> alluserName=new FirestoreRecyclerOptions.Builder<FirebaseModel>().setQuery(query,FirebaseModel.class).build();

        ChatAdapter =new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(alluserName) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull FirebaseModel firebaseModel) {

                noteViewHolder.particularusername.setText(firebaseModel.getName());
                String uri=firebaseModel.getImage();

                Picasso.get().load(uri).into(imageView);

                if(firebaseModel.getStatus().equals("Online"))
                {
                    noteViewHolder.statusofuser.setText(firebaseModel.getStatus());
                    noteViewHolder.statusofuser.setTextColor(Color.GREEN);
                }
                else{
                    noteViewHolder.statusofuser.setText(firebaseModel.getStatus());
                }

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(getActivity(), SpecificChat.class);
                        intent.putExtra("name",firebaseModel.getName());
                        intent.putExtra("receiveruid",firebaseModel.getUid());
                        intent.putExtra("imageuri",firebaseModel.getImage());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_relative_layout,parent,false);
                return  new NoteViewHolder(v);
            }
        };

        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ChatAdapter);


        return view;
    }

    public boolean contactExists(Context context, String number) {
        // number is the phone number
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                cur.close();
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }




    private class NoteViewHolder extends RecyclerView.ViewHolder
    {
        private TextView particularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.nameofuser);
            statusofuser=itemView.findViewById(R.id.statusofuser);
            imageView=itemView.findViewById(R.id.imageviewofuser);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        ChatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(ChatAdapter!=null)
        {
            ChatAdapter.stopListening();
        }
    }
}