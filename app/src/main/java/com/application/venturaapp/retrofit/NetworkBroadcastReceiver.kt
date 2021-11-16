package com.application.venturaapp.retrofit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.Network.NetworkOnActivity
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.login.LoginViewModel
import com.application.venturaapp.tables.PersonalDB
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.gson.JsonArray
import com.google.gson.JsonObject


@Suppress("DEPRECATION")
class NetworkBroadcastReceiver : BroadcastReceiver() {
    lateinit var viewModel: LoginViewModel


    override fun onReceive(context: Context?, intent: Intent?) {
       /* val connMgr = context
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val wifi = connMgr
            .getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        val mobile = connMgr
            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (wifi?.isAvailable!! || mobile?.isAvailable!!) {
                context?.startActivity(Intent(context, NetworkOnActivity::class.java))
            }*/
            //viewModel = ViewModelProviders.of(context?.applicationContext).get(LoginViewModel::class.java)

        if(isInternetConnected(context))
            {
                context?.startActivity(Intent(context, NetworkOnActivity::class.java))
                Toast.makeText(context,"Network Connected", Toast.LENGTH_LONG).show()
            }else
            {
                Toast.makeText(context,"No Network Connected", Toast.LENGTH_LONG).show()

            }


    }

    fun isInternetConnected(context: Context?): Boolean {
        val ConnectionManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    

}