package com.cse.ku.communication
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class ChatRecyclerAdapter(private val groupList: ArrayList<ChatGroup>) : RecyclerView.Adapter<ChatRecyclerAdapter.MyViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        MyApplication.userAddList = arrayListOf()
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.group_item,
            parent,false)
        return MyViewHolder(itemView)
    }
    public fun ChatRecyclerAdapter(context: Context){
        this.context = context

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = groupList[position]

        holder.groupName.text = currentitem.groupTitle
        holder.groupInfo.text = currentitem.GroupInfo
        Log.w("DEBUG","1: ${holder.gid.text.toString()}")
        holder.gid.text = currentitem.GroupId
        Log.w("DEBUG","2: ${holder.gid.text.toString()}")
        holder.groupIcon.setImageResource(R.drawable.user_demo_icon)
        holder.recyclerViewHolder.setOnClickListener {
            //on click listener
            val intent = Intent(holder.groupIcon.context, GroupChatActivity::class.java)
            intent.putExtra("groupId", holder.gid.text.toString())
            intent.putExtra("groupName", holder.groupName.text.toString())

            holder.groupIcon.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {

        return groupList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val groupName : TextView = itemView.findViewById(R.id.textViewGName)
        val groupInfo : TextView = itemView.findViewById(R.id.textViewGInfo)
        val gid: TextView = itemView.findViewById(R.id.textViewGID)
        val groupIcon: ImageView = itemView.findViewById(R.id.imageViewGIcon)

        val recyclerViewHolder: ConstraintLayout = itemView.findViewById(R.id.groupRecyclerViewHolder)

    }

}