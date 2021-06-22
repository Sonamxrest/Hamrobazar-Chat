package com.xrest.hamrobazar.Response

import com.xrest.hamrobazar.ResponseClass.User

data class LoginResponse(
    val success:Boolean?=null,
    val token:String?=null,
    val data: User?=null
) {
}