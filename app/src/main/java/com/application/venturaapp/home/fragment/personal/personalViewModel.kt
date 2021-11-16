package com.application.venturaapp.home.fragment.personal

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.helper.Helper
import com.application.venturaapp.home.fragment.entities.LaboresPersonal
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.entities.Proveedor
import com.application.venturaapp.tables.PersonalDB
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.gson.JsonObject
import okhttp3.Cache

class personalViewModel (application: Application) : AndroidViewModel(application) {

    val personalDataModel: personalDataModel = personalDataModel()
    val personalResult: MutableLiveData<List<PersonalDato>> = MutableLiveData()
    val laborResult: MutableLiveData<List<LaboresPersonal>> = MutableLiveData()
    val messageResultDelete: MutableLiveData<String> = MutableLiveData()
    val proveedorResult: MutableLiveData<List<Proveedor>> = MutableLiveData()

    val messageResult: MutableLiveData<String> = MutableLiveData()
    val pgbVisibility: MutableLiveData<Int> = MutableLiveData()
    var allPersonal = listOf<PersonalDatoRoom>()
    val codeResult: MutableLiveData<Int> = MutableLiveData()

    //room
    /*  private val listPersonalRoom: LiveData<List<PersonalDatoRoom>>
    private val repository : PersonalDatoRepository*/

    init {
        /* val personalDAO =PersonalDB.getDatabase(application).personalDao()
        repository  = PersonalDatoRepository(personalDAO)
        listPersonalRoom = repository.listPersonal*/
        setupObservers()
    }
    fun getAllObservers():List<PersonalDatoRoom>
    {
        return allPersonal
    }
    fun getPersonalRoom(): List<PersonalDatoRoom>?
    {
        val personal = PersonalDB.getDatabase(getApplication()).personalDao()
        val list: List<PersonalDatoRoom>? = personal?.getAll()

        return list

    }
    fun insertPersonalRoom(entity : PersonalDatoRoom)
    {
        val personal = PersonalDB.getDatabase(getApplication()).personalDao()
        personal?.insertPersonal(entity)
    }
    private fun setupObservers() {
        personalDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResult.value = it
            }
        }
        personalDataModel.codeError.observeForever {
            it?.let {
                codeResult.value = it
            }
        }
        personalDataModel.messageLiveDataDelete.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResultDelete.value = it
            }
        }
        personalDataModel.responseLiveData.observeForever {
            it?.let {
                if (it != null) {
                    personalResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }

        personalDataModel.responseLaboresLiveData.observeForever {
            it?.let {
                if (it != null) {
                    laborResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
        }

        personalDataModel.responseProveedorLiveData.observeForever {
            it?.let {
                if (it != null) {
                    proveedorResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
        }
    }

    private fun onRetrieveLoginUserFinish() {
        pgbVisibility.value = View.GONE
    }

    private fun onRetrieveLoginUser() {
        pgbVisibility.value = View.VISIBLE
    }

    fun updatePersonal(sessionid: String, code: String , data: JsonObject) {
        ValidateAndSendTask(this, sessionid, code, data).execute()

    }
    fun deleteContacto(sessionid: String, code: String , data: JsonObject) {
        DeleteContactoTask(this, sessionid, code, data).execute()

    }
    fun addPersonal(sessionid: String,  data: JsonObject) {
        addPersonalTask(this, sessionid, data).execute()

    }
    fun listaProveedor(sessionid: String, httpCacheDirectory: Cache, context: Context) {
        listaProveedorTask(this, sessionid,httpCacheDirectory,context).execute()
    }
    fun consultarPersonal(sessionid: String, httpCacheDirectory: Cache, context: Context) {
        consultarPersonalTask(this, sessionid,httpCacheDirectory,context).execute()
    }

    fun consultarLabor(sessionid:String, httpCacheDirectory: Cache, context: Context) {
        consultarLaborTask(this, sessionid,httpCacheDirectory,context).execute()
    }


    private class consultarPersonalTask internal constructor(
        private var viewModel: personalViewModel,
        private var sessionid: String,
        private var httpCacheDirectory: Cache,
        private var context:Context
    )
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return null//Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.personalDataModel.listPersonal(sessionid,httpCacheDirectory, context)
           /* else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class consultarCodePersonalTask internal constructor(private var viewModel: personalViewModel,
                                                                 private var sessionid: String,
                                                                 private var code: String)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.personalDataModel.listPersonalCode(sessionid, code)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }
    private class addPersonalTask internal constructor(private var viewModel: personalViewModel,
                                                           private var sessionid: String,
                                                           private var json: JsonObject) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            if (!aBoolean!!)
                viewModel.personalDataModel.messageLiveData.setValue("No tienes acceso a internet")
            else {
                viewModel.personalDataModel.addPersonal(sessionid, json)
            }
        }
    }
    private class DeleteContactoTask internal constructor(private var viewModel: personalViewModel,
                                                           private var sessionid: String,
                                                           private var code: String,
                                                           private var json: JsonObject) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            if (!aBoolean!!)
                viewModel.personalDataModel.messageLiveData.setValue("No tienes acceso a internet")
            else {
                viewModel.personalDataModel.deleteContacto(sessionid, code, json)
            }
        }
    }

    private class ValidateAndSendTask internal constructor(private var viewModel: personalViewModel,
                                                           private var sessionid: String,
                                                           private var code: String,
                                                           private var json: JsonObject) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            if (!aBoolean!!)
                viewModel.personalDataModel.messageLiveData.setValue("No tienes acceso a internet")
            else {
                viewModel.personalDataModel.updatePersonal(sessionid, code, json)
            }
        }
    }
    private class listaProveedorTask internal constructor(private var viewModel: personalViewModel,
                                                          private var sessionid: String,
                                                          private var httpCacheDirectory: Cache,
                                                          private var context:Context)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.personalDataModel.listProveedor(sessionid,httpCacheDirectory, context)
           /* else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
    private class consultarLaborTask internal constructor(private var viewModel: personalViewModel,
                                                             private var sessionid: String,
                                                          private var httpCacheDirectory: Cache,
                                                          private var context:Context)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.personalDataModel.listLabor(sessionid,httpCacheDirectory, context)
          /*  else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
}