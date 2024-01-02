package com.cse.ku.communication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cse.ku.communication.databinding.ActivityAddStudentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AddStudent : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<User>

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog
    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    // group database ref
    private lateinit var gdbref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        //get intent data
        val courseName = intent.extras?.get("CourseName").toString().trim()
        val courseTitle = intent.extras?.get("CourseTitle").toString().trim()

        //set course details data to layout
        binding.courseName.text = courseName
        binding.courseTitle.text = courseTitle


        userRecyclerview = binding.recyclerView
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<User>()
        getUserData()

        //Configure Progress Dialogue
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Adding course...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth

        firebaseAuth = FirebaseAuth.getInstance()
        //get firebase user and uid
        val firebaseUser = firebaseAuth.currentUser
        val uid = firebaseUser?.uid as String

        // get firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses")
        gdbref = FirebaseDatabase.getInstance().getReference("Groups")



        // add button click handle

        binding.btnAdd.setOnClickListener {
            // add course to course list
            //Toast.makeText(this,MyApplication.userAddList.size.toString(),Toast.LENGTH_SHORT).show()
            /*dbref.child(uid).get().addOnSuccessListener {
                val teacherName = it.child("name").value.toString()
                val teacherEmail = it.child("email").value.toString()
                val teacherUID = it.child("uid").value.toString()
            }
            val teacher = User()*/
            if (MyApplication.userAddList.size > 0) {
                // User selected
                createFirebaseCourse(courseTitle, courseName, uid)
            }
            else{
                //No user selected
                Toast.makeText(this,"Select user", Toast.LENGTH_SHORT).show()

            }

        }

    }


    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(User::class.java)
                        if (user?.Role != "Teacher") {
                            userArrayList.add(user!!)
                        }

                    }

                    userRecyclerview.adapter = RecyclerAdapter(userArrayList)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

    private fun createFirebaseCourse(courseTitle: String, courseName: String, uid: String) {

        var participants = MyApplication.userAddList

        val course = Courses(courseName,uid,participants)

        // show progress bar
        progressDialog.show()

        if (uid != null){
            databaseReference.child(courseTitle).setValue(course)
                .addOnSuccessListener {
                    // course creation successful
                    // create a group chat with the course name
                    //createFirebaseGroup(courseTitle,courseName, uid)
                    Toast.makeText(this,"Course Added",Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnCanceledListener {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed due to: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun createFirebaseGroup(courseTitle: String, courseName: String, uid: String) {
        val group = Group(courseName, uid)
        if(uid != null){
            gdbref.child(courseTitle).setValue(group)
                .addOnSuccessListener {
                    // group creation successful
                    progressDialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to create group chat: ${it.message}", Toast.LENGTH_LONG).show()
                }
                .addOnCanceledListener {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                }
        }

    }
}