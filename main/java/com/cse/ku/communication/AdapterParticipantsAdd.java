package com.cse.ku.communication;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterParticipantsAdd extends RecyclerView.Adapter<AdapterParticipantsAdd.HolderParticipantAdd> {

    private Context context;
    public ArrayList<ModelUser> userList;
    private String groupId, groupRole;

    public AdapterParticipantsAdd(Context context, ArrayList<ModelUser> userList, String groupId, String groupRole) {
        this.context = context;
        this.userList = userList;
        this.groupId = groupId;
        this.groupRole = groupRole;
    }

    @NonNull
    @Override
    public HolderParticipantAdd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_particapant_add,parent,false);

        return new HolderParticipantAdd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderParticipantAdd holder, int position) {

        ModelUser modelUser = userList.get(position);

        String name = modelUser.getName();
        String email = modelUser.getEmail();
        String role = modelUser.getRole();
        String image = modelUser.getPhoto();
        String uid = modelUser.getId();


        holder.name.setText(name);
        holder.email.setText(email);


        cheekIsAlreadyExists(modelUser, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId);
                ref.child("Participants");
                ref.child(uid);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            String hisPreviousRole = "" + snapshot.child("role").getValue();
                            String options[];

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Choose Option");
                            if (groupRole.equals("creator")) {
                                if (hisPreviousRole.equals("admin")) {
                                    options = new String[]{"Remove Admin", "Remove User"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0){
                                                removeAdmin(modelUser);
                                            }
                                            else {
                                                removeParticipants(modelUser);
                                            }

                                        }
                                    }).show();
                                }
                                else if (hisPreviousRole.equals("participants")){

                                    options = new String[]{"Make Admin","Remove User"};

                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0){
                                                makeAdmin(modelUser);
                                            }
                                            else {
                                                removeParticipants(modelUser);
                                            }

                                        }
                                    }).show();
                                }

                            }
                            else if (groupRole.equals("admin")){
                                if (hisPreviousRole.equals("creator")){

                                    Toast.makeText(context, "Creator of Group... ", Toast.LENGTH_SHORT).show();
                                }
                                else if (hisPreviousRole.equals("admin")){

                                    options = new String[]{"Remove Admin","Remove User"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0){
                                                removeAdmin(modelUser);
                                            }
                                            else {
                                                removeParticipants(modelUser);
                                            }

                                        }
                                    }).show();

                                }
                                else if (hisPreviousRole.equals("participants")){
                                    options = new String[]{"Make Admin","Remove User"};
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0){
                                                makeAdmin(modelUser);
                                            }
                                            else {
                                                removeParticipants(modelUser);
                                            }

                                        }
                                    }).show();

                                }
                            }
                        }

                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Add Participant")
                                    .setMessage("Add this user in this group")
                                    .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            addParticipant(modelUser);
                                        }
                                    })
                                    .setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void addParticipant(ModelUser modelUser) {

        String timestamp = ""+System.currentTimeMillis();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", modelUser.getId());
        hashMap.put("role", "Participant");
        hashMap.put("timestamp", ""+timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participant").child(modelUser.getId()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Added Successfully...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void makeAdmin(ModelUser modelUser) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "Admin");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modelUser.getId()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "The user is now admin...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void removeParticipants(ModelUser modelUser) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modelUser.getId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void removeAdmin(ModelUser modelUser) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("role", "Participant");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modelUser.getId()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "The user is no longer admin...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cheekIsAlreadyExists(ModelUser modelUser, HolderParticipantAdd holder) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participant").child(modelUser.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String hisRole = ""+snapshot.child("role").getValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HolderParticipantAdd extends RecyclerView.ViewHolder {

        private ImageView defaultProfile;
        private EditText name, email, role;


        public HolderParticipantAdd(@NonNull View itemView) {
            super(itemView);

            defaultProfile = itemView.findViewById(R.id.defaultProfile);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            role = itemView.findViewById(R.id.userRole);

        }
    }
}
