package com.example.rodolfosarabo.testezendeskchatapi.models

//Model to get list of Requests
data class RequestList(val requests: List<RequestModel>, val next_page: String?,
                       val previous_page: String?, val count: Int?)

//Model to add new Request
data class NewRequest(val request: RequestModel)

//Generic Request Model
data class RequestModel(val url: String? = null, val id: Long? = null, val status: String? = null,
                        val subject: String? = null, val description: String? = null,
                        val requester_id: Long? = null, val created_at: String? = null,
                        val comment: CommentModel? = null, val assignee_id: Long? = null)

//region addComment
data class AddComment(val request: CommentRequest)

data class CommentRequest(val comment: CommentModel)
//endregion

data class CommentsList(val comments: List<CommentModel>?, val users: List<User>,
                        val organizations: List<Organization>, val next_page: String?,
                        val previous_page: String?, val count: Int?)

data class CommentModel(val url: String? = null, val id: Long? = null, val type: String? = null,
                        val request_id: Long? = null, val body: String? = null,
                        val author_id: Long? = null, val created_at: String? = null,
                        var loading: Boolean = false)

data class AddUser(val user: UserModel)

data class UserModel(val id: Long? = null, val name: String?, val email: String?,
                     val verified: Boolean? = null)

data class UserList(val users: List<User>)

data class User(val id: Long? = null, val name: String? = null, val photo: String? = null,
                val agent: Boolean? = null, val email: String? = null, val organization_id: Long? = null)

data class Organization(val id: Long?, val name: String?)

