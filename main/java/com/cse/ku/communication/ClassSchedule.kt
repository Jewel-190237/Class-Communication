package com.cse.ku.communication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager.widget.PagerAdapter
import com.cse.ku.communication.databinding.ActivityClassScheduleBinding
import com.google.android.material.tabs.TabLayout

class ClassSchedule : AppCompatActivity() {

    private lateinit var binding: ActivityClassScheduleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClassScheduleBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        binding.viewPager.adapter = ClassScheduleAdapter(fm = supportFragmentManager)
        binding.viewPager.offscreenPageLimit = 2
        binding.tabLayout.setupWithViewPager(binding.viewPager)




    }
}