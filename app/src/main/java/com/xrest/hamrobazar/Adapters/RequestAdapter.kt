package com.xrest.hamrobazar.Adapters

import android.content.Context
import android.content.Intent
import android.widget.*
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.RequestRepo
import com.xrest.hamrobazar.ResponseClass.Request
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.UI.UserProfile
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestAdapter(val request:Request,val context: Context):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var image: ImageView =viewHolder.itemView.findViewById(R.id.profile)
        var confirm: Button = viewHolder.itemView.findViewById(R.id.confirm)
        var delete:Button = viewHolder.itemView.findViewById(R.id.delete)
        var name: TextView = viewHolder.itemView.findViewById(R.id.name)
        var username = viewHolder.itemView.findViewById(R.id.username) as TextView

        Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${request.From?.Profile!!}").into(image)
        name.text = request.From?.Name
        username.text = request.From?.Username

        image.setOnClickListener(){
            val intent = Intent(context, UserProfile::class.java)
            intent.putExtra("user",request.From!!)
            context.startActivity(intent)
        }
        confirm.setOnClickListener(){

            CoroutineScope(Dispatchers.IO).launch {
                val response = RequestRepo().acceptReq(request)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "You are now friends", Toast.LENGTH_SHORT).show()
                        notifyChanged()
                    }
                }

            }


        }
        delete.setOnClickListener(){

            CoroutineScope(Dispatchers.IO).launch {
                val response = RequestRepo().deleteRequest(request._id!!)
                if(response.success==true)
                {
                    withContext(Dispatchers.Main){

                        Toast.makeText(context, "Removed from request", Toast.LENGTH_SHORT).show()
                        notifyChanged()
                    }
                }

            }
        }


    }

    override fun getLayout(): Int {
return R.layout.confirm_request
    }
}