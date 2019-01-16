package com.example.rodolfosarabo.testezendeskchatapi.view.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.rodolfosarabo.testezendeskchatapi.R
import com.example.rodolfosarabo.testezendeskchatapi.models.CommentModel
import com.example.rodolfosarabo.testezendeskchatapi.models.User
import java.util.*

class MessageAdapter(val userId: Long?, val ctx: Context, private var messageList: List<CommentModel>?, val users: List<User>, val agentId: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    private lateinit var agentName: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.message_row_client, parent, false)
            SentMessageHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.message_row_agent, parent, false)
            ReceivedMessageHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList?.get(position)
        return if (message?.author_id == userId) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList?.get(position)

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(message)
        }
    }

    inner class ReceivedMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val text: TextView = itemView.findViewById(R.id.agentMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvAgentName: TextView = itemView.findViewById(R.id.tvAgentName)

        fun bind(message: CommentModel?) {
            text.text = message?.body
            tvTime.text = formatTime(message?.created_at ?: "")
            users.forEach {
                if (it.agent == true && it.id == agentId) {
                    agentName = it.name ?: ""
                }
            }

            tvAgentName.text = agentName
        }
    }

    inner class SentMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val text: TextView = itemView.findViewById(R.id.clientMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val ivStatus: ImageView = itemView.findViewById(R.id.ivStatus)


        fun bind(message: CommentModel?) {
            text.text = message?.body
            tvTime.text = formatTime(message?.created_at ?: "")
            if (message?.loading == true) {
                ivStatus.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_clock))
            } else {
                ivStatus.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_check))
            }
        }

    }

    fun formatTime(dateTime: String): String {
        return if (!dateTime.isEmpty()) {
            val time = dateTime.split("T")[1]
            val timeF = time.split(":")
            "${timeF[0]}:${timeF[1]}"
        } else {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            return "$hour:$minute"
        }
    }

    fun atualizar(comments: List<CommentModel>) {
        this.messageList = comments
        notifyDataSetChanged()
    }

}