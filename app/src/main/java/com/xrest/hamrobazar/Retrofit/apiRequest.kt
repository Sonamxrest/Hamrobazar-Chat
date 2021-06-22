package com.xrest.hamrobazar.Retrofit

import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

abstract class apiRequest {
    suspend fun <T:Any?> handelApiRequest(call :suspend() ->Response<T>):T{
         val response = call.invoke()
        if(response.isSuccessful)
        {
            return  response.body()!!
        }
        else{
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                try{
                    message.append(JSONObject(it).getString("success"))
                }
                catch (ex:Exception){}
                  }
            message.append("Error Code: "+response.code().toString() )
            println(error)
            throw IOException(message.toString())


        }


    }





}