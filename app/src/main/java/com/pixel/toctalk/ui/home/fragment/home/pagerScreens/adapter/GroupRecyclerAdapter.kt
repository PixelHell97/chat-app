package com.pixel.toctalk.ui.home.fragment.home.pagerScreens.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.pixel.toctalk.data.model.Group
import com.pixel.toctalk.databinding.ItemGroupBinding

class GroupRecyclerAdapter(option: FirestoreRecyclerOptions<Group>) :
    FirestoreRecyclerAdapter<Group, GroupRecyclerAdapter.GroupViewHolder>(option) {
    class GroupViewHolder(val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding =
            ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int, group: Group) {
        holder.binding.group = group
        holder.binding.root.setOnClickListener {
            onGroupClickListener?.onItemClick(group)
        }
    }

    private var onGroupClickListener: OnItemClickListener? = null
    fun setOnGroupClickListener(listener: OnItemClickListener) {
        onGroupClickListener = listener
    }

    fun interface OnItemClickListener {
        fun onItemClick(group: Group)
    }
}
