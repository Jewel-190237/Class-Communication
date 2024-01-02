package com.cse.ku.communication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.cse.ku.communication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    // Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    // Firebase database
    private lateinit var databaseReference : DatabaseReference

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth

        firebaseAuth = FirebaseAuth.getInstance()

        // get firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        //check if logged in
        checkUser()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        mySettings()
    }

    private fun mySettings() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val notifications = prefs.getBoolean("Notifications", true)
        val Massages = prefs.getBoolean("Massages", true)
        val Vibrations = prefs.getBoolean("Vibrations", true)
        val Alarms = prefs.getBoolean("Alarms", true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        //return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.settings -> {
                //Toast.makeText(applicationContext, "ActivitySettings", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.about -> {
                Toast.makeText(applicationContext, "About",Toast.LENGTH_SHORT).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun checkUser() {
        //if user is already logged in go to  chat
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // already logged in
            // get user name
            val uid = firebaseUser.uid
            databaseReference.child(uid).child("name").get()
                .addOnSuccessListener {
                    // Show user name as logged in
                   // Toast.makeText(this,"Logged in as: ${it.value}",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                   // Toast.makeText(this,"Error getting user data: $it",Toast.LENGTH_SHORT).show()
                }
        }
        else{
            // User not logged in, go to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


}