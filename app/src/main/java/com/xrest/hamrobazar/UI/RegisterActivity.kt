package com.xrest.hamrobazar.UI

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.User
import com.xrest.hamrobazar.Repo.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var name: EditText
    private lateinit var profile:ImageView
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var phone:EditText
    private  lateinit var register:TextView
    var image:String?=null
    var GALLERY_CODE =0
    var CAMERA_CODE =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



    initializeView()
        profile.setOnClickListener(this)
        register.setOnClickListener(this)
 }

    private fun initializeView() {
        name = findViewById(R.id.name)
        profile = findViewById(R.id.profile)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        phone = findViewById(R.id.phone)
        register = findViewById(R.id.register)
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.profile->{
                loadPopUp()
            }
            R.id.register -> {
                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
                registerUser()
            }


        }
    }

    private fun registerUser() {
        val user = User(Name = name.text.toString(),Username = username.text.toString(),PhoneNumber = phone.text.toString(),Password = password.text.toString())
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val repo = UserRepository()
                val response = repo.register(user)
                if(response.success==true)
                {
                        uploadImage(response.data?._id!!,image!!)

//                        withContext(Main){
//                            Toast.makeText(this@RegisterActivity, "User Have Been Registered", Toast.LENGTH_SHORT).show()
//
//                        startActivity(Intent(this@RegisterActivity,MainActivity::class.java))
//                    }
                }
                else{
                    withContext(Main){
                        Toast.makeText(this@RegisterActivity, "opps Something went Wrong", Toast.LENGTH_SHORT).show()
                    }
                }



            }
            catch (ex:Exception)
            {
                withContext(Main)
                {
                    Toast.makeText(this@RegisterActivity, "${ex.printStackTrace()}", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    private fun loadPopUp() {

val popupMenu = PopupMenu(this,profile)
        popupMenu.menuInflater.inflate(R.menu.gallerycamera,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener{


            when(it.itemId)
                {
                R.id.gallery->{openGallery()}
                R.id.camera->{openCamera()}

                }
            true

        }
        popupMenu.show()





    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,CAMERA_CODE)

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type ="image/*"
        startActivityForResult(intent,GALLERY_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                image = cursor.getString(columnIndex)
                profile.setImageBitmap(BitmapFactory.decodeFile(image))
                cursor.close()
            } else if (requestCode == CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                image = file!!.absolutePath
                profile.setImageBitmap(BitmapFactory.decodeFile(image))
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, name: String): File? {
        var file:File?=null
        return try{
            file = File(


                getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + name
            )

            file.createNewFile()
            var bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,0,bos)
            var data = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(data)
            fos.flush()
            fos.close()


            file
        }
        catch(ex:java.lang.Exception)
        {
            ex.printStackTrace()
            file
        }


    }

    fun uploadImage(id:String,image:String) {

        val file = File(image)
        val extention = MimeTypeMap.getFileExtensionFromUrl(image)
        val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
        val reqBody = RequestBody.create(MediaType.parse(mimetype),file)
        val body = MultipartBody.Part.createFormData("profile",file.name,reqBody)

        CoroutineScope(Dispatchers.IO).launch {

            try{
                val repo = UserRepository()
                val response = repo.updateProfile(id,body)
                if(response.success==true)
                {
                    withContext(Main){
                        Toast.makeText(this@RegisterActivity, "User Inserted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }

            }
            catch (ex:Exception)
            {

            }




        }









    }


    }


