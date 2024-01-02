package com.cse.ku.communication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse.ku.communication.data.model.ModelGroupChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class GroupChatActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private String  groupRole, groupName;
    public String groupId;
    private Toolbar toolBer;
    private ImageView GroupIcon;
    private ImageButton attachBtn, sendBtn;
    private TextView groupTitle;
    private EditText messageEt;
    private RecyclerView chat;
    private static final String  DEBUG = "DEBUG";

    private ArrayList<ModelGroupChat> groupChatList;
    private AdapterGroupChat adapterGroupChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        GroupIcon = findViewById(R.id.GroupIcon);
        attachBtn = findViewById(R.id.attachBtn);
        groupTitle = findViewById(R.id.groupTitle);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);
        chat = findViewById(R.id.chat);


        //setSupportActionBar(toolBer);


        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        Log.w(DEBUG,"Click:"+ groupId);

        //set actionbar title
        getSupportActionBar().setTitle(groupName);

        firebaseAuth = FirebaseAuth.getInstance();

        loadGroupInfo(groupId);
        loadGroupMessage(groupId);
        groupRole();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(message)){
                    Toast.makeText(GroupChatActivity.this, "Can't send Empty Message", Toast.LENGTH_SHORT).show();

                }
                else {
                    if (groupId != null && !groupId.isEmpty()) {
                        Log.w("DEBUG","Success: "+groupId);
                        sendMessage(groupId,message);
                    }
                    else{
                        Log.w("DEBUG", "Failed: "+groupId);
                    }
                }
            }
        });



    }

    private void groupRole() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participant")
                .orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot s: snapshot.getChildren()){
                            groupRole = ""+s.child("role").getValue();
                            invalidateOptionsMenu();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadGroupMessage(String gid) {

        groupChatList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(gid).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        groupChatList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelGroupChat model = ds.getValue(ModelGroupChat.class);
                            groupChatList.add(model);
                        }
                        adapterGroupChat = new AdapterGroupChat(GroupChatActivity.this, groupChatList);

                        chat.setAdapter(adapterGroupChat);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendMessage(String gid, String message) {
        String timestamp = ""+System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",""+ firebaseAuth.getUid());
        hashMap.put("message", ""+message);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("type", "" + "text");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(gid).child("messages").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        messageEt.setText("");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(GroupChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void loadGroupInfo(String gid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.orderByChild("GroupId").equalTo(gid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds: snapshot.getChildren()){
                            String groupTitle = ""+ds.child("groupTitle").getValue();
                            String GroupInfo = ""+ds.child("GroupInfo").getValue();
                            String GroupIcon = ""+ds.child("GroupIcon").getValue();
                            String timesTemp = ""+ds.child("timesTemp").getValue();
                            String CreateTedBy = ""+ds.child("CreateTedBy").getValue();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.chat_menu,menu);


        if (groupRole.equals("creator") || groupRole.equals("admin")){

            menu.findItem(R.id.add_participant).setVisible(true);
        }
        else {

            menu.findItem(R.id.add_participant).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater2 = getMenuInflater();
        menuInflater2.inflate(R.menu.chat_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_participant){
            Intent intent = new Intent(this, GroupParticipantsAddActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}