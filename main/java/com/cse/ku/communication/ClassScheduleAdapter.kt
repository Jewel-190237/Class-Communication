package com.cse.ku.communication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class ClassScheduleAdapter (fm : FragmentManager): FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0-> (return YesterdayFragment())
            1-> (return TodayFragment())
            2-> (return TomorrowFragment())
            else ->
                return TodayFragment()
            }
        }


    override fun getPageTitle(position: Int): CharSequence? {
        when(position)
        {
            0-> (return "Yesterday")
            1-> (return "Today")
            2-> (return "Tomorrow")

        }
        return super.getPageTitle(position)
    }

}

