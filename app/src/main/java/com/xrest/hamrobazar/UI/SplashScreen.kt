package com.xrest.hamrobazar.UI

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.xrest.hamrobazar.NetworkConnection
import com.xrest.hamrobazar.R
import com.xrest.hamrobazar.Repo.UserRepository
import com.xrest.hamrobazar.Retrofit.RetrofitBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.util.concurrent.Executor

class SplashScreen : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val pref = getSharedPreferences("Saved",MODE_PRIVATE)
        val username = pref.getString("username","").toString()
        val password =pref.getString("password","").toString()



        if(NetworkConnection.checkInternetConnection(this@SplashScreen))
        {
            CoroutineScope(Dispatchers.IO).launch {
                val response = UserRepository().login(username,password)
                delay(5000)
                if(response.success==true)
                {
                    withContext(Main)
                    {   RetrofitBuilder.token ="Bearer "+response.token!!
                        RetrofitBuilder.user=response.data!!
                        startActivity(Intent(this@SplashScreen,Dashboard::class.java))
                    }
                }
                else{
                    startActivity(Intent(this@SplashScreen,MainActivity::class.java))

                }

            }
        }
        else{
            Toast.makeText(this, "No Network Established", Toast.LENGTH_LONG).show()
        }


    }





}