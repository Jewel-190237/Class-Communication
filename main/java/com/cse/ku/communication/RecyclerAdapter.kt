package com.cse.ku.communication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class RecyclerAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        MyApplication.userAddList = arrayListOf()
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]

            holder.name.text = currentitem.Name
            holder.email.text = currentitem.Email
            holder.uid.text = currentitem.uid
            holder.profilePicture.setImageResource(R.drawable.user_demo_icon)
            holder.recyclerViewHolder.setOnClickListener {
                if (holder.checkBox.visibility == View.VISIBLE){
                    holder.checkBox.visibility= View.GONE
                    MyApplication.userAddList = MyApplication.userAddList.filterNot { it == User(currentitem.Name,currentitem.Email,currentitem.Role,currentitem.uid)} as ArrayList<User>
                }
                else{
                    holder.checkBox.visibility = View.VISIBLE
                    MyApplication.userAddList.add(User(currentitem.Name,currentitem.Email,currentitem.Role,currentitem.uid))
                }
            }

    }

    override fun getItemCount(): Int {

        return userList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.textViewName)
        val email : TextView = itemView.findViewById(R.id.textViewEmail)
        val uid: TextView = itemView.findViewById(R.id.textViewUID)
        val profilePicture: ImageView = itemView.findViewById(R.id.imageViewProfile)
        val checkBox: ImageView = itemView.findViewById(R.id.checkBox)
        val recyclerViewHolder: ConstraintLayout = itemView.findViewById(R.id.recyclerViewHolder)

    }

}