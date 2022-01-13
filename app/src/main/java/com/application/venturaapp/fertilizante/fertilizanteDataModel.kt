package com.application.venturaapp.fertilizante

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.fitosanitario.entity.GeneralResponse
import com.application.venturaapp.fitosanitario.entity.VSAGRRCOS
import com.application.venturaapp.helper.ErrorBody
import com.application.venturaapp.home.fragment.entities.*
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
    val responseVSAGRFERLiveData = MutableLiveData<List<VSAGRRCOS>>()
    val listFitosanitario =  arrayListOf<VSAGRRCOS>()

    lateinit var pref: PreferenceManager
    val codeError = MutableLiveData<Int>()


    fun listFertilizante(sessionid:String,codigoPEP : String, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listPEPFertilizante(sessionid)
        call?.enqueue(object : Callback<GeneralResponse<VSAGRRCOS>> {
            override fun onResponse(call: Call<GeneralResponse<VSAGRRCOS>>, response: Response<GeneralResponse<VSAGRRCOS>>) {
                if (response.isSuccessful) {
                    //responseLiveData.value = response.body()?.datos
                    for(item in response.body()?.datos!!)
                    {
                        if(item.U_VS_AGR_CDPP  == codigoPEP)
                        {
                            listFitosanitario.add(item)
                        }

                    }
                    responseVSAGRFERLiveData.value = listFitosanitario
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

            override fun onFailure(call: Call<GeneralResponse<VSAGRRCOS>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }



}