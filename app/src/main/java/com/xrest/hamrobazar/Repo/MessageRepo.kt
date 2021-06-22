package com.xrest.hamrobazar.Repo

import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.Response.MessageResponse
import com.xrest.hamrobazar.Response.SingleMessageResponse
import com.xrest.hamrobazar.ResponseClass.InnerMessage
import com.xrest.hamrobazar.Routes.MessageRoutes
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.Retrofit.apiRequest
import okhttp3.MultipartBody

class MessageRepo: apiRequest(){
    val api = RetrofitBuilder.buildApiRequest(MessageRoutes::class.java)



    suspend fun getMessages():MessageResponse{
        return  handelApiRequest {
            api.getMessage(RetrofitBuilder.token!!)
        }
    }
    suspend fun getMes(id:String):SingleMessageResponse{

        return handelApiRequest {
            api.getMes(id)
        }

    }

    suspend fun  createInbox(Userid:String):CommonResponse{
        return handelApiRequest {
            api.creatInbox(Userid, RetrofitBuilder.token!!)
        }
    }
    suspend fun sendMessages(messageId:String,message:InnerMessage):CommonResponse{
        return handelApiRequest {
            api.sendMessage(messageId, RetrofitBuilder.token!!,message)
        }
    }
suspend fun sendImage(id:String,body:MultipartBody.Part) :CommonResponse{
    return handelApiRequest {
        api.sendImage(id=id,token=RetrofitBuilder.token!!,body=body)
    }
}


}