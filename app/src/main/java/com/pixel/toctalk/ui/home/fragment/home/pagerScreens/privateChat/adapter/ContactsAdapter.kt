package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.privateChat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.R
import com.pixel.toctalk.data.database.MyDatabase
import com.pixel.toctalk.data.model.Contact
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.databinding.ItemContactBinding

class ContactsAdapter(val context: Context, options: FirestoreRecyclerOptions<Contact>) :
    FirestoreRecyclerAdapter<Contact, ContactsAdapter.ContactsViewHolder>(options) {
    class ContactsViewHolder(val context: Context, val binding: ItemContactBinding) :
        ViewHolder(binding.root) {
        fun bind(contactUser: User?, contact: Contact) {
            binding.lastMessageTv.visibility = View.VISIBLE
            binding.lastMessageDateTv.visibility = View.VISIBLE
            binding.user = contactUser
            binding.contact = contact
            val message = contact.lastMessage?.content
            if (message.isNullOrEmpty()) return
            if (message.length >= 32) {
                if (contact.lastMessage.sender?.uid == Firebase.auth.currentUser?.uid) {
                    binding.lastMessageTv.text =
                        context.resources.getString(
                            R.string.you,
                            message.replaceRange(28, message.length, "…"),
                        )
                } else {
                    binding.lastMessageTv.text =
                        message.replaceRange(28, message.length, "…")
                }
            } else {
                if (contact.lastMessage.sender?.uid == Firebase.auth.currentUser?.uid) {
                    binding.lastMessageTv.text =
                        context.resources.getString(
                            R.string.you,
                            message,
                        )
                } else {
                    binding.lastMessageTv.text = message
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ContactsViewHolder {
        val binding =
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return ContactsViewHolder(context, binding)
    }

    override fun onBindViewHolder(
        holder: ContactsViewHolder,
        position: Int,
        contact: Contact,
    ) {
        val myUserID = Firebase.auth.currentUser?.uid
        for (uid in contact.usersID!!) {
            if (uid != myUserID) {
                MyDatabase.getUser(uid) { task ->
                    if (task.isSuccessful) {
                        val contactUser = task.result.toObject(User::class.java)
                        holder.bind(contactUser, contact)
                    }
                }
            }
        }
        holder.binding.root.setOnClickListener {
            onContactClickListener?.onItemClick(contact)
        }
    }

    private var onContactClickListener: OnItemClickListener? = null
    fun setOnContactClickListener(listener: OnItemClickListener) {
        onContactClickListener = listener
    }

    fun interface OnItemClickListener {
        fun onItemClick(contact: Contact)
    }
}
