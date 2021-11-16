package com.application.venturaapp.home.fragment.maquinaria

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.fitosanitario.entity.GeneralResponse
import com.application.venturaapp.maquinarias.entity.VSAGRRMAQ
import com.application.venturaapp.helper.ErrorBody
import com.application.venturaapp.home.fragment.entities.Campania
import com.application.venturaapp.login.LoginDataModel
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.retrofit.RetrofitClient
import com.google.gson.Gson
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class maquinariaDataModel () {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()
    val messageLiveData = MutableLiveData<String>()
    val responseLiveData = MutableLiveData<List<VSAGRRMAQ>>()
    val responseCampaniaLiveData = MutableLiveData<List<Campania>>()

    lateinit var pref: PreferenceManager
    val codeError = MutableLiveData<Int>()


    fun listMaquinaria(sessionid:String, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listPEPFMaquinaria(sessionid)
        call?.enqueue(object : Callback<GeneralResponse<VSAGRRMAQ>> {
            override fun onResponse(call: Call<GeneralResponse<VSAGRRMAQ>>, response: Response<GeneralResponse<VSAGRRMAQ>>) {
                if (response.isSuccessful) {
                    responseLiveData.value = response.body()?.datos
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)

                    if(json.error.code==301)
                    {
                        codeError.value = json.error.code

                    }else {
                        messageLiveData.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<GeneralResponse<VSAGRRMAQ>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

}