package com.cse.ku.communication.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cse.ku.communication.*
import com.cse.ku.communication.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerview: RecyclerView
    private lateinit var groupArrayList: ArrayList<ChatGroup>

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(ChatViewModel::class.java)

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it

            userRecyclerview = binding.chatRecyclerView
            userRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            userRecyclerview.setHasFixedSize(true)

            groupArrayList = arrayListOf<ChatGroup>()
            getGroupData()

            binding.floatingActionButton.setOnClickListener {

                startActivity(Intent(requireContext(), GroupCreateActivity::class.java))

            }
        }
        return root
    }

    private fun getGroupData() {

        dbref = FirebaseDatabase.getInstance().getReference("Create")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val group = userSnapshot.getValue(ChatGroup::class.java)

                            groupArrayList.add(group!!)


                    }

                    userRecyclerview.adapter = ChatRecyclerAdapter(groupArrayList)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
}

