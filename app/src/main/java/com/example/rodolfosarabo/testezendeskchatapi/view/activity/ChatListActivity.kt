package com.example.rodolfosarabo.testezendeskchatapi.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.rodolfosarabo.testezendeskchatapi.*
import com.example.rodolfosarabo.testezendeskchatapi.models.RequestList
import com.example.rodolfosarabo.testezendeskchatapi.models.RequestModel
import com.example.rodolfosarabo.testezendeskchatapi.models.UserModel
import com.example.rodolfosarabo.testezendeskchatapi.view.adapter.ChatListAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.chat_list_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListActivity : BaseActivity(), ChatListAdapter.OnItemClickListener {

    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_list_activity)
        val prefs = Prefs(this)
        user = prefs.getUserData()

        btnNewChat.setOnClickListener {
            startActivity(Intent(this, NewChatFormActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        findTickets()
    }

    private fun findTickets() {

        val chamada = apontamentos.listRequests(prefs.getUserAuth(USER_AUTH))

        chamada.enqueue(object : Callback<Any> {

            override fun onFailure(call: Call<Any>, t: Throwable) {

            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("Response", response.toString())
                if (response.code() in 200..300) {
                    try {
                        val jsonObject = Gson().toJsonTree(response.body()).asJsonObject
                        val objeto = Gson().fromJson<RequestList>(jsonObject, RequestList::class.java)
                        val myTickets: MutableList<RequestModel> = mutableListOf()
                        objeto.requests.forEach {
                            myTickets.add(it)
                            Log.d("Request", it.subject)
                        }
                        setupRecycler(myTickets)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    Toast.makeText(this@ChatListActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun setupRecycler(requests: List<RequestModel>?) {
        rvConversas.setHasFixedSize(true)
        rvConversas.visibility = View.VISIBLE
        val lm = LinearLayoutManager(this)
        rvConversas.layoutManager = lm
        val adapter = ChatListAdapter(requests, this)
        adapter.setClickListener(this)
        rvConversas.adapter = adapter
    }

    override fun onClick(view: View, position: Int, chatId: Long, agentId: Long) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(CHAT_ID, chatId)
        intent.putExtra(AGENT_ID, agentId)
        startActivity(intent)
    }
}