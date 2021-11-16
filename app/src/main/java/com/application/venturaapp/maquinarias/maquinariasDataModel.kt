package com.application.venturaapp.maquinarias

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.fitosanitario.entity.GeneralResponse
import com.application.venturaapp.maquinarias.entity.VSAGRRMAQ
import com.application.venturaapp.helper.ErrorBody
import com.application.venturaapp.login.LoginDataModel
import com.application.venturaapp.retrofit.RetrofitClient
import com.google.gson.Gson
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class maquinariasDataModel () {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()
    val messageLiveData = MutableLiveData<String>()
    val responseMaquinariasLiveData = MutableLiveData<List<VSAGRRMAQ>>()
    val listFitosanitario =  arrayListOf<VSAGRRMAQ>()

    val codeError = MutableLiveData<Int>()


    fun listMaquinarias(sessionid:String,codigoPEP : String, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listPEPFMaquinaria(sessionid)
        call?.enqueue(object : Callback<GeneralResponse<VSAGRRMAQ>> {
            override fun onResponse(call: Call<GeneralResponse<VSAGRRMAQ>>, response: Response<GeneralResponse<VSAGRRMAQ>>) {
                if (response.isSuccessful) {
                    //responseLiveData.value = response.body()?.datos
                    for(item in response.body()?.datos!!)
                    {
                        if(item.U_VS_AGR_CDPP  == codigoPEP)
                        {
                            listFitosanitario.add(item)
                        }

                    }
                    responseMaquinariasLiveData.value = listFitosanitario
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