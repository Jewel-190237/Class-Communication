package com.cse.ku.communication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupParticipantsAddActivity extends AppCompatActivity {

    private RecyclerView user;
    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    private String groupId;
    private String groupRole;

    private ArrayList<ModelUser> userList;
    private AdapterParticipantsAdd adapterParticipantsAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_particapant_add);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add Participant");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        user = findViewById(R.id.Groups);

        groupId = getIntent().getStringExtra("groupId");
        Toast.makeText(this,groupId,Toast.LENGTH_SHORT).show();
        Log.w("DEBUG","AddParticipantActivity: "+groupId);

        userList = new ArrayList<ModelUser>();
        adapterParticipantsAdd = new AdapterParticipantsAdd(GroupParticipantsAddActivity.this, userList,""+groupId, ""+groupRole);
        user.setAdapter(adapterParticipantsAdd);
        loadGroupInfo();

    }


    private void loadGroupInfo() {
        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s: snapshot.getChildren()){
                    String groupId = ""+s.child("groupId").getValue();
                    String groupTitle = ""+s.child("groupTitle").getValue();
                    String groupDescription = ""+s.child("groupDescription").getValue();
                    String groupIcon = ""+s.child("groupIcon").getValue();
                    String createBy = ""+s.child("createBy").getValue();
                    String timestamp = ""+s.child("timestamp").getValue();

                    actionBar.setTitle("Add Participant");

                    ref1.child(groupId).child("Participants").child(firebaseAuth.getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        groupRole = ""+snapshot.child("role").getValue();
                                        actionBar.setTitle(groupTitle + "("+groupRole+")");

                                        getAllUsers();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    Log.w("DEBUG","Inside");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.w("DEBUG","Load Function call");
    }

    private void getAllUsers() {

        userList = new ArrayList<ModelUser>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){

                    ModelUser modelUser = ds.getValue(ModelUser.class);

                    if (!firebaseAuth.getUid().equals(modelUser.getId())){
                        userList.add(modelUser);
                    }

                }

                Log.w("DEBUG","userList size: "+userList.size());

                adapterParticipantsAdd = new AdapterParticipantsAdd(GroupParticipantsAddActivity.this, userList,""+groupId, ""+groupRole);
                user.setAdapter(adapterParticipantsAdd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        Log.w("DEBUG","Run here");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}