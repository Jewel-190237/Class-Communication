package com.cse.ku.communication.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cse.ku.communication.*
import com.cse.ku.communication.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DashboardFragment : Fragment() {

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    // Firebase database
    private lateinit var databaseReference : DatabaseReference

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        //init firebase auth

        firebaseAuth = FirebaseAuth.getInstance()

        // get firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //check user role and show dashboard accordingly
        checkUserRole()



        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun checkUserRole(){
        //if user is already logged in go to  chat
        // get current user
        var role: String = ""
        val firebaseUser = firebaseAuth.currentUser

            val uid = firebaseUser?.uid as String
            databaseReference.child(uid).child("role").get()
                .addOnSuccessListener {
                    // get role
                    role = it.value as String
                    //hide progressbar
                    binding.progressBar.visibility = View.GONE
                    // set dashboard
                    if (role == "Student"){

                        binding.dashboardCr.crDashboardFragment.visibility = View.GONE
                        binding.dashboardTeacher.teacherDashboardFragment.visibility = View.GONE
                        binding.dashboardStudent.studentDashboardFragment.visibility = View.VISIBLE

                        //handle click

                        val studentView = binding.dashboardStudent
                        studentView.profileBtn.setOnClickListener{

                        }
                        studentView.classScheduleBtn.setOnClickListener{
                            startActivity(Intent(requireContext(),ClassSchedule::class.java))
                        }
                        studentView.registeredCourseBtn.setOnClickListener{
                            startActivity(Intent(requireContext(),RegisteredCourse::class.java))
                        }
                        studentView.invitedCourseBtn.setOnClickListener{
                            startActivity(Intent(requireContext(),InvitedCourse::class.java))
                        }
                    }
                    else if (role == "CR"){
                        binding.dashboardCr.crDashboardFragment.visibility = View.VISIBLE
                        binding.dashboardTeacher.teacherDashboardFragment.visibility = View.GONE
                        binding.dashboardStudent.studentDashboardFragment.visibility = View.GONE

                        //handle click
                        val crView = binding.dashboardCr
                        crView.profileBtn.setOnClickListener {

                        }
                        crView.classScheduleBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),ClassSchedule::class.java))
                        }
                        crView.addCourseBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),AddCourse::class.java))
                        }
                        crView.registeredCourseBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),RegisteredCourse::class.java))
                        }
                        crView.editCourseBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),EditClassSchedule::class.java))
                        }
                        crView.invitedCourseBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),InvitedCourse::class.java))
                        }

                    }
                    else if (role == "Teacher"){
                        binding.dashboardCr.crDashboardFragment.visibility = View.GONE
                        binding.dashboardTeacher.teacherDashboardFragment.visibility = View.VISIBLE
                        binding.dashboardStudent.studentDashboardFragment.visibility = View.GONE

                        //handle click
                        val teacherView = binding.dashboardTeacher
                        teacherView.scheduleClassButton.setOnClickListener {
                            // add class schedule
                            startActivity(Intent(requireContext(),AddClassSchedule::class.java))
                        }
                        teacherView.classScheduleBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),ClassSchedule::class.java))
                        }
                        teacherView.addCourseBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),AddCourse::class.java))
                        }
                        teacherView.addStudentBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),AddStudent::class.java))
                        }
                        teacherView.registeredCourseBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),RegisteredCourse::class.java))
                        }
                        teacherView.editCourseBtn.setOnClickListener {
                            startActivity(Intent(requireContext(),EditClassSchedule::class.java))
                        }

                    }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}