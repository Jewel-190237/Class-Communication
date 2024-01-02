package com.cse.ku.communication;
// 36 no video

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class GroupCreateActivity extends AppCompatActivity {

    private static final int CAMERA_REQUIRED_CODE = 100;
    private static final int STORAGE_REQUIRED_CODE = 200;

    private static final int IMAGE_PIC_CAMERA_CODE = 300;
    private static final int IMAGE_PIC_GALLERY_CODE = 400;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri image_uri = null;


    private ActionBar actionBar;

    private FirebaseAuth firebaseAuth;

    private ImageView GroupIcon;
    private EditText groupTitleEt, GroupInfoEt;
    private FloatingActionButton CreateGroupBtn;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Create Group");

        GroupIcon = findViewById(R.id.GroupIcon);
        groupTitleEt = findViewById(R.id.groupTitle);
        GroupInfoEt = findViewById(R.id.GroupInfo);
        CreateGroupBtn = findViewById(R.id.CreateGroupBtn);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        GroupIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImagePicDialoge();

            }
        });

        CreateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCreateGroup();



            }
        });
    }

    private void startCreateGroup(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Create Group");

        String groupTitle = groupTitleEt.getText().toString().trim();
        String GroupInfo = GroupInfoEt.getText().toString().trim();

        if (TextUtils.isEmpty(groupTitle)){
            Toast.makeText(this, "Please Enter Group Title", Toast.LENGTH_SHORT).show();

            return;
        }

        progressDialog.show();

        String g_timestemp = ""+System.currentTimeMillis();

        if (image_uri == null){
            createGroup(
                    ""+g_timestemp,
                    ""+groupTitle,
                    ""+GroupInfo,
                    ""
            );
        }
        else  {

            String fileNameAndPath = "Group_image/" + "image" + g_timestemp;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(fileNameAndPath);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> p_uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!p_uriTask.isSuccessful());
                            Uri p_downloadUri = p_uriTask.getResult();
                            if (p_uriTask.isSuccessful()){

                                createGroup(
                                        ""+g_timestemp,
                                        ""+groupTitle,
                                        ""+GroupInfo,
                                        ""+p_downloadUri
                                );

                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(GroupCreateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


        }

    }

    private void createGroup(String g_timestemp, String groupTitle, String GroupInfo, String GroupIcon) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("GroupId", ""+ g_timestemp);
        hashMap.put("groupTitle", ""+ groupTitle);
        hashMap.put("GroupInfo", ""+ GroupInfo);
        hashMap.put("GroupIcon", ""+ GroupIcon);
        hashMap.put("timesTemp", ""+ g_timestemp);
        hashMap.put("CreateTedBy", ""+ firebaseAuth.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Create");
        ref.child(g_timestemp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put("Uid", firebaseAuth.getUid());
                hashMap1.put("Role", "Creator");
                hashMap1.put("TimesTamp", g_timestemp);

                DatabaseReference refl = FirebaseDatabase.getInstance().getReference("Groups");
                refl.child(g_timestemp).child("Participants").child(firebaseAuth.getUid())
                        .setValue(hashMap1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(GroupCreateActivity.this, "Groups Created...", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(GroupCreateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

            //Add participat page
                Intent intent = new Intent(GroupCreateActivity.this, GroupParticipantsAddActivity.class);
                intent.putExtra("groupId",g_timestemp);
                startActivity(intent);
                finish();


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(GroupCreateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }

    private void showImagePicDialoge() {

        String[] options = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0)
                        {
                            if (!cheekCameraPermission()){
                                requestCameraPermission();
                            }
                            else {
                                picFromCamera();
                            }

                        }
                        else {
                            if (!cheekStoragePermission()){
                                requestStoragePermission();
                            }
                            else {
                                picFromGallery();
                            }

                        }

                    }
                }).show();

    }


    private void picFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("Image/*");
        startActivityForResult(intent,IMAGE_PIC_GALLERY_CODE);
    }

    private void picFromCamera(){

        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Group Image Title");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Group Image Description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent,IMAGE_PIC_CAMERA_CODE);
    }

    private void requestStoragePermission(){

        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUIRED_CODE);

    }

    private boolean cheekStoragePermission(){

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){

        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUIRED_CODE);

    }

    private boolean cheekCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }



    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    actionBar.setSubtitle(user.getEmail());

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUIRED_CODE:{
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        picFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera and Storage permission are required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUIRED_CODE:{
                if (grantResults.length > 0){
                    boolean storeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storeAccepted){
                        picFromGallery();
                    }
                    else {
                        Toast.makeText(this, "Store permission required", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PIC_GALLERY_CODE){

                image_uri = data.getData();
                GroupIcon.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PIC_CAMERA_CODE){

                GroupIcon.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }
}
