package com.xrest.hamrobazar.UI.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.Adapters.Com
import com.xrest.hamrobazar.Adapters.Posts
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.Comments
import com.xrest.hamrobazar.ResponseClass.Post
import com.xrest.hamrobazar.Repo.PostRepo
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DashboardFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        var adapter = GroupAdapter<GroupieViewHolder>()
        var lst:MutableList<Post> = mutableListOf()
        try {
            CoroutineScope(Dispatchers.IO).launch {

              val repo = PostRepo()
                val response = repo.getUser()
                if(response.success==true)
                {

withContext(Main)
{
    for(Data in response.data!!)
    {
        adapter.add(Posts(Data,requireActivity()))
    }
    val rv:RecyclerView = view.findViewById(R.id.rv)
    rv.layoutManager = LinearLayoutManager(requireContext())
    rv.adapter = adapter

}

                }

            }
        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }

        return view
    }
}




