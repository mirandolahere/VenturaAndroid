package com.application.venturaapp.login

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.view.View
import com.application.venturaapp.helper.Helper
import com.application.venturaapp.login.entity.LoginResponse
import com.application.venturaapp.login.entity.UserLoginResponse
import com.google.gson.JsonObject
import okhttp3.Cache

//import com.application.ventura.login.R

class LoginViewModel : ViewModel() {

    val loginDataModel: LoginDataModel = LoginDataModel()
    val loginResult: MutableLiveData<LoginResponse> = MutableLiveData()
    val loginUsersResult: MutableLiveData<UserLoginResponse> = MutableLiveData()
    val loginListUsersResult: MutableLiveData<List<UserLoginResponse>> = MutableLiveData()

    val messageUpdateResult: MutableLiveData<String> = MutableLiveData()
    val messageResult: MutableLiveData<String> = MutableLiveData()
    val messageErrorResult: MutableLiveData<String> = MutableLiveData()

    val pgbVisibility: MutableLiveData<Int> = MutableLiveData()
    init {
        setupObservers()
    }
    private fun setupObservers() {
        loginDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResult.value = it
            }
        }
        loginDataModel.messageUpdateLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageUpdateResult.value = it
            }
        }
        loginDataModel.messageUpdateErrorLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageErrorResult.value = it
            }
        }
        loginDataModel.responseUsersLiveData.observeForever {
            it?.let {
                if (it.Code != null) {
                    loginUsersResult.value = it
                } else {
                    messageResult.value = "Error"  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }
        loginDataModel.responseListUsersLiveData.observeForever {
            it?.let {

                loginListUsersResult.value = it

            }
            onRetrieveLoginUserFinish()
        }
        loginDataModel.responseLiveData.observeForever {
            it?.let {
                if (it.SessionId != null) {
                    loginResult.value = it
                } else {
                    messageResult.value = "Error"  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }
    }

    fun ActualizarUsuario(token: String, code: String, json: JsonObject) {
        ActualizarUsuarioTask(this, token, code,json ).execute()
    }
    fun LoginGeneral(url:String, puerto:String, CompanyDB:String, UserName:String,Password:String) {
        onRetrieveLoginUser()
        ValidateAndLoginGeneralTask(this,url, puerto, CompanyDB, UserName,Password).execute()
    }
    fun userLoginMemory(session : String,httpCacheDirectory: Cache, context: Context) {

        onRetrieveLoginUser()
        ValidateAndLoginMemoryTask(this,session,httpCacheDirectory,context).execute()
    }

    fun userLogin(code: String, password: String, session : String,httpCacheDirectory: Cache, context: Context, sincronizacion: Int) {
        if (!areRequiredParameters(code, password))
            return
        onRetrieveLoginUser()
        ValidateAndLoginTask(this, code,password,session,httpCacheDirectory,context,sincronizacion).execute()
    }
    fun forgetPass(username: String) {
        if (!requiredEmail(username))
            return
        onRetrieveLoginUser()
        ValidateAndForgetTask(this, username).execute()
    }


    private fun onRetrieveLoginUserFinish() {
        pgbVisibility.value = View.GONE
    }
    private fun onRetrieveLoginUser() {
        pgbVisibility.value = View.VISIBLE
    }
    private fun areRequiredParameters(username: String, password: String): Boolean {

        if (username.isEmpty()) {
            messageResult.setValue("Debe ingresar su usuario")
            return false
        }

        if (password.isEmpty()) {
            messageResult.setValue("Debe ingresar su contraseña")
            return false
        }
        return true
    }
    private fun requiredEmail(username: String): Boolean {

        if (username.isEmpty()) {
            messageResult.setValue("Debe ingresar su código")
            return false
        }
        return true
    }
    private class ActualizarUsuarioTask internal constructor(private var viewModel: LoginViewModel,
                                                             private var token: String,
                                                             private var code: String,
                                                            private var json:JsonObject)
        : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.loginDataModel.actualizarUsuario(token,code,json)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }
    private class ValidateAndLoginGeneralTask internal constructor(
        private var viewModel: LoginViewModel,
        private var url:String,
        private var puerto:String,
        private var CompanyDB:String,
        private var UserName:String,
        private var Password:String

    )
        : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.loginDataModel.loginUser(url , puerto, CompanyDB,UserName,Password)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }
    private class ValidateAndLoginMemoryTask internal constructor(private var viewModel: LoginViewModel,
                                                                  private  var session: String,
                                                                  private var httpCacheDirectory: Cache,
                                                            private var context: Context)
        : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            // if (aBoolean == true)
            viewModel.loginDataModel.UserCredentialsMemory(session,httpCacheDirectory,context)
            /* else
                 viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
    private class ValidateAndLoginTask internal constructor(private var viewModel: LoginViewModel,
                                                            private var code: String,
                                                            private var password:String,
                                                            private  var session: String,
                                                            private var httpCacheDirectory: Cache,
                                                            private var context: Context,
                                                            private var sincronizacion: Int)
        : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.loginDataModel.UserCredentials(code, password,session,httpCacheDirectory,context,sincronizacion)
           /* else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
    private class ValidateAndForgetTask internal constructor(private var viewModel: LoginViewModel,
                                                            private var username: String)
        : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.loginDataModel.forgetPassword(username)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }
    /*private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }*/
}