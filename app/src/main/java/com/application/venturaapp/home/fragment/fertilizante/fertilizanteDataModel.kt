package com.application.venturaapp.home.fragment.fertilizante

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.helper.ErrorBody
import com.application.venturaapp.home.fragment.entities.Campania
import com.application.venturaapp.home.fragment.entities.PEPDato
import com.application.venturaapp.home.fragment.entities.PersonalResponse
import com.application.venturaapp.login.LoginDataModel
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.retrofit.RetrofitClient
import com.google.gson.Gson
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class fertilizanteDataModel () {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()
    val messageLiveData = MutableLiveData<String>()
    val responseLiveData = MutableLiveData<List<PEPDato>>()
    val responseCampaniaLiveData = MutableLiveData<List<Campania>>()

    lateinit var pref: PreferenceManager
    val codeError = MutableLiveData<Int>()


    fun listFertilizante(sessionid:String, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listPEPLaborCultural(sessionid)
        call?.enqueue(object : Callback<PersonalResponse<PEPDato>> {
            override fun onResponse(call: Call<PersonalResponse<PEPDato>>, response: Response<PersonalResponse<PEPDato>>) {
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

            override fun onFailure(call: Call<PersonalResponse<PEPDato>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }



}