package com.kalios.cantactapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.query.RealmResults

class ContactAdapter {class ContactAdapter(private val contacts: RealmResults<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile : ImageView = itemView.findViewById(R.id.profile)
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.phone_number)
        val btn_Delete: ImageButton= itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameTextView.text = contact.name
        holder.phoneNumberTextView.text = contact.phoneNumber
        val image_bitmap = BitmapUtils.byteArrayToBitmap(contact.profile_img)
        holder.profile.setImageBitmap(image_bitmap)
        holder.btn_Delete
        holder.btn_Delete.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Deleted $position", Toast.LENGTH_SHORT).show()
            notifyItemRemoved(position)
        }
    }
    override fun getItemCount(): Int = contacts.size
}
}