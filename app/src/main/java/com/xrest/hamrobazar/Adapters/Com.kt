package com.xrest.hamrobazar.Adapters

import android.content.Context
import android.widget.TextView
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.Comments
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView

class Com(val comments: Comments, val context: Context): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val image: CircleImageView = viewHolder.itemView.findViewById(R.id.circleImageView2)
        val name: TextView = viewHolder.itemView.findViewById(R.id.name)
        val comment: TextView = viewHolder.itemView.findViewById(R.id.comment)
        Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${comments.user!!.Profile}").into(image)
        name.text = comments.user.Name!!
        comment.text = comments.comment!!

    }

    override fun getLayout(): Int {
        return R.layout.comments
    }

}