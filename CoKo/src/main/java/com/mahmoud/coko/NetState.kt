package com.mahmoud.coko

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat


//HOW TO USE
// in Application class(or any other context if u just want to use isonline() ) initialize the class before using any methods or will throw exception
//NetState.initialize(this)
//call NeState.getLiveConnectivity() <--- returns LiveData of Boolean  , u can  observe connectivity change
//call NetState.isOnline()   <-- returns Boolean of connectivity state true or false
//make sure ACCESS_NETWORK_STATE is granted in manifest


class NetState private  constructor(){


    companion object {
        private lateinit var mcontext: Context
       private var initialized = false
        private  var isConnected = MutableLiveData<Boolean>()
        private lateinit var connectivityManager: ConnectivityManager


        fun getLiveConnectivity(): LiveData<Boolean> {
            if (initialized) {
                if(ContextCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){
                    return isConnected
                }else{
                    throw Exception("Permission ACCESS_NETWORK_STATE is needed")
                }
            } else {
              throw Exception("NetState must be initialized first : call initialize(Context)")
            }
        }

        @SuppressLint("MissingPermission")
         fun isOnline(): Boolean {
            try {

                val netInfo = connectivityManager.activeNetworkInfo
                //should check null because in airplane mode it will be null
                return netInfo != null && netInfo.isConnected
            } catch (e: NullPointerException) {
                e.printStackTrace()
                return false
            }

        }

        private val broadcastReceiverConnectionChanged = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                isConnected.value=isOnline()
            }
        }

         fun initialize( context:Context){
            if(initialized) {

                return
            }
            mcontext=context
            connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            //  register broadcast receiver after starting activity
            val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            context.registerReceiver(broadcastReceiverConnectionChanged, intentFilter)


            initialized=true

        }
    }


}