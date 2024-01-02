package com.cse.ku.communication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.cse.ku.communication.databinding.ActivityAddCourseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddCourse : AppCompatActivity() {
    private lateinit var binding: ActivityAddCourseBinding

    // initialize edittext variables

    var CourseTitle = ""
    var CourseName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        //enable back button on toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //next button click handle
        binding.btnNext.setOnClickListener {
            //get data
            CourseName = binding.courseName.text.toString().trim()
            CourseTitle = binding.courseTitle.text.toString().trim()

            //validate data
            if (TextUtils.isEmpty(CourseName)){
                //no name entered
                binding.courseName.error = "Course name is required"
            }
            else if (TextUtils.isEmpty(CourseTitle)){
                //no name entered
                binding.courseTitle.error = "Course title is required"
            }
            else{
                //data is valid
                //send data to Add student activity
                val intent = Intent(this, AddStudent::class.java)
                intent.putExtra("CourseTitle", CourseTitle)
                intent.putExtra("CourseName", CourseName)

                startActivity(intent)
            }
        }
        //cancel button click handle
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

}