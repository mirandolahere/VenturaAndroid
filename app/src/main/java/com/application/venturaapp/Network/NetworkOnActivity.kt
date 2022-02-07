package com.application.venturaapp.Network
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.HomeActivity
import com.application.venturaapp.home.fragment.Sincronizacion
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.login.LoginViewModel
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.application.venturaapp.tables.LaborCulturalRoom
import com.application.venturaapp.tables.PersonalDB
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_labor_cultural.pgbLaborRealizada
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loading
import kotlinx.android.synthetic.main.activity_personal_add.*
import kotlinx.android.synthetic.main.activity_usuario.*
import okhttp3.Cache


class NetworkOnActivity   : AppCompatActivity() {

    var documentoLista = ArrayList<TipoDocumento>()
    private val REQUEST_ACTIVITY = 100
    lateinit var pref: PreferenceManager
    lateinit var laborList  : ArrayList<LaborCulturalListResponse>
    lateinit var viewModel: LoginViewModel
    lateinit var personalViewModels: personalViewModel
    lateinit var laborViewModels: laborCulturaViewModel
    lateinit var httpCacheDirectory : Cache
    var docentrydb:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_network)
        pref = PreferenceManager(this)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)

        initViews()

        //setUpViews()
        setUpObservers()

    }
    fun initViews() {
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        viewModel.LoginGeneral(pref.getString(Constants.URL)!!,
            pref.getString(Constants.PUERTO)!!,
            pref.getString(Constants.COMPANYDB)!!,
            pref.getString(Constants.USER)!!,
            pref.getString(Constants.PASSWORD)!!)


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun personal() {
        val personal = PersonalDB.getDatabase(getApplication()).personalDao()

        val list: List<PersonalDatoRoom>? = personal?.getAll()

        if (list!!.size>0) {
            for (item in list) {
                val body3 = JsonArray()
                val body = JsonObject()
                var body5 = JsonObject()
                var primerNombre: String = ""
                var segundoNombre: String = ""
                var primerApellido: String = ""
                var segundoApellido: String = ""


                body.addProperty("U_VS_AGR_FENA", item.FechaNacimiento)
                body.addProperty("U_VS_AGR_BPNO", item.PrimerNombre)
                primerNombre = item.PrimerNombre
                body.addProperty("U_VS_AGR_BPN2", item.PrimerNombre)
                segundoNombre = item.PrimerNombre
                body.addProperty("U_VS_AGR_BPAP", item.ApellidoPaterno)
                primerApellido = item.ApellidoPaterno
                body.addProperty("U_VS_AGR_BPAM", item.ApellidoMaterno)
                segundoApellido = item.ApellidoMaterno
                body.addProperty("U_VS_AGR_DOIN", item.NumeroDocumento)
                body.addProperty(
                    "Name",
                    "$primerApellido $segundoApellido $primerNombre $segundoNombre"
                )

                body.addProperty("U_VS_AGR_TEMP", item.TipoEmpleado)

                if (item.TipoEmpleado == "T") {
                    body.addProperty("U_VS_AGR_CPRV", item.CodigoProveedor)
                    body.addProperty("U_VS_AGR_NPRV", item.NombreProveedor)
                }

                body.addProperty("U_VS_AGR_TIDO", item.TipoDocumento)

                body.addProperty("U_VS_AGR_DELA", item.DescripcionLabor)
                body.addProperty("U_VS_AGR_LBCA", item.CodigoLabor)
                body.addProperty("U_VS_AGR_ACTV", "Y")
                body.addProperty("Code", item.Code)

                /*
            try {
                val size: Int = (rvContactos.getAdapter() as PersonalAdapter).getItemCount()
                Log.d("tamanio", size.toString())
                var index: Int = 0
                while (index < size) {
                    Log.d("tamanio", index.toString())

                    val element =
                        (rvContactos.getAdapter() as PersonalAdapter).getItem(index)
                    if (element != null) {
                        body5 = JsonObject()
                        if (element.LineId != 0) {
                            body5.addProperty("LineId", element.LineId)

                        }
                        if (element.U_VS_AGR_PARE != "") {
                            body5.addProperty("U_VS_AGR_PARE", element.U_VS_AGR_PARE)

                        }
                        if (element.U_VS_AGR_TEL1 != "") {
                            if (element.U_VS_AGR_TEL1.length >= 7) {
                                body5.addProperty("U_VS_AGR_TEL1", element.U_VS_AGR_TEL1)
                                isCorrect1 = true
                            } else {
                                isCorrect1 = false
                            }

                        }
                        if (element.U_VS_AGR_TEL2 != "") {
                            if (element.U_VS_AGR_TEL2.length >= 7) {
                                body5.addProperty("U_VS_AGR_TEL2", element.U_VS_AGR_TEL2)
                                isCorrect2 = true
                            } else {
                                isCorrect2 = false
                            }

                        }
                        if (element.U_VS_AGR_NOCM != "") {
                            body5.addProperty("U_VS_AGR_NOCM", element.U_VS_AGR_NOCM)
                            body3.add(body5)

                        }

                    }
                    index++
                }
                if (body3.size() > 0) {
                    body.add("VS_AGR_CONTCollection", body3)
                }
                Log.d("tamanio", body.toString())
            } catch (e: Exception) {
            }*/


                /*pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                    personalViewModels.addPersonal(
                        it1, body
                    )
                }*/
                pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                    personalViewModels.addPersonal(
                        it1, body
                    )
                }
            }
        }else{
            labores()
        }
    }

    fun labores() {
        val personal = PersonalDB.getDatabase(getApplication()).personalDao()

        val list: List<LaborCulturalRoom>? = personal?.getLaborAll()

        if (list!!.size > 0) {
            for (item in list) {
                val body = JsonObject()
                docentrydb = item.id
                body.addProperty("U_VS_AGR_FERG", item.U_VS_AGR_FERG)
                body.addProperty("U_VS_AGR_CDCA", item.U_VS_AGR_CDCA)
                body.addProperty("U_VS_AGR_CDPP", item.U_VS_AGR_CDPP)
                body.addProperty("U_VS_AGR_CDEP", item.U_VS_AGR_CDEP)
                body.addProperty("U_VS_AGR_ACTV", "Y")
                body.addProperty("U_VS_AGR_RORI", "AP")
                pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                    laborViewModels.addLabor(
                        it1, body
                    )
                }
            }
        }else
        {
            val personal = PersonalDB.getDatabase(getApplication()).personalDao()

            val list: List<LaborCulturalDetalleRoom>? = personal?.getDetallWithouteAll()
            if (list != null) {
                for(item in list) {
                    docentrydb = item.DocEntry!!
                    pref.getString(Constants.B1SESSIONID)?.let { it1->laborViewModels.listaDetalleLaborCultural(it1,
                        item.DocEntry.toString(),
                        httpCacheDirectory,this) }
                }
            }
        }
    }
    fun laboresdetalle() {
        val personal = PersonalDB.getDatabase(getApplication()).personalDao()

        val list: List<LaborCulturalRoom>? = personal?.getLaborAll()

        if (list != null) {
            for (item in list) {
                val body = JsonObject()

                body.addProperty("U_VS_AGR_CDCA", item.U_VS_AGR_CDCA)
                body.addProperty("U_VS_AGR_CDPP", item.U_VS_AGR_CDPP)
                body.addProperty("U_VS_AGR_CDEP", item.U_VS_AGR_CDEP)
                body.addProperty("U_VS_AGR_ACTV", "Y")
                body.addProperty("U_VS_AGR_RORI", "AP")
                pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                    personalViewModels.addPersonal(
                        it1, body
                    )
                }
            }
        }
    }
    fun ActualizarLabor(body: JsonObject, Estado: String)
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.actualizarLaborCultural(it, docentrydb.toString(), body, Estado) }

    }
   fun setUpObservers(){
       laborViewModels.laborResponseResult.observe(this, Observer {
           pref.getString(Constants.B1SESSIONID)?.let { it1->laborViewModels.listaDetalleLaborCultural(it1,
               it.DocEntry.toString(),
               httpCacheDirectory,this) }

       }
       )

       laborViewModels.laborPorCodeResult.observe(this, Observer {
           val personal = PersonalDB.getDatabase(getApplication()).personalDao()

               val list: List<LaborCulturalDetalleRoom>? = personal?.getDetalleAll(docentrydb)

           if (list != null) {
               for (items in list) {

                   var detalle: ArrayList<LaborCulturalDetalleResponse> =
                       it.VS_AGR_DCULCollection as ArrayList<LaborCulturalDetalleResponse>
                   var detalleNuevo: LaborCulturalDetalleResponse? = it.DocEntry?.let { it1 ->
                       LaborCulturalDetalleResponse(
                           it1.toInt(),
                           0,
                           0,
                           "",
                           "",
                           "",
                           "",
                           "",
                           items.U_VS_AGR_CDLC,
                           items.U_VS_AGR_CDPS,items.U_VS_AGR_HRIN,items.U_VS_AGR_HRFN,
                           "P",
                           0,
                           0,
                           "Y",
                           "",
                           "",
                           "AP",
                           items.U_VS_AGR_TOJR,
                           items.U_VS_AGR_TOPL
                       )

                   }
                   if (detalleNuevo != null) {
                       detalle.add(detalleNuevo)
                   }

                   val body = JsonObject()
                   val body3 = JsonArray()
                   var body5 = JsonObject()

                   body.addProperty("DocNum", it.DocEntry)
                   docentrydb = it.DocEntry.toInt()
                   body.addProperty("Period", it.Period)
                   body.addProperty("Instance", it.Instance)
                   body.addProperty("Series", it.Series)
                   body.addProperty("Handwrtten", it.Handwrtten)
                   body.addProperty("RequestStatus", it.RequestStatus)
                   body.addProperty("Creator", it.Creator)
                   body.addProperty("Remark", it.Remark)
                   body.addProperty("DocEntry", it.DocEntry)
                   body.addProperty("Canceled", it.Canceled)
                   body.addProperty("Object", it.Object)
                   body.addProperty("LogInst", it.LogInst)
                   body.addProperty("UserSign", it.UserSign)
                   body.addProperty("Transfered", it.Transfered)
                   body.addProperty("CreateDate", it.CreateDate)
                   body.addProperty("CreateTime", it.CreateTime)
                   body.addProperty("UpdateDate", it.UpdateDate)
                   body.addProperty("UpdateTime", it.UpdateTime)
                   body.addProperty("DataSource", it.DataSource)
                   body.addProperty("U_VS_AGR_CDCA", it.U_VS_AGR_CDCA)
                   body.addProperty("U_VS_AGR_CDPP", it.U_VS_AGR_CDPP)
                   body.addProperty("U_VS_AGR_CDEP", it.U_VS_AGR_CDEP)
                   body.addProperty("U_VS_AGR_FERG", it.U_VS_AGR_FERG)
                   body.addProperty("U_VS_AGR_ACTV", it.U_VS_AGR_ACTV)
                   body.addProperty("U_VS_AGR_USCA", it.U_VS_AGR_USCA)
                   body.addProperty("U_VS_AGR_USAA", it.U_VS_AGR_USAA)
                   body.addProperty("U_VS_AGR_USAA", it.U_VS_AGR_USAA)


                   for (item in detalle) {
                       body5 = JsonObject()

                       //nuevo
                       body5.addProperty("DocEntry",  it.DocEntry)
                       body5.addProperty("LineId", item.LineId)
                       body5.addProperty("VisOrder", item.VisOrder)
                       body5.addProperty("Object", item.Object)
                       body5.addProperty("LogInst", item.LogInst)
                       body5.addProperty("U_VS_AGR_CDLC", item.U_VS_AGR_CDLC)

                       body5.addProperty("U_VS_AGR_CDPS", item.U_VS_AGR_CDPS)
                       body5.addProperty("U_VS_AGR_HRIN", item.U_VS_AGR_HRIN)
                       body5.addProperty("U_VS_AGR_HRFN", item.U_VS_AGR_HRFN)
                       body5.addProperty("U_VS_AGR_ESTA", item.U_VS_AGR_ESTA)
                       body5.addProperty("U_VS_AGR_IDEP", item.U_VS_AGR_IDEP)
                       body5.addProperty("U_VS_AGR_LNEP", item.U_VS_AGR_LNEP)

                       body5.addProperty("U_VS_AGR_ACTV", item.U_VS_AGR_ACTV)
                       body5.addProperty("U_VS_AGR_USCA", item.U_VS_AGR_USCA)
                       body5.addProperty("U_VS_AGR_USAA", item.U_VS_AGR_USAA)
                       body5.addProperty("U_VS_AGR_RORI", item.U_VS_AGR_RORI)
                       body5.addProperty("U_VS_AGR_TOJR", item.U_VS_AGR_TOJR)
                       body5.addProperty("U_VS_AGR_TOPL", item.U_VS_AGR_TOPL)
                       body3.add(body5)


                   }
                   body.add("VS_AGR_DCULCollection", body3)

                   ActualizarLabor(body, "2")
               }
           }

       })
       viewModel.loginResult.observe(this, Observer {
           it?.let {

               pref.saveString(Constants.SESSIONID, Gson().toJson(it.SessionId))
               pref.saveString(Constants.B1SESSIONID, "B1SESSION=" + it.SessionId)

               pref.saveString(Constants.VERSION, it.Version)
               personal()

           }

       })

       personalViewModels.messageResult.observe(this, Observer {
           pgbLaborRealizada.visibility = View.GONE
           val personal = PersonalDB.getDatabase(getApplication()).personalDao()

           personal?.deletePersonal()
           labores()

       })
       laborViewModels.messageUpdateResult.observe(this, Observer {
           it?.let {
               val personal = PersonalDB.getDatabase(getApplication()).personalDao()

               personal?.deleteLabor()
               personal?.deleteDetalle()
               startActivity(Intent(this@NetworkOnActivity, HomeActivity::class.java))
               finish()
           }
       })
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (pref.getString(Constants.B1SESSIONID) != null) {
                        startActivity(Intent(this@NetworkOnActivity, Sincronizacion::class.java))
                        finish()
                    }
                }

            }

            }
        }


}

