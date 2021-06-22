package com.xrest.hamrobazar.Adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.PostRepo
import com.xrest.hamrobazar.ResponseClass.Comments
import com.xrest.hamrobazar.ResponseClass.Post
import com.xrest.hamrobazar.ResponseClass.Users
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.UI.UserProfile
import com.xrest.hamrobazar.UI.ui.dashboard.DashboardFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import java.lang.Exception

class Posts(val post: Post, val context: Activity): Item<GroupieViewHolder>(), View.OnClickListener {
    private lateinit var likees: TextView
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        var image: ImageView = viewHolder.itemView.findViewById(R.id.mainImage)
        var name: TextView = viewHolder.itemView.findViewById(R.id.name)
        var uname: TextView = viewHolder.itemView.findViewById(R.id.uname)
        var sold: TextView = viewHolder.itemView.findViewById(R.id.sold)
        sold.isVisible==false
        sold.text=""


        var images : CircleImageView = viewHolder.itemView.findViewById(R.id.profile)
        var menu:ImageButton = viewHolder.itemView.findViewById(R.id.menu)
        name.text= post.Name!!
        uname.text= post.User?.Name


        likees = viewHolder.itemView.findViewById(R.id.txtLikes)

    images.setOnClickListener()
    {
        val intent = Intent(context, UserProfile::class.java)
        intent.putExtra("user",post.User)
        context.startActivity(intent)
    }


        Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${post.Images}").into(image)
        Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${post.User!!.Profile}").into(images)
        if(post.User._id==RetrofitBuilder.user!!._id)
        {
        menu.isVisible=true
        }
        else{
            menu.isVisible=false
        }
        if(post.SoldOut==true)
        {
            sold.text="Sold Out"

        }
        else{
            sold.text=""
        }

        var like: ImageButton = viewHolder.itemView.findViewById(R.id.like)
        var comment: ImageButton = viewHolder.itemView.findViewById(R.id.comment)


        for(data in post.Likes!!)
        {
            if(data.user==RetrofitBuilder.user?._id)
            {
                like.setImageResource(R.drawable.like)
                likees.text ="You and ${post.Likes!!.size-1} People Liked It"

            }
            else{
                likees.text ="${post.Likes!!.size} People Liked It"
            }
        }


        menu.setOnClickListener(){
            val popupMenu =PopupMenu(context,menu)
            popupMenu.menuInflater.inflate(R.menu.menutop,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.navUpdate->{

                        popUpForm()
                    }
                    R.id.navDelete -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = PostRepo().delete(post._id!!)
                            if (response.success == true) {
                                withContext(Main)
                                {
                                    Toast.makeText(context, "One Item Deleted", Toast.LENGTH_SHORT).show()
                                    notifyChanged()
                                }

                            }
                        }
                    }
                    R.id.navSold->{
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = PostRepo().sold(post._id!!)
                            if (response.success == true) {
                                withContext(Main)
                                {
                                    Toast.makeText(context, "Item Sold", Toast.LENGTH_SHORT).show()
                                    notifyChanged()
                                }

                            }
                        }
                    }

                }
                true
            }
            popupMenu.show()
        }
        comment.setOnClickListener(this)

        like.setOnClickListener(){

                    CoroutineScope(Dispatchers.IO).launch {
                        try {

                            var repo = PostRepo()
                            var response = repo.Like(post._id!!)
                            if (response.success == true) {
                                withContext(Dispatchers.Main)
                                {

                                    if(response.message=="Liked")
                                    {
                                        like.setImageResource(R.drawable.like)
                                        likees.text = "You and ${post.Likes!!.size} People Liked it"
                                    }
                                    else
                                    {
                                        like.setImageResource(R.drawable.ic_baseline_thumb_up_24)
                                        likees.text = "${post.Likes!!.size} Liked It"
                                    }
                                }

                            }
                            else if(response.success==false){
                                withContext(Dispatchers.Main)
                                {

                                }
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

            }

        }


    }

    override fun getLayout(): Int {
        return  R.layout.main_view
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {

            R.id.comment->{
                var dialog = Dialog(context)
                dialog.setContentView(R.layout.comment_popup)
                var rv: RecyclerView = dialog.findViewById(R.id.rvc)
                rv.layoutManager = LinearLayoutManager(context)
                val adapter= GroupAdapter<GroupieViewHolder>()
                for(data in post.Comments!!)
                {
                    adapter.add(Com(data,context))
                }
                rv.adapter = adapter
                var image : ImageView = dialog.findViewById(R.id.hi)
                dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${post.Images}").into(image)
                var edt: EditText =dialog.findViewById(R.id.etComment)
                var button: Button = dialog.findViewById(R.id.send)
                button.setOnClickListener(){

                    try{
                        val repo = PostRepo()
                        CoroutineScope(Dispatchers.IO).launch {

                            val response = repo.comment(Comments(_id= post._id!!,comment = edt.text.toString()))
                            if(response.success==true)
                            {
                                withContext(Dispatchers.Main)
                                {

                               adapter.add(Com(Comments(user=Users(_id=RetrofitBuilder.user?._id,Profile = RetrofitBuilder.user?.Profile,Name = RetrofitBuilder.user?.Name),comment = edt.text.toString()),context))
                                }

                            }
                        }
                    }
                    catch (ex: Exception)
                    {

                    }

                }


                dialog.show()
            }

        }
    }
    val GALLERY_CODE =0
    val CAMERA_CODE =1
    fun popUpForm(){
        val array = arrayOf("Electronics","Musical","AutoMobiles","Furniture","Real State")
        val array2 = arrayOf("Brand New","Like New","Used")
        var img:String?=null
        lateinit var imageView:CircleImageView
        lateinit var spinner:Spinner
        lateinit var spinner2:Spinner

        var category: String? = null
        var condition: String? = null
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.product_form)
        dialog.setCancelable(true)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        var nego: Boolean?=null
        val name: EditText = dialog.findViewById(R.id.name)
        val price: EditText = dialog.findViewById(R.id.price)
        val description = dialog.findViewById(R.id.description) as EditText
        val usedFor: EditText = dialog.findViewById(R.id.usedFor)
        imageView = dialog.findViewById(R.id.image)
        val rg: RadioButton = dialog.findViewById(R.id.r1)
        val r2: RadioButton = dialog.findViewById(R.id.r2)
        val posts: TextView = dialog.findViewById(R.id.post)
        name.setText(post.Name)
        price.setText(post.Price)
        description.setText(post.Description)
       // usedFor.setText(post.UsedFor!!.toInt())
        Glide.with(context).load("${RetrofitBuilder.BASE_URL}images/${post.Images}").into(imageView)
        if(post.Negotiable==true)
        {
            rg.isChecked=true
        }
        else{
            r2.isChecked=true
        }






        posts.setOnClickListener() {

            if(rg.isChecked)
            {
                nego=true
            }else if(r2.isChecked)
            {
                nego=false
            }
           var p = Post(
                    _id = post._id,
                    Name= name.text.toString(),
                    Price = price.text.toString(),
                    Description = description.text.toString(),
                    UsedFor = usedFor.text.toString().toInt(),
                    Negotiable = nego,
                    Condition = condition,
                    Category = category)
            try{
                CoroutineScope(Dispatchers.IO).launch {
                    val response = PostRepo().update(post._id!!,p)
                    if(response.success==true)
                    {
                        withContext(Main)
                        {
                            dialog.cancel()
                            Toast.makeText(context, "Product Updated", Toast.LENGTH_SHORT).show()

                        }
                         }
                }

            }
            catch (ex:Exception){

            }


            dialog.cancel()

        }
        imageView.setOnClickListener(){

            val popupMenu = PopupMenu(dialog.context,imageView)
            popupMenu.menuInflater.inflate(R.menu.gallerycamera,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId)
                {
                    R.id.gallery->{
                        openGallery()
                    }
                    R.id.camera->{
                        openCamera()
                    }
                }
                true

            }
            popupMenu.show()
        }

        spinner = dialog.findViewById(R.id.spinner)
        spinner2 =dialog.findViewById(R.id.spinner2)
        spinner2!!.adapter = ArrayAdapter(context,android.R.layout.simple_list_item_1,array2)
        spinner!!.adapter =ArrayAdapter(context,android.R.layout.simple_expandable_list_item_1,array)

        spinner!!.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        spinner2!!.onItemSelectedListener = object :AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                condition = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        dialog.show()

    }





    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        context.startActivityForResult(intent,CAMERA_CODE)

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        context.startActivityForResult(intent,GALLERY_CODE)
    }



}