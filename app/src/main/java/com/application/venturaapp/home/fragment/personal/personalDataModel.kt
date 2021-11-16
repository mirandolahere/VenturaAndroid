package com.application.venturaapp.home.fragment.personal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.ErrorBody
import com.application.venturaapp.home.fragment.entities.*
import com.application.venturaapp.login.LoginDataModel
import com.application.venturaapp.login.entity.LoginResponse
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.retrofit.RetrofitClient
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class personalDataModel () {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()
    val messageLiveDataDelete = MutableLiveData<String>()
    val messageLiveData = MutableLiveData<String>()
    val codeError = MutableLiveData<Int>()

    val responseLiveData = MutableLiveData<List<PersonalDato>>()
    val responseLaboresLiveData = MutableLiveData<List<LaboresPersonal>>()
    val responseProveedorLiveData = MutableLiveData<List<Proveedor>>()

    val responseLoginLiveData = MutableLiveData<LoginResponse>()
    val personalCodeRoom = ArrayList<PersonalDatoRoom>()
    val personalList = ArrayList<PersonalDato>()

    lateinit var pref: PreferenceManager

    fun loginUser(): Boolean {
        val call = loginApiService?.loginUser(buildJsonBody())
        call?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    //pref = PreferenceManager(geta)

                    val token = response.body()?.SessionId
                    pref.saveString(Constants.B1SESSIONID, "B1SESSION=" + token)

                } else {
                    messageLiveData.value = "Usuario y/o contraseña inválidos."
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
        return true;
    }
    fun listPersonal(sessionid: String, httpCacheDirectory: Cache, context: Context) {
      //  val cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)

        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listPersonal(sessionid,"odata.maxpagesize=1000")
        call?.enqueue(object : Callback<PersonalResponse<PersonalDato>> {
            override fun onResponse(call: Call<PersonalResponse<PersonalDato>>, response: Response<PersonalResponse<PersonalDato>>) {
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

            override fun onFailure(call: Call<PersonalResponse<PersonalDato>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun listPersonalCode(sessionid:String, personalCode :String) {
        val call = loginApiService?.listPersonalCode(sessionid,personalCode )
        call?.enqueue(object : Callback<PersonalDato> {
            override fun onResponse(call: Call<PersonalDato>, response: Response<PersonalDato>) {
                if (response.isSuccessful) {
                    response.body()?.let { personalList.add(it) }
                    responseLiveData.value = personalList
                } else {
                    messageLiveData.value = "No se encontraron coincidencias" //response.message()
                }
            }

            override fun onFailure(call: Call<PersonalDato>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun addPersonal(sessionid:String,  body :JsonObject) {
        val call = loginApiService?.addPersonal(sessionid,body)
        call?.enqueue(object : Callback<PersonalDato> {
            override fun onResponse(call: Call<PersonalDato>, response: Response<PersonalDato>) {
                if (response.isSuccessful) {
                    // response.body()?.let { personalList.add(it) }
                    //responseLiveData.value = personalList
                    messageLiveData.value = "Se creó personal correctamente."

                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value

                }
            }

            override fun onFailure(call: Call<PersonalDato>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun deleteContacto(sessionid:String, personalCode :String, body :JsonObject) {
        val call = loginApiService?.deleteContacto(sessionid,personalCode,body)
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // response.body()?.let { personalList.add(it) }
                    //responseLiveData.value = personalList
                    messageLiveDataDelete.value = "Contacto eliminado."

                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveDataDelete.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                messageLiveDataDelete.value = "Falla en la conexión"
            }
        })
    }
    fun updatePersonal(sessionid:String, personalCode :String, body :JsonObject) {
        val call = loginApiService?.updatePersonal(sessionid,personalCode,body)
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                   // response.body()?.let { personalList.add(it) }
                    //responseLiveData.value = personalList
                    messageLiveData.value = "Actualización exitosa."

                } else {
                    messageLiveData.value = "No se actualizó. Intenteló nuevamente." //response.message()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun listLabor(sessionid :String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.laboresPersonal(sessionid,"odata.maxpagesize=1000")
        call?.enqueue(object : Callback<PersonalResponse<LaboresPersonal>> {
            override fun onResponse(call: Call<PersonalResponse<LaboresPersonal>>, response: Response<PersonalResponse<LaboresPersonal>>) {

                if (response.isSuccessful) {
                    responseLaboresLiveData.value = response.body()?.datos
                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<PersonalResponse<LaboresPersonal>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    fun listProveedor(sessionid :String, httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.listaProveedor(sessionid,"odata.maxpagesize=200")
        call?.enqueue(object : Callback<PersonalResponse<Proveedor>> {
            override fun onResponse(call: Call<PersonalResponse<Proveedor>>, response: Response<PersonalResponse<Proveedor>>) {
                if (response.isSuccessful) {
                    responseProveedorLiveData.value = response.body()?.datos
                } else {
                    messageLiveData.value = response.message()
                }
            }

            override fun onFailure(call: Call<PersonalResponse<Proveedor>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }
    private fun builddBody(body: JsonObject): JsonObject {
        val outerJson = JsonObject()
        outerJson.add("Parametros", body)
        Log.d(TAG, "data: $outerJson")
        return outerJson
    }
    private fun buildJsonBody(): JsonObject {
        val inner = JsonObject()
        inner.addProperty("CompanyDB", "SBO_FRAP_PROD")
        inner.addProperty("UserName", "manager")
        inner.addProperty("Password", "sbosap")

        val outer = JsonObject()
        outer.add("Parametros", inner)
        return inner
    }
}