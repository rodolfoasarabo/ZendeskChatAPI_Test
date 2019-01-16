package com.example.rodolfosarabo.testezendeskchatapi.view.activity

import android.os.Bundle
import android.util.Log
import com.example.rodolfosarabo.testezendeskchatapi.R
import com.zendesk.service.ErrorResponse
import com.zendesk.service.ZendeskCallback
import kotlinx.android.synthetic.main.activity_zendesk_sdk.*
import zendesk.core.Zendesk
import zendesk.support.*

class ZendeskSdkActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zendesk_sdk)

        var request: Request? = null
        var comments: MutableList<CommentResponse>? = mutableListOf()

        val requestProvider = Support.INSTANCE.provider()?.requestProvider()

        requestProvider?.getAllRequests(object : ZendeskCallback<List<Request>>() {
            override fun onSuccess(p0: List<Request>?) {
                p0?.forEach {
                    Log.d("Teste", "ID: ${it.id} \nDescription: ${it.subject}")
                }

                request = p0?.get(0)

                requestProvider.getComments(p0?.first()?.id
                        ?: "", object : ZendeskCallback<CommentsResponse>() {
                    override fun onSuccess(p0: CommentsResponse?) {
                        p0?.comments?.forEach {
                            Log.d("Teste", "ID: ${it.id} \nBody: ${it.body}")
                            comments?.add(it)
                        }
                    }

                    override fun onError(p0: ErrorResponse?) {
                        Log.d("error", p0.toString())
                    }

                })
            }

            override fun onError(p0: ErrorResponse?) {
                Log.d("error", p0.toString())
            }

        })

        btnRefresh.setOnClickListener {
            requestProvider?.getUpdatesForDevice(object : ZendeskCallback<RequestUpdates>(){
                override fun onSuccess(p0: RequestUpdates?) {
                    p0?.requestUpdates?.forEach {
                        Log.d("Updates", "Key: ${it.key} Value: ${it.value}")
                    }
                    Support.INSTANCE.provider()?.requestProvider()?.markRequestAsRead(request?.id ?: "", comments?.size ?: 0)



                    Log.d("Updates", p0?.hasUpdatedRequests().toString())
                }

                override fun onError(p0: ErrorResponse?) {
                    Log.d("error", p0?.toString())
                }

            })
        }


    }
}