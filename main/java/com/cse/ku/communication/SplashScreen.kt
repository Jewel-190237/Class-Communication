package com.cse.ku.communication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SplashScreen : AppCompatActivity() {

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //hide actionbar
        supportActionBar?.hide()

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()


        Handler().postDelayed({
            checkUser()
        }, 1500)
    }

    private fun checkUser() {
        //if user is already logged in go to  chat
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // already logged in
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}