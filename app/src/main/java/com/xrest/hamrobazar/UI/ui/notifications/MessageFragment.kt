package com.xrest.hamrobazar.UI.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.Adapters.UserItem
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.Message
import com.xrest.hamrobazar.Repo.MessageRepo
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.UI.MessageActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MessageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        var adapter = GroupAdapter<GroupieViewHolder>()
        var rv:RecyclerView = root!!.findViewById(R.id.rv)
        rv.layoutManager= LinearLayoutManager(requireContext())


        CoroutineScope(Dispatchers.IO).launch {

            val repo = MessageRepo()
            val response = repo.getMessages()
            if(response.success==true)
            {
                withContext(Main)
                {
                    for(i in response.data!!)
                    {
                        adapter.add(UserItem(i, requireContext()))
                    }
                }


            }



        }
        rv.adapter=adapter




        return root
    }
}


