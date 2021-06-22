package com.xrest.hamrobazar

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkConnection {


    fun checkInternetConnection(context: Context):Boolean{
        var result = false
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            var network = connectivityManager.activeNetwork ?: return false
            var connection = connectivityManager.getNetworkCapabilities(network) ?: return false
         result = when{

             connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
             connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
             else->false
         }

        }
        else{

            connectivityManager.run {

                connectivityManager.activeNetworkInfo?.run {

                    result = when(type)
                    {

                        ConnectivityManager.TYPE_WIFI->true
                        ConnectivityManager.TYPE_MOBILE->true
                        else ->false
                    }



                }


            }


        }




        return result
    }


}