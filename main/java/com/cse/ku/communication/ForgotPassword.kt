package com.cse.ku.communication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import com.cse.ku.communication.databinding.ActivityForgotPasswordBinding
import com.cse.ku.communication.databinding.LoginTabFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityForgotPasswordBinding
    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    var email =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize binding
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val root = binding.root

        setContentView(root)

        //Configure Progress Dialogue
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Sending mail...")
        progressDialog.setCanceledOnTouchOutside(false)

        //enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //handle forgot password click

        binding.btnResetPassword.setOnClickListener {

            // get email
            email = binding.email.text.toString().trim()
            // validate email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                //invalid email format
                binding.email.error = "Invalid Email"

            }
            else{
                // email is valid, send reset mail
                //show progress
                progressDialog.show()
                // send mail
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        //mail sent
                        progressDialog.dismiss()
                        Toast.makeText(this, "Password reset mail sent",Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { e->
                        // Failed to send mail
                        progressDialog.dismiss()
                        Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}