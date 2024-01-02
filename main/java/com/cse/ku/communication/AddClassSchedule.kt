package com.cse.ku.communication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.format.Time
import android.view.MotionEvent
import android.widget.Toast
import com.cse.ku.communication.databinding.ActivityAddClassScheduleBinding
import com.cse.ku.communication.databinding.ActivityAddStudentBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AddClassSchedule : AppCompatActivity() {

    private lateinit var binding: ActivityAddClassScheduleBinding
    var courseTitle = ""
    var courseName = ""
    var inputDate = ""
    var inputTime = ""
    private lateinit var calendar: Calendar
    //database reference
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddClassScheduleBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        //enable back button on toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //calendar
        calendar = Calendar.getInstance()
        // date picker
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
            updateEditTextDate(calendar)
        }

        // time picker
        val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            updateEditTextTime(calendar)
        }


        // initialize database
        dbref = FirebaseDatabase.getInstance().getReference("ClassSchedule")

        //add onclick listener
        binding.btnCancel.setOnClickListener {
            //handle cancel
            finish()
        }

        binding.dateSelection.inputType = InputType.TYPE_NULL
        //binding.dateSelection.keyListener = null
        binding.dateSelection.setOnTouchListener { view, motionEvent ->
            val action = motionEvent.action
            if (action == MotionEvent.ACTION_UP) {
                DatePickerDialog(
                    this,
                    datePicker,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
            true
        }

        binding.timeSelection.setOnTouchListener { view, motionEvent ->
            val action = motionEvent.action
            if (action == MotionEvent.ACTION_UP){
                TimePickerDialog(
                    this,
                    timePicker,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false
                )
                    .show()
        }
        true
    }

        binding.btnAdd.setOnClickListener {
            //add class schedule

            // get data
            courseTitle = binding.courseTitle.text.toString().trim()
            courseName = binding.courseName.text.toString().trim()
            inputDate = binding.dateSelection.text.toString()
            inputTime = binding.timeSelection.text.toString()

            val key = dbref.push().key.toString()

            var schedule = ClassList(courseTitle, courseName, inputTime, inputDate)
            dbref.child(key).setValue(schedule)
                .addOnSuccessListener {
                    // course creation successful
                    // create a group chat with the course name
                    //createFirebaseGroup(courseTitle,courseName, uid)
                    Toast.makeText(this,"Schedule Added",Toast.LENGTH_SHORT).show()
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

    private fun updateEditTextTime(calendar: Calendar) {
        binding.timeSelection.setText(SimpleDateFormat("HH:mm").format(calendar.time))

    }

    private fun updateEditTextDate(calendar: Calendar) {
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format,Locale.UK)
        binding.dateSelection.setText(sdf.format(calendar.time))

    }
}