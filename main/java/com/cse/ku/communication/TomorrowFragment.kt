package com.cse.ku.communication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cse.ku.communication.databinding.FragmentTomorrowBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList


class TomorrowFragment : Fragment() {

    private lateinit var binding: FragmentTomorrowBinding
    private lateinit var dbef : DatabaseReference
    private lateinit var classListRecyclerView: RecyclerView
    private lateinit var classListArrayList: ArrayList<ClassList>
    private var adapter : RecyclerView.Adapter<AdapterClassSchedule.MyViewHolder>?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = FragmentTomorrowBinding.inflate(layoutInflater)
        val root = binding.root

        classListArrayList = arrayListOf<ClassList>()
        classListRecyclerView = binding.ClassList
        classListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        classListRecyclerView.setHasFixedSize(true)

        getClassList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterClassSchedule(classListArrayList)
    }

    private fun getClassList() {

        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.DAY_OF_MONTH,1)
        val tomorrow = calendar.time
        val tomorrowDate = SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(tomorrow)

        dbef = FirebaseDatabase.getInstance().getReference("ClassSchedule")
        dbef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (classSnapshot in snapshot.children){

                        val classes = classSnapshot.getValue(ClassList::class.java)
                        if (classes?.date == tomorrowDate) {

                            classListArrayList.add(classes!!)
                        }

                    }

                    classListRecyclerView.adapter = AdapterClassSchedule(classListArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }

}

