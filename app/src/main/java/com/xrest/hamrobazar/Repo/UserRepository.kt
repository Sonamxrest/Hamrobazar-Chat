package com.xrest.hamrobazar.Repo

import com.google.android.gms.common.internal.service.Common
import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.Response.LoginResponse
import com.xrest.hamrobazar.Response.UserResponse
import com.xrest.hamrobazar.Response.UserResponses
import com.xrest.hamrobazar.ResponseClass.User
import com.xrest.hamrobazar.ResponseClass.Users
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.Routes.UserRoutes
import com.xrest.hamrobazar.Retrofit.apiRequest
import okhttp3.MultipartBody

class UserRepository: apiRequest() {
    val api = RetrofitBuilder.buildApiRequest(UserRoutes::class.java)
    suspend fun login(username:String,password:String):LoginResponse{

        return handelApiRequest {
            api.login(username,password)
        }

    }

    suspend fun register(user:User): UserResponse{

        return handelApiRequest {
            api.register(user)
        }

    }
    suspend fun updateProfile(id:String,body:MultipartBody.Part):UserResponse
    {
        return handelApiRequest {
            api.updateProfile(id,body)
        }
    }
    suspend fun updateUser(user: Users):CommonResponse{
        return handelApiRequest {
            api.updateUser(user,RetrofitBuilder.token!!)
        }
    }

    suspend fun getFreiends():UserResponse{
        return handelApiRequest {
            api.showFriends(RetrofitBuilder.token!!)
        }
    }
    suspend fun logout():CommonResponse{
        return handelApiRequest {
            api.logout(RetrofitBuilder.token!!)
        }
    }
    suspend fun all():UserResponses{
        return handelApiRequest {
            api.All()
        }
    }



}