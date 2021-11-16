package com.application.venturaapp.home.fragment.fertilizante

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

class fertilizanteViewModel (application: Application) : AndroidViewModel(application)  {

    val fertilizanteDataModel: fertilizanteDataModel = fertilizanteDataModel()
    val fertilizanteResult: MutableLiveData<List<PEPDato>> = MutableLiveData()
    val campaniaResult: MutableLiveData<List<Campania>> = MutableLiveData()
    val codeResult: MutableLiveData<Int> = MutableLiveData()

    val messageResult: MutableLiveData<String> = MutableLiveData()
    val pgbVisibility: MutableLiveData<Int> = MutableLiveData()
    init {
        setupObservers()
    }

    private fun setupObservers() {
        fertilizanteDataModel.codeError.observeForever {
            it?.let {
                codeResult.value = it
            }
        }
        fertilizanteDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResult.value = it
            }
        }
        fertilizanteDataModel.responseLiveData.observeForever {
            it?.let {
                if (it != null) {
                    fertilizanteResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }
        fertilizanteDataModel.responseCampaniaLiveData.observeForever {
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
    fun listaFertilizante(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        listaFertilizanteTask(this, sessionid,httpCacheDirectory,context).execute()
    }

    private class listaFertilizanteTask internal constructor(
        private var viewModel: fertilizanteViewModel,
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
                viewModel.fertilizanteDataModel.listFertilizante(sessionid,httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }


}