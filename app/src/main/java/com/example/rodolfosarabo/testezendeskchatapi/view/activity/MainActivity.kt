package com.example.rodolfosarabo.testezendeskchatapi.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.example.rodolfosarabo.testezendeskchatapi.*
import com.example.rodolfosarabo.testezendeskchatapi.models.AddUser
import com.example.rodolfosarabo.testezendeskchatapi.models.UserList
import com.example.rodolfosarabo.testezendeskchatapi.models.UserModel
import com.google.gson.Gson
import com.zendesk.service.ErrorResponse
import com.zendesk.service.ZendeskCallback
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk
import zendesk.support.CreateRequest
import zendesk.support.Request
import zendesk.support.Support

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val identity = AnonymousIdentity.Builder()
                .withNameIdentifier("Rodolfo iFood EndUser")
                .withEmailIdentifier("rodolfo.sarabo@ifood.com.br")
                .build()
        Zendesk.INSTANCE.setIdentity(identity)
        prefs = Prefs(this)


        btnLogin.setOnClickListener {
            //            verifyUser()
            createTicket()
        }

        enterSDK.setOnClickListener {
            startActivity(Intent(this, ZendeskSdkActivity::class.java))
//            RequestActivity.builder()
//                    .withRequestSubject("Testing Support SDK 2.0")
//                    .show(this@MainActivity)
        }

        btnSignIn.setOnClickListener {
            signInUser()
        }
    }

    private fun createTicket() {
        val requestProvider = Support.INSTANCE.provider()?.requestProvider()

        val request = CreateRequest().apply {
            description = "Teste de Ticket"
            subject = "Oie"
        }

        requestProvider?.createRequest(request, object : ZendeskCallback<Request>() {
            override fun onSuccess(p0: Request?) {
                Log.d("Request", p0.toString())
            }

            override fun onError(p0: ErrorResponse?) {
                Log.d("error", p0.toString())
            }

        })

    }

    private fun verifyUser() {
        val chamada = apontamentos.getUsers(prefs.getUserAuth(AGENT_AUTH))

        chamada.enqueue(object : Callback<UserList> {
            override fun onFailure(call: Call<UserList>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                Log.d("Users", response.toString())
                Log.d("Users", response.body().toString())
                val email = email.text.toString()
                val jsonObject = Gson().toJsonTree(response.body()).asJsonObject
                val users = Gson().fromJson<UserList>(jsonObject, UserList::class.java)
                users.users.forEach {
                    if (it.email == email) {
                        Log.d("USER", "FOUND")
                        startActivity(Intent(this@MainActivity, ChatListActivity::class.java))
                    }
                }

            }

        })
    }

    private fun signInUser() {
        val user = AddUser(UserModel(name = name.text.toString(), email = email.text.toString(), verified = true))

        val chamada = apontamentos.createUser(prefs.getUserAuth(AGENT_AUTH), user)
        chamada.enqueue(object : Callback<Any> {

            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("User Response", response.toString())
                Log.d("User Response", response.body().toString())
                if (response.code() == 201) {
                    Log.d("User", "Usuario criado")
                    val jsonObject = Gson().toJsonTree(response.body()).asJsonObject
                    val userModel = Gson().fromJson<AddUser>(jsonObject, AddUser::class.java).user
                    prefs.saveUserData(userModel)
                    val userToken = String.format(USER_TOKEN, userModel.email)
                    val byteArray = userToken.toByteArray(Charsets.UTF_8)
                    val tmp = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                    Prefs(this@MainActivity).saveUserAuth(USER_AUTH, "Basic $tmp")
                    Log.d("User", "Id: ${userModel.id}\nName: ${userModel.name}\nEmail: ${userModel.email}")
                    startActivity(Intent(this@MainActivity, ChatListActivity::class.java))
                }
            }

        })
    }
}