package com.xrest.hamrobazar.Repo

import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.Response.PostResponse
import com.xrest.hamrobazar.ResponseClass.Comments
import com.xrest.hamrobazar.ResponseClass.Post
import com.xrest.hamrobazar.Routes.PostRoutes
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.Retrofit.apiRequest
import okhttp3.MultipartBody

class PostRepo: apiRequest() {

    val api = RetrofitBuilder.buildApiRequest(PostRoutes::class.java)


    suspend fun  insertPost(post: Post):CommonResponse{
        return handelApiRequest {
            api.addPost(RetrofitBuilder.token!!,post)
        }
    }
    suspend fun  updatePostImage(id:String,body:MultipartBody.Part):CommonResponse{
        return handelApiRequest {
            api.updatePimage(id,body)
        }
    }
    suspend fun  getUser():PostResponse{
        return handelApiRequest {
            api.getProducts()
        }
    }

    suspend fun Like(id:String):CommonResponse{
        return handelApiRequest {
            api.likeProduct(RetrofitBuilder.token!!,id)
        }
    }
    suspend fun comment(comment:Comments):CommonResponse{
        return handelApiRequest {
            api.comment(RetrofitBuilder.token!!,comment)
        }
    }

suspend fun update(id:String,post:Post):CommonResponse
{
    return handelApiRequest {
        api.updateProduct(id,post)
    }
}
    suspend fun delete(id:String):CommonResponse{
        return  handelApiRequest {
            api.delete(id)
        }
    }
    suspend fun sold(id:String):CommonResponse{
        return  handelApiRequest {
            api.sold(id)
        }
    }

    suspend fun pp(id:String):PostResponse{
        return handelApiRequest {
            api.getPP(id)
        }
    }

}