package com.application.venturaapp.home.fragment.produccion

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
import com.application.venturaapp.home.fragment.entities.Sector
import com.application.venturaapp.home.fragment.entities.Fundo

class laborViewModel (application: Application) : AndroidViewModel(application)  {

    val laborsDataModel: laborDataModel = laborDataModel()
    val laborResult: MutableLiveData<List<PEPDato>> = MutableLiveData()
    val campaniaResult: MutableLiveData<List<Campania>> = MutableLiveData()

    val fundoResult : MutableLiveData<List<Fundo>> = MutableLiveData()
    val sectorResult : MutableLiveData<List<Sector>> = MutableLiveData()

    val codeResult: MutableLiveData<Int> = MutableLiveData()

    val messageResult: MutableLiveData<String> = MutableLiveData()
    val pgbVisibility: MutableLiveData<Int> = MutableLiveData()
    init {
        setupObservers()
    }

    private fun setupObservers() {
        laborsDataModel.codeError.observeForever {
            it?.let {
                codeResult.value = it
            }
        }
        laborsDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResult.value = it
            }
        }
        laborsDataModel.responseLiveData.observeForever {
            it?.let {
                if (it != null) {
                    laborResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }
        laborsDataModel.responseCampaniaLiveData.observeForever {
            it?.let {
                if (it != null) {
                    campaniaResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }

        }

        laborsDataModel.responseFundoLiveData.observeForever {

            it?.let {
                if (it != null) {
                    fundoResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
        }

        laborsDataModel.responseSectorLiveData.observeForever {

            it?.let {
                if (it != null) {
                    sectorResult.value = it
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
    fun listaLaborCultural(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        listaLaborCulturalTask(this, sessionid,httpCacheDirectory,context).execute()
    }
    fun consultarCampania(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        consultarCampaniaTask(this, sessionid,httpCacheDirectory,context).execute()
    }

    fun consultarFundo(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context) {

        consultarFundoTask(this, sessionid, httpCacheDirectory, context).execute()
    }

    fun consultarSector(
        fundo: String,
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {

        consultarSectorTask(this, fundo, sessionid, httpCacheDirectory, context).execute()
    }

    fun consultarLote(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context) {
        consultarLoteTask(this, sessionid, httpCacheDirectory, context).execute()
    }
    private class listaLaborCulturalTask internal constructor(
        private var viewModel: laborViewModel,
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
                viewModel.laborsDataModel.listLaborCultural(sessionid,httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class consultarCampaniaTask internal constructor(
        private var viewModel: laborViewModel,
        private var sessionid: String,
        private var httpCacheDirectory: Cache,
        private var context: Context
    ): AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            // if (aBoolean == true)
            viewModel.laborsDataModel.listCampania(sessionid,httpCacheDirectory,context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class consultarFundoTask internal constructor(
        private var viewModel: laborViewModel,
        private var sessionid: String,
        private var httpCacheDirectory: Cache,
        private var context: Context
    ) : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            // if (aBoolean == true)
            viewModel.laborsDataModel.listFundo(sessionid, httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class consultarSectorTask(
        private var viewModel: laborViewModel,
        private var fundo: String,
        private var sessionid: String,
        private var httpCacheDirectory: Cache,
        private var context: Context
    ) : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            // if (aBoolean == true)
            viewModel.laborsDataModel.listSector(fundo, sessionid, httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class consultarLoteTask(
        private var viewModel: laborViewModel,
        private var sessionid: String,
        private var httpCacheDirectory: Cache,
        private var context: Context
    )  : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }
        public override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            viewModel.laborsDataModel.listLote(sessionid, httpCacheDirectory, context)
        }

    }

}