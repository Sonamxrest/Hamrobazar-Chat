package com.xrest.hamrobazar.Response

import com.xrest.hamrobazar.ResponseClass.Message

class SingleMessageResponse(
        val success:Boolean?=null,
        val data:Message?=null
) {
}