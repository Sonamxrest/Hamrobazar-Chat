package com.xrest.hamrobazar.Response

import com.xrest.hamrobazar.ResponseClass.Message

class MessageResponse(
        val success:Boolean?=null,
        val data:MutableList<Message>?=null
) {
}