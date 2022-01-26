package com.application.venturaapp.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.helper.ErrorBody
import com.application.venturaapp.home.fragment.entities.PersonalResponse
import com.application.venturaapp.login.entity.LoginResponse
import com.application.venturaapp.login.entity.UserLoginResponse
import com.application.venturaapp.retrofit.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Cache
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginDataModel() {

    private val TAG = LoginDataModel::class.java.simpleName
    private val loginApiService = RetrofitClient.getApiService()

    private val forgetApiService = RetrofitClient.getApiServicePassword()
    val messageUpdateErrorLiveData = MutableLiveData<String>()
    val messageUpdateLiveData = MutableLiveData<String>()

    val messageLiveData = MutableLiveData<String>()
    val responseLiveData = MutableLiveData<LoginResponse>()
    val responseUsersLiveData = MutableLiveData<UserLoginResponse>()
    val responseListUsersLiveData = MutableLiveData<List<UserLoginResponse>>()

    val responseForgetLiveData = MutableLiveData<String>()

    fun UserCredentialsMemory(session:String,httpCacheDirectory: Cache, context: Context) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.UserCredentialsMemory(session)

    }

    fun UserCredentials(
        personalCode: String,
        password: String,
        session: String,
        httpCacheDirectory: Cache,
        context: Context,
        sincronizacion: Int) {
        val call = RetrofitClient.getApiService(httpCacheDirectory, context)?.UserCredentialsMemory(session)
        call?.enqueue(object : Callback<PersonalResponse<UserLoginResponse>> {
            override fun onResponse(call: Call<PersonalResponse<UserLoginResponse>>, response: Response<PersonalResponse<UserLoginResponse>>) {
                if(sincronizacion == 0) {
                if (response.isSuccessful) {

                        for (item in response.body()?.datos!!) {
                            if (password == item.U_VS_AGR_CONT && personalCode == item.Code) {
                                responseUsersLiveData.value = item

                            }
                        }
                    if(responseUsersLiveData.value==null)
                        messageLiveData.value = "Usuario y/o contraseña inválidos."


                    }
                }
                if(sincronizacion == 1) {
                    responseListUsersLiveData.value = response.body()?.datos!!

                }

            }

            override fun onFailure(call: Call<PersonalResponse<UserLoginResponse>>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun actualizarUsuario(token: String, code: String, json: JsonObject) {
        val call = loginApiService?.updateUsuario(token, code,json)
        call?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    messageUpdateLiveData.value = "Actualización exitosa."

                } else {
                    messageUpdateErrorLiveData.value = "La contraseña debe tener al menos 6 caracteres."
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                messageUpdateErrorLiveData.value = "Falla en la conexión"
            }

        })
    }
    fun loginUser(url:String, puerto:String,CompanyDB:String, UserName:String,Password:String ) {
        val call = RetrofitClient.getApiServiceConfiguration(url,puerto)?.loginUser(buildJsonBody(CompanyDB, UserName,Password ))
        call?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    responseLiveData.value = response.body()

                } else {
                    messageLiveData.value = "Falla en el servidor."
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión"
            }
        })
    }

    fun forgetPassword(username: String) {
        val call = forgetApiService?.password(username)
        call?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    messageLiveData.value = "Se envio el enlace a su correo asociado."
                } else {
                    var json = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    messageLiveData.value = json.error.message.value
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                messageLiveData.value = "Falla en la conexión ó usuario no válido"
            }
        })
    }

    private fun buildJsonBody(CompanyDB:String, UserName:String,Password:String ): JsonObject {
        val inner = JsonObject()
        inner.addProperty("CompanyDB", CompanyDB) //"SBO_SANTALUCIA_PRUEBAS"
        inner.addProperty("UserName", UserName) //"manager"
        inner.addProperty("Password", Password)//"B1Admin"

        val outer = JsonObject()
        outer.add("Parametros", inner)
        return inner
    }
}