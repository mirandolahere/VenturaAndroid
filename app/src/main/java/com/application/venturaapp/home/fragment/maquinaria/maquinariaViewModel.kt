package com.application.venturaapp.home.fragment.maquinaria

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.helper.Helper
import com.application.venturaapp.home.fragment.entities.Campania
import com.application.venturaapp.home.fragment.entities.PEPDato
import com.application.venturaapp.maquinarias.entity.VSAGRRMAQ
import okhttp3.Cache

class maquinariaViewModel (application: Application) : AndroidViewModel(application)  {

    val maquinariaDataModel: maquinariaDataModel = maquinariaDataModel()
    val maquinariaResult: MutableLiveData<List<VSAGRRMAQ>> = MutableLiveData()
    val campaniaResult: MutableLiveData<List<Campania>> = MutableLiveData()
    val codeResult: MutableLiveData<Int> = MutableLiveData()

    val messageResult: MutableLiveData<String> = MutableLiveData()
    val pgbVisibility: MutableLiveData<Int> = MutableLiveData()
    init {
        setupObservers()
    }

    private fun setupObservers() {
        maquinariaDataModel.codeError.observeForever {
            it?.let {
                codeResult.value = it
            }
        }
        maquinariaDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResult.value = it
            }
        }
        maquinariaDataModel.responseLiveData.observeForever {
            it?.let {
                if (it != null) {
                    maquinariaResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }
        maquinariaDataModel.responseCampaniaLiveData.observeForever {
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
    fun listaMaquinaria(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        listaMaquinariaTask(this, sessionid,httpCacheDirectory,context).execute()
    }

    private class listaMaquinariaTask internal constructor(
        private var viewModel: maquinariaViewModel,
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
                viewModel.maquinariaDataModel.listMaquinaria(sessionid,httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }


}