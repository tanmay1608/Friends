package com.example.friends;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.friends.Models.GroupsFireModel;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity {
    TextView sizeTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        sizeTextview=findViewById(R.id.size);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<GroupsFireModel> arrayList = (ArrayList<GroupsFireModel>) args.getSerializable("ARRAYLIST");
        int size=arrayList.size();
        sizeTextview.setText(size+"");

    }
}