package com.application.venturaapp.fertilizante

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.application.venturaapp.fitosanitario.entity.VSAGRRCOS
import com.application.venturaapp.helper.Helper
import com.application.venturaapp.laborCultural.entity.*
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.application.venturaapp.tables.LaborCulturalRoom
import com.application.venturaapp.tables.PersonalDB
import okhttp3.Cache

class fertilizanteViewModel (application: Application) : AndroidViewModel(application)  {

    val fertilizanteDataModel: fertilizanteDataModel =  fertilizanteDataModel()
    val VSAGRFER: MutableLiveData<List<VSAGRRCOS>> = MutableLiveData()




    val laborsDataModel: fertilizanteDataModel = fertilizanteDataModel()
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

        fertilizanteDataModel.messageLiveData.observeForever {
            it?.let {
                onRetrieveLoginUserFinish()
                messageUpdateResult.value = it
            }
        }
        fertilizanteDataModel.responseVSAGRFERLiveData.observeForever {
            it?.let {
                VSAGRFER.value = it

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
        codigoPEP : String,
        httpCacheDirectory: Cache,
        context: Context
    ) {
        onRetrieveLoginUser()

        listaFertilizanteTask(this, sessionid,codigoPEP,httpCacheDirectory,context).execute()
    }


////////////////////////// TASK
    private class listaFertilizanteTask internal constructor(
    private var viewModel: fertilizanteViewModel,
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
            viewModel.fertilizanteDataModel.listFertilizante(sessionid,codigoPEP,httpCacheDirectory, context)
            /*else
                viewModel.messageResult.setValue("No tienes acceso a internet")*/
        }
    }



}