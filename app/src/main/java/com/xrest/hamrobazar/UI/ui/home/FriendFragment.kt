package com.xrest.hamrobazar.UI.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.hamrobazar.Adapters.FriendsAdapter
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.UserRepository
import com.xrest.hamrobazar.Response.UserResponse
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val rv:RecyclerView = root.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        val adapter = GroupAdapter<GroupieViewHolder>()
        CoroutineScope(Dispatchers.IO).launch{

            val repo = UserRepository()
            val response = repo.getFreiends()
            if(response.success==true){

                withContext(Main){

                for(data in response.data?.Friends!!)
                {
                    adapter.add(FriendsAdapter(data,requireContext()))
                }


                }


            }


        }
        rv.adapter=adapter

        return root
    }
}