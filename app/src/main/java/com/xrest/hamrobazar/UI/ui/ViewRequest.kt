package com.xrest.hamrobazar.UI.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.hamrobazar.Adapters.RequestAdapter
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.RequestRepo
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class ViewRequest : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_request, container, false)

        val rv:RecyclerView = view.findViewById(R.id.rv)
        var adapter = GroupAdapter<GroupieViewHolder>()
        CoroutineScope(Dispatchers.IO).launch {
            val response = RequestRepo().getRequest()
            if(response.success==true)
            {
                for(data in response.data!!)
                {
                    withContext(Main){
                        adapter.add(RequestAdapter(data,requireContext()))
                    }

                }
            }
        }

        rv.layoutManager =LinearLayoutManager(requireContext())
        rv.adapter=adapter
        return view
    }


}