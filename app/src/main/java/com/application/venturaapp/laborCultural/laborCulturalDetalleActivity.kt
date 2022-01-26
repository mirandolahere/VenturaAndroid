package com.application.venturaapp.laborCultural

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.venturaapp.R
import com.application.venturaapp.helper.*
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.laborCultural.adapter.laborCulturalDetalleAdapter
import com.application.venturaapp.laborCultural.entity.ContActualizacionResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.listener.LaborDetalleItemListener
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_labor_cultural.*
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.*
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.etBuscador
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.etCampania
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.etCodigoPEP
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.etCultivo
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.etEtapa
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.etFecha
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.etTipoCampania
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.ivExpand
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.ivPersonalAdd
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.llCabecera
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.pgbLaborRealizada
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.rvLaborCultural
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.tvCampania
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.tvCultivo
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.tvEtapa
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.tvFecha
import kotlinx.android.synthetic.main.activity_labor_cultural_detalle.tvTipoCampania
import kotlinx.android.synthetic.main.activity_labor_cultural_personal.*
import okhttp3.Cache
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class laborCulturalDetalleActivity   : AppCompatActivity() , LaborDetalleItemListener {

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var Cultivo = ""
    private var Fecha = ""
    private var TipoCampania = ""
    private var Etapa = ""
    private var DocEntry =""
    private var Variedad =""
    private var CodCampania =""
    private var DocEntryPEP = ""
    var personalExistente = arrayListOf<String>()
    var lineas = arrayListOf<String>()
    var estados = arrayListOf<String>()
    var periodos = arrayListOf<String>()
    var hrj = arrayListOf<Int>()
    var hrx = arrayListOf<Double>()
    var labores_HRXT = arrayListOf<String>()

    var labores = arrayListOf<String>()
    var jornales  = arrayListOf<Int>()
    var hrs  = arrayListOf<Double>()
    var laborDes  = arrayListOf<String>()

    var LineId: Int = 0

    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborCulturaViewModel
    lateinit var laborList  : ArrayList<LaborCulturalListResponse>
    lateinit var laborPorCodeList  : LaborCulturalListResponse
    var laborPorCodeDetalleList  =arrayListOf<LaborCulturalDetalleResponse>()
    lateinit var personalViewModels: personalViewModel
    var personalLista = arrayListOf<PersonalDato>()
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_DETALLE = 120
    lateinit var verificarList  : ContActualizacionResponse
    private val REQUEST_ACTIVITY_CONTABILIZAR = 140
    private val REQUEST_ACTIVITY_ELIMINAR = 170

    private val REQUEST_ACTIVITY_MESSAGE = 150
    private val REQUEST_ACTIVITY_CONFIRMACION = 160
    lateinit var httpCacheDirectory : Cache
    private val REQUEST_ACTIVITY_ERROR_CONTABILIZAR = 180


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_labor_cultural_detalle)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        pref = PreferenceManager(this)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)

        LaboresListar()

        setUpViews()
        setUpObservers()
        //LaborListar()

    }
    private fun personalListar() {
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarPersonal(
            it,
            httpCacheDirectory,this
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
        DocEntry = intent.getSerializableExtra("DOCENTRY").toString()
        Variedad = intent.getSerializableExtra("VARIEDAD").toString()
        CodCampania = intent.getSerializableExtra("CODIGOCAMPANIA").toString()
        DocEntryPEP = intent.getSerializableExtra("DOCENTRYPEP").toString()

        setCabecera()

        ListarDetalle()

    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }
    fun ListarDetalle()
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listaDetalleLaborCultural(it, DocEntry,
            httpCacheDirectory, this) }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                Log.d("arrow", "arrow")
                val myIntent = Intent(applicationContext, laborCulturalActivity::class.java)
                myIntent.putExtra("CODIGOPEP", CodigoPEP)
                myIntent.putExtra("CAMPAÑA", Campania)
                myIntent.putExtra("CULTIVO", Cultivo)
                myIntent.putExtra("VARIEDAD", Variedad)
                myIntent.putExtra("CODIGOCAMPANIA", CodCampania)
                myIntent.putExtra("TIPOCAMPANIA", TipoCampania)
                myIntent.putExtra("DOCENTRYPEP", DocEntryPEP)


                startActivity(myIntent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @SuppressLint("SetTextI18n")
    fun setCabecera()
    {
        etCodigoPEP.setText(CodigoPEP)
        etCampania.setText(Campania)
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
        ivExpand.setImageResource(R.drawable.ic_down)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpObservers(){

        laborViewModels.sendResult.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = View.GONE
                pref.saveString(Constants.MESSAGE_ALERT, "Se contabilizó de manera correcta")
                startActivityForResult(Intent(this@laborCulturalDetalleActivity, AlertActivity::class.java), REQUEST_ACTIVITY_CONTABILIZAR)


            }
            })
     /*   personalViewModels.personalResult.observe(this, Observer {
            it?.let {
                if (it.size != 0) {
                    val codeList = java.util.ArrayList<String>()

                    personalLista = it as java.util.ArrayList<PersonalDato>


                    for (item in laborPorCodeDetalleList) {
                        for (persona in personalLista) {
                            if (item.U_VS_AGR_CDPS == persona.Code) {
                                personalExistente.add(persona.Code)
                                Log.d("PERSONALLISTA", persona.Code)

                                item.Nombre = persona.PrimerNombre + " " + persona.ApellidoPaterno
                                item.NumeroDocumento = persona.NumeroDocumento
                                for((index,labor) in labores.withIndex())
                                {
                                    if(labor == item.U_VS_AGR_CDLC)
                                    {
                                        item.DescripcionDeLabor = laborDes[index]

                                    }
                                }

                            }
                            if(item.U_VS_AGR_ESTA == "P")
                                 ivSincronizacion.visibility = View.VISIBLE


                        }
                    }
                    Log.d("MOSTRARDATOS", laborDes.toString())
                    pref.saveString(Constants.PEPSXLAB,personalExistente.toString())

                    rvLaborCultural.adapter = (laborCulturalDetalleAdapter(this, laborPorCodeDetalleList))
                    rvLaborCultural.layoutManager = LinearLayoutManager(this)

                    pgbLaborRealizada.visibility = View.GONE


                } else {

                }

            }
        }) */
        personalViewModels.laborResult.observe(this, Observer {
            it?.let {
                if (it.size != 0) {
                   for(item in it)
                   {
                       laborDes.add(item.Name)
                       labores.add(item.Code)
                       labores_HRXT.add(item.U_VS_AGR_CDRX)

                   }

                    Log.d("ORDEN", "3")
                    Log.d("ORDEN",laborDes.toString())
                    Log.d("ORDEN",labores.toString())


                } else {

                }
                initViews()


            }
        })

        laborViewModels.ReverificarResult.observe(this, Observer {

            val body3 = JsonArray()
            val body = JsonObject()
            var body5 = JsonObject()
            var totalHora=0
            var totalHoraEx=0

            var simpleFormat = DateTimeFormatter.ISO_DATE;
            var convertedDate = LocalDate.parse(laborPorCodeList.U_VS_AGR_FERG, simpleFormat)
            val mes: String
            val dia: String

            if (convertedDate.monthValue < 10)
                mes = "0" + convertedDate.monthValue
            else
                mes = convertedDate.monthValue.toString()

            val periodo: String = convertedDate.year.toString()+ "-" + mes
            var baseLine=""
            var baseLineEx=""

            Log.d("CONTABILIZACIONPASO2",verificarList.ProductionOrderLines.toString())
            Log.d("CONTABILIZACIONPASO2",laborPorCodeList.VS_AGR_DCULCollection.toString())

            for((index,i )in estados.withIndex())
            {
                if(hrj[index]!=0 || hrx[index]!=0.0) {
                    for (item in it.ProductionOrderLines) {

                        if (i == item.U_VS_AGR_CDRA && item.U_VS_AGR_HRXT=="Y" && item.U_VS_AGR_CDPE ==periodos [index]) {
                            body5 = JsonObject()

                            body5.addProperty("U_VS_AGR_LNRL", lineas[index])
                            body5.addProperty("BaseEntry", DocEntryPEP)
                            body5.addProperty("Quantity", hrx[index])
                            body5.addProperty("BaseLine", item.LineNumber)
                            body3.add(body5)
                        }
                        if (i == item.U_VS_AGR_CDRA && item.U_VS_AGR_HRXT=="N"  && item.U_VS_AGR_CDPE ==periodos [index]) {
                            body5 = JsonObject()

                            body5.addProperty("U_VS_AGR_LNRL", lineas[index])
                            body5.addProperty("BaseEntry", DocEntryPEP)
                            body5.addProperty("Quantity", hrj[index])
                            body5.addProperty("BaseLine", item.LineNumber)
                            body3.add(body5)
                        }

                    }
                }
            }
            body.add("DocumentLines",body3)

            body.addProperty("DocDate",laborPorCodeList.U_VS_AGR_FERG)
            body.addProperty("DocDueDate",laborPorCodeList.U_VS_AGR_FERG)
            body.addProperty("U_VS_AGR_DERL",DocEntry)

            Log.d("CONTABILIZACIONPASO2",body.toString())

              pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.send(it,body) }

        })

        laborViewModels.roomResult.observe(this, Observer {

           // laborPorCodeList =  LaborCulturalListResponse
            //laborPorCodeDetalleList = laborPorCodeList.VS_AGR_DCULCollection as ArrayList<LaborCulturalDetalleResponse>

            var personal : List<LaborCulturalDetalleRoom>? = laborViewModels.getLaborDetalleRoom(DocEntry.toInt())
                if (personal != null) {
                    for(item in personal) {
                        val personal = LaborCulturalDetalleResponse(
                            item.DocEntry!!,
                            item.LineId,item.VisOrder,item.Object,item.LogInst,"","","",item.U_VS_AGR_CDLC,item.U_VS_AGR_CDPS,
                            item.U_VS_AGR_HRIN,item.U_VS_AGR_HRFN,item.U_VS_AGR_ESTA,0,item.U_VS_AGR_LNEP,item.U_VS_AGR_ACTV,
                            item.U_VS_AGR_USCA,item.U_VS_AGR_USAA,item.U_VS_AGR_RORI,item.U_VS_AGR_TOJR,item.U_VS_AGR_TOHX.toFloat())
                        laborPorCodeDetalleList.add(personal)
                    }
                }


                Log.d("ORDEN", "4")

                    var jornal:Int=0
                    var hr :Double = 0.0
                Log.d("ORDEN", labores.toString())
                for(labor in labores)
                {
                    Log.d("ORDEN", labor)

                    for (item in laborPorCodeDetalleList) {
                        Log.d("TABADINAMICA", item.toString())

                        if (labor == item.U_VS_AGR_CDLC) {
                            jornal+=item.U_VS_AGR_TOJR
                            hr+=item.U_VS_AGR_TOHX.toDouble()
                        }

                    }

                   jornales.add(jornal)
                   hrs.add(hr)
                    jornal = 0
                    hr = 0.0
                }


                if (laborPorCodeDetalleList.size != 0) {
                    Log.d("VALORTABLA", jornales.toString())
                    Log.d("VALORTABLA", hrs.toString())

                    //tableDinamica()
                    personalListar()

                } else
                    pgbLaborRealizada.visibility = View.GONE



        })
        laborViewModels.laborPorCodeResult.observe(this, Observer {
            it?.let {
                laborPorCodeList = it as LaborCulturalListResponse
                laborPorCodeDetalleList = laborPorCodeList.VS_AGR_DCULCollection as ArrayList<LaborCulturalDetalleResponse>


                var personal : List<LaborCulturalDetalleRoom>? = laborViewModels.getLaborDetalleRoom(DocEntry.toInt())
                if (personal != null) {
                    for(item in personal) {
                        val personal = LaborCulturalDetalleResponse(
                            item.DocEntry!!,
                            item.LineId,item.VisOrder,item.Object,item.LogInst,"","","",item.U_VS_AGR_CDLC,item.U_VS_AGR_CDPS,
                            item.U_VS_AGR_HRIN,item.U_VS_AGR_HRFN,item.U_VS_AGR_ESTA,0,item.U_VS_AGR_LNEP,item.U_VS_AGR_ACTV,
                            item.U_VS_AGR_USCA,item.U_VS_AGR_USAA,item.U_VS_AGR_RORI,item.U_VS_AGR_TOJR,item.U_VS_AGR_TOHX.toFloat())
                        laborPorCodeDetalleList.add(personal)
                    }
                }


                 for (item in laborPorCodeDetalleList) {

                     if (item.U_VS_AGR_ESTA == "P")
                         ivSincronizacion.visibility = View.VISIBLE
                 }


                rvLaborCultural.adapter = (laborCulturalDetalleAdapter(this, laborPorCodeDetalleList))
                rvLaborCultural.layoutManager = LinearLayoutManager(this)



                pgbLaborRealizada.visibility = View.GONE

            }
        })
        laborViewModels.verificarResult.observe(this, Observer {
            it?.let {
                val body3 = JsonArray()
                val body = JsonObject()

                var body5 = JsonObject()
                 verificarList = it


                for (item in labores)
                { var periodo: String = ""
                  var linea = ""
                    var hr = 0.0
                    var hj = 0

                    for(i in laborPorCodeList.VS_AGR_DCULCollection!!) {
                        var simpleFormat = DateTimeFormatter.ISO_DATE;

                        var convertedDate = LocalDate.parse(laborPorCodeList.U_VS_AGR_FERG, simpleFormat)
                        val mes: String
                        val dia: String

                        if (convertedDate.monthValue < 10)
                            mes = "0" + convertedDate.monthValue
                        else
                            mes = convertedDate.monthValue.toString()

                         periodo = convertedDate.year.toString()+ "-" + mes

                        if (i.U_VS_AGR_ESTA == "P" && i.U_VS_AGR_CDLC == item) {

                            hr += i.U_VS_AGR_TOHX.toDouble()
                            linea = linea+i.LineId + ","
                            hj += i.U_VS_AGR_TOJR
                        }
                    }

                    estados.add(item)
                    periodos.add(periodo)
                    hrx.add(hr)
                    hrj.add(hj)
                    lineas.add(linea.dropLast(1))
                }

                    Log.d("CONTABILIZACION" , estados.toString())
                Log.d("CONTABILIZACION" , periodos.toString())
                Log.d("CONTABILIZACION" , hrx.toString())
                Log.d("CONTABILIZACION" , lineas.toString())



                for((index,i) in estados.withIndex())
                {
                    if(hrx[index]!=0.0) {
                        for ((ind, item) in verificarList.ProductionOrderLines.withIndex()) {

                            if (i == item.U_VS_AGR_CDRA && item.ItemNo == labores_HRXT[index] &&  item.U_VS_AGR_CDPE == periodos[index] && item.U_VS_AGR_HRXT == "Y") {
                                /*
                                if (item.U_VS_AGR_CDPE == periodos[index]) {
                                    if (item.U_VS_AGR_HRXT == "Y") {*/
                                        body5 = JsonObject()
                                        body5.addProperty("PlannedQuantity", item.PlannedQuantity + hrx[index].toDouble())
                                        body5.addProperty("LineNumber", item.LineNumber)
                                        body3.add(body5)
                                        break
                                   /* } else {
                                        body5 = JsonObject()
                                        body5.addProperty("PlannedQuantity", hrx[index].toDouble())
                                        body5.addProperty("U_VS_AGR_HRXT", "Y")
                                        body5.addProperty("U_VS_AGR_CDPE", periodos[index])
                                        body5.addProperty("U_VS_AGR_CDRA", i)
                                        body5.addProperty("U_VS_AGR_OBAG", "VSAGRLACA")
                                        body5.addProperty("ItemType", "pit_Resource")
                                        body5.addProperty("ProductionOrderIssueType", "im_Manual")
                                        body5.addProperty("ItemNo", labores_HRXT[index])
                                        body5.addProperty("LineNumber", ind+1)

                                        body3.add(body5)
                                        break


                                    }
                                } else {
                                    if (verificarList.ProductionOrderLines.size == ind + 1) {
                                        body5 = JsonObject()
                                        Log.d("CONTABILIZAR", item.PlannedQuantity.toString())
                                        body5.addProperty("PlannedQuantity", hrx[index].toDouble())
                                        body5.addProperty("U_VS_AGR_CDPE", periodos[index])
                                        body5.addProperty("U_VS_AGR_HRXT", "Y")
                                        body5.addProperty("U_VS_AGR_CDRA", i)
                                        body5.addProperty("U_VS_AGR_OBAG", "VSAGRLACA")
                                        body5.addProperty("ItemType", "pit_Resource")
                                        body5.addProperty("ProductionOrderIssueType", "im_Manual")
                                        body5.addProperty("ItemNo", labores_HRXT[index])
                                        body5.addProperty("LineNumber", ind+1)
                                        body3.add(body5)
                                        break
                                    }


                                }*/
                            } else {
                                if (verificarList.ProductionOrderLines.size == ind + 1) {
                                    body5 = JsonObject()
                                    body5.addProperty("PlannedQuantity", hrx[index].toDouble())
                                    body5.addProperty("U_VS_AGR_CDPE", periodos[index])
                                    body5.addProperty("U_VS_AGR_HRXT", "Y")
                                    body5.addProperty("U_VS_AGR_CDRA", i)
                                    body5.addProperty("U_VS_AGR_OBAG", "VSAGRLACA")
                                    body5.addProperty("ItemType", "pit_Resource")
                                    body5.addProperty("ProductionOrderIssueType", "im_Manual")
                                    body5.addProperty("ItemNo", labores_HRXT[index])
                                    body5.addProperty("LineNumber", ind+1)

                                    body3.add(body5)

                                    break
                                }

                            }


                        }
                    }

                }
                body.add("ProductionOrderLines",body3)
                body.addProperty("U_VS_AGR_ORGN","AGR")
                Log.d("CONTABILIZACIONNUEVA1",body.toString())
                Log.d("CONTABILIZACIONNUEVA1",DocEntryPEP.toString())

                pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.validarPatch(it,DocEntryPEP,body) }



            }})

        laborViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = it

            }

        })
        laborViewModels.messageResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it)
                startActivityForResult(Intent(this@laborCulturalDetalleActivity, AlertActivity::class.java), REQUEST_ACTIVITY_ERROR_CONTABILIZAR)

            }
        })
        laborViewModels.messageValidarLiveData.observe(this, Observer {
            it?.let {
                Log.d("CONTABILIZAR","CONTABILIZARPASO2")

                //ContabilizarPaso2()
            pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.Revalidar(it,DocEntryPEP) }

            }
        })

        laborViewModels.laborPorCodeResultDelete.observe(this, Observer {
            it?.let {
                if (LineId != 0) {
                    var detalle: ArrayList<LaborCulturalDetalleResponse> =
                        it.VS_AGR_DCULCollection as ArrayList<LaborCulturalDetalleResponse>

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

                            body3.add(body5)
                        }
                    }
                    body.add("VS_AGR_DCULCollection", body3)

                    ActualizarLabor(body, "3") //eliminar

                }
            }
        })

        laborViewModels.messageUpdateResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivityForResult(Intent(this@laborCulturalDetalleActivity, AlertActivity::class.java), REQUEST_ACTIVITY)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d("ELIMINAR", "ELIMINAR2")
            }
        })
    }
    fun ActualizarLabor(body: JsonObject, Estado: String)
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.actualizarLaborCultural(it, DocEntry.toString(), body, Estado) }

    }

    @SuppressLint("ResourceAsColor")
    fun tableDinamica()
    {
        Log.d("VALORTABLA", laborDes.size.toString())
        var tableTipo = TableLayout(this)
        tableTipo.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        tableTipo.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        tableTipo.setPadding(10, 5, 8, 0)

        var index = 0
        var item = 0
        var row =0
        var prueba = false

        while(index<laborDes.size)
        {
            var tipoMenu = TableRow(this)
            tipoMenu.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT))

            tipoMenu.setPadding(10, 0, 0, 0)
            if(jornales.get(index)!=0 || hrs.get(index)!=0.0) {
                while (item <= 3) {
                    if (item == 0) {
                        var texto = TextView(this)
                        texto.setLayoutParams(TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f))
                        texto.setText(laborDes.get(index))
                        texto.setPadding(10, 10, 0, 10)
                        texto.setTextColor(R.color.black)
                        texto.textSize = 12F
                        texto.setTypeface(null, Typeface.BOLD)
                        texto.gravity = Gravity.LEFT
                        texto.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto)
                    }

                    if (item == 1) {
                        var texto2 = TextView(this)
                        texto2.setLayoutParams(TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
                        texto2.setText(jornales.get(index).toString())
                        Log.d("VALORTABLA", jornales.get(index).toString())
                        texto2.setPadding(0, 10, 0, 10)
                        texto2.setTypeface(null, Typeface.BOLD)
                        texto2.gravity = Gravity.CENTER_HORIZONTAL

                        texto2.setTextColor(R.color.black)
                        texto2.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto2)

                    }

                    if (item == 2) {
                        var texto3 = TextView(this)
                        texto3.setLayoutParams(TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
                        texto3.setText(hrs.get(index).toString())
                        texto3.setPadding(0, 10, 0, 10)
                        texto3.setTextColor(R.color.black)
                        texto3.setTypeface(null, Typeface.BOLD)
                        texto3.gravity = Gravity.CENTER_HORIZONTAL

                        texto3.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto3)

                    }
                    item++

                }
            }

            index++
            item=0
            tableTipo.addView(tipoMenu);

        }
        tbLayout.addView(tableTipo)

    }
    fun hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    fun setUpViews()
    {
        rlLaborDetalle.setOnClickListener {
            hideSoftKeyboard()
        }
        ivPersonalAdd.setOnClickListener {

            /*startActivity(Intent(this@personalFragment, personalAdd::class.java))
            finish()*/
            var gson =  Gson()
            var jsonClass = gson.toJson(personalExistente)

            val intent = Intent(this, laborCulturalPersonalActivity::class.java)
            intent.putExtra("CODIGOPEP", CodigoPEP)
            intent.putExtra("CULTIVO", Cultivo)
            intent.putExtra("CAMPANIA", Campania)
            intent.putExtra("FECHA", Fecha)
            intent.putExtra("ETAPA", Etapa)
            intent.putExtra("TIPOCAMPANIA", TipoCampania)
            intent.putExtra("DOCENTRY", DocEntry)

            intent.putExtra("ESTADO", 1)

            startActivityForResult(intent, REQUEST_ACTIVITY)

        }
        ivSincronizacion.setOnClickListener {
            if(checkInternet()) {
                val intent = Intent(this, ContabilizadorActivity::class.java)
                startActivityForResult(intent, REQUEST_ACTIVITY_MESSAGE)
            }else{
                pref.saveString(Constants.MESSAGE_ALERT, "No tienes acceso a internet")
                startActivityForResult(Intent(this@laborCulturalDetalleActivity, AlertActivity::class.java), REQUEST_ACTIVITY_ERROR_CONTABILIZAR)
            }

           // Validar()
        }
        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                filter(s.toString());

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        llCabecera.setOnClickListener {
            when(tvCampania.visibility)
            {
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
    }
    fun Validar()
    {
        Log.d("CONTABILIZACIONPROCESO","CONTABILIZARPASO1")
        Log.d("CONTABILIZACIONPROCESO",DocEntryPEP.toString())

        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.validar(it,DocEntryPEP) }

    }
    private fun LaboresListar() {
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarLabor(it,httpCacheDirectory, this) }
    }
    private fun filter(s: String)
    {
        val filter: java.util.ArrayList<LaborCulturalDetalleResponse> = java.util.ArrayList()

        for (item in laborPorCodeDetalleList) {
            if (item.U_VS_AGR_CDPS.toLowerCase().contains(s.toLowerCase()) ||item.Nombre.toLowerCase().contains(s.toLowerCase()) ) {
                filter.add(item)
            }
        }
        rvLaborCultural.adapter = (laborCulturalDetalleAdapter(this, filter))
        rvLaborCultural.layoutManager = LinearLayoutManager(this)
    }
    private fun LaborListar() {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listaLaborCultural(it, CodigoPEP,Campania,httpCacheDirectory,this) }
    }

    override fun laborItemClickListener(position: LaborCulturalDetalleResponse?) {
        var gson =  Gson()
        var jsonClass = gson.toJson(position)
        var tipoCampania:String =""

        val intent = Intent(this, laborCulturalPersonalActivity::class.java)
        intent.putExtra("CODIGOPEP", CodigoPEP)
        intent.putExtra("CULTIVO", Cultivo)
        intent.putExtra("CAMPANIA", Campania)
        intent.putExtra("FECHA", Fecha)
        intent.putExtra("ETAPA", Etapa)
        intent.putExtra("TIPOCAMPANIA", TipoCampania)
        intent.putExtra("OBJECTPERSONA", jsonClass)

        intent.putExtra("ESTADO", 0)

        startActivity(intent)
    }
    override fun laborItemDeletelickListener(position: LaborCulturalDetalleResponse?) {
        if(checkInternet()) {
            if (position != null) {
                LineId = position.LineId
            }
            pref.saveString(Constants.MESSAGE_ALERT, "¿Desea eliminar esta la labor cultural?")
            startActivityForResult(
                Intent(
                    this@laborCulturalDetalleActivity,
                    AlertActivity::class.java
                ), REQUEST_ACTIVITY_ELIMINAR
            )
        }else
        {
            val builder =  android.app.AlertDialog.Builder(this)

            builder.setTitle("Eliminar labor")
            builder.setMessage("Operación no permitida de modo offline.")
            builder.show()
        }
    }
    override fun laborItemUpdateClickListener(position: LaborCulturalDetalleResponse?) {
        var gson =  Gson()
        var jsonClass = gson.toJson(position)
        var tipoCampania:String =""

        val intent = Intent(this, laborCulturalPersonalActivity::class.java)
        intent.putExtra("CODIGOPEP", CodigoPEP)
        intent.putExtra("DOCENTRY", DocEntry)

        intent.putExtra("CULTIVO", Cultivo)
        intent.putExtra("CAMPANIA", Campania)
        intent.putExtra("FECHA", Fecha)
        intent.putExtra("ETAPA", Etapa)
        intent.putExtra("TIPOCAMPANIA", TipoCampania)
        intent.putExtra("OBJECTPERSONA", jsonClass)
        intent.putStringArrayListExtra("OBJECT", personalExistente)

        intent.putExtra("ESTADO", 2)
        if (position != null) {
            intent.putExtra("LINEID", position.LineId)
        }

        startActivityForResult(intent, REQUEST_ACTIVITY)
    }

    override fun onBackPressed() {
        Log.d("arrow", "arrow")
        val myIntent = Intent(applicationContext, laborCulturalActivity::class.java)
        myIntent.putExtra("CODIGOPEP", CodigoPEP)
        myIntent.putExtra("CAMPAÑA", Campania)
        myIntent.putExtra("CULTIVO", Cultivo)
        myIntent.putExtra("VARIEDAD", Variedad)
        myIntent.putExtra("CODIGOCAMPANIA", CodCampania)
        myIntent.putExtra("TIPOCAMPANIA", TipoCampania)
        myIntent.putExtra("DOCENTRYPEP", DocEntryPEP)


        startActivity(myIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("RELOAD", resultCode.toString())
        Log.d("RELOAD", requestCode.toString())

        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                   startActivity(intent)
                    LaboresListar()

                }
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(intent)
                }

            }
            REQUEST_ACTIVITY_ELIMINAR -> {
                if (resultCode == Activity.RESULT_CANCELED) {


                }
                if (resultCode == Activity.RESULT_OK) {
                    pref.getString(Constants.B1SESSIONID)?.let {
                        laborViewModels.listaDetalleLaborCulturalDelete(
                            it,
                            DocEntry.toString(),
                            httpCacheDirectory,
                            this
                        )
                    }
                }

            }
            REQUEST_ACTIVITY_ERROR_CONTABILIZAR  -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                }
                if (resultCode == Activity.RESULT_OK) {
                }

            }
            REQUEST_ACTIVITY_CONTABILIZAR  -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    startActivity(intent)
                }
                if (resultCode == Activity.RESULT_OK) {
                    startActivity(intent)
                }

            }
            REQUEST_ACTIVITY_MESSAGE  -> {
                if (resultCode == Activity.RESULT_CANCELED) {

                }
                if (resultCode == Activity.RESULT_OK) {
                    val intent = Intent(this, ContabilizadorConfirmarActivity::class.java)
                    startActivityForResult(intent,REQUEST_ACTIVITY_CONFIRMACION)
                }

            }
            REQUEST_ACTIVITY_CONFIRMACION  -> {
                if (resultCode == Activity.RESULT_CANCELED) {

                }
                if (resultCode == Activity.RESULT_OK) {
                    pgbLaborRealizada.visibility = View.VISIBLE
                    Validar()
                }

            }
        }
    }

}

