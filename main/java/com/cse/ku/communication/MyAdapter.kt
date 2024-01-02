package com.cse.ku.communication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val classList : ArrayList<ClassList>): RecyclerView.Adapter<MyAdapter.Myholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.class_item,
            parent, false )

        return Myholder(itemView)
    }

    override fun onBindViewHolder(holder: Myholder, position: Int) {

        val currentClass = classList[position]

        holder.courseTitle.text = currentClass.courseTitle
        holder.courseName.text = currentClass.courseName
        holder.startingTime.text = currentClass.startingTime
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    class Myholder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val courseTitle : TextView = itemView.findViewById(R.id.CourseTitle)
        val courseName : TextView = itemView.findViewById(R.id.CourseName)
        val startingTime : TextView = itemView.findViewById(R.id.StartingTime)
    }
}