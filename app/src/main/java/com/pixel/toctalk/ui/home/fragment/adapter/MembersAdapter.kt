package com.pixel.toctalk.ui.home.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.pixel.toctalk.data.model.User
import com.pixel.toctalk.databinding.ItemContactBinding

class MembersAdapter(option: FirestoreRecyclerOptions<User>) :
    FirestoreRecyclerAdapter<User, MembersAdapter.GroupMembersViewHolder>(option) {
    class GroupMembersViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.lastMessageTv.visibility = View.GONE
            binding.lastMessageDateTv.visibility = View.GONE
            binding.user = user
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMembersViewHolder {
        val binding = ItemContactBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return GroupMembersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupMembersViewHolder, position: Int, user: User) {
        holder.bind(user)
        holder.binding.root.setOnClickListener {
            onContactClickListener?.onItemClick(user)
        }
    }

    private var onContactClickListener: OnItemClickListener? = null
    fun setOnContactClickListener(listener: OnItemClickListener) {
        onContactClickListener = listener
    }

    fun interface OnItemClickListener {
        fun onItemClick(user: User)
    }
}
