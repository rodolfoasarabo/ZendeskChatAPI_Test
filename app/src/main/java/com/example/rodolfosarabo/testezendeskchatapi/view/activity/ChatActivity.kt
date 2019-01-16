package com.example.rodolfosarabo.testezendeskchatapi.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.rodolfosarabo.testezendeskchatapi.*
import com.example.rodolfosarabo.testezendeskchatapi.models.*
import com.example.rodolfosarabo.testezendeskchatapi.view.adapter.MessageAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : BaseActivity() {

    private var chatId: Long = 0
    private var agentId: Long = 0
    val listMessage: MutableList<CommentModel> = mutableListOf()
    val listUser: MutableList<User> = mutableListOf()
    private var user: UserModel? = null
    private lateinit var agent: UserModel
    private var adapter: MessageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        prefs = Prefs(this)

        chatId = intent.getSerializableExtra(CHAT_ID) as Long
        agentId = intent.getSerializableExtra(AGENT_ID) as Long

        user = prefs.getUserData()

        val chamada = apontamentos.getRequestComments(prefs.getUserAuth(USER_AUTH), chatId)

        chamada.enqueue(object : Callback<Any> {

            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val jsonObject = Gson().toJsonTree(response.body()).asJsonObject
                val objeto = Gson().fromJson<CommentsList>(jsonObject, CommentsList::class.java)
                Log.d("MensagensObjeto", "Previous Page: ${objeto.previous_page}\n" +
                        "Next Page: ${objeto.next_page}\n" +
                        "Count: ${objeto.count}")
                objeto.comments?.forEach {
                    Log.d("Comments", "Comment: ${it.body} - AuthorId: ${it.author_id}")
                }

                objeto.users.forEach {
                    Log.d("Users", "User: ${it.name} - AuthorId: ${it.id}")
                }

                objeto.comments?.let {
                    listMessage.addAll(it)
                }

                objeto.users.let {
                    listUser.addAll(it)
                }

                setupRecycler(listMessage)
            }

        })

        btnSendMessage.setOnClickListener { addComment(chatId) }
    }

    private fun setupRecycler(comments: List<CommentModel>?) {
        rvChat.setHasFixedSize(true)
        rvChat.visibility = View.VISIBLE
        val lm = LinearLayoutManager(this)
        rvChat.layoutManager = lm
        comments?.size?.let {
            if (it > 1) {
                rvChat.scrollToPosition(it - 1)
            } else {
                rvChat.scrollToPosition(it)
            }
        }
        adapter = MessageAdapter(user?.id, this, comments, listUser, agentId)
        rvChat.adapter = adapter
    }

    private fun addComment(requestId: Long?) {
        val mensagemObject = AddComment(CommentRequest(CommentModel(body = etMessage.text.toString(), loading = true, author_id = user?.id)))

        val teste = Gson().toJson(mensagemObject)
        Log.d("JSON", teste.toString())

        listMessage.add(listMessage.size, mensagemObject.request.comment)
        etMessage.setText("")
        adapter?.atualizar(listMessage) ?: setupRecycler(listMessage)

        listMessage.size.let {
            if (it > 1) {
                rvChat.scrollToPosition(it - 1)
            } else {
                rvChat.scrollToPosition(it)
            }
        }

        requestId?.let {
            val chamada = apontamentos.updateRequest(prefs.getUserAuth(USER_AUTH), it, mensagemObject)

            chamada.enqueue(object : Callback<Any> {
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    listMessage[listMessage.size - 1].loading = false
                    adapter?.atualizar(listMessage)
                            ?: setupRecycler(listMessage)
                    Log.d("Mensagem Enviada", response.toString())

                }

            })
        }

    }
}