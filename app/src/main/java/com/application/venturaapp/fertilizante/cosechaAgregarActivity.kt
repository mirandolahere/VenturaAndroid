package com.application.venturaapp.fertilizante

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.fertilizante.entity.Almacen
import com.application.venturaapp.fertilizante.entity.UnidadMedida
import com.application.venturaapp.fitosanitario.entity.VSAGRRCOS
import com.application.venturaapp.fitosanitario.entity.VS_AGR_DSCOCollection
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.DatePickerFragment
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.fragment.entities.LaboresPersonal
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.entities.VS_AGR_CAPPCollection
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.VSAGRDPCLResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_cosecha_articulo.*
import kotlinx.android.synthetic.main.activity_cosecha_articulo.addPep
import kotlinx.android.synthetic.main.activity_cosecha_articulo.etCampania
import kotlinx.android.synthetic.main.activity_cosecha_articulo.etCodigoPEP
import kotlinx.android.synthetic.main.activity_cosecha_articulo.etEtapa
import kotlinx.android.synthetic.main.activity_cosecha_articulo.etFecha
import kotlinx.android.synthetic.main.activity_cosecha_articulo.etJornales
import kotlinx.android.synthetic.main.activity_cosecha_articulo.etLaborDefecto
import kotlinx.android.synthetic.main.activity_cosecha_articulo.pgbLaborRealizada
import kotlinx.android.synthetic.main.activity_cosecha_articulo.rlButton
import kotlinx.android.synthetic.main.activity_cosecha_articulo.rlLaborPersonal
import kotlinx.android.synthetic.main.activity_cosecha_articulo.tbLayout
import okhttp3.Cache
import java.util.*
import kotlin.collections.ArrayList
import com.application.venturaapp.laborCultural.entity.EtapaProduccionListResponse
import java.text.SimpleDateFormat


class cosechaAgregarActivity   : AppCompatActivity() {

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var CodeCampania = ""
    private var Fecha = ""
    private var Estado = ""
    private var LineId = ""
    private var CODARTIUCLO = ""
    private var DESCRIPCIONARTICULO = ""
    private var ALMACEN = ""
    private var OPERACION = ""
    private var MEDIDA = ""

    private var idTipoOperacion = ""

    private var listLotes = arrayListOf<VS_AGR_DSCOCollection>()
    private var DocEntry:Int? = 0
    val codeList = ArrayList<String>()
    lateinit var personalViewModels: personalViewModel
    lateinit var personalLista: java.util.ArrayList<PersonalDato>
    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborCulturaViewModel
     var laborList = arrayListOf<LaboresPersonal>()
    lateinit var personalLabor : LaborCulturalDetalleResponse
    var unidadMedida = arrayListOf<String>()
    var unidadMedidaList =  arrayListOf<UnidadMedida>()
    var aLmacenList =  arrayListOf<Almacen>()
    var almacen =  arrayListOf<String>()

    var CodigoMedida = ""
    var CodigoAlmacen = ""

    var CodigoPersonal =""
    var  json=""
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_BUSCAR = 101
    lateinit var campanias  : ArrayList<String>

    var pepList  = arrayListOf<VS_AGR_CAPPCollection>()
    var CodeLote: String = ""
    var pepListSelected  = arrayListOf<VS_AGR_CAPPCollection>()
    var epListSelected = arrayListOf<VS_AGR_CAPPCollection>()
    var labListSelected = arrayListOf<LaborCulturalDetalleResponse>()

    var laborListSelected  = arrayListOf<LaborCulturalDetalleResponse>()

    lateinit var etapaList  : ArrayList<EtapaProduccionListResponse>
    var CodeEtapa: String = ""


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
        setContentView(R.layout.activity_cosecha_articulo)
        pref = PreferenceManager(this)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        initViews()
        ExisteLote()
        SpinnerOperacion()
        ListarUnidad()
        setUpViews()
        campaniaListar()

       // LaborListar()
      /*  setUpViews()*/
        setUpObservers()


        // setUpObservers()

    }
    private fun campaniaListar() {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listEtapa(it,CodeCampania,httpCacheDirectory, this) }

    }
    fun ExisteLote()
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.ExisteLote(
            it,
            CODARTIUCLO,
            httpCacheDirectory,
            this
        ) }
    }
    fun ListarUnidad(){


        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.unidadMedida(
            it,
            httpCacheDirectory,
            this
        ) }
    }

    fun ListarAlmacen(){


        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listarAlmacen(
            it,
            httpCacheDirectory,
            this
        ) }
    }

    fun SpinnerOperacion()
    {
        val operacion = resources.getStringArray(R.array.Operacion)

        val adapterOperacion = ArrayAdapter(
            this,
            R.layout.spinner, operacion
        )
        adapterOperacion.setDropDownViewResource(R.layout.spinner_list)
        etEtapa.adapter = adapterOperacion


        etEtapa.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                when (position)
                {
                    0 -> idTipoOperacion = "R"
                    1 -> idTipoOperacion = "C"
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        if(Estado == "0" || Estado =="2"){
            when (OPERACION)
            {
                "R" -> etEtapa.setSelection(0)
                "C" -> etEtapa.setSelection(1)

            }

            for((index, item) in unidadMedidaList.withIndex())
            {
                if(MEDIDA == item.U_VS_UM_ABREV)
                {
                    etJornales.setSelection(index)
                }
            }

            etEtapa.isEnabled = false
            etJornales.isEnabled = false



        }

    }
    fun Spinner()
    {
        val adapterMedida = ArrayAdapter(
            this,
            R.layout.spinner, unidadMedida
        )

        val adapterAlmacen = ArrayAdapter(
            this,
            R.layout.spinner, almacen
        )

        adapterMedida.setDropDownViewResource(R.layout.spinner_list)
        etJornales.adapter = adapterMedida

        etJornales.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                CodigoMedida=unidadMedida.get(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        adapterAlmacen.setDropDownViewResource(R.layout.spinner_list)
        etLaborDefecto.adapter = adapterAlmacen

        etLaborDefecto.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {

                for(item in aLmacenList){

                    if(item.WarehouseName ==  almacen.get(position))
                        CodigoAlmacen=item.WarehouseCode

                }

                Log.d("CodigoAlmacen", CodigoAlmacen)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

    }

    fun initViews() {
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo_isotipo)
        CodigoPEP = intent.getSerializableExtra("CODIGOPEP").toString()
        Campania = intent.getSerializableExtra("CAMPANIA").toString()
        CodeCampania = intent.getSerializableExtra("CODIGOCAMPANIA").toString()
        Fecha = intent.getSerializableExtra("FECHA").toString()
        Estado = intent.getSerializableExtra("ESTADO").toString()
        CODARTIUCLO = intent.getSerializableExtra("ARTICULO").toString()
        DESCRIPCIONARTICULO = intent.getSerializableExtra("DESCRIPCION").toString()
        ALMACEN = intent.getSerializableExtra("ALMACEN").toString()

        if(Estado == "1")
        {

            //listaCosecha()

        }
        else if(Estado == "0")
        {
            etCantidadRef.setText(intent.getSerializableExtra("REFENCIA").toString())
            etTotalAprox.setText(intent.getSerializableExtra("TOTAL").toString())
            MEDIDA = (intent.getSerializableExtra("MEDIDA").toString())
            OPERACION = (intent.getSerializableExtra("OPERACION").toString())

            rlButton.visibility = View.GONE


        }else if(Estado == "2")
        {
            btnCosecha.setBackgroundResource(R.drawable.btn_personal)
            btnCosecha.text="Actualizar"
            DocEntry = intent.getSerializableExtra("DOCENTRY").toString().toInt()
            LineId = intent.getSerializableExtra("LINEID").toString()
            etCantidadRef.setText(intent.getSerializableExtra("REFENCIA").toString())
            etTotalAprox.setText(intent.getSerializableExtra("TOTAL").toString())
            MEDIDA = (intent.getSerializableExtra("MEDIDA").toString())
            OPERACION = (intent.getSerializableExtra("OPERACION").toString())

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
        etFecha.setOnClickListener {
            showDatePickerDialogHead()
        }
        etFechaLote.setOnClickListener {
            showDatePickerDialog()
        }


        addPep.setOnClickListener {
            listTable()
            tbLayout.removeAllViews()
            tableDinamica(listLotes)
        }

        setCabecera()

    }

    @SuppressLint("ResourceAsColor")
    fun tableDinamica(pepListSelected: ArrayList<VS_AGR_DSCOCollection>)
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
                    texto.setText(pepListSelected.get(index).U_VS_AGR_CDLT)
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
                    texto2.setText(pepListSelected.get(index).U_VS_AGR_TOAT.toString())
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
                    texto3.setText(pepListSelected.get(index).U_VS_AGR_FEVC)
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
                    texto3.setText(pepListSelected.get(index).U_VS_AGR_COMN)
                    texto3.setPadding(0, 0,0, 0)
                    texto3.setTextColor(R.color.black)
                    texto3.setTypeface(null, Typeface.BOLD)
                    texto3.gravity = Gravity.CENTER_HORIZONTAL
                    texto3.textSize = 10F
                    texto3.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                    tipoMenu.addView(texto3)

                }

                item++

            }


            index++
            item=0
            tableTipo.addView(tipoMenu);

        }
        tbLayout.addView(tableTipo)

    }
    fun listTable(){
        var item = VS_AGR_DSCOCollection(0, 1, "", 0, etLote.text.toString(),
            etTotalKg.text.toString().toDouble(), etFechaLote.text.toString(), etComment.text.toString(), "" )
        listLotes.add(item)
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
            etFechaLote.setText(selectedDate)
        }

        )

        newFragment.show(supportFragmentManager, "datePicker")

    }

    private fun showDatePickerDialogHead() {
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
            val selectedDate = "$year-$mes-$dia"
            etFecha.setText(selectedDate)
            filterEtapa("$year-$mes-$dia")
        }

        )

        newFragment.show(supportFragmentManager, "datePicker")

    }

    fun filterEtapa(param: String) {

        val format = SimpleDateFormat("yyyy-MM-dd")
        format.parse(param)
        for( item in  etapaList)
        {
            Log.d("FECHAETAPA",item.U_VS_AGR_FEFC.toString() )
            Log.d("FECHAETAPA",item.U_VS_AGR_FEIC.toString() )
            if((format.parse(item.U_VS_AGR_FEIC)<= format.parse(param))
                && (format.parse(param) <= format.parse(item.U_VS_AGR_FEFC)))
            {
                CodeEtapa = item.U_VS_AGR_CDEP
            }
        }

    }
    fun listaCosecha(){

        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listaCosecha(it,CodigoPEP,CodeCampania,httpCacheDirectory, this) }

    }
    fun actualizarDatos()
    {
       /* for((index, item) in laboresList.withIndex())
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
        etExtra.setText(personalLabor.U_VS_AGR_TOHX.toString())*/

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
       /* when(TipoCampania)
        {
            "D" -> etTipoCampania.setText(TipoCampania + " - Desarrollo")
            "P" -> etTipoCampania.setText(TipoCampania + " - Producción")
            "I" -> etTipoCampania.setText(TipoCampania + " - Investigación")

        }*/
      //  etCultivo.setText(Cultivo)
        etFecha.setText(Fecha)
        etCodigoArticulo.setText(CODARTIUCLO)
        etDescripcionArticulo.setText(DESCRIPCIONARTICULO)

        //  etEtapa.setText(Etapa)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpObservers(){

        laborViewModels.etapaResult.observe(this, Observer {
            it?.let {
                etapaList = it as ArrayList<EtapaProduccionListResponse>
            }
        })

        laborViewModels.ValidacionLote.observe(this, Observer {
            it?.let {

                if(it=="tNO")
                {
                    llOcultar.visibility=View.GONE
                }
                else
                {
                    llOcultar.visibility=View.VISIBLE
                }

                ListarAlmacen()

            }
        })

        laborViewModels.responseAddCosechaResult.observe(this, Observer {
            it?.let { it1 ->
                pref.saveString(Constants.MESSAGE_ALERT, "Se guardó con éxito.")
                startActivityForResult(
                    Intent(
                        this@cosechaAgregarActivity,
                        AlertActivity::class.java
                    ), REQUEST_ACTIVITY
                )
            }
        })

        laborViewModels.respondeDeleteCosechaResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivityForResult(
                    Intent(
                        this@cosechaAgregarActivity,
                        AlertActivity::class.java
                    ), REQUEST_ACTIVITY
                )
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        })
        laborViewModels.responseCosechaResult.observe(this, Observer {
            it?.let { it1 ->
                    armarJson(it1)
            }
        })

        laborViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = it
            }
        })
        laborViewModels.unidadMedidadLiveData.observe(this, Observer {
                it.let {
                    unidadMedidaList = it as ArrayList<UnidadMedida>
                    for(item in it){
                        unidadMedida.add(item.U_VS_UM_ABREV)
                    }
                }
            }
        )
        laborViewModels.AlmacenLiveData.observe(this, Observer {
            it.let {
                aLmacenList = it as ArrayList<Almacen>
                almacen = ArrayList<String>()

                for(item in it){
                    almacen.add(item.WarehouseName)
                }
                Spinner()
            }
        }
        )
        laborViewModels.messageUpdateResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivityForResult(
                    Intent(
                        this@cosechaAgregarActivity,
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
        if(etFecha.text.toString() == "")
        {
            etFecha.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }
        if(etCantidadRef.text.toString() == "")
        {
            etCantidadRef.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }
        if(etTotalAprox.text.toString().isEmpty())
        {
            etTotalAprox.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }
        if(CodeEtapa == "")
        {
            Toast.makeText(this, "La fecha no cuenta con una etapa.", Toast.LENGTH_SHORT).show()

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

    fun armarJson(it: List<VSAGRRCOS>) {
        Log.d("armarJson1", "armarJson")
        val body = JsonObject()
        val bodyDetalle = JsonObject()
        var bodyArray = JsonArray()
        body.addProperty("U_VS_AGR_CDCA", CodeCampania)
        body.addProperty("U_VS_AGR_CDPP", CodigoPEP)
        body.addProperty("U_VS_AGR_FERG", etFecha.text.toString())
        body.addProperty("U_VS_AGR_CDEP", CodeEtapa)
        body.addProperty("U_VS_AGR_CDAT", CODARTIUCLO)
        body.addProperty("U_VS_AGR_DSAT", DESCRIPCIONARTICULO)
        body.addProperty("U_VS_AGR_CDAL", CodigoAlmacen)
        body.addProperty("U_VS_AGR_UMAT", CodigoMedida)
        body.addProperty("U_VS_AGR_TOAL", etCantidadRef.text.toString())
        body.addProperty("U_VS_AGR_TOAT", etTotalAprox.text.toString())
        body.addProperty("U_VS_AGR_CLAS", idTipoOperacion)
        if(Estado == "2"){

            body.addProperty("DocNum", DocEntry)
            body.addProperty("Series", it[0].Series)

            for(item in it.iterator()){
                bodyArray = JsonArray()
                for(collection in item.VS_AGR_DSCOCollection.iterator()) {
                    if(LineId.toInt() == collection.LineId)
                    {
                        bodyDetalle.addProperty("U_VS_AGR_TOAT", etTotalAprox.text.toString())

                    }
                    else{
                        bodyDetalle.addProperty("U_VS_AGR_TOAT", collection.U_VS_AGR_TOAT)
                    }
                    bodyDetalle.addProperty("LineId", collection.LineId)
                    bodyDetalle.addProperty("U_VS_AGR_CDLT", "")
                    bodyDetalle.addProperty("U_VS_AGR_FEVC", "")
                    bodyDetalle.addProperty("U_VS_AGR_COMN", "")

                    bodyArray.add(bodyDetalle)

                }
            }


        }else {

            bodyDetalle.addProperty("U_VS_AGR_CDLT", "")
            bodyDetalle.addProperty("U_VS_AGR_FEVC", "")
            bodyDetalle.addProperty("U_VS_AGR_COMN", "")
            bodyDetalle.addProperty("U_VS_AGR_TOAT", etTotalAprox.text.toString())

            bodyArray.add(bodyDetalle)
        }
        body.add("VS_AGR_DSCOCollection",bodyArray)
        Log.d("armarJson1", DocEntry.toString())
        Log.d("armarJson1", body.toString())

        if(Estado == "2")
            pref.getString(Constants.B1SESSIONID)?.let { it1 -> laborViewModels.putCosecha(it1, DocEntry, body,httpCacheDirectory, this) }
        else
            pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.addCosecha(it,body,httpCacheDirectory, this) }


    }

 /*   @SuppressLint("ResourceAsColor")
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

    } */

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpViews() {
        rlLaborPersonal.setOnClickListener {
            hideSoftKeyboard()
        }
       /* addPep.setOnClickListener {
            for(item in pepList ){
                if(CodeLote == item.U_VS_AGR_CDLT){
                    pepListSelected.add(item)
                }
            }
            tbLayout.removeAllViews()
            tableDinamica(pepListSelected)
        } */

        etFechaLote.setOnClickListener {
            showDatePickerDialog()
        }

        btnCosecha.setOnClickListener {
            if(checkInternet()) {
                        if(validar())
                        listaCosecha()

                }
                else
                {
                    if(validar()) {
                        if (Estado == "2") {
                            val detalle = LaborCulturalDetalleRoom(
                                0,
                                DocEntry,
                                0,
                                0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "P",
                                0,
                                "Y",
                                "",
                                "",
                                "AP",
                                0,
                                etCantidadRef.text.toString().toInt()
                            )

                            laborViewModels.updateLaborDetalleRoom(detalle)
                            pref.saveString(Constants.MESSAGE_ALERT, "Se actualizó en la memoria interna.")
                            startActivityForResult(
                                Intent(
                                    this@cosechaAgregarActivity,
                                    AlertActivity::class.java
                                ), REQUEST_ACTIVITY
                            )
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        } else {
                            var extra:Int = 0
                            if(etCantidadRef.text.toString().isEmpty())
                            {
                                extra = 0
                            }
                            else
                            {
                                extra =  etCantidadRef.text.toString().toInt()
                            }
                            val detalle = LaborCulturalDetalleRoom(
                                0,
                                DocEntry,
                                0,
                                0,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "P",
                                0,
                                "Y",
                                "",
                                "",
                                "AP",
                                0,
                                extra
                            )
                            laborViewModels.insertLaborDetalleRoom(detalle)
                            pref.saveString(Constants.MESSAGE_ALERT, "Se guardó en la memoria interna.")
                            startActivityForResult(
                                Intent(
                                    this@cosechaAgregarActivity,
                                    AlertActivity::class.java
                                ), REQUEST_ACTIVITY
                            )
                        }
                    }
                }

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }



    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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

        }
    }


}

