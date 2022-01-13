package com.application.venturaapp.home.fragment.produccion

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.home.fragment.entities.*
import com.application.venturaapp.login.LoginDataModel
import com.application.venturaapp.preference.PreferenceManager
import com.google.gson.Gson
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.jvm.internal.Intrinsics

import androidx.core.app.NotificationCompat
import com.application.venturaapp.helper.ErrorBody

import com.application.venturaapp.home.fragment.entities.Fundo
import com.application.venturaapp.home.fragment.entities.Sector
import com.application.venturaapp.retrofit.RetrofitClient

import com.application.venturaapp.home.fragment.entities.PersonalResponse

class laborDataModel () {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()
    val messageLiveData = MutableLiveData<String>()
    val responseLiveData = MutableLiveData<List<PEPDato>>()
    val responseCampaniaLiveData = MutableLiveData<List<Campania>>()
    val responseFundoLiveData =  MutableLiveData<List<Fundo>>();
    val responseSectorLiveData =  MutableLiveData<List<Sector>>();
    lateinit var pref: PreferenceManager
    val codeError = MutableLiveData<Int>()
    val listSector =  ArrayList<Sector>()

    fun listLaborCultural(sessionid:String, httpCacheDirectory: Cache, context: Context) {
       val session = "B1SESSION=da6ef198-9ce8-11eb-8000-525400b2b3b7"
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

    fun listCampania(sessionid :String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listCampanas(sessionid)
       call?.enqueue(object : Callback<PersonalResponse<Campania>> {
            override fun onResponse(call: Call<PersonalResponse<Campania>>, response: Response<PersonalResponse<Campania>>) {
                if (response.isSuccessful) {
                    responseCampaniaLiveData.value = response.body()?.datos
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

            override fun onFailure(call: Call<PersonalResponse<Campania>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listFundo(sessionid: String, httpCacheDirectory: Cache, context: Context) {

        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listFundo(sessionid)
        call?.enqueue(object : Callback<PersonalResponse<Fundo>> {
            override fun onResponse(
                call: Call<PersonalResponse<Fundo>>,
                response: Response<PersonalResponse<Fundo>>
            ) {
                if (response.isSuccessful) {
                    responseFundoLiveData.value = response.body()?.datos as List<Fundo>?
                } else {
                    var json =
                        Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)

                    if (json.error.code == 301) {
                        codeError.value = json.error.code

                    } else {
                        messageLiveData.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<PersonalResponse<Fundo>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }


    fun listSector(fundo: String, sessionid: String, httpCacheDirectory: Cache, context: Context) {

        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listSector(sessionid)
        call?.enqueue(object : Callback<PersonalResponse<Sector>> {
            // from class: com.application.venturaapp.home.fragment.produccion.laborDataModel$listSector$1
            // retrofit2.Callback
            override fun onResponse(call: Call<PersonalResponse<Sector>>, response: Response<PersonalResponse<Sector>>) {

                if (response.isSuccessful) {

                    for (item in response.body()?.datos!!) {
                        if (item.U_VS_AGR_FNDO ==  fundo) {
                            listSector.add(item)
                        }
                    }
                    responseSectorLiveData.value = listSector
                }else {
                    var json =
                        Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)

                    if (json.error.code == 301) {
                        codeError.value = json.error.code

                    } else {
                        messageLiveData.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<PersonalResponse<Sector>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listLote(sessionid: String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listFundo(sessionid)
        call?.enqueue(object : Callback<PersonalResponse<Fundo>> {
            override fun onResponse(call: Call<PersonalResponse<Fundo>>,response: Response<PersonalResponse<Fundo>>) {
                var str: String? = null
                var list: List<Fundo?>? = null
                if (response.isSuccessful) {
                    responseFundoLiveData.value= response.body()?.datos
                }else {
                    var json =
                        Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)

                    if (json.error.code == 301) {
                        codeError.value = json.error.code

                    } else {
                        messageLiveData.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<PersonalResponse<Fundo>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

}