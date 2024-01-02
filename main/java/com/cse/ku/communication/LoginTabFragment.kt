package com.cse.ku.communication

import android.app.ProgressDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.cse.ku.communication.databinding.LoginTabFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class LoginTabFragment : Fragment() {

    //view binding
    private lateinit var binding: LoginTabFragmentBinding
    //Action bar

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog
    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    // Get edittext
    private var email = ""
    private var password = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = LoginTabFragmentBinding.inflate(layoutInflater)
        //val root = inflater.inflate(R.layout.login_tab_fragment, container, false) as ViewGroup
        val root = binding.root

        //Configure Progress Dialogue
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // handle login btn click

        binding.btnLogin.setOnClickListener{

            validateData()
        }

        //handle Forgot password click

        binding.forgotPassword.setOnClickListener{
            startActivity(Intent(requireContext(),ForgotPassword::class.java))
        }


        return root
    }

    private fun checkUser() {
        //if user is already logged in go to  chat
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            //user is already logged in
            startActivity(Intent(requireContext(),MainActivity::class.java))
            activity?.finish()
        }
    }

    private fun validateData(){
        //get data
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.email.error = "Invalid Email"

        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.password.error = "Please enter password"
        }
        else{
            //data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progress
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                //Toast.makeText(requireContext(),"Logged in as: $email.", Toast.LENGTH_SHORT).show()

                //open chat
                startActivity(Intent(requireContext(),MainActivity::class.java))
                activity?.finish()
            }
            .addOnFailureListener { e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Login failed due to: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}