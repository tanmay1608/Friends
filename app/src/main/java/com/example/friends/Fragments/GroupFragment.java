package com.example.friends.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friends.GroupChatActivity;
import com.example.friends.InfoGroup;
import com.example.friends.Models.GroupsFireModel;
import com.example.friends.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GroupFragment extends Fragment {

    private ImageView createNewGroup;
    private RecyclerView groupRecyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static int PICK_IMAGE=123;
    private Uri imagepath;
    public String ImageUriAccessToken;
    ArrayList<GroupsFireModel> arrayList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private ProgressBar progressBar ;
    FirestoreRecyclerAdapter<GroupsFireModel, GroupsViewHolder> groupAdapter;



    public GroupFragment() {
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
        View view=inflater.inflate(R.layout.fragment_group, container, false);
        createNewGroup=view.findViewById(R.id.create_new_group);
        groupRecyclerView=view.findViewById(R.id.recycler_view_group);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference=firebaseStorage.getReference();
        progressBar=view.findViewById(R.id.pg_bar_grp_frag);
        progressBar.setVisibility(View.VISIBLE);


        Query query=firebaseFirestore.collection("Groups");


        FirestoreRecyclerOptions<GroupsFireModel> allgroupName=new FirestoreRecyclerOptions.Builder<GroupsFireModel>().setQuery(query,GroupsFireModel.class).build();

        groupAdapter=new FirestoreRecyclerAdapter<GroupsFireModel, GroupsViewHolder>(allgroupName) {
            @Override
            protected void onBindViewHolder(@NonNull GroupsViewHolder groupsViewHolder, int i, @NonNull GroupsFireModel groupsFireModel) {
                groupsViewHolder.groupname.setText(groupsFireModel.getGroupName());
                String uri=groupsFireModel.getGroupImage();
                Picasso.get().load(uri).into(groupsViewHolder.groupimage);


                groupsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  progressBar.setVisibility(View.GONE);
                        Intent intent=new Intent(getActivity(), GroupChatActivity.class);
                        intent.putExtra("groupName",groupsFireModel.getGroupName());
                        intent.putExtra("groupImage",groupsFireModel.getGroupImage());
                        startActivity(intent);
                    }
                });
                progressBar.setVisibility(View.GONE);



            }

            @NonNull
            @Override
            public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(getContext()).inflate(R.layout.groups_layout,parent,false);
                return new GroupsViewHolder(view);
            }


        };


        groupRecyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        groupRecyclerView.setLayoutManager(linearLayoutManager);
        groupRecyclerView.setAdapter(groupAdapter);
        //progressBar.setVisibility(View.INVISIBLE);









//


        createNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.AlertDialog);
                builder.setTitle("Enter Group Name");
                final EditText groupNameEditText=new EditText(getActivity());
                groupNameEditText.setHintTextColor(getResources().getColor(R.color.grey_dark));
                groupNameEditText.setTextColor(getResources().getColor(R.color.grey_dark));
                groupNameEditText.setHint("e.g Apni Thadi");
                builder.setView(groupNameEditText);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String groupName=groupNameEditText.getText().toString();

                        if (TextUtils.isEmpty(groupName))
                        {
                            Toast.makeText(getActivity(), "Please Enter Group Name", Toast.LENGTH_SHORT).show();
                        }
                        else{
                               Intent intent=new Intent(getActivity(), InfoGroup.class);
                             //  intent.putExtra("groupname",groupName);
                               startActivity(intent);
                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });

                builder.show();

            }


        });



        return view;
    }

    private class GroupsViewHolder extends RecyclerView.ViewHolder{
        ImageView groupimage;
        TextView groupname;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);
            groupimage=itemView.findViewById(R.id.imageviewofgroup);
            groupname=itemView.findViewById(R.id.nameofgroup);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        groupAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if(groupAdapter!=null)
        {
            groupAdapter.stopListening();
        }
    }


    }


