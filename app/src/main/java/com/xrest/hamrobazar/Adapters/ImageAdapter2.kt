package com.xrest.hamrobazar.Adapters

import android.content.Context
import android.graphics.Color
import android.widget.ImageView

import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide.with
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.InnerMessage
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ImageAdapter2( val message:InnerMessage):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val imageView = viewHolder.itemView.findViewById(R.id.img) as ImageView
     val transform = RoundedTransformationBuilder().borderColor(Color.BLACK)
             .borderWidthDp(3f)
             .cornerRadiusDp(30f)
             .oval(false)
             .build();
        Picasso.get()
                .load("${RetrofitBuilder.BASE_URL}images/${message.message!!}")
                .fit()
                .transform(transform)
                .into(imageView);
    }

    override fun getLayout(): Int {
      return R.layout.image_right
    }
}