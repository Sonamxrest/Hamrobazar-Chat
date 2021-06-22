package com.xrest.hamrobazar.Adapters

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.MessageRepo
import com.xrest.hamrobazar.ResponseClass.User
import com.xrest.hamrobazar.ResponseClass.friend
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.UI.MessageActivity
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class FriendsAdapter(val user: friend, val context: Context): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val ll: LinearLayout = viewHolder.itemView.findViewById(R.id.ll)
        val profile: CircleImageView = viewHolder.itemView.findViewById(R.id.pp)
        val name: TextView = viewHolder.itemView.findViewById(R.id.name)
        val online: CircleImageView = viewHolder.itemView.findViewById(R.id.online)
        val lastMessage:TextView = viewHolder.itemView.findViewById(R.id.lastMessage)
        lastMessage.isVisible=false
        online.isVisible=false

            if(user.user?.isOnline==true)
            {
                online.isVisible=true
            }
            name.text = user.user?.Name
            Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${user.user?.Profile}").into(profile)



        ll.setOnClickListener(){

            CoroutineScope(Dispatchers.IO).launch {
                val repo =MessageRepo()
                val response = repo.createInbox(user.user._id!!)
                if(response.success==true)
                {
                    withContext(Main){
                        val intent = Intent(context, MessageActivity::class.java)
                        intent.putExtra("id",response.message)
                        context.startActivity(intent)
                    }

                }

            }


        }

    }

    override fun getLayout(): Int {
        return R.layout.user_layout
    }
}