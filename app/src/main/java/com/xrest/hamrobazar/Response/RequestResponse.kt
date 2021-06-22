package com.xrest.hamrobazar.Response

import com.xrest.hamrobazar.ResponseClass.Request

class RequestResponse(
        val success:Boolean?=null,
        val data:MutableList<Request>?=null
) {
}