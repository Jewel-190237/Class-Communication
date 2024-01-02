package com.cse.ku.communication

import android.app.ProgressDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.cse.ku.communication.databinding.RegTabFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupTabFragment : Fragment() {


    //view binding
    private lateinit var binding: RegTabFragmentBinding
    //Action bar

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog
    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    // Firebase database
    private lateinit var databaseReference : DatabaseReference
    // Get edittext
    private var name = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""
    private var role = ""
    private lateinit var radioButton : RadioButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = RegTabFragmentBinding.inflate(layoutInflater)
        //val root = inflater.inflate(R.layout.reg_tab_fragment, container, false) as ViewGroup
        val root = binding.root

        //Configure Progress Dialogue
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Signing up...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth

        firebaseAuth = FirebaseAuth.getInstance()

        // get firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        // handle click, begin signup
        binding.btnRegister.setOnClickListener{

            validateData()

        }
        return root
    }

    private fun validateData() {
        //get data
        name = binding.name.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString()
        confirmPassword = binding.confirmPassword.text.toString()
        val selectedRadiobuttonId : Int = binding.radiogroup.checkedRadioButtonId
        radioButton = binding.root.findViewById(selectedRadiobuttonId)
        role = radioButton.text.toString().trim()

        //validate data
        if (TextUtils.isEmpty(name)){
            //no name entered
            binding.name.error = "Please enter a display name"
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.email.error = "Invalid Email"

        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.password.error = "Please enter password"
        }
        else if (password.length < 6){
            //password length is less than 6
            binding.password.error = "Password must be alleast 6 characters long"
        }
        else if(password != confirmPassword){
            // confirm password mismatch
            binding.confirmPassword.error = "Password did not match"
        }
        else if (selectedRadiobuttonId == -1){
            binding.roleChoose.error = "Choose a role"
        }
        else{
            //data is valid, continue signup
            firebaseSignup()
        }
    }

    private fun firebaseSignup() {
        // show progress bar
        progressDialog.show()

        // create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // signup success

                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val emails = firebaseUser!!.email
                Toast.makeText(requireContext(),"Account created with: $emails.", Toast.LENGTH_SHORT).show()

                //add profile
                val uid = firebaseUser.uid
                val user = User(name,email,role)

                if (uid != null){
                    databaseReference.child(uid).setValue(user)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                //successfully added profile data
                                Toast.makeText(requireContext(),"Successful", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                //failed to add profile data
                                Toast.makeText(requireContext(),"Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }


                //open chat
                startActivity(Intent(requireContext(),MainActivity::class.java))
                activity?.finish()
            }
            .addOnFailureListener { e->
                // signup failed
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Signup failed due to: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}