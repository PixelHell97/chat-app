package com.pixel.toctalk.ui.home.fragment.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pixel.toctalk.data.model.ChatMessage
import com.pixel.toctalk.databinding.RowGroupMessageBinding
import com.pixel.toctalk.databinding.RowPrivateMessageBinding
import com.pixel.toctalk.ui.home.fragment.chat.MessageState

class ChatRecyclerView(val state: MessageState, options: FirestoreRecyclerOptions<ChatMessage>) :
    FirestoreRecyclerAdapter<ChatMessage, ChatRecyclerView.BaseViewHolder>(options) {

    abstract class BaseViewHolder(itemView: View) : ViewHolder(itemView) {
        abstract fun bind(message: ChatMessage)
    }

    class PrivateMessageViewHolder(val binding: RowPrivateMessageBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(message: ChatMessage) {
            if (message.sender?.uid == Firebase.auth.currentUser?.uid) {
                binding.itemPrivateReceived.visibility = View.GONE
                binding.itemPrivateSent.visibility = View.VISIBLE
                binding.message = message
            } else {
                binding.itemPrivateReceived.visibility = View.VISIBLE
                binding.itemPrivateSent.visibility = View.GONE
                binding.message = message
            }
        }
    }

    class GroupMessageViewHolder(val binding: RowGroupMessageBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(message: ChatMessage) {
            if (message.sender?.uid == Firebase.auth.currentUser?.uid) {
                binding.itemGroupReceived.visibility = View.GONE
                binding.itemGroupSent.visibility = View.VISIBLE
                binding.message = message
            } else {
                binding.itemGroupReceived.visibility = View.VISIBLE
                binding.itemGroupSent.visibility = View.GONE
                binding.message = message
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (state) {
            MessageState.CONTACT -> {
                MessageState.CONTACT.value
            }

            else -> {
                MessageState.GROUP.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            MessageState.GROUP.value -> {
                val binding = RowGroupMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
                return GroupMessageViewHolder(binding)
            }

            else -> {
                val binding = RowPrivateMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
                PrivateMessageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, message: ChatMessage) {
        when (holder) {
            is PrivateMessageViewHolder -> {
                holder.bind(message)
            }

            is GroupMessageViewHolder -> {
                holder.bind(message)
            }
        }
        holder.bind(message)
    }
}
