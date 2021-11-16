package com.application.venturaapp.fitosanitario

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.fitosanitario.entity.*
import com.application.venturaapp.helper.Helper
import com.application.venturaapp.laborCultural.entity.*
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.application.venturaapp.tables.LaborCulturalRoom
import com.application.venturaapp.tables.PersonalDB
import okhttp3.Cache

class fitosanitarioViewModel (application: Application) : AndroidViewModel(application)  {

    val fitosanitarioDataModel: fitosanitarioDataModel =  fitosanitarioDataModel()
    val VSAGRFIT: MutableLiveData<List<VSAGRRFIT>> = MutableLiveData()
    val VSAGRFITDetalle: MutableLiveData<VSAGRRFIT> = MutableLiveData()
    val Almacen: MutableLiveData<List<Almacen>> = MutableLiveData()
    val Distribucion: MutableLiveData<List<FitosanitarioDistribucionResponse>> = MutableLiveData()
    val ValidacionLote: MutableLiveData<String> = MutableLiveData()
    val Producto: MutableLiveData<List<VSAGRFEQU>> = MutableLiveData()

    val laborsDataModel: fitosanitarioDataModel = fitosanitarioDataModel()
    val laborResult: MutableLiveData<List<LaborCulturalListResponse>> = MutableLiveData()
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

        fitosanitarioDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageUpdateResult.value = it
            }
        }
        fitosanitarioDataModel.responseDistribucionLiveData.observeForever {
            it?.let {
                Distribucion.value = it

            }
        }
        fitosanitarioDataModel.responseAlmacenLiveData.observeForever {
            it?.let {
                Almacen.value = it

            }
        }
        fitosanitarioDataModel.responseVSAGRFITLiveData.observeForever {
            it?.let {
                VSAGRFIT.value = it

            }
        }
        fitosanitarioDataModel.responseDetalleLiveData.observeForever {
            it?.let {
                VSAGRFITDetalle.value = it

            }
        }
        fitosanitarioDataModel.ValidacionLote.observeForever {
            it?.let {
                ValidacionLote.value = it

            }
        }
        fitosanitarioDataModel.responseProductoLiveData.observeForever {
            it?.let {
                Producto.value = it

            }
        }
    }
    private fun onRetrieveLoginUserFinish() {
        pgbVisibility.value = View.GONE
    }
    private fun onRetrieveLoginUser() {
        pgbVisibility.value = View.VISIBLE
    }

    fun list(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        listaAlmacenTask(this, sessionid,httpCacheDirectory,context).execute()
    }

    fun listaAlmacen(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        listaAlmacenTask(this, sessionid,httpCacheDirectory,context).execute()
    }
    fun listaDistribucion(
        sessionid: String,
        Entry :Int,
        LineId: Int,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        listaDistribucionTask(this, sessionid,Entry, LineId, httpCacheDirectory,context).execute()
    }

    fun ValidarLote(
        sessionid: String,
        Code : String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        ValidarLoteTask(this, sessionid,Code, httpCacheDirectory,context).execute()
    }
    fun listaFitosanitario(
        sessionid: String,
        codigoPEP : String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        listaFitosanitarioTask(this, sessionid,codigoPEP,httpCacheDirectory,context).execute()
    }
    fun listaDetalleFitosanitario(
        sessionid: String,
        docEntry : String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        listaDetalleFitosanitarioTask(this, sessionid,docEntry,httpCacheDirectory,context).execute()
    }

    fun listaProducto(
        sessionid: String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        ProductoTask(this, sessionid,httpCacheDirectory,context).execute()
    }
////////////////////////// TASK
private class listaDetalleFitosanitarioTask internal constructor(
    private var viewModel: fitosanitarioViewModel,
    private var sessionid: String,
    private var docEntry : String,
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
        viewModel.fitosanitarioDataModel.listDetalleFitosanitario(sessionid,docEntry,httpCacheDirectory, context)
        /*else
            viewModel.messageResult.setValue("No tienes acceso a internet")*/
    }
}

    private class listaFitosanitarioTask internal constructor(
        private var viewModel: fitosanitarioViewModel,
        private var sessionid: String,
        private var codigoPEP : String,
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
            viewModel.fitosanitarioDataModel.listFitosanitario(sessionid,codigoPEP,httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class listaAlmacenTask internal constructor(
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
            viewModel.fitosanitarioDataModel.listAlmacen(sessionid,httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class listaDistribucionTask internal constructor(
        private var viewModel: fitosanitarioViewModel,
        private var sessionid: String,
        private var Entry : Int,
        private var LineId : Int,
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
            viewModel.fitosanitarioDataModel.listDistribucionFitosanitario(sessionid,Entry,LineId, httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }

    private class ValidarLoteTask internal constructor(
        private var viewModel: fitosanitarioViewModel,
        private var sessionid: String,
        private var Code : String,
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
            viewModel.fitosanitarioDataModel.ValidarLote(sessionid,Code, httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }


    private class ProductoTask internal constructor(
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
            viewModel.fitosanitarioDataModel.listFertilizante(sessionid, httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }
}