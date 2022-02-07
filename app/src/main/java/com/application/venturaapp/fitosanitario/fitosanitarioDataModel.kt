package com.application.venturaapp.fitosanitario

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.fertilizante.entity.Almacen
import com.application.venturaapp.fitosanitario.entity.*
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

class fitosanitarioDataModel () {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()
    val messageLiveData = MutableLiveData<String>()
    val responseLiveData = MutableLiveData<List<PEPDato>>()
    val responseVSAGRFITLiveData = MutableLiveData<List<VSAGRRFIT>>()
    val responseAlmacenLiveData = MutableLiveData<List<Almacen>>()
    val responseProductoLiveData = MutableLiveData<List<VSAGRFEQU>>()

    val listLotesFitosanitario =  arrayListOf<FitosanitarioDistribucionResponse>()
    val responseDistribucionLiveData = MutableLiveData<List<FitosanitarioDistribucionResponse>>()
    val ValidacionLote = MutableLiveData<String>()


    val listFitosanitario =  arrayListOf<VSAGRRFIT>()
    val responseDetalleLiveData = MutableLiveData<VSAGRRFIT>()

    lateinit var pref: PreferenceManager
    val codeError = MutableLiveData<Int>()

    fun listAlmacen(sessionid:String,httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listAlmacen(sessionid)
        call?.enqueue(object : Callback<GeneralResponse<Almacen>> {
            override fun onResponse(call: Call<GeneralResponse<Almacen>>, response: Response<GeneralResponse<Almacen>>) {
                if (response.isSuccessful) {

                    responseAlmacenLiveData.value = response.body()?.datos
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

            override fun onFailure(call: Call<GeneralResponse<Almacen>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listFitosanitario(sessionid:String,codigoPEP : String, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listPEPFitosanitario(sessionid)
        call?.enqueue(object : Callback<GeneralResponse<VSAGRRFIT>> {
            override fun onResponse(call: Call<GeneralResponse<VSAGRRFIT>>, response: Response<GeneralResponse<VSAGRRFIT>>) {
                if (response.isSuccessful) {
                    //responseLiveData.value = response.body()?.datos
                    for(item in response.body()?.datos!!)
                    {
                        if(item.U_VS_AGR_CDPP  == codigoPEP)
                        {
                            listFitosanitario.add(item)
                        }

                    }
                    responseVSAGRFITLiveData.value = listFitosanitario
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

            override fun onFailure(call: Call<GeneralResponse<VSAGRRFIT>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }


    fun listDetalleFitosanitario(sessionid:String,docEntry : String, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listDetalleFitosanitario(sessionid, docEntry)
        call?.enqueue(object : Callback<VSAGRRFIT> {
            override fun onResponse(call: Call<VSAGRRFIT>, response: Response<VSAGRRFIT>) {
                if (response.isSuccessful) {
                    responseDetalleLiveData.value = response.body()

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

            override fun onFailure(call: Call<VSAGRRFIT>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }


    fun listDistribucionFitosanitario(sessionid:String,Entry:Int, LineId : Int, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listFitosanitarioDistribucion(sessionid, Entry)
        call?.enqueue(object : Callback<DistribucionResponse> {
            override fun onResponse(call: Call<DistribucionResponse>, response: Response<DistribucionResponse>) {
                if (response.isSuccessful) {

                    for(item in response.body()?.datos!!)
                    {
                        if(item.U_VS_AGR_LNDR == LineId)
                        {
                            listLotesFitosanitario.add(item)
                        }
                    }
                    responseDistribucionLiveData.value = listLotesFitosanitario

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

            override fun onFailure(call: Call<DistribucionResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun ValidarLote(sessionid:String,Code : String, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.ValidarLote(sessionid, Code)
        call?.enqueue(object : Callback<ValidarLote> {
            override fun onResponse(call: Call<ValidarLote>, response: Response<ValidarLote>) {
                if (response.isSuccessful) {


                    ValidacionLote.value = response.body()?.value

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

            override fun onFailure(call: Call<ValidarLote>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listFertilizante(sessionid:String,httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.listFertilizantesQuimicos(sessionid)
        call?.enqueue(object : Callback<GeneralResponse<VSAGRFEQU>> {
            override fun onResponse(call: Call<GeneralResponse<VSAGRFEQU>>, response: Response<GeneralResponse<VSAGRFEQU>>) {
                if (response.isSuccessful) {

                    responseProductoLiveData.value = response.body()?.datos
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

            override fun onFailure(call: Call<GeneralResponse<VSAGRFEQU>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
}