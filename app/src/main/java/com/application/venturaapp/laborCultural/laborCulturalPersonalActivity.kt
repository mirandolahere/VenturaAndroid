package com.application.venturaapp.laborCultural

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.fragment.entities.LaboresPersonal
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.entity.VSAGRDPCLResponse
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.*
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.btnPersonal
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.etCodigoPersona
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.etFecha
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.etLaborDefecto
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.etNumeroDocumento
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.rlButton
import kotlinx.android.synthetic.main.activity_personal_add.*
import okhttp3.Cache
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class laborCulturalPersonalActivity   : AppCompatActivity() {

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var Cultivo = ""
    private var Fecha = ""
    private var TipoCampania = ""
    private var Etapa = ""
    private var Estado = ""
    private var LineId = ""

    private var DocEntry:Int? = 0
    val codeList = ArrayList<String>()
    lateinit var personalViewModels: personalViewModel
    lateinit var personalLista: java.util.ArrayList<PersonalDato>
    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborCulturaViewModel
     var laborList = arrayListOf<LaboresPersonal>()
    lateinit var personalLabor : LaborCulturalDetalleResponse
    var CodigoLabor = ""
    var CodigoPersonal =""
    var  json=""
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_BUSCAR = 101
    var hourSelect:Int = 0
    var minuteSelect:Int =0
    var nombre =""
    var labor =""
    var dni =""
    var codigo =""
    var descripcion =""
    lateinit var labores  : ArrayList<String>
     var laboresTitulo  = arrayListOf<String>()

    var laboresList = arrayListOf<VSAGRDPCLResponse>()
    lateinit var httpCacheDirectory : Cache
    lateinit var peopleClass : PersonalDato
    var personalexists = arrayListOf<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_labor_cultural_personal)
        pref = PreferenceManager(this)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        initViews()
        LaborListar()
        setUpViews()
        setUpObservers()

        // setUpObservers()

    }
    fun Spinner()
    {
        val adapterLabores = ArrayAdapter(
            this,
            R.layout.spinner, labores
        )//LABORES
        adapterLabores.setDropDownViewResource(R.layout.spinner_list)
        etLaborDefecto.adapter = adapterLabores


        etLaborDefecto.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                descripcion = labores.get(position)

                for( item in laboresList)
                {
                    if(item.U_VS_AGR_CDLC == descripcion)
                    {
                        CodigoLabor = item.U_VS_AGR_CDLC

                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }



    }
    private fun personalListar() {
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarPersonal(
            it,
            httpCacheDirectory, this
        ) }
    }
    private fun LaboresListar() {
        Log.d("LABORESPLANIFICADA","LABORESPLANIFICADASS")
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarLabor(
            it,
            httpCacheDirectory,this
        ) }
    }
    private fun LaborListar() {
        Log.d("MOSTRARDATOS", CodigoPEP)
        Log.d("MOSTRARDATOS", Etapa)

        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.laborPlanificada(
            it,
            CodigoPEP,
            Etapa,
            httpCacheDirectory,
            this
        ) }
    }
    fun initViews() {
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo_isotipo)
             CodigoPEP = intent.getSerializableExtra("CODIGOPEP").toString()
             Campania = intent.getSerializableExtra("CAMPANIA").toString()
             Cultivo = intent.getSerializableExtra("CULTIVO").toString()
            TipoCampania = intent.getSerializableExtra("TIPOCAMPANIA").toString()
            Fecha = intent.getSerializableExtra("FECHA").toString()
            Etapa = intent.getSerializableExtra("ETAPA").toString()
            Estado = intent.getSerializableExtra("ESTADO").toString()

            if(Estado == "1")
        {   var entry:String
            entry = intent.getSerializableExtra("DOCENTRY").toString()
            DocEntry = entry.toInt()
            personalListar()

        }
        else if(Estado == "0")
        {
            rlButton.visibility = View.GONE
            val gson = Gson()
            if(intent.getSerializableExtra("OBJECTPERSONA") !=null) {

                val people = intent.getSerializableExtra("OBJECTPERSONA") //as? PersonalDato
                personalLabor = gson.fromJson(
                    people.toString(),
                    LaborCulturalDetalleResponse::class.java
                )
                mostrarDatos()

            }
        }else if(Estado == "2")
        {
            btnPersonal.setBackgroundResource(R.drawable.btn_personal)
            btnPersonal.text="Actualizar"
            var entry:String
            entry = intent.getSerializableExtra("DOCENTRY").toString()
            LineId = intent.getSerializableExtra("LINEID").toString()

            DocEntry = entry.toInt()
            val gson = Gson()
            if(intent.getSerializableExtra("OBJECTPERSONA") !=null) {

                val people = intent.getSerializableExtra("OBJECTPERSONA") //as? PersonalDato
                personalLabor = gson.fromJson(
                    people.toString(),
                    LaborCulturalDetalleResponse::class.java
                )
                actualizarDatos()

            }
        }

        setCabecera()

    }
    fun mostrarDatos()
    {
        btnBuscar.visibility = View.GONE

        for((index, item) in laboresList.withIndex())
        {
            if(item.U_VS_AGR_CDLC == personalLabor.U_VS_AGR_CDLC)
            {
                etLaborDefecto.setSelection(index)
                Log.d("MOSTRARDATOS", index.toString())

            }
        }

        etCodigoPersona.setText(personalLabor.U_VS_AGR_CDPS)
        etNumeroDocumento.setText(personalLabor.NumeroDocumento)
        etNombre.setText(personalLabor.Nombre)
        etInicio.setText(personalLabor.U_VS_AGR_HRIN)
        etFin.setText(personalLabor.U_VS_AGR_HRFN)
        etJornales.setText(personalLabor.U_VS_AGR_TOJR.toString())
        etExtra.setText(personalLabor.U_VS_AGR_TOHX.toString())
        etLaborDefecto.isEnabled = false
        etCodigoPersona.isEnabled = false
        etNumeroDocumento.isEnabled = false
        etNombre.isEnabled = false
        etInicio.isEnabled = false
        etFin.isEnabled = false
        etJornales.isEnabled = false
        etExtra.isEnabled = false

    }
    fun actualizarDatos()
    {
        for((index, item) in laboresList.withIndex())
            {
                if(item.U_VS_AGR_CDLC == personalLabor.U_VS_AGR_CDLC)
                {
                    etLaborDefecto.setSelection(index)
                }
            }
        //etLaborDefecto.setText(personalLabor.DescripcionDeLabor)
        etCodigoPersona.setText(personalLabor.U_VS_AGR_CDPS)
        etNumeroDocumento.setText(personalLabor.NumeroDocumento)
        etNombre.setText(personalLabor.Nombre)
        etInicio.setText(personalLabor.U_VS_AGR_HRIN)
        etFin.setText(personalLabor.U_VS_AGR_HRFN)
        etJornales.setText(personalLabor.U_VS_AGR_TOJR.toString())
        etExtra.setText(personalLabor.U_VS_AGR_TOHX.toString())

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("SetTextI18n")
    fun setCabecera()
    {
        etCodigoPEP.setText(CodigoPEP)
        etCampania.setText(Campania)
        Log.d("campania", TipoCampania)
        when(TipoCampania)
        {
            "D" -> etTipoCampania.setText(TipoCampania + " - Desarrollo")
            "P" -> etTipoCampania.setText(TipoCampania + " - Producción")
            "I" -> etTipoCampania.setText(TipoCampania + " - Investigación")

        }
        etCultivo.setText(Cultivo)
        etFecha.setText(Fecha)
        etEtapa.setText(Etapa)

        tvCampania.visibility = View.GONE
        etCampania.visibility = View.GONE
        tvCultivo.visibility = View.GONE
        etCultivo.visibility = View.GONE
        etTipoCampania.visibility = View.GONE
        tvTipoCampania.visibility = View.GONE
        tvFecha.visibility = View.GONE
        etFecha.visibility = View.GONE
        tvEtapa.visibility = View.GONE
        etEtapa.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpObservers(){

        laborViewModels.responseLaborPlanificadaResult.observe(this, Observer {
            it?.let {
                var simpleFormat = DateTimeFormatter.ISO_DATE;
                var convertedDate = LocalDate.parse(Fecha, simpleFormat)
                val mes: String
                val dia: String

                if (convertedDate.monthValue < 10)
                    mes = "0" + convertedDate.monthValue
                else
                    mes = convertedDate.monthValue.toString()

                val periodo: String = convertedDate.year.toString() + "-" + mes
                Log.d("LABORESPLANIFICADAS",it.toString())

                for (item in it) {
                    //if (item.U_VS_AGR_CDPE == periodo)
                        laboresList.add(item)
                }

                labores = java.util.ArrayList<String>()
                for (item in laboresList) {
                    labores.add(item.U_VS_AGR_CDLC)

                    for(labor in laborList)
                    {
                        if(item.U_VS_AGR_CDLC == labor.Code)
                        {
                            laboresTitulo.add(item.U_VS_AGR_CDLC + " - " + labor.Name)
                        }
                    }
                }

                Spinner()
                initViews()


            }
        })
        laborViewModels.laborPorCodeResult.observe(this, Observer {
            it?.let {

                if (checkInternet()) {
                    var detalle: ArrayList<LaborCulturalDetalleResponse> =
                        it.VS_AGR_DCULCollection as ArrayList<LaborCulturalDetalleResponse>
                    if (Estado != "2") {
                        var extra: Int = 0
                        if (etExtra.text.toString().isEmpty()) {
                            extra = 0
                        } else {
                            extra = etExtra.text.toString().toInt()
                        }

                        var detalleNuevo: LaborCulturalDetalleResponse? = DocEntry?.let { it1 ->
                            LaborCulturalDetalleResponse(
                                it1,
                                0,
                                0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                CodigoLabor,
                                CodigoPersonal,
                                etInicio.text.toString(),
                                etFin.text.toString(),
                                "P",
                                0,
                                0,
                                "Y",
                                "",
                                "",
                                "AP",
                                etJornales.text.toString().toInt(),
                                extra
                            )

                        }
                        if (detalleNuevo != null) {
                            detalle.add(detalleNuevo)
                        }
                    }
                    val body = JsonObject()
                    val body3 = JsonArray()
                    var body5 = JsonObject()

                    body.addProperty("DocNum", it.DocEntry)
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

                        if (Estado == "2") { //actualizar
                            if (item.LineId != LineId.toInt()) {
                                body5.addProperty("DocEntry", item.DocEntry)
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
                                body5.addProperty("U_VS_AGR_TOHX", item.U_VS_AGR_TOHX)

                                Log.d("JSONREQUESTUPDATE", body5.toString())
                                body3.add(body5)
                            } else {
                                body5.addProperty("DocEntry", item.DocEntry)
                                body5.addProperty("LineId", item.LineId)
                                body5.addProperty("VisOrder", item.VisOrder)
                                body5.addProperty("Object", item.Object)
                                body5.addProperty("LogInst", item.LogInst)
                                body5.addProperty("U_VS_AGR_CDLC", CodigoLabor)

                                body5.addProperty("U_VS_AGR_CDPS", etCodigoPersona.text.toString())
                                body5.addProperty("U_VS_AGR_HRIN", etInicio.text.toString())
                                body5.addProperty("U_VS_AGR_HRFN", etFin.text.toString())
                                body5.addProperty("U_VS_AGR_ESTA", item.U_VS_AGR_ESTA)
                                body5.addProperty("U_VS_AGR_IDEP", item.U_VS_AGR_IDEP)
                                body5.addProperty("U_VS_AGR_LNEP", item.U_VS_AGR_LNEP)

                                body5.addProperty("U_VS_AGR_ACTV", item.U_VS_AGR_ACTV)
                                body5.addProperty("U_VS_AGR_USCA", item.U_VS_AGR_USCA)
                                body5.addProperty("U_VS_AGR_USAA", item.U_VS_AGR_USAA)
                                body5.addProperty("U_VS_AGR_RORI", item.U_VS_AGR_RORI)
                                body5.addProperty("U_VS_AGR_TOJR", etJornales.text.toString())
                                body5.addProperty("U_VS_AGR_TOHX", etExtra.text.toString())
                                body3.add(body5)

                                Log.d("JSONREQUESTUPDATE", body5.toString())
                            }
                        } else {    //nuevo
                            body5.addProperty("DocEntry", item.DocEntry)
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
                            body5.addProperty("U_VS_AGR_TOHX", item.U_VS_AGR_TOHX)
                            body3.add(body5)

                        }
                    }
                    body.add("VS_AGR_DCULCollection", body3)

                    ActualizarLabor(body, Estado)
                } else {
                    val detalle = LaborCulturalDetalleRoom(
                        0,
                        DocEntry,
                        0,
                        0,
                        "",
                        "",
                        CodigoLabor,
                        CodigoPersonal,
                        etInicio.text.toString(),
                        etFin.text.toString(),
                        "P",
                        0,
                        "Y",
                        "",
                        "",
                        "AP",
                        etJornales.text.toString().toInt(),
                        etExtra.text.toString().toInt()
                    )
                    laborViewModels.insertLaborDetalleRoom(detalle)
                    pref.saveString(Constants.MESSAGE_ALERT, "Se guardó en la memoria interna.")
                    startActivityForResult(
                        Intent(
                            this@laborCulturalPersonalActivity,
                            AlertActivity::class.java
                        ), REQUEST_ACTIVITY
                    )
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        })

        laborViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = it
            }
        })
        personalViewModels.laborResult.observe(this, Observer {
            it?.let {
                laborList = it as ArrayList<LaboresPersonal>

                Log.d("LABORESPLANIFICADAS", it.toString())
                LaborListar()

                /* etapaList = it as ArrayList<EtapaProduccionListResponse>
                etapa = java.util.ArrayList<String>()
                for (item in it) {
                    etapa.add(item.U_VS_AGR_DSEP)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }
                Spinner()*/
                /* rvLaborCultural.adapter = (laborCulturalPEPAdapter(this,laborList))
                rvLaborCultural.layoutManager = LinearLayoutManager(this)
*/
                pgbLaborRealizada.visibility = View.GONE
            }
        })

        laborViewModels.messageUpdateResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivityForResult(
                    Intent(
                        this@laborCulturalPersonalActivity,
                        AlertActivity::class.java
                    ), REQUEST_ACTIVITY
                )
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d("ELIMINAR", "ELIMINAR2")
            }
        })
    }

    fun ActualizarLabor(body: JsonObject, Estado: String)
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.actualizarLaborCultural(
            it,
            DocEntry.toString(),
            body,
            Estado
        ) }

    }

    fun validar():Boolean
    {
        if(etInicio.text.toString() == "")
        {
            etInicio.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }
        if(etFin.text.toString() == "")
        {
            etFin.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }
        if(etJornales.text.toString().isEmpty())
        {
            etJornales.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }

        if(etExtra.text.toString() == "")
        {
            etNombre.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }

            return true
    }
    fun hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(
                this,
                InputMethodManager::class.java
            )!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpViews() {
        rlLaborPersonal.setOnClickListener {
            hideSoftKeyboard()
        }
        btnBuscar.setOnClickListener {

            val intent = Intent(this, popupPersonalActivity::class.java)
            var gson =  Gson()


                Log.d("PERSONALLIST11",personalexists.toString())
                intent.putStringArrayListExtra("OBJECT", personalexists)



            startActivityForResult(intent, REQUEST_ACTIVITY_BUSCAR)

        }
        btnPersonal.setOnClickListener {
            if(checkInternet()) {
                if (validar()) {

                    pref.getString(Constants.B1SESSIONID)?.let {
                        laborViewModels.listaDetalleLaborCultural(
                            it,
                            DocEntry.toString(),
                            httpCacheDirectory,
                            this
                        )
                    }

                }
            }
            else
            { if(validar()) {
                if (Estado == "2") {
                    val detalle = LaborCulturalDetalleRoom(
                        0,
                        DocEntry,
                        0,
                        0,
                        "",
                        "",
                        CodigoLabor,
                        "",
                        etInicio.text.toString(),
                        etFin.text.toString(),
                        "P",
                        0,
                        "Y",
                        "",
                        "",
                        "AP",
                        etJornales.text.toString().toInt(),
                        etExtra.text.toString().toInt()
                    )
                    laborViewModels.updateLaborDetalleRoom(detalle)
                    pref.saveString(Constants.MESSAGE_ALERT, "Se actualizó en la memoria interna.")
                    startActivityForResult(
                        Intent(
                            this@laborCulturalPersonalActivity,
                            AlertActivity::class.java
                        ), REQUEST_ACTIVITY
                    )
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    } else {
                        var extra:Int = 0
                    if(etExtra.text.toString().isEmpty())
                    {
                        extra = 0
                    }
                    else
                    {
                        extra =  etExtra.text.toString().toInt()
                    }
                    val detalle = LaborCulturalDetalleRoom(
                        0,
                        DocEntry,
                        0,
                        0,
                        "",
                        "",
                        CodigoLabor,
                        "",
                        etInicio.text.toString(),
                        etFin.text.toString(),
                        "P",
                        0,
                        "Y",
                        "",
                        "",
                        "AP",
                        etJornales.text.toString().toInt(),
                        extra
                    )
                        laborViewModels.insertLaborDetalleRoom(detalle)
                        pref.saveString(Constants.MESSAGE_ALERT, "Se guardó en la memoria interna.")
                        startActivityForResult(
                            Intent(
                                this@laborCulturalPersonalActivity,
                                AlertActivity::class.java
                            ), REQUEST_ACTIVITY
                        )
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
            }
        }

        etInicio.setOnClickListener{
            val mcurrentTime: Calendar = Calendar.getInstance()
             val hour= mcurrentTime.get(Calendar.HOUR_OF_DAY)
             val minute= mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->
                hourSelect = selectedHour
                minuteSelect = selectedMinute
                var horaFormateada = ""
                var minutoFormateada = ""
                if (hourSelect < 10) {
                    horaFormateada = "0" + hourSelect
                } else {
                    horaFormateada = hourSelect.toString()
                }
                if (minuteSelect < 10) {
                    minutoFormateada = "0" + minuteSelect
                } else {
                    minutoFormateada = minuteSelect.toString()
                }
                etInicio.setText(horaFormateada + ":" + minutoFormateada)

                /*if(etInicio.text.toString()!="" && etFin.text.toString() !="")
                {
                    var timeIni = LocalTime.parse(etInicio.text.toString())
                    var timeFin = LocalTime.parse(etFin.text.toString())
                    var jornada =( (((timeFin.hour*60)+timeFin.minute) - ((timeIni.hour*60)+timeIni.minute)) /60)/8

                    etJornales.setText(jornada.toString())

                }*/
            }, hour, minute, true) //Yes 24 hour time

            mTimePicker.setTitle("Seleccione la hora de Inicio")
            mTimePicker.show()
        }
        etFin.setOnClickListener{
            val mcurrentTime: Calendar = Calendar.getInstance()
            val hourFin: Int = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minuteFin: Int = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour,
                                                   selectedMinute ->
                var horaFormateada = ""
                var minutoFormateada = ""

                if (selectedHour < 10) {
                    horaFormateada = "0" + selectedHour
                } else {
                    horaFormateada = selectedHour.toString()
                }
                if (selectedMinute < 10) {
                    minutoFormateada = "0" + selectedMinute
                } else {
                    minutoFormateada = selectedMinute.toString()
                }
                etFin.setText(horaFormateada + ":" + minutoFormateada)

                /*if(etInicio.text.toString()!="" && etFin.text.toString() !="")
                {
                    var timeIni = LocalTime.parse(etInicio.text.toString())
                    var timeFin = LocalTime.parse(etFin.text.toString())

                    var jornada =( (((timeFin.hour*60)+timeFin.minute) - ((timeIni.hour*60)+timeIni.minute)) /60)/8

                    etJornales.setText(jornada.toString())
                }*/
            }, hourFin, minuteFin, true) //Yes 24 hour time



            mTimePicker.setTitle("Seleccione la hora de Fin")
            mTimePicker.show()






        }

        llCabecera.setOnClickListener {
            when (tvCampania.visibility) {
                0 -> {
                    tvCampania.visibility = View.GONE
                    etCampania.visibility = View.GONE
                    tvCultivo.visibility = View.GONE
                    etCultivo.visibility = View.GONE
                    etTipoCampania.visibility = View.GONE
                    tvTipoCampania.visibility = View.GONE
                    tvFecha.visibility = View.GONE
                    etFecha.visibility = View.GONE
                    tvEtapa.visibility = View.GONE
                    etEtapa.visibility = View.GONE
                    ivExpand.setImageResource(R.drawable.ic_down)

                }
                8 -> {
                    tvCampania.visibility = View.VISIBLE
                    etCampania.visibility = View.VISIBLE
                    tvCultivo.visibility = View.VISIBLE
                    etCultivo.visibility = View.VISIBLE
                    etTipoCampania.visibility = View.VISIBLE
                    tvTipoCampania.visibility = View.VISIBLE
                    tvFecha.visibility = View.VISIBLE
                    etFecha.visibility = View.VISIBLE
                    tvEtapa.visibility = View.VISIBLE
                    etEtapa.visibility = View.VISIBLE
                    ivExpand.setImageResource(R.drawable.ic_up)

                }
            }

        }

        var adapterLabores = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item, codeList
        )
        etCodigoPersona.setAdapter(adapterLabores)

        etCodigoPersona.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            Log.d("HOLAAAAAAAAA", position.toString())
            Log.d("HOLAAAAAAAAA", parent.getItemAtPosition(position).toString())

            for (item in personalLista) {
                if (item.Code == parent.getItemAtPosition(position).toString()) {
                    CodigoLabor = item.CodigoLabor
                    CodigoPersonal = item.Code
                    //etLaborDefecto.setText(item.DescripcionLabor)

                    for ((index, y) in laboresList.withIndex()) {
                        if (item.Name == item.DescripcionLabor) {
                            etLaborDefecto.setSelection(index)
                        }
                    }

                    etNumeroDocumento.setText(item.NumeroDocumento)
                    etNombre.setText(item.PrimerNombre + " " + item.ApellidoPaterno)
                }
            }

        })
    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ELIMINAR", requestCode.toString())
        Log.d("ELIMINAR", resultCode.toString())

        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    setResult(RESULT_OK)
                    finish()
                }

            }
            REQUEST_ACTIVITY_BUSCAR -> {
                if (resultCode == Activity.RESULT_OK) {
                    nombre = pref.getString(Constants.NOMBREBUSCADOR).toString()
                    //CodigoLabor = pref.getString(Constants.LABORBUSCADOR).toString()
                    dni = pref.getString(Constants.DNIBUSCADOR).toString()
                    CodigoPersonal = pref.getString(Constants.CODIGOBUSCADOR).toString()
                    // descripcion = pref.getString(Constants.DESCRIPCIONBUSCADOR).toString()


                    /*for((index, y) in laboresList.withIndex())
                    {
                        if(y.U_VS_AGR_CDLC == descripcion)
                        {
                            etLaborDefecto.setSelection(index)
                        }
                    }*/

                    etNumeroDocumento.setText(dni)
                    etNombre.setText(nombre)
                    etCodigoPersona.setText(CodigoPersonal)

                }

            }
        }
    }


}

