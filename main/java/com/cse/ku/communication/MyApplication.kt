package com.cse.ku.communication

import android.app.Application

class MyApplication: Application() {
    companion object{
        var userAddList: ArrayList<User> = arrayListOf()
    }

    override fun onCreate() {
        super.onCreate()
    }
}