package com.xrest.hamrobazar.UI

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import com.xrest.hamrobazar.Repo.UserRepository

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var register:TextView
    private lateinit var login:TextView
    private lateinit var etUsername :EditText
    private lateinit var etPassword :EditText
    private lateinit var check :CheckBox


    val permission = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!checkPermission())
        {
            requestPermission()
        }
        register = findViewById(R.id.register)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        login = findViewById(R.id.login)
        check = findViewById(R.id.check)
        login.setOnClickListener(this)
        register.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.login -> {

                if (check.isChecked()) {
                    Toast.makeText(this, "You should accept our terms and condition to Login", Toast.LENGTH_SHORT).show()

                    try {

                        CoroutineScope(Dispatchers.IO).launch {
                            val repo = UserRepository()
                            val response = repo.login(etUsername.text.toString(), etPassword.text.toString())
                            if (response.success == true) {

                                RetrofitBuilder.token = "Bearer " + response.token!!
                                RetrofitBuilder.user=response.data
                                var pref = getSharedPreferences("Saved", MODE_PRIVATE)
                                val value = pref.edit()
                                value.putString("username",etUsername.text.toString())
                                value.putString("password",etPassword.text.toString())
                                value.apply()
                                value.commit()
                                startActivity(Intent(this@MainActivity, Dashboard::class.java))

                            } else {
                                withContext(Main)
                                {
                                    Toast.makeText(this@MainActivity, "Wrong Credentials", Toast.LENGTH_LONG).show()
                                }
                            }

                        }


                    } catch (Ex: Exception) {
                        Toast.makeText(this@MainActivity, "${Ex.toString()}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "You should accept our terms and condition to Login", Toast.LENGTH_SHORT).show()

                }



            }
        }
    }

    fun checkPermission():Boolean{

        for(p in permission)
        {
            if(ActivityCompat.checkSelfPermission(this, p)!=PackageManager.PERMISSION_GRANTED)
            {
                return false
            }
        }

        return true
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permission, 1)
    }





}
