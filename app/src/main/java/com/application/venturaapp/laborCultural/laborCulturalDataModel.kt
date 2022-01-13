package com.application.venturaapp.laborCultural

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.fitosanitario.entity.UnidadMedida
import com.application.venturaapp.fitosanitario.entity.VSAGRRCOS
import com.application.venturaapp.helper.ErrorBody
import com.application.venturaapp.home.fragment.entities.*
import com.application.venturaapp.laborCultural.entity.*
import com.application.venturaapp.login.LoginDataModel
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.retrofit.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class laborCulturalDataModel () {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()
    val messageLiveData = MutableLiveData<String>()
    val roomLiveData = MutableLiveData<String>()

    val messageUpdateLiveData = MutableLiveData<String>()
    val messageValidarLiveData = MutableLiveData<String>()
    val finLiveData = MutableLiveData<String>()

    val responseLaborLiveData = MutableLiveData<List<LaborCulturalListResponse>>()
    val responseLaborPorCodeLiveData = MutableLiveData<LaborCulturalListResponse>()
    val responseLaborPorCodeLiveDataDelete = MutableLiveData<LaborCulturalListResponse>()

    val sendLiveData = MutableLiveData<SendResponse>()
    val responseLaborPlanificadaLiveData = MutableLiveData<List<VSAGRDPCLResponse>>()

    val responseUnidadMedidaLiveData = MutableLiveData<List<UnidadMedida>>()


    val responseLaborItemLiveData = MutableLiveData<LaborCulturalListResponse>()
    val responseValidarLiveData = MutableLiveData<ContActualizacionResponse>()
    val responseReValidarLiveData = MutableLiveData<ContActualizacionResponse>()

    val responseEtapaLiveData = MutableLiveData<List<EtapaProduccionListResponse>>()
    val responseCosechaLiveData = MutableLiveData<List<VSAGRRCOS>>()
    val responseAddCosechaLiveData = MutableLiveData<VSAGRRCOS>()
    val responseDeleteCosechaLiveData = MutableLiveData<String>()


    val validateForPEP =  arrayListOf<LaborCulturalListResponse>()
    lateinit var pref: PreferenceManager

    fun sendTask(sessionid :String , code :JsonObject) {
        val call = loginApiService?.ContabilizarPaso2(sessionid, code)
        call?.enqueue(object : Callback<SendResponse> {
            override fun onResponse(call: Call<SendResponse>, response: Response<SendResponse>) {
                if (response.isSuccessful) {
                    sendLiveData.value = response.body()
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<SendResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun actualizarLaborCultural(sessionid:String, code: String, json:JsonObject, Estado:String) {
        Log.d("JSONREQUEST","JSONREQUEST")
        val call = loginApiService?.actualizarlLaborCultural(sessionid,code,json)
        call?.enqueue(object : Callback<LaborCulturalListResponse> {
            override fun onResponse(call: Call<LaborCulturalListResponse>, response: Response<LaborCulturalListResponse>) {
                if (response.isSuccessful) {
                    if(Estado == "3")
                        messageUpdateLiveData.value = "Se eliminó correctamente."
                    else
                        if(Estado!="2")
                        messageUpdateLiveData.value = "Se créo correctamente."
                    else
                        messageUpdateLiveData.value = "Se actualizó correctamente."
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageUpdateLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<LaborCulturalListResponse>, t: Throwable) {
                t.printStackTrace()
                messageUpdateLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listDetalleLaborCultural(sessionid:String, code: String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listDetallLaborCultural(sessionid,code)
        call?.enqueue(object : Callback<LaborCulturalListResponse> {
            override fun onResponse(call: Call<LaborCulturalListResponse>, response: Response<LaborCulturalListResponse>) {
                if (response.isSuccessful) {

                    responseLaborPorCodeLiveData.value = response.body()
                } else if(response.code()==504)
                {
                    roomLiveData.value = ""
                }
                else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<LaborCulturalListResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun listDetalleLaborCulturalDelete(sessionid:String, code: String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listDetallLaborCultural(sessionid,code)
        call?.enqueue(object : Callback<LaborCulturalListResponse> {
            override fun onResponse(call: Call<LaborCulturalListResponse>, response: Response<LaborCulturalListResponse>) {
                if (response.isSuccessful) {

                    responseLaborPorCodeLiveDataDelete.value = response.body()
                } else if(response.code()==504)
                {
                    roomLiveData.value = ""
                }
                else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<LaborCulturalListResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun unidadMedida(sessionid :String,  httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.unidadMedida(sessionid,"odata.maxpagesize=200")
        call?.enqueue(object : Callback<PersonalResponse<UnidadMedida>> {
            override fun onResponse(call: Call<PersonalResponse<UnidadMedida>> , response: Response<PersonalResponse<UnidadMedida>> ) {
                if (response.isSuccessful) {

                    responseUnidadMedidaLiveData.value = response.body()?.datos

                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<PersonalResponse<UnidadMedida>> , t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun laboresPlanificadas(sessionid :String, code: String, etapa :String?, httpCacheDirectory: Cache, context: Context) {
        val call =  RetrofitClient.getApiService(httpCacheDirectory, context)?.laboresPlanificadas(sessionid,"odata.maxpagesize=200")
        call?.enqueue(object : Callback<PersonalResponse<VSAGRPCULResponse>> {
            override fun onResponse(call: Call<PersonalResponse<VSAGRPCULResponse>> , response: Response<PersonalResponse<VSAGRPCULResponse>> ) {
                if (response.isSuccessful) {
                    var laborPlanificada =  arrayListOf<VSAGRDPCLResponse>()
                    val VS_AGR_PCUL = response.body()
                    for(item in response.body()?.datos!!)
                    {
                        if(item.U_VS_AGR_CDPP == code && item.U_VS_AGR_CDEP == etapa)
                        {
                            responseLaborPlanificadaLiveData.value = item.VS_AGR_DPCLCollection
                        }
                    }
                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<PersonalResponse<VSAGRPCULResponse>> , t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }


    fun listLaborCultural(sessionid:String, code: String, campania: String, httpCacheDirectory: Cache, context: Context) {
       val filtro = "$" +"filter=U_VS_AGR_CDPP eq "+ "'" + code +  "'"
            val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listLaborCultural(sessionid,"odata.maxpagesize=200")
        call?.enqueue(object : Callback<LaborCulturalPEPResponse> {
            override fun onResponse(call: Call<LaborCulturalPEPResponse>, response: Response<LaborCulturalPEPResponse>) {
                if (response.isSuccessful) {
                    for(item in response.body()?.value!!)
                    {
                        if(item.U_VS_AGR_CDPP  == code && campania == item.U_VS_AGR_CDCA)
                        {
                            validateForPEP.add(item)
                        }

                    }
                    responseLaborLiveData.value = validateForPEP
                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<LaborCulturalPEPResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listLaborCulturalJornal(sessionid:String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listLaborCultural(sessionid,"odata.maxpagesize=200")
        call?.enqueue(object : Callback<LaborCulturalPEPResponse> {
            override fun onResponse(call: Call<LaborCulturalPEPResponse>, response: Response<LaborCulturalPEPResponse>) {
                if (response.isSuccessful) {
                    for(item in response.body()?.value!!)
                    {
                        validateForPEP.add(item)

                    }
                    responseLaborLiveData.value = validateForPEP
                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<LaborCulturalPEPResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun Revalidar(sessionid :String , code :String) {
        val call = loginApiService?.verificar(sessionid, code)
        call?.enqueue(object : Callback<ContActualizacionResponse> {
            override fun onResponse(call: Call<ContActualizacionResponse>, response: Response<ContActualizacionResponse>) {
                if (response.isSuccessful) {
                    responseReValidarLiveData.value = response.body()
                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<ContActualizacionResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun validar(sessionid :String , code :String) {
        val call = loginApiService?.verificar(sessionid, code)
        call?.enqueue(object : Callback<ContActualizacionResponse> {
            override fun onResponse(call: Call<ContActualizacionResponse>, response: Response<ContActualizacionResponse>) {
                if (response.isSuccessful) {

                    responseValidarLiveData.value = response.body()
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<ContActualizacionResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun enviar(sessionid :String , code :String, json:JsonObject) {
        val call = loginApiService?.send(sessionid, code,json)
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    finLiveData.value = "Existoso"
                } else {
                    finLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                finLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun validarPatch(sessionid :String , code :String, json:JsonObject) {
        val call = loginApiService?.ContabilizarPaso1(sessionid, code,json)
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    messageValidarLiveData.value = "Existoso"
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageValidarLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                messageValidarLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listEtapa(sessionid :String , code :String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listEtapaProducción(sessionid, code)
       call?.enqueue(object : Callback<EtapaProduccionResponse> {
            override fun onResponse(call: Call<EtapaProduccionResponse>, response: Response<EtapaProduccionResponse>) {
                if (response.isSuccessful) {
                    responseEtapaLiveData.value = response.body()?.VS_AGR_EPEPCollection
                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<EtapaProduccionResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listCosecha(sessionid :String , code :String, campania: String,httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listCosecha(sessionid, "odata.maxpagesize=200")
        call?.enqueue(object : Callback<PersonalResponse<VSAGRRCOS>> {
            override fun onResponse(call: Call<PersonalResponse<VSAGRRCOS>>, response: Response<PersonalResponse<VSAGRRCOS>>) {
                if (response.isSuccessful) {
                    var listCosecha = arrayListOf<VSAGRRCOS>()
                    for(item in response.body()?.datos!!){
                        if(item.U_VS_AGR_CDCA == campania && item.U_VS_AGR_CDPP== code){
                            listCosecha.add(item)
                        }
                    }
                    responseCosechaLiveData.value = listCosecha
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<PersonalResponse<VSAGRRCOS>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun addCosecha(sessionid :String , body : JsonObject ,httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.addCosecha(sessionid, body)
        call?.enqueue(object : Callback<VSAGRRCOS> {
            override fun onResponse(call: Call<VSAGRRCOS>, response: Response<VSAGRRCOS>) {
                if (response.isSuccessful) {
                    responseAddCosechaLiveData.value = response.body()
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<VSAGRRCOS>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun putCosecha(sessionid :String , code:Int?, body : JsonObject ,httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.putCosecha(sessionid,code, body)
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    responseDeleteCosechaLiveData.value = "Se actualizó con éxito."
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun deleteCosecha(sessionid :String ,code:Int ,httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.deleteCosecha(sessionid,code )
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    responseDeleteCosechaLiveData.value = "Cosecha eliminada."
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun addLaborCultural(sessionid:String, json: JsonObject) {
        val call = loginApiService?.addLaborCultural(sessionid,json)
        call?.enqueue(object : Callback<LaborCulturalListResponse> {
            override fun onResponse(call: Call<LaborCulturalListResponse>, response: Response<LaborCulturalListResponse>) {
                if (response.isSuccessful) {
                    responseLaborItemLiveData.value = response.body()
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    Log.d("CONTABILIZACIONNUEVA",json.toString())
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<LaborCulturalListResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }


}