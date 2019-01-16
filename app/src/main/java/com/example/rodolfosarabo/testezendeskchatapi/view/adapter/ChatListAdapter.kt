package com.example.rodolfosarabo.testezendeskchatapi.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.rodolfosarabo.testezendeskchatapi.R
import com.example.rodolfosarabo.testezendeskchatapi.models.RequestModel

class ChatListAdapter(private val requests: List<RequestModel>?, private val ctx: Context) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    private lateinit var clickListener: ChatListAdapter.OnItemClickListener

    fun setClickListener(itemClickListener: ChatListAdapter.OnItemClickListener) {
        this.clickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.chat_list_item, parent, false)
        return ChatListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return requests?.size ?: 0
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val title = requests?.get(position)?.subject
        holder.title.text = title
    }


    inner class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView = itemView.findViewById(R.id.title)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickListener.onClick(view, adapterPosition, requests?.get(adapterPosition)?.id
                    ?: 0, requests?.get(adapterPosition)?.assignee_id ?: 0)
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, position: Int, chatId: Long, agentId: Long)
    }

}