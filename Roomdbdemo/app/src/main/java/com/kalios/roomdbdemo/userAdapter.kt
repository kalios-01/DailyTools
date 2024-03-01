package com.kalios.roomdbdemo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class userAdapter(
    val context: Context,
    private val UserClickDeleteInterface: UserClickDeleteInterface,
    private val UserClickInterface: UserClickInterface
) :
    RecyclerView.Adapter<userAdapter.ViewHolder>() {

    // on below line we are creating a
    // variable for our all notes list.
    private val getUserAll = ArrayList<User>()

    // on below line we are creating a view holder class.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are creating an initializing all our
        // variables which we have added in layout file.
        val profile: ImageView = itemView.findViewById(R.id.profile)
        val firstName: TextView = itemView.findViewById(R.id.firstName)
        val lastName: TextView = itemView.findViewById(R.id.lastName)
        val deleteBtn: ImageView = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.user_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = getUserAll[position]


        holder.firstName.text = user.firstName
        holder.lastName.text = user.lastName
        val userprofile = BitmapUtils.byteArrayToBitmap(user.profile)
        holder.profile.setImageBitmap(userprofile)
        holder.deleteBtn.setImageResource(R.drawable.delete)
        // on below line we are adding click listener to our delete image view icon.
        holder.deleteBtn.setOnClickListener {
            // on below line we are calling a note click
            // interface and we are passing a position to it.
            UserClickDeleteInterface.onDeleteIconClick(getUserAll[position])
        }

        // on below line we are adding click listener
        // to our recycler view item.
        holder.itemView.setOnClickListener {
            // on below line we are calling a note click interface
            // and we are passing a position to it.
            UserClickInterface.onUserClick(getUserAll[position])
        }
    }

    override fun getItemCount(): Int {
        // on below line we are
        // returning our list size.
        return getUserAll.size
    }

    // below method is use to update our list of notes.
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<User>) {
        getUserAll.clear()
        // on below line we are adding a
        // new list to our all notes list.
        getUserAll.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }
}

interface UserClickDeleteInterface {
    // creating a method for click
    // action on delete image view.
    fun onDeleteIconClick(user: User)
}

interface UserClickInterface {
    // creating a method for click action
    // on recycler view item for updating it.
    fun onUserClick(user: User)
}
