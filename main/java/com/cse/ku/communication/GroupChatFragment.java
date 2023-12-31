package com.cse.ku.communication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cse.ku.communication.data.model.ModelGroupChatList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GroupChatFragment extends Fragment {


    private RecyclerView groups;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelGroupChatList> groupChatLists;
    private AdapterGroupChatList adapterGroupChatList;

    public GroupChatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        groups = view.findViewById(R.id.Groups);

        loadGroupChatList();

        return view;
    }

    private void loadGroupChatList() {

        groupChatLists = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                groupChatLists.size();

                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("Participant").child(firebaseAuth.getUid()).exists()){
                        ModelGroupChatList model = ds.getValue(ModelGroupChatList.class);
                        groupChatLists.add(model);
                    }
                }
                adapterGroupChatList = new AdapterGroupChatList(getActivity(), groupChatLists);
                groups.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void SearchGroupChatList(String query) {

        groupChatLists = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                groupChatLists.size();

                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("Participant").child(firebaseAuth.getUid()).exists()){

                        if (ds.child("GroupTitle").toString().toLowerCase().contains(query.toLowerCase())){
                            ModelGroupChatList model = ds.getValue(ModelGroupChatList.class);
                            groupChatLists.add(model);
                        }


                    }
                }
                adapterGroupChatList = new AdapterGroupChatList(getActivity(), groupChatLists);
                groups.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}