package com.cse.ku.communication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class AdapterClassSchedule(private val classList: ArrayList<ClassList>) : RecyclerView.Adapter<AdapterClassSchedule.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.class_item,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = classList[position]

        holder.courseName.text = currentitem.courseName
        holder.courseTitle.text = currentitem.courseTitle
        holder.startingTime.text = currentitem.startingTime

        holder.recyclerViewHolder.setOnClickListener {
            // On click class item

        }

    }

    override fun getItemCount(): Int {

        return classList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val courseName : TextView = itemView.findViewById(R.id.CourseName)
        val courseTitle : TextView = itemView.findViewById(R.id.CourseTitle)
        val startingTime : TextView = itemView.findViewById(R.id.StartingTime)
        val recyclerViewHolder: ConstraintLayout = itemView.findViewById(R.id.classListLayout)

    }

}