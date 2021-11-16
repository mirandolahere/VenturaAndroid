package com.application.venturaapp.laborCultural

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.helper.Helper
import com.application.venturaapp.laborCultural.entity.*
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.application.venturaapp.tables.LaborCulturalRoom
import com.application.venturaapp.tables.PersonalDB
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.gson.JsonObject
import okhttp3.Cache

class laborCulturaViewModel (application: Application) : AndroidViewModel(application)  {

    val laborsDataModel: laborCulturalDataModel = laborCulturalDataModel()
    val laborResult: MutableLiveData<List<LaborCulturalListResponse>> = MutableLiveData()
    val etapaResult: MutableLiveData<List<EtapaProduccionListResponse>> = MutableLiveData()
    val laborResponseResult: MutableLiveData<LaborCulturalListResponse> = MutableLiveData()
    val laborPorCodeResult: MutableLiveData<LaborCulturalListResponse> = MutableLiveData()
    val laborPorCodeResultDelete: MutableLiveData<LaborCulturalListResponse> = MutableLiveData()

    val messageUpdateResult: MutableLiveData<String> = MutableLiveData()
    val sendResult: MutableLiveData<SendResponse> = MutableLiveData()
    val ReverificarResult: MutableLiveData<ContActualizacionResponse> = MutableLiveData()
    val responseLaborPlanificadaResult: MutableLiveData<List<VSAGRDPCLResponse>> = MutableLiveData()

    val verificarResult: MutableLiveData<ContActualizacionResponse> = MutableLiveData()
    val messageResult: MutableLiveData<String> = MutableLiveData()
    val roomResult: MutableLiveData<String> = MutableLiveData()

    val finLiveData: MutableLiveData<String> = MutableLiveData()

    val messageValidarLiveData: MutableLiveData<String> = MutableLiveData()
    val pgbVisibility: MutableLiveData<Int> = MutableLiveData()
    init {
        setupObservers()
    }
    fun getLaborRoom(): List<LaborCulturalRoom>?
    {
        val labor = PersonalDB.getDatabase(getApplication()).personalDao()
        val list: List<LaborCulturalRoom>? = labor?.getLaborAll()

        return list

    }
    fun getLaborDetalleRoomID(): List<LaborCulturalRoom>?
    {
        val detalle = PersonalDB.getDatabase(getApplication()).personalDao()
        val list: List<LaborCulturalRoom>? = detalle?.getLaborAllID()

        return list

    }
    fun getLaborDetalleRoom(DocEntry:Int): List<LaborCulturalDetalleRoom>?
    {
        val detalle = PersonalDB.getDatabase(getApplication()).personalDao()
        val list: List<LaborCulturalDetalleRoom>? = detalle?.getDetalleAll(DocEntry)

        return list

    }
    fun getLaborDetalleAllRoom(): List<LaborCulturalDetalleRoom>?
    {
        val detalle = PersonalDB.getDatabase(getApplication()).personalDao()
        val list: List<LaborCulturalDetalleRoom>? = detalle?.getDetallWithouteAll()

        return list

    }
    fun insertLaborRoom(entity : LaborCulturalRoom)
    {
        val labor = PersonalDB.getDatabase(getApplication()).personalDao()
        labor?.insertLaborCultural(entity)
    }
    fun insertLaborDetalleRoom(entity : LaborCulturalDetalleRoom)
    {
        val labor = PersonalDB.getDatabase(getApplication()).personalDao()
        labor?.insertLaborDetalleCultural(entity)
    }
    fun updateLaborDetalleRoom(entity : LaborCulturalDetalleRoom)
    {
        val labor = PersonalDB.getDatabase(getApplication()).personalDao()
        labor?.updateDetalle(entity)
    }
    private fun setupObservers() {
        laborsDataModel.messageUpdateLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageUpdateResult.value = it
            }
        }
        laborsDataModel.roomLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                roomResult.value = it
            }
        }
        laborsDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageResult.value = it
            }
        }
        laborsDataModel.finLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                finLiveData.value = it
            }
        }
        laborsDataModel.messageValidarLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageValidarLiveData.value = it
            }
        }
        laborsDataModel.sendLiveData.observeForever {
            it?.let {
                if (it != null) {
                    sendResult.value = it
                }
            }
        }
        laborsDataModel.responseValidarLiveData.observeForever {
            it?.let {
                if (it != null) {
                    verificarResult.value = it
                }
            }
        }
        laborsDataModel.responseReValidarLiveData.observeForever {
            it?.let {
                if (it != null) {
                    ReverificarResult.value = it
                }
            }
        }
        laborsDataModel.responseLaborPorCodeLiveData.observeForever {
            it?.let {
                    laborPorCodeResult.value = it

            }
        }
        laborsDataModel.responseLaborPorCodeLiveDataDelete.observeForever {
            it?.let {
                laborPorCodeResultDelete.value = it

            }
        }
        laborsDataModel.responseLaborPlanificadaLiveData.observeForever {
            it?.let {
                if (it != null) {
                    responseLaborPlanificadaResult.value = it
                }
            }
        }
        laborsDataModel.responseLaborLiveData.observeForever {
            it?.let {
                if (it != null) {
                    laborResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
            onRetrieveLoginUserFinish()
        }

        laborsDataModel.responseEtapaLiveData.observeForever {
            it?.let {
                if (it != null) {
                    etapaResult.value = it
                } else {
                    messageResult.value = "No se encontraron datos."  //it.mensaje
                }
            }
        }
        laborsDataModel.responseLaborItemLiveData.observeForever {
            it?.let {
                if (it != null) {
                    laborResponseResult.value = it
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
    fun listaLaborCultural(sessionid:String,code: String,
                           httpCacheDirectory: Cache,
                           context:Context) {
        onRetrieveLoginUser()
        listaLaborCulturalTask(this, sessionid,code,httpCacheDirectory,context).execute()
    }
    fun listaLaborJornalCultural(
        sessionid: String,
        httpCacheDirectory: Cache,
        context:Context
    ) {
        onRetrieveLoginUser()
        JornalLaborCulturalTask(this, sessionid,httpCacheDirectory,context).execute()
    }
    fun actualizarLaborCultural (sessionid: String, code: String, json: JsonObject, Estado : String)
    {        onRetrieveLoginUser()
        actualizarLaborCulturalTask(this, sessionid,code, json, Estado).execute()

    }
    fun listaDetalleLaborCultural(sessionid:String,code: String, httpCacheDirectory: Cache,
                                  context:Context) {
        onRetrieveLoginUser()

        listaDetalleLaborCulturalTask(this, sessionid,code,httpCacheDirectory,context).execute()
    }
    fun listaDetalleLaborCulturalDelete(sessionid:String,code: String, httpCacheDirectory: Cache,
                                  context:Context) {
        onRetrieveLoginUser()

        listaDetalleLaborCulturalDeleteTask(this, sessionid,code,httpCacheDirectory,context).execute()
    }
    fun listEtapa(sessionid:String, code:String,
                  httpCacheDirectory: Cache,
                  context:Context) {
        consultarEtapaTask(this, sessionid,code,httpCacheDirectory,context).execute()
    }
    fun send(sessionid:String,json: JsonObject) {
        sendTask(this, sessionid,json).execute()
    }
    fun validarPatch(sessionid:String, code:String,json: JsonObject) {
        validarPatchTask(this, sessionid,code,json).execute()
    }
    fun laborPlanificada(sessionid:String, code:String, etapa: String?,httpCacheDirectory: Cache,
                         context:Context) {
        laborPlanificadaTask(this, sessionid,code,etapa,httpCacheDirectory,context).execute()
    }
    fun validar(sessionid:String, code:String) {
        validarTask(this, sessionid,code).execute()
    }
    fun Revalidar(sessionid:String, code:String) {
        RevalidarTask(this, sessionid,code).execute()
    }
    fun addLabor(sessionid:String, json:JsonObject) {
        onRetrieveLoginUser()
        addLaborTask(this, sessionid,json).execute()
    }

    private class  sendTask internal constructor(private var viewModel: laborCulturaViewModel,
                                                    private var sessionid: String,
                                                    private var code: JsonObject)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.laborsDataModel.sendTask(sessionid,code)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }


    private class addLaborTask internal constructor(private var viewModel: laborCulturaViewModel
                                                              , private var sessionid: String, private var json: JsonObject)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.laborsDataModel.addLaborCultural(sessionid,json)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }
    private class listaLaborCulturalTask internal constructor(private var viewModel: laborCulturaViewModel
                                                                , private var sessionid: String, private var code: String,
                                                              private var httpCacheDirectory: Cache,  private var context: Context)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.laborsDataModel.listLaborCultural(sessionid,code,httpCacheDirectory,context)
           /* else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
    private class JornalLaborCulturalTask internal constructor(
        private var viewModel: laborCulturaViewModel
        , private var sessionid: String,  private var httpCacheDirectory: Cache,  private var context: Context
    )
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            //if (aBoolean == true)
                viewModel.laborsDataModel.listLaborCulturalJornal(sessionid,httpCacheDirectory,context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class actualizarLaborCulturalTask internal constructor(private var viewModel: laborCulturaViewModel
                                                                     , private var sessionid: String, private var code: String
                                                                     ,private  var json:JsonObject, private var Estado: String)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.laborsDataModel.actualizarLaborCultural(sessionid,code,json, Estado)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }

    private class listaDetalleLaborCulturalTask internal constructor(private var viewModel: laborCulturaViewModel
                                                              , private var sessionid: String, private var code: String,
        private var httpCacheDirectory: Cache,  private var context: Context)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.laborsDataModel.listDetalleLaborCultural(sessionid,code,httpCacheDirectory,context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
    private class listaDetalleLaborCulturalDeleteTask internal constructor(private var viewModel: laborCulturaViewModel
                                                                     , private var sessionid: String, private var code: String,
                                                                     private var httpCacheDirectory: Cache,  private var context: Context)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            // if (aBoolean == true)
            viewModel.laborsDataModel.listDetalleLaborCulturalDelete(sessionid,code,httpCacheDirectory,context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class consultarEtapaTask internal constructor(private var viewModel: laborCulturaViewModel,
                                                             private var sessionid: String,
                                                            private var code: String,
                                                          private var httpCacheDirectory: Cache,  private var context: Context)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            //if (aBoolean == true)
                viewModel.laborsDataModel.listEtapa(sessionid,code,httpCacheDirectory,context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
    private class  validarTask   constructor(private var viewModel: laborCulturaViewModel,
                                                          private var sessionid: String,
                                                          private var code: String)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.laborsDataModel.validar(sessionid,code)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }
    private class  RevalidarTask   constructor(private var viewModel: laborCulturaViewModel,
                                             private var sessionid: String,
                                             private var code: String)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.laborsDataModel.Revalidar(sessionid,code)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }
    private class  validarPatchTask internal constructor(private var viewModel: laborCulturaViewModel,
                                                    private var sessionid: String,
                                                    private var code: String,
                                                    private var json:JsonObject)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
            if (aBoolean == true)
                viewModel.laborsDataModel.validarPatch(sessionid,code,json)
            else
                viewModel.messageResult.setValue("No tienes acceso a internet")
        }
    }

    private class  laborPlanificadaTask internal constructor(private var viewModel: laborCulturaViewModel,
                                                         private var sessionid: String,
                                                         private var code: String,
                                                         private var etapa:String?,
                                                         private var httpCacheDirectory: Cache,  private var context: Context)
        : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg voids: Void): Boolean? {
            return Helper.isOnline()
        }

        override fun onPostExecute(aBoolean: Boolean?) {

            super.onPostExecute(aBoolean)
           // if (aBoolean == true)
                viewModel.laborsDataModel.laboresPlanificadas(sessionid,code,etapa, httpCacheDirectory,context)
           /* else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
}