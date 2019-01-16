package com.example.rodolfosarabo.testezendeskchatapi.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.rodolfosarabo.testezendeskchatapi.CHAT_ID
import com.example.rodolfosarabo.testezendeskchatapi.R
import com.example.rodolfosarabo.testezendeskchatapi.USER_AUTH
import com.example.rodolfosarabo.testezendeskchatapi.models.CommentModel
import com.example.rodolfosarabo.testezendeskchatapi.models.NewRequest
import com.example.rodolfosarabo.testezendeskchatapi.models.RequestModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_chat_form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewChatFormActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat_form)

        btnStartChat.setOnClickListener {
            createTicket()
        }

    }

    private fun createTicket() {
        val request = NewRequest(RequestModel(subject = etMotivo.text.toString(), comment = CommentModel(body = etMensagem.text.toString())))

        val requestJson = Gson().toJson(request)
        Log.d("JSON", requestJson.toString())

        val chamada = apontamentos.createRequest(prefs.getUserAuth(USER_AUTH), request)

        chamada.enqueue(object : Callback<Any> {

            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("response", response.toString())
                val jsonObject = Gson().toJsonTree(response.body())
                Log.d("teste", jsonObject.toString())
                Log.d("teste", response.body().toString())
                val objeto = Gson().fromJson<NewRequest>(jsonObject, NewRequest::class.java).request

                Log.d("objeto", objeto.toString())
                startActivity(Intent(this@NewChatFormActivity, ChatActivity::class.java).apply {
                    putExtra(CHAT_ID, objeto.id)
                })
            }

        })
    }
}