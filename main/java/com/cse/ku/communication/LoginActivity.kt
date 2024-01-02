package com.cse.ku.communication

import FragmentAdapter
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.cse.ku.communication.databinding.ActivityLoginBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        //Configure actionbar
        supportActionBar?.hide()


        var viewPager = findViewById(R.id.view_pager) as ViewPager
        var tablayout = findViewById(R.id.tab_layout) as TabLayout

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(LoginTabFragment(),"Login")
        fragmentAdapter.addFragment(SignupTabFragment(),"Signup")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

    }

}