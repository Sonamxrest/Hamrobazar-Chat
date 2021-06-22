package com.xrest.hamrobazar.UI

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.xrest.hamrobazar.Adapters.Posts
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.PostRepo
import com.xrest.hamrobazar.Repo.UserRepository
import com.xrest.hamrobazar.ResponseClass.User
import com.xrest.hamrobazar.ResponseClass.Users
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

class UserProfile : AppCompatActivity() {
    var img:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val user = intent.getParcelableExtra<Users>("user")
        Log.d("xxxx",user?.Name!!)
        var image:CircleImageView = findViewById(R.id.profile)
        val edit:ImageButton = findViewById(R.id.edit)
        Glide.with(applicationContext).load("${RetrofitBuilder.BASE_URL}images/${user!!.Profile}").into(image)
        var rv:RecyclerView = findViewById(R.id.rv)
        val adapter = GroupAdapter<GroupieViewHolder>()
        edit.setOnClickListener(){
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.update_profile)
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.setCancelable(true)
            dialog.show()
             var name: EditText = dialog.findViewById(R.id.name)
             var profile: ImageView= dialog.findViewById(R.id.profile)
             var username: EditText= dialog.findViewById(R.id.username)
             var password: EditText= dialog.findViewById(R.id.password)
             var phone: EditText= dialog.findViewById(R.id.phone)
             var register: TextView= dialog.findViewById(R.id.register)
            name.setText(user!!.Name)
            Picasso.get().load("${RetrofitBuilder.BASE_URL}images/${user!!.Profile}").into(profile)
            username.setText(user!!.Username)
            phone.setText(user!!.PhoneNumber)
            profile.setOnClickListener(){
                val pop = PopupMenu(this,profile)
                pop.menuInflater.inflate(R.menu.gallerycamera,pop.menu)
                pop.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.camera->{
                            val intent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent,1)
                        }
                        R.id.gallery->{
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type="image/*"
                            startActivityForResult(intent,0)
                        }
                    }
                    true
                }
                pop.show()
            }
            register.setOnClickListener(){

                val user = Users(Name = name.text.toString(),Username = username.text.toString(),PhoneNumber = phone.text.toString(),Password = password.text.toString())
                try{

                    CoroutineScope(Dispatchers.IO).launch {

                        val response = UserRepository().updateUser(user)
                        if(response.success==true)
                        {
                            if(img!=null)
                            {
                                val file = File(img)
                                val extention = MimeTypeMap.getFileExtensionFromUrl(img)
                                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
                                val reqBody = RequestBody.create(MediaType.parse(mimeType),file)
                                val body = MultipartBody.Part.createFormData("profile",file.name,reqBody)
                                val response= UserRepository().updateProfile(user._id!!,body)
                                if(response.success==true)
                                {
                                    withContext(Main)
                                    {
                                        Toast.makeText(this@UserProfile, "Update Success", Toast.LENGTH_SHORT).show()
                                    }
                                }



                            }
                            else{
                                withContext(Main)
                                {
                                    Toast.makeText(this@UserProfile, "Update Success", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                    }

                }
                catch (ex:Exception)
                {

                }

            }






        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = PostRepo().pp(user._id!!)
            if(response.success==true)
            {
                for(data in response.data!!)
                {
                    withContext(Main)
                    {
                        adapter.add(Posts(data,this@UserProfile))
                    }

                }

            }
        }
        rv.layoutManager=LinearLayoutManager(this)
        rv.adapter=adapter


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}