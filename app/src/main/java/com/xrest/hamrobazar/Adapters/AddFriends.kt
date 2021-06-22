package com.xrest.hamrobazar.Adapters

import android.content.Context
import android.content.Intent
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.RequestRepo
import com.xrest.hamrobazar.ResponseClass.Users
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.UI.UserProfile
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFriends(val user: Users, val context: Context):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var image: ImageView =viewHolder.itemView.findViewById(R.id.profile)
        var add:ImageButton = viewHolder.itemView.findViewById(R.id.add)
        var name:TextView = viewHolder.itemView.findViewById(R.id.name)
        var username = viewHolder.itemView.findViewById(R.id.username) as TextView

        Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${user.Profile!!}").into(image)
        name.text = user.Name
        username.text = user.Username
        image.setOnClickListener(){
            val intent = Intent(context, UserProfile::class.java)
            intent.putExtra("user",user)
            context.startActivity(intent)
        }
        add.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                val response = RequestRepo().sendRequest(user._id!!)
                if(response.success==true)
                {
                    withContext(Main){
                        Toast.makeText(context, "Request Sent Successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun getLayout(): Int {
       return R.layout.unknown_person
    }
}