package com.xrest.hamrobazar.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xrest.hamrobazar.Adapters.ChatItem
import com.xrest.hamrobazar.Adapters.ChatItem2
import com.xrest.hamrobazar.Adapters.ImageAdapter
import com.xrest.hamrobazar.Adapters.ImageAdapter2
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.InnerMessage
import com.xrest.hamrobazar.Repo.MessageRepo
import com.xrest.hamrobazar.Response.CommonResponse
import com.xrest.hamrobazar.ResponseClass.Users
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

val adapter = GroupAdapter<GroupieViewHolder>()
lateinit var rv:RecyclerView
lateinit var id:String
 lateinit var socket:WebSocket

 lateinit var ib:ImageButton
 lateinit var image:ImageView
 lateinit var ed:EditText
 lateinit var type:String
 var cameraCode=1
var galleryCode=0
class MessageActivity : AppCompatActivity(), View.OnClickListener {
    var img:String?=null
    val url="ws://192.168.0.106:3000"
    var okHttpClient = OkHttpClient()
    val request= Request.Builder().url(url).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dhasboard_fragment)
        adapter.clear()
        type="Message"
         id = intent.getStringExtra("id")!!
        ib = findViewById(R.id.imageb)
        image = findViewById(R.id.image)
        image.isVisible=false
         rv = findViewById(R.id.rv)
         ed = findViewById(R.id.message)
        var send: Button =findViewById(R.id.send)

        socket = okHttpClient.newWebSocket(request,SocketListener(applicationContext))

        actionBar?.setTitle("Messages")
        rv.layoutManager= LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            val repo = MessageRepo()
            val response = repo.getMes(id!!)
            if(response.success==true)
            {
                withContext(Main){
                    for(data in response.data?.Messages!!)
                    {
                        if(data.user?._id==RetrofitBuilder.user?._id)
                        {
                            if(data.format=="Image")
                            {
                                adapter.add(ImageAdapter2(data))
                            }
                            else{
                                adapter.add(ChatItem2(applicationContext,data))
                            }
                        }
                        else{
                            if(data.format=="Image")
                            {
                                adapter.add(ImageAdapter(data))

                            }
                            else{
                                adapter.add(ChatItem(applicationContext,data))
                            }
                        }

                    }
                }
            }
        }
        send.setOnClickListener(this)
        ib.setOnClickListener(this)

        rv.adapter =adapter



    }

    override fun onRestart() {
        super.onRestart()
        socket = okHttpClient.newWebSocket(request,SocketListener(applicationContext))
    }
    override fun onStop() {

        socket.close(1000,"Bye")
        super.onStop()
    }
    fun sendMessage(){
        Log.d("sxxx",type)
        val message = ed.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val repo = MessageRepo()
            var response:CommonResponse?=null
            var json = JSONObject()
            if(img==null)
            {
                response = repo.sendMessages(id!!, InnerMessage(message=message,format = type))!!
                if(response!!.success==true)
                {

                    try {
                        json.put("id", id)
                        json.put("message", message)
                        json.put("user",RetrofitBuilder.user?._id)
                        json.put("format","Message")

                        socket.send(json.toString())
                        withContext(Main)
                        {  image.isVisible=false
                            ed.setText(null)
                            rv.smoothScrollToPosition(adapter.getItemCount() );
                        }

                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                    }

                }
            }
            else if(img!=null)
            {
                val file = File(img)
                val extention = MimeTypeMap.getFileExtensionFromUrl(img)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
                val reqFile = RequestBody.create(MediaType.parse(mimeType),file)
                val body = MultipartBody.Part.createFormData("image",file.name,reqFile)
                response = repo.sendImage(id,body)
                if(response!!.success==true)
                {


                    try {

                        json.put("id", id)
                        json.put("message", response.message)
                        json.put("user",RetrofitBuilder.user?._id)
                        json.put("format","Image")

                        socket.send(json.toString())
                        withContext(Main)
                        {  image.isVisible=false
                            ed.setText(null)
                            rv.smoothScrollToPosition(adapter.getItemCount() );

                        }
                        img=null

                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                    }

                }
            }



        }




    }

fun openCamera(){
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    startActivityForResult(intent, cameraCode)
    image.isVisible= true



}
    fun openGallery(){

        val intent = Intent(Intent.ACTION_PICK)
        intent.type ="image/*"
        startActivityForResult(intent, galleryCode)
        image.isVisible=true

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.imageb->{ val pop = PopupMenu(this,ib)
                pop.menuInflater.inflate(R.menu.gallerycamera,pop.menu)
                pop.setOnMenuItemClickListener {
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
                img="asdasdasd"
            pop.show()}
            R.id.send->{

                sendMessage()

                type="Message"





            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == galleryCode && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                img = cursor.getString(columnIndex)
                image.setImageBitmap(BitmapFactory.decodeFile(img))
                type="Image"

                cursor.close()
            } else if (requestCode == cameraCode && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                img = file!!.absolutePath
                image.setImageBitmap(BitmapFactory.decodeFile(img))
                type="Image"
            }
        }







    }

    private fun bitmapToFile(bitmap: Bitmap, timeStamp: String): File? {
        var file:File?=null
        try{

            file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString()+File.separator+timeStamp)
            file.createNewFile()

            var byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data)
            fileOutputStream.flush()
            fileOutputStream.close()
 }
        catch (ex:java.lang.Exception)
        {

        }




return file
    }
}

 class SocketListener(val context: Context) : WebSocketListener() {
     
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response);

    }
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("success",text)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                try {
                    var json : JSONObject = JSONObject(text)
                    var idz= json.getString("id")
                    var message= json.getString("message")
                    var userId= json.getString("user")
                    var type = json.getString("format")
                      if(idz==id)
                      {

                          if(userId==RetrofitBuilder.user?._id)
                          {
                              if(type=="Image")
                              {

                                  adapter.add(ImageAdapter2( InnerMessage(message=message,user= Users(_id=RetrofitBuilder.user!!._id,Profile = RetrofitBuilder.user!!.Profile))))
                                  ChatItem2(context,InnerMessage()).notifyChanged()
                                  rv.smoothScrollToPosition(adapter.getItemCount() - 1);

                              }
                              else{
                                  adapter.add(ChatItem2(context, InnerMessage(message=message,user= Users(_id=RetrofitBuilder.user!!._id,Profile = RetrofitBuilder.user!!.Profile))))
                                  ChatItem2(context,InnerMessage()).notifyChanged()
                                  rv.smoothScrollToPosition(adapter.getItemCount() - 1);
                              }

                          }
                          else{
                              if(type=="Image")
                              {


                                  adapter.add(ImageAdapter( InnerMessage(message=message,user= Users(_id=RetrofitBuilder.user!!._id,Profile = RetrofitBuilder.user!!.Profile))))
                                  ChatItem2(context,InnerMessage()).notifyChanged()
                                  rv.smoothScrollToPosition(adapter.getItemCount() - 1);
                                  ChatItem(context,InnerMessage()).notifyChanged()
                              }
                              else{
                                  adapter.add(ChatItem(context, InnerMessage(message=message,user= Users(_id=RetrofitBuilder.user!!._id,Profile = RetrofitBuilder.user!!.Profile))))
                                  ChatItem2(context,InnerMessage()).notifyChanged()
                                  rv.smoothScrollToPosition(adapter.getItemCount() - 1);
                                  ChatItem(context,InnerMessage()).notifyChanged()
                              }


                          }

                      }




                    MessageActivity().img=null


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

    }

     override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
         super.onClosing(webSocket, code, reason)
         socket.close(1000,"Bye")
         socket.cancel()
     }



}

