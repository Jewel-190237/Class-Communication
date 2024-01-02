package com.cse.ku.communication.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cse.ku.communication.LoginActivity
import com.cse.ku.communication.R
import com.cse.ku.communication.SettingsActivity
import com.cse.ku.communication.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    // Firebase database
    private lateinit var databaseReference : DatabaseReference
    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        //tell to get a menu
        setHasOptionsMenu(true)

        //Configure Progress Dialogue
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Signing out...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth

        firebaseAuth = FirebaseAuth.getInstance()

        // get firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //get user data
        setUserData()

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logout -> {
                //logout

                // show progress bar
                progressDialog.show()
                firebaseAuth.signOut()
                //dismiss progressbar
                progressDialog.dismiss()
                startActivity(Intent(requireContext(),LoginActivity::class.java))
                activity?.finish()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear() //clear the menu
        inflater.inflate(R.menu.profile_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUserData() {
        //if user is already logged in go to  chat
        // get current user
        val firebaseUser = firebaseAuth.currentUser

        val uid = firebaseUser?.uid as String
        databaseReference.child(uid).get().addOnSuccessListener {

            //get data
            val name = it.child("name").value.toString()
            val role = it.child("role").value.toString()
            val email = it.child("email").value.toString()

            //set data
            binding.name.text = name
            binding.userRole.text = role
            binding.email.text = email
            //hide progressbar
            binding.progressBar.visibility = View.GONE
            //show pofile layout
            binding.profileLayout.visibility = View.VISIBLE

            //handle logout
            binding.logout.setOnClickListener {

                // show progress bar
                progressDialog.show()
                firebaseAuth.signOut()
                //dismiss progressbar
                progressDialog.dismiss()
                startActivity(Intent(requireContext(),LoginActivity::class.java))
                activity?.finish()
            }
            //handle edit profile button
            binding.editProfile.setOnClickListener {
                //open edit profile page

            }
            // handle password change button
            binding.changePassword.setOnClickListener {
                //open change password page

            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}