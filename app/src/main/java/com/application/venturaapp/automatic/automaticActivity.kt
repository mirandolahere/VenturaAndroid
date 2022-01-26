package com.application.venturaapp.automatic

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.DatePickerFragment
import com.application.venturaapp.home.fragment.entities.Campania
import com.application.venturaapp.home.fragment.entities.VS_AGR_CAEPCollection
import com.application.venturaapp.home.fragment.entities.VS_AGR_CAPPCollection
import com.application.venturaapp.home.fragment.produccion.laborViewModel
import com.application.venturaapp.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_automatic.*
import kotlinx.android.synthetic.main.activity_automatic.etFecha
import kotlinx.android.synthetic.main.activity_automatic.tbLayout
import okhttp3.Cache
import kotlin.collections.ArrayList
import android.widget.LinearLayout
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.VSAGRDPCLResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_automatic.addPep
import kotlinx.android.synthetic.main.activity_automatic.etJornales
import kotlinx.android.synthetic.main.activity_automatic.etLaborDefecto
import kotlinx.android.synthetic.main.activity_cosecha_articulo.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class automaticActivity : AppCompatActivity() {

    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborViewModel
    lateinit var laborCulturalViewModels: laborCulturaViewModel

    lateinit var httpCacheDirectory : Cache
    lateinit var broadcast : BroadcastReceiver
    lateinit var campanias  : ArrayList<String>
    var campaniasList  = arrayListOf<Campania>()
    var CodeCampania: String = ""
    var Descripcion: String = ""

    lateinit var labor  : ArrayList<String>
    var laborPlanList  = arrayListOf<VSAGRDPCLResponse>()
    var CodeLabor: String = ""
    var DescripcionLabor: String = ""

    lateinit var etapas  : ArrayList<String>
    var etapasList  = arrayListOf<VS_AGR_CAEPCollection>()
    var CodeEtapa: String = ""
    var DescripcionEtapa: String = ""

    lateinit var peps  : ArrayList<String>
    var pepList  = arrayListOf<VS_AGR_CAPPCollection>()

    var pepListSelected  = arrayListOf<VS_AGR_CAPPCollection>()
    var epListSelected = arrayListOf<VS_AGR_CAPPCollection>()
    var labListSelected = arrayListOf<LaborCulturalDetalleResponse>()

    var laborListSelected  = arrayListOf<LaborCulturalDetalleResponse>()


    var CodePEP: String = ""
    var DescripcionPEP: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_automatic)
        pref = PreferenceManager(this)
        laborViewModels = ViewModelProviders.of(this).get(laborViewModel::class.java)
        laborCulturalViewModels= ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        campaniaListar()
        setUpObservers()
        setUpView()
        /* initViews()
         SetupView()
         setUpObservers()
         EtapaListar()*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private fun setUpView() {
            etInicio.setOnClickListener{
            val mcurrentTime: Calendar = Calendar.getInstance()
             val hour= mcurrentTime.get(Calendar.HOUR_OF_DAY)
             val minute= mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->
                val  hourSelect = selectedHour
                val minuteSelect = selectedMinute
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
        etFecha.setOnClickListener {
            showDatePickerDialog()
        }

        etFechaLote.setOnClickListener {
            showDatePickerDialog()
        }

        llPepSelection.setOnClickListener {
            llPep.visibility = View.VISIBLE
            llLabores.visibility = View.GONE
            llPepSelection.setBackgroundColor(R.color.celesteVentura)
            tvPepSelection.setTextColor(R.color.white)
            llLaborSelection.setBackgroundColor(R.color.marcaHeader)
            tvLaborSelection.setTextColor(R.color.plomo)


        }
        llLaborSelection.setOnClickListener {

            llPep.visibility = View.GONE
            llLabores.visibility = View.VISIBLE
            llLaborSelection.setBackgroundColor(R.color.celesteVentura)
            tvLaborSelection.setTextColor(R.color.white)
            llPepSelection.setBackgroundColor(R.color.marcaHeader)
            tvPepSelection.setTextColor(R.color.plomo)



        }

        addPep.setOnClickListener {
            for(item in pepList ){
                for(select in pepListSelected) {
                    if (CodePEP == item.U_VS_AGR_CDPP && select.U_VS_AGR_CDPP != CodePEP) {
                        pepListSelected.add(item)
                    }
                }
            }
            tbLayout.removeAllViews()
            tableDinamica(pepListSelected)
        }
        addLabores.setOnClickListener {
            if (pepListSelected.size > 0) {
                for (item in laborPlanList) {
                    if (CodeLabor == item.U_VS_AGR_CDLC) {
                        var labor = LaborCulturalDetalleResponse(
                            0,
                            0,
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            CodeLabor,
                            "",
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
                            0.toFloat()

                        )
                        laborListSelected.add(labor)
                    }
                }
                tbLayoutLabor.removeAllViews()
                tableDinamicaLabor(laborListSelected)
            } else {
                Toast.makeText(this, "Debe agregar un PEP", Toast.LENGTH_SHORT).show()
            }
        }
        btnAutomatic.setOnClickListener {
            if(pepListSelected.size>0) {
                for (item in pepListSelected)
                    AddLaborHead(item)
            }else
                Toast.makeText(this, "Debe agregar un PEP", Toast.LENGTH_SHORT).show()

        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun  AddLaborHead(item: VS_AGR_CAPPCollection) {
        val body = JsonObject()
        var fechaAutomatic=""
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
            fechaAutomatic =
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
        }

        body.addProperty("U_VS_AGR_CDCA", CodeCampania)
        body.addProperty("U_VS_AGR_CDPP", item.U_VS_AGR_CDPP)
        body.addProperty("U_VS_AGR_CDEP", CodeEtapa)
        body.addProperty("U_VS_AGR_ACTV", "Y")
        body.addProperty("U_VS_AGR_RORI", "AP")

        pref.getString(Constants.B1SESSIONID)
                ?.let { laborCulturalViewModels.addLabor(it, body) }
    }

    private fun setUpObservers() {
        laborCulturalViewModels.laborResponseResult.observe(this, Observer {

        })
        laborViewModels.campaniaResult.observe(this, Observer {
            it?.let {
                campanias = ArrayList<String>()
                campaniasList = it as ArrayList<Campania>
                for (item in it) {
                    campanias.add(item.Name)
                }

                Spinner()
            }
        })
        laborCulturalViewModels.responseLaborPlanificadaResult.observe(this, Observer {
            it?.let {
                labor = java.util.ArrayList<String>()
                laborPlanList = it as ArrayList<VSAGRDPCLResponse>
                for (item in it) {
                    labor.add(item.U_VS_AGR_CDLC)
                }

                SpinnerLabor()
            }
        })
    }
    fun SpinnerLabor()
    {
        val adapterLabor =
            ArrayAdapter(
                this,
                R.layout.spinner, labor
            )


        if (adapterLabor != null) {
            adapterLabor.setDropDownViewResource(R.layout.spinner_list)
            etLaborDefecto.adapter = adapterLabor

            etLaborDefecto.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    DescripcionLabor = labor.get(position)

                    for( (index,item) in laborPlanList.withIndex())
                    {
                        if(item.U_VS_AGR_CDLC == DescripcionLabor)
                        {
                            CodeLabor = item.U_VS_AGR_CDLC

                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }



    }
    fun Spinner()
    {
        val adapter =
            ArrayAdapter(
                this,
                R.layout.spinner, campanias
            )


        if (adapter != null) {
            adapter.setDropDownViewResource(R.layout.spinner_list)
            splCampanias.adapter = adapter

            splCampanias.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    Descripcion = campanias.get(position)

                    for( (index,item) in campaniasList.withIndex())
                    {
                        if(item.Name == Descripcion)
                        {
                            CodeCampania = item.Code
                           // SpinnerEtapa(campaniasList[index].VS_AGR_CAEPCollection)
                            SpinnerPEP(campaniasList[index].VS_AGR_CAPPCollection)
                            etapasList = campaniasList[index].VS_AGR_CAEPCollection as ArrayList<VS_AGR_CAEPCollection>

                            break
                        }else{
                            CodeCampania = Descripcion
                        }

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }



    }
  /*  private fun  SpinnerEtapa(vsAgrCaepcollection: List<VS_AGR_CAEPCollection>) {

        etapas = ArrayList<String>()
        etapasList = vsAgrCaepcollection as ArrayList<VS_AGR_CAEPCollection>
        for (item in vsAgrCaepcollection) {
            etapas.add(item.U_VS_AGR_DSEP)
        }

        val adapterEtapa =
            ArrayAdapter(
                this,
                R.layout.spinner, etapas
            )


        if (adapterEtapa != null) {
            adapterEtapa.setDropDownViewResource(R.layout.spinner_list)
            spEtapa.adapter = adapterEtapa

            spEtapa.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    DescripcionEtapa = etapas.get(position)

                    for( (index,item) in etapasList.withIndex())
                    {
                        if(item.U_VS_AGR_DSEP == DescripcionEtapa)
                        {
                            CodeEtapa = item.U_VS_AGR_CDEP
                            break
                        }else{
                            CodeEtapa = DescripcionEtapa
                        }

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

    } */
    private fun  SpinnerPEP(vsAgrCaepcollection: List<VS_AGR_CAPPCollection>) {

        peps = ArrayList<String>()
        pepList = vsAgrCaepcollection as ArrayList<VS_AGR_CAPPCollection>
        for (item in vsAgrCaepcollection) {
            peps.add(item.U_VS_AGR_DSPP)
        }

        val adapterPep =
            ArrayAdapter(
                this,
                R.layout.spinner, peps
            )


        if (adapterPep != null) {
            adapterPep.setDropDownViewResource(R.layout.spinner_list)
            spPEP.adapter = adapterPep

            spPEP.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    DescripcionPEP = peps.get(position)

                    for( (index,item) in pepList.withIndex())
                    {
                        if(item.U_VS_AGR_DSPP == DescripcionPEP)
                        {
                            CodePEP = item.U_VS_AGR_CDPP
                            break
                        }else{
                            CodePEP = DescripcionPEP
                        }

                    }
                    LaborListar(CodePEP, CodeEtapa)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

    }
    private fun campaniaListar() {
        pref.getString(Constants.B1SESSIONID)?.let {
            it1 ->
                laborViewModels.consultarCampania(
                    it1,
                    httpCacheDirectory,
                    this
                )

        }
    }
    private fun LaborListar(CodigoPEP:String, Etapa:String) {

        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.laborPlanificada(
            it,
            CodigoPEP,
            Etapa,
            httpCacheDirectory,
            this
        ) }
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
            val selectedDate = dia + "-" + mes + "-" + year
            etFecha.setText(selectedDate)
            filterEtapa("$year-$mes-$dia")

        }

        )

        newFragment.show(supportFragmentManager, "datePicker")

    }


    fun filterEtapa(param: String) {

        val format = SimpleDateFormat("yyyy-MM-dd")
        format.parse(param)
        edEtapa.setText("")
        for( item in etapasList)
        {
            if((format.parse(item.U_VS_AGR_FEIP)<= format.parse(param))
                && (format.parse(param) <= format.parse(item.U_VS_AGR_FEFP)))
            {
                CodeEtapa = item.U_VS_AGR_CDEP

                edEtapa.setText(item.U_VS_AGR_DSEP)

            }
        }

    }

    @SuppressLint("ResourceAsColor")
    fun tableDinamica(pepListSelected: ArrayList<VS_AGR_CAPPCollection>)
    {
        var tableTipo = TableLayout(this)
        tableTipo.removeAllViews()

        tableTipo.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        tableTipo.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        tableTipo.setPadding(10, 5, 10, 5)

        var index = 0
        var item = 0
        var row =0
        var prueba = false

        while(index<pepListSelected.size)
        {
            var tipoMenu = TableRow(this)
            tipoMenu.setLayoutParams(
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

            tipoMenu.setPadding(10, 5, 10, 5)
                while (item <= 5) {
                    if (item == 0) {
                        var texto = TextView(this)
                        texto.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                        texto.setText(pepListSelected.get(index).U_VS_AGR_DSPP)
                        texto.setPadding(0, 0, 0, 0)
                        texto.setTextColor(R.color.black)
                        texto.textSize = 10F
                        texto.setTypeface(null, Typeface.BOLD)
                        texto.gravity = Gravity.LEFT
                        texto.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto)
                    }

                    if (item == 1) {
                        var texto2 = TextView(this)
                        texto2.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                        texto2.setText(pepListSelected.get(index).U_VS_AGR_DSFD)
                        texto2.setPadding(0, 0, 0, 0)
                        texto2.setTypeface(null, Typeface.BOLD)
                        texto2.gravity = Gravity.CENTER_HORIZONTAL
                        texto2.textSize = 10F
                        texto2.setTextColor(R.color.black)
                        texto2.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto2)

                    }

                    if (item == 2) {
                        var texto3 = TextView(this)
                        texto3.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                        texto3.setText(pepListSelected.get(index).U_VS_AGR_DSSC)
                        texto3.setPadding(0, 0, 0, 0)
                        texto3.setTextColor(R.color.black)
                        texto3.setTypeface(null, Typeface.BOLD)
                        texto3.gravity = Gravity.CENTER_HORIZONTAL
                        texto3.textSize = 10F
                        texto3.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto3)

                    }
                    if (item == 3) {
                        var texto3 = TextView(this)
                        texto3.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                        texto3.setText(pepListSelected.get(index).U_VS_AGR_DSLT)
                        texto3.setPadding(0, 0,0, 0)
                        texto3.setTextColor(R.color.black)
                        texto3.setTypeface(null, Typeface.BOLD)
                        texto3.gravity = Gravity.CENTER_HORIZONTAL
                        texto3.textSize = 10F
                        texto3.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto3)

                    }
                    if (item == 4) {
                        var texto3 = TextView(this)
                        texto3.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                        texto3.setText(pepListSelected.get(index).U_VS_AGR_DNOF.toString())
                        texto3.setPadding(0, 0, 0, 0)
                        texto3.setTextColor(R.color.black)
                        texto3.setTypeface(null, Typeface.BOLD)
                        texto3.gravity = Gravity.CENTER_HORIZONTAL
                        texto3.textSize = 10F
                        texto3.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto3)

                    }
                    if (item == 5) {
                        var image = ImageView(this)
                        image.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f))
                        image.setPadding(10, 0, 10, 0)
                        image.setBackgroundResource(R.drawable.ic_delete)

                        image.layoutParams.width = 20
                        image.layoutParams.height = 40

                        tipoMenu.addView(image)
                        image.setOnClickListener(View.OnClickListener { v ->
                            // Current Row Index
                            val row = v.parent as TableRow
                            val index: Int = tableTipo.indexOfChild(row)
                            deletePep(pepListSelected,pepListSelected[index])

                            // Do what you need to do.
                        })
                    }
                    item++

                }


            index++
            item=0
            tableTipo.addView(tipoMenu);

        }
        tbLayout.addView(tableTipo)

    }

    @SuppressLint("ResourceAsColor")
    fun tableDinamicaLabor(pepListSelected: ArrayList<LaborCulturalDetalleResponse>)
    {
        var tableTipo = TableLayout(this)
        tableTipo.removeAllViews()

        tableTipo.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        tableTipo.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        tableTipo.setPadding(10, 5, 10, 5)

        var index = 0
        var item = 0
        var row =0
        var prueba = false

        while(index<pepListSelected.size)
        {
            var tipoMenu = TableRow(this)
            tipoMenu.setLayoutParams(
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT))

            tipoMenu.setPadding(10, 5, 10, 5)
            while (item <= 5) {
                if (item == 0) {
                    var texto = TextView(this)
                    texto.setLayoutParams(
                        TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                    texto.setText(pepListSelected.get(index).U_VS_AGR_CDLC)
                    texto.setPadding(0, 0, 0, 0)
                    texto.setTextColor(R.color.black)
                    texto.textSize = 10F
                    texto.setTypeface(null, Typeface.BOLD)
                    texto.gravity = Gravity.LEFT
                    texto.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                    tipoMenu.addView(texto)
                }

                if (item == 1) {
                    var texto2 = TextView(this)
                    texto2.setLayoutParams(
                        TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                    texto2.setText(pepListSelected.get(index).U_VS_AGR_HRIN +"-"+pepListSelected.get(index).U_VS_AGR_HRFN )
                    texto2.setPadding(0, 0, 0, 0)
                    texto2.setTypeface(null, Typeface.BOLD)
                    texto2.gravity = Gravity.CENTER_HORIZONTAL
                    texto2.textSize = 10F
                    texto2.setTextColor(R.color.black)
                    texto2.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                    tipoMenu.addView(texto2)

                }

                if (item == 2) {
                    var texto3 = TextView(this)
                    texto3.setLayoutParams(
                        TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                    texto3.setText(pepListSelected.get(index).U_VS_AGR_TOJR.toString())
                    texto3.setPadding(0, 0, 0, 0)
                    texto3.setTextColor(R.color.black)
                    texto3.setTypeface(null, Typeface.BOLD)
                    texto3.gravity = Gravity.CENTER_HORIZONTAL
                    texto3.textSize = 10F
                    texto3.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                    tipoMenu.addView(texto3)

                }
                if (item == 3) {
                    var image = ImageView(this)
                    image.setLayoutParams(
                        TableRow.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f))
                    image.setPadding(10, 0, 10, 0)
                    image.setBackgroundResource(R.drawable.ic_delete)

                    image.layoutParams.width = 10
                    image.layoutParams.height = 20

                    tipoMenu.addView(image)
                    image.setOnClickListener(View.OnClickListener { v ->
                        // Current Row Index
                        val row = v.parent as TableRow
                        val index: Int = tableTipo.indexOfChild(row)
                        deleteLabores(pepListSelected,pepListSelected[index])

                        // Do what you need to do.
                    })

                }

                item++

            }


            index++
            item=0
            tableTipo.addView(tipoMenu);

        }
        tbLayoutLabor.addView(tableTipo)

    }

    private fun deletePep(
        pepListSelected: ArrayList<VS_AGR_CAPPCollection>,
        get: VS_AGR_CAPPCollection
    ) {

        for(item in pepListSelected){
            if(get.LineId != item.LineId){
                epListSelected.add(item)
            }
        }
        tbLayout.removeAllViews()
        tableDinamica(epListSelected)


    }


    private fun deleteLabores(
        pepListSelected: ArrayList<LaborCulturalDetalleResponse>,
        get: LaborCulturalDetalleResponse
    ) {

        for(item in pepListSelected){
            if(get.U_VS_AGR_CDLC != item.U_VS_AGR_CDLC){
                labListSelected.add(item)
            }
        }
        tbLayoutLabor.removeAllViews()
        tableDinamicaLabor(labListSelected)


    }

}