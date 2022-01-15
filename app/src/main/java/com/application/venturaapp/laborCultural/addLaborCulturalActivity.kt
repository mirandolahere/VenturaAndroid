package com.application.venturaapp.laborCultural

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.DatePickerFragment
import com.application.venturaapp.laborCultural.entity.EtapaProduccionListResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.tables.LaborCulturalRoom
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_add_labor_cultural.*
import kotlinx.android.synthetic.main.activity_add_labor_cultural.etFecha
import kotlinx.android.synthetic.main.activity_personal_add.*
import okhttp3.Cache
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class addLaborCulturalActivity  : AppCompatActivity()  {

    private var CodigoPEP = ""
    private var Campania = ""
    private var CodigoCampania = ""
    private var TipoCampania = ""
    private var DocEntryPEP = ""

    private var Cultivo = ""
    private var Variedad = ""
    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborCulturaViewModel
    lateinit var etapa  : ArrayList<String>
    lateinit var etapaList  : ArrayList<EtapaProduccionListResponse>
    var CodeEtapa: String = ""
    var FechaIni: String = ""
    var FechaFin: String = ""

    var DescripcionEtapa: String = ""
    lateinit var httpCacheDirectory : Cache
    
    var fecha:String =""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_add_labor_cultural)
        pref = PreferenceManager(this)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)

        initViews()
        SetupView()
        setUpObservers()
        EtapaListar()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun setUpObservers(){

        laborViewModels.messageResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivity(Intent(this, AlertActivity::class.java))
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        })
        laborViewModels.etapaResult.observe(this, Observer {
            it?.let {
                etapaList = it as ArrayList<EtapaProduccionListResponse>
                etapa = java.util.ArrayList<String>()
                for (item in it) {
                    etapa.add(item.U_VS_AGR_DSEP)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }
               // Spinner()
            }
        })

        laborViewModels.laborResponseResult.observe(this, Observer {
            it?.let {
                val intent = Intent(this, laborCulturalDetalleActivity::class.java)
                intent.putExtra("CODIGOPEP", CodigoPEP)
                intent.putExtra("CAMPANIA", Campania)
                intent.putExtra("CULTIVO", Cultivo)
                intent.putExtra("FECHA", it.U_VS_AGR_FERG)
                intent.putExtra("ETAPA", it.U_VS_AGR_CDEP)
                intent.putExtra("TIPOCAMPANIA", TipoCampania)
                intent.putExtra("DOCENTRY", it.DocEntry)
                intent.putExtra("VARIEDAD", Variedad)
                intent.putExtra("CODIGOCAMPANIA", CodigoCampania)
                intent.putExtra("DOCENTRYPEP", DocEntryPEP)


                startActivity(intent)
            }
        })

        laborViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = it
            }
        })

        laborViewModels.laborResult.observe(this, Observer {
            it?.let {
                var list : List<LaborCulturalRoom>? =  laborViewModels.getLaborRoom()
                

                val labor = LaborCulturalRoom(0,
                    fecha,
                    CodigoCampania,
                    CodigoPEP,
                    CodeEtapa,"Y","AP"

                )
                if(validarExistente(it, list, labor))
                {
                    val builder =  AlertDialog.Builder(this)

                    builder.setTitle("¡Error!")
                    builder.setMessage("La labor cultural ya se encuentra registrada.")
                    builder.show()

                }else {
                    laborViewModels.insertLaborRoom(labor)
                    var returnid: List<LaborCulturalRoom>? = laborViewModels.getLaborDetalleRoomID()
                    //labor.id
                    pref.saveString(Constants.MESSAGE_ALERT, "Se guardó en la memoria interna.")
                    val intent = Intent(this, laborCulturalDetalleActivity::class.java)
                    intent.putExtra("CODIGOPEP", CodigoPEP)
                    intent.putExtra("CAMPANIA", Campania)
                    intent.putExtra("CULTIVO", Cultivo)
                    intent.putExtra("FECHA", fecha)
                    intent.putExtra("ETAPA", CodeEtapa)
                    intent.putExtra("TIPOCAMPANIA", TipoCampania)
                    returnid?.get(0)?.let { it1 -> intent.putExtra("DOCENTRY", it1.id) }
                    intent.putExtra("VARIEDAD", Variedad)
                    intent.putExtra("CODIGOCAMPANIA", CodigoCampania)
                    intent.putExtra("DOCENTRYPEP", DocEntryPEP)


                    startActivity(intent)
                }

            }
        })
    }

    private fun validarExistente(it: List<LaborCulturalListResponse>, list: List<LaborCulturalRoom>?, labor: LaborCulturalRoom): Boolean {

        if (list != null) {
            for(item in list)
            {
                if(labor.U_VS_AGR_ACTV == item.U_VS_AGR_ACTV && labor.U_VS_AGR_CDCA ==item.U_VS_AGR_CDCA &&
                    labor.U_VS_AGR_CDEP == item.U_VS_AGR_CDEP && labor.U_VS_AGR_CDPP == item.U_VS_AGR_CDPP &&
                    labor.U_VS_AGR_FERG == item.U_VS_AGR_FERG)
                {
                    return  true
                }
            }
        }

        for(item in it)
        {
            if(labor.U_VS_AGR_ACTV == item.U_VS_AGR_ACTV && labor.U_VS_AGR_CDCA ==item.U_VS_AGR_CDCA &&
                    labor.U_VS_AGR_CDEP == item.U_VS_AGR_CDEP && labor.U_VS_AGR_CDPP == item.U_VS_AGR_CDPP &&
                    labor.U_VS_AGR_FERG == item.U_VS_AGR_FERG)
                {
                    return  true

                }
        }

        return false
    }

   /* fun Spinner() {
        val adapterEtapa = ArrayAdapter(
                this,
                R.layout.spinner, etapa
        )

        adapterEtapa.setDropDownViewResource(R.layout.spinner_list)
        spEtapa.adapter = adapterEtapa
        spEtapa.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
            ) {

                DescripcionEtapa = etapa.get(position)
                Log.d("JSONPERSONAL",DescripcionEtapa)

                for( item in etapaList)
                {
                    if(item.U_VS_AGR_DSEP == DescripcionEtapa)
                    {
                        CodeEtapa = item.U_VS_AGR_CDEP
                        FechaIni = item.U_VS_AGR_FEIP
                        FechaFin = item.U_VS_AGR_FEFP

                        Log.d("JSONPERSONAL",CodeEtapa)

                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    } */
    private fun EtapaListar() {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listEtapa(it,CodigoCampania,httpCacheDirectory, this) }
    }

    fun initViews() {
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo_isotipo)
        CodigoPEP = intent.getSerializableExtra("CODIGOPEP").toString()
        Campania = intent.getSerializableExtra("CAMPANIA").toString()
        Cultivo = intent.getSerializableExtra("CULTIVO").toString()
        Variedad = intent.getSerializableExtra("VARIEDAD").toString()
        CodigoCampania = intent.getSerializableExtra("CODIGOCAMPANIA").toString()
        TipoCampania = intent.getSerializableExtra("TIPOCAMPANIA").toString()
        DocEntryPEP = intent.getSerializableExtra("DOCENTRYPEP").toString()


        etCampania.setText(Campania)
        etCultivo.setText(Cultivo)
        etCodigoPEP.setText(CodigoPEP)
        etVariedad.setText(Variedad)

        /* var peopleClass = gson.fromJson(people.toString(), PersonalDato::class.java)
        if (peopleClass != null && accion == 1) {
            MostrarDatos(peopleClass)
        }
        if (peopleClass != null && accion == 2) {
            ActualizarDatos(peopleClass)

        }
    }
    if(intent.getSerializableExtra("ACCION") == 0) {
        Nuevo()
    }*/

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun SetupView()
    {
        etFecha.setOnClickListener {
            showDatePickerDialog()
        }
        addLabor.setOnClickListener {

            var completo = true
            val body = JsonObject()
             fecha=""
                    if (etFecha.text.toString() != "") {
                val date = LocalDate.parse(
                    etFecha.text,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                )
                var mes:String = date.monthValue.toString()
                var dia:String = date.dayOfMonth.toString()
                if(date.monthValue<10)
                {
                  mes = "0"+date.monthValue
                }

                if(date.dayOfMonth<10)
                {
                    dia = "0"+date.dayOfMonth
                }
                 fecha =
                    date.year.toString() + "-" + mes + "-" + dia
            }

            if (etFecha.text.toString() != "") {
                val date = LocalDate.parse(
                    etFecha.text,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                )

                val fecha: String =
                    date.year.toString() + "-" + date.monthValue + "-" + date.dayOfMonth.toString()
                body.addProperty("U_VS_AGR_FERG", fecha)
            } else {
                etFecha.setBackgroundResource(R.drawable.edittext_border_error)
                completo = false
            }
            if (checkInternet())
            {
                body.addProperty("U_VS_AGR_CDCA", CodigoCampania)
                body.addProperty("U_VS_AGR_CDPP", CodigoPEP)
                body.addProperty("U_VS_AGR_CDEP", CodeEtapa)
                body.addProperty("U_VS_AGR_ACTV", "Y")
                body.addProperty("U_VS_AGR_RORI", "AP")

                Log.d("JSONREQUEST", body.toString())
                if (completo)
                    pref.getString(Constants.B1SESSIONID)
                        ?.let { laborViewModels.addLabor(it, body) }
            }
            else
            {
                if(validarFecha() && completo) {

                    validarLaborOf()

                }
            }



        }
    }
    fun validarLaborOf()
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listaLaborCultural(it, CodigoPEP,Campania,httpCacheDirectory, this) }

       
    }
    @SuppressLint("SimpleDateFormat")
    fun validarFecha():Boolean
    {
        var dateIni: Date? = null
        var dateFin: Date? = null
        val format = SimpleDateFormat("yyyy-MM-dd")
        try {
             dateIni = format.parse(FechaIni)
            dateFin = format.parse(FechaFin)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("Fechaingresada", dateIni.toString())
        Log.d("Fechaingresada", dateFin.toString())

        val format2 = SimpleDateFormat("dd-MM-yyy")
        try {
            var select = etFecha.text.toString()
            val dateSelected: Date = format2.parse(select)

            Log.d("Fechaingresada", dateSelected.toString())

            if((dateSelected.after(dateIni)|| dateSelected.equals(dateIni)) && ((dateSelected.before(dateFin)|| dateSelected.equals(dateFin))) )
            {
                return true
            }else
            {
                val builder =  AlertDialog.Builder(this)

                builder.setTitle("¡Error!")
                builder.setMessage("La fecha ingresa no esta permitida.")
                builder.show()
                return false
            }

            return true
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return true

    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 because January is zero
            var mes = ""
            var dia =""
            if(month<9)
            {
                mes = "0"+ (month+1)
            }else
            {
                mes = (month+1).toString()
            }

            if(day<10)
            {
                dia ="0"+day
            }else
            {
                dia=day.toString()
            }
            val selectedDate = "$dia-$mes-$year"
            etFecha.setText(selectedDate)
            filterEtapa("$year-$mes-$dia")
        }

        )

        newFragment.show(supportFragmentManager, "datePicker")

    }

    fun filterEtapa(param: String) {

        val format = SimpleDateFormat("yyyy-MM-dd")
        format.parse(param)
        spEtapa.setText("")
        for( item in etapaList)
        {
            if((format.parse(item.U_VS_AGR_FEIP)<= format.parse(param))
                && (format.parse(param) <= format.parse(item.U_VS_AGR_FEFP)))
            {
                CodeEtapa = item.U_VS_AGR_CDEP
                FechaIni = item.U_VS_AGR_FEIP
                FechaFin = item.U_VS_AGR_FEFP

                spEtapa.setText(item.U_VS_AGR_DSEP)

            }
        }

    }

}