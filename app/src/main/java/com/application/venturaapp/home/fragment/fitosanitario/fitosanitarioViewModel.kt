package com.application.venturaapp.home.fragment.fitosanitario

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.helper.Helper
import com.application.venturaapp.home.fragment.entities.Campania
import com.application.venturaapp.home.fragment.entities.PEPDato
import okhttp3.Cache

class fitosanitarioViewModel (application: Application) : AndroidViewModel(application)  {

    val fitosanitarioDataModel: fitosanitarioDataModel = fitosanitarioDataModel()
    val fitosanitarioResult: MutableLiveData<List<PEPDato>> = MutableLiveData()
    val campaniaResult: MutableLiveData<List<Campania>> = MutableLiveData()
    val codeResult: MutableLiveData<Int> = MutableLiveData()

    val messageResult: MutableLiveData<String> = MutableLiveData()
    val pgbVisibility: MutableLiveData<Int> = MutableLiveData()
    init {
        setupObservers()
    }

    private fun setupObservers() {
        fitosanitarioDataModel.codeError.observeForever {
            it?.let {
                codeResult.value = it
            }
        }
        fitosanitarioDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResult.value = it
            }
        }
        fitosanitarioDataModel.responseLiveData.observeForever {
            it?.let {
                if (it != null) {
                    fitosanitarioResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }
        fitosanitarioDataModel.responseCampaniaLiveData.observeForever {
            it?.let {
                if (it != null) {
                    campaniaResult.value = it
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
    fun listaFitosanitario(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        listaFitosanitarioTask(this, sessionid,httpCacheDirectory,context).execute()
    }

    private class listaFitosanitarioTask internal constructor(
        private var viewModel: fitosanitarioViewModel,
        private var sessionid: String,
        private var httpCacheDirectory: Cache,
        private var context: Context
    )
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.fitosanitarioDataModel.listFitosanitario(sessionid,httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }


}