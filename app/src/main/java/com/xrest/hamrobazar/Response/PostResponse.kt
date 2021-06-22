package com.xrest.hamrobazar.Response

import com.xrest.hamrobazar.ResponseClass.Post

data class PostResponse(
        val success:Boolean?=null,
        val data:MutableList<Post>?=null
) {
}