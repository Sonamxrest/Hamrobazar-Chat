package com.xrest.hamrobazar.UI

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.ResponseClass.Post
import com.xrest.hamrobazar.Repo.PostRepo
import com.xrest.hamrobazar.Repo.UserRepository
import com.xrest.hamrobazar.ResponseClass.Users
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.UI.ui.SendRequest
import com.xrest.hamrobazar.UI.ui.ViewRequest
import com.xrest.hamrobazar.UI.ui.dashboard.DashboardFragment
import com.xrest.hamrobazar.UI.ui.home.FriendFragment
import com.xrest.hamrobazar.UI.ui.notifications.MessageFragment
import de.hdodenhof.circleimageview.CircleImageView

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
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {
    var img:String?=null
    lateinit var imageView:CircleImageView
    lateinit var spinner:Spinner
    lateinit var spinner2:Spinner
    val GALLERY_CODE =0
    val CAMERA_CODE =1

    lateinit var toggle:ActionBarDrawerToggle
    lateinit var drawerLayout:DrawerLayout

    val permission =
            arrayOf(android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
    val array = arrayOf("Electronics","Musical","AutoMobiles","Furniture","Real State")
    val array2 = arrayOf("Brand New","Like New","Used")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val navView: BottomNavigationView = findViewById(R.id.nav_view2)
        var navView2:NavigationView = findViewById(R.id.sideNave)
        var header = navView2.getHeaderView(0)
        val cv:CircleImageView = header.findViewById(R.id.cv)
        val name:TextView = header.findViewById(R.id.name)
        val number:TextView = header.findViewById(R.id.number)
        Glide.with(applicationContext).load("${RetrofitBuilder.BASE_URL}images/${RetrofitBuilder.user?.Profile}").into(cv)
        name.text = RetrofitBuilder.user?.Name
        number.text = RetrofitBuilder.user?.PhoneNumber

        actionBar?.setTitle("Hamrobazar")
        val view:FrameLayout = findViewById(R.id.fl)
        navView.background=null
        navView.menu.getItem(2).isEnabled =false
        val action:FloatingActionButton = findViewById(R.id.fab)
        drawerLayout = findViewById(R.id.container)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        action.isVisible=false

        navView2.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.logout -> {
               logout()
                }
                R.id.message->{
                    changeFragment(MessageFragment())
                }
                R.id.feeds->{
                    val intent = Intent(this,UserProfile::class.java)
                    val u = RetrofitBuilder.user!!
                    val user = Users(_id=u._id,Name = u.Name,Username = u.Username,Profile = u.Profile,PhoneNumber = u.PhoneNumber,Password =u.Password)
                    intent.putExtra("user",user)
                    startActivity(intent)
                }
                R.id.request->{
                    changeFragment(ViewRequest())
                }
                R.id.online->{
                    changeFragment(FriendFragment())
                }
            }

            true
        }
          changeFragment(DashboardFragment())
          navView.setOnNavigationItemSelectedListener() {
           when(it.itemId)
           {
               R.id.navigation_dashboard->{
                   changeFragment(FriendFragment())
               }
               R.id.nav_profile->{
                   changeFragment(SendRequest())

               }
               R.id.navigation_notifications->{
                   changeFragment(MessageFragment())
               }
               R.id.navigation_home->{

                   action.isVisible=true
                   changeFragment(DashboardFragment())
               }

           }
          true
        }

        action.setOnClickListener() {
           popUpForm()
}
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun logout(){
        CoroutineScope(Dispatchers.IO).launch {
            val reponse = UserRepository().logout()
            if (reponse.success == true) {
                withContext(Main){
                    val intent = Intent(this@Dashboard,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }

            } else {
                withContext(Main) {
                    Toast.makeText(this@Dashboard, "Sorry Our server is out", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun changeFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {

            replace(R.id.fl,fragment)
            addToBackStack(null)
            commit()

        }

    }




    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,CAMERA_CODE)

    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,GALLERY_CODE)

    }
    fun checkPermission():Boolean{

        for(p in permission)
        {
            if(ActivityCompat.checkSelfPermission(this,p)!= PackageManager.PERMISSION_GRANTED)
            {
                return false
            }
        }

        return true
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(this,permission,1)
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
                img = cursor.getString(columnIndex)
                imageView.setImageBitmap(BitmapFactory.decodeFile(img))
                cursor.close()
            } else if (requestCode == CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                img = file!!.absolutePath
                imageView.setImageBitmap(BitmapFactory.decodeFile(img))
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, name: String): File? {
    var file :File?=null
     return try {
         file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + File.separator + name)
         file.createNewFile()
         var bos = ByteArrayOutputStream()
         bitmap.compress(Bitmap.CompressFormat.PNG,0,bos)
         var data = bos.toByteArray()
         var fos = FileOutputStream(file)
         fos.write(data)
         fos.flush()
         fos.close()
         file
     }
     catch (ex:java.lang.Exception)
     {
         ex.printStackTrace()
         file
     }
    }
    fun addPost(post: Post)
    {
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val repo = PostRepo()
                val response = repo.insertPost(post)
                if(response.success==true)
                {
                    uploadImage(response.message,img)
                }
            }
        }
        catch(ex:Exception)
        {
            ex.printStackTrace()
        }
    }

    private fun uploadImage(_id: String?, img: String?) {

        try{
            val file = File(img)
            val extention = MimeTypeMap.getFileExtensionFromUrl(img)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention)
            val reqBody = RequestBody.create(MediaType.parse(mimeType),file)
            val body = MultipartBody.Part.createFormData("image",file.name,reqBody)
            CoroutineScope(Dispatchers.IO).launch {
                val repo = PostRepo()
                val response = repo.updatePostImage(_id!!,body)
                if(response.success==true)
                {
                    withContext(Main)
                    {
                        Toast.makeText(this@Dashboard, "Post have been added", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        catch(ex:java.lang.Exception)
        {
        }
    }



    fun popUpForm(){

        var category: String? = null
        var condition: String? = null
        val dialog = Dialog(this)
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

        if(rg.isChecked==true)
        {
            nego=true
        }else if(r2.isChecked==true)
        {
            nego=false
        }

        val post: TextView = dialog.findViewById(R.id.post)
        post.setOnClickListener() {
            addPost(Post(
                    Name= name.text.toString(),
                    Price = price.text.toString(),
                    Description = description.text.toString(),
                    UsedFor = usedFor.text.toString().toInt(),
                    Negotiable = nego,
                    Condition = condition,
                    Category = category)
            )
            changeFragment(DashboardFragment())
            dialog.cancel()

        }
        imageView.setOnClickListener(){
            if(!checkPermission())
            {
                requestPermission()
            }
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
        spinner2!!.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,array2)
        spinner!!.adapter =ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,array)

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




}


