package com.xrest.hamrobazar.UI.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xrest.hamrobazar.Adapters.AddFriends
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.UserRepository
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.lang.Exception


class SendRequest : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view= inflater.inflate(R.layout.fragment_send_request, container, false)
        val rv:RecyclerView = view.findViewById(R.id.rv)
        val adapter = GroupAdapter<GroupieViewHolder>()
        var flag:Boolean? =null
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = UserRepository().all()
                if(response.success==true)
                {

                for(data in response.data!!)
                {

                    if(RetrofitBuilder.user?.Friends?.size==0)
                    {
                     withContext(Main)
                     {
                         adapter.add(AddFriends(data,requireContext()))
                     }
                    }
                    else{
                        for(datas in RetrofitBuilder.user?.Friends!!)
                        {

                            if(data._id==datas.user._id)
                            {
                                flag = true
                            }
                            else{
                                flag =false
                            }
                        }
                        if(flag==false)
                        {
                            withContext(Main)
                            {
                                adapter.add(AddFriends(data,requireContext()))
                            }
                        }
                        else{

                        }
                    }



                }

                }
            }
            catch (ex:Exception){

            }
        }
        rv.layoutManager= LinearLayoutManager(requireContext())
        rv.adapter=adapter


        return view
    }

}