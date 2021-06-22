package com.xrest.hamrobazar.Response

import com.xrest.hamrobazar.ResponseClass.User
import com.xrest.hamrobazar.ResponseClass.Users

class UserResponse(
        val success:Boolean?=null,
        val data: User?=null
) {
}
class UserResponses(
        val success:Boolean?=null,
        val data: MutableList<Users>?=null
)