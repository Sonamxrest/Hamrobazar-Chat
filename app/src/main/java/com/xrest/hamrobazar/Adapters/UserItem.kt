package com.xrest.hamrobazar.Adapters

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.Message
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.UI.MessageActivity
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView


class UserItem(val message: Message, val context: Context): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val ll: LinearLayout = viewHolder.itemView.findViewById(R.id.ll)
        val profile: CircleImageView = viewHolder.itemView.findViewById(R.id.pp)
        val name: TextView = viewHolder.itemView.findViewById(R.id.name)
        val online: CircleImageView = viewHolder.itemView.findViewById(R.id.online)
        val lastMessage:TextView = viewHolder.itemView.findViewById(R.id.lastMessage)


        online.isVisible=false
if(message.Messages?.size==0)
{

lastMessage.isVisible=false
}
        else{
    lastMessage.isVisible=true

    lastMessage.text= message.Messages!!.get(message.Messages?.size!!.minus(1)).message.toString()
        }


        if(message.Sender?._id== RetrofitBuilder.user?._id)
        {

            if(message.Reciever?.isOnline==true)
            {
                online.isVisible=true
            }
            name.text = message.Reciever?.Name
            Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${message.Reciever?.Profile}").into(profile)

        }
        else if(message.Reciever?._id== RetrofitBuilder.user?._id)
        {
            if(message.Sender?.isOnline==true)
            {
                online.isVisible=true
            }
            name.text = message.Sender?.Name
            Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${message.Sender?.Profile}").into(profile)
        }

        ll.setOnClickListener(){



            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("id",message._id)
            context.startActivity(intent)

        }

    }

    override fun getLayout(): Int {
        return R.layout.user_layout
    }

}

