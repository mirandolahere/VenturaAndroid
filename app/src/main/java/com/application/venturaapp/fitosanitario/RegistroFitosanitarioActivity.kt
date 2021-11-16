package com.application.venturaapp.fitosanitario

import android.annotation.SuppressLint
import android.app.Activity
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
import com.application.venturaapp.fitosanitario.entity.Almacen
import com.application.venturaapp.fitosanitario.entity.FitosanitarioDetalleResponse
import com.application.venturaapp.fitosanitario.entity.FitosanitarioDistribucionResponse
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.fragment.entities.LaboresPersonal
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.laborCultural.entity.EtapaProduccionListResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.preference.PreferenceManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registro_fitosanitario.*
import kotlinx.android.synthetic.main.activity_registro_fitosanitario.pgbLaborRealizada
import kotlinx.android.synthetic.main.activity_registro_fitosanitario.tbLayout
import okhttp3.Cache
import kotlin.collections.ArrayList


class RegistroFitosanitarioActivity   : AppCompatActivity() {

    lateinit var FITDetalle : FitosanitarioDetalleResponse
    lateinit var etapa  : java.util.ArrayList<String>
    var etapaList  = arrayListOf<EtapaProduccionListResponse>()
    var CodeEtapa: String = ""
    lateinit var fitosanitarioViewModels: fitosanitarioViewModel
    lateinit var almacen  : java.util.ArrayList<String>
    var almacenList = arrayListOf<Almacen>()
    var CodeAlmacen: String = ""

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var Cultivo = ""
    private var Fecha = ""
    private var TipoCampania = ""
    private var Etapa = ""
    private var Estado = ""
    private var Entry = ""

    private var LineId = ""

    private var DocEntry:Int? = 0
    val codeList = ArrayList<String>()
    lateinit var personalViewModels: personalViewModel
    lateinit var personalLista: java.util.ArrayList<PersonalDato>
    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborCulturaViewModel
    lateinit var laborList  : ArrayList<LaboresPersonal>
    var CodigoLabor = ""
    var CodigoPersonal =""
    var  json=""
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_BUSCAR = 101
    lateinit var labores  : ArrayList<String>
    lateinit var httpCacheDirectory : Cache
    lateinit var peopleClass : PersonalDato
    var personalexists = arrayListOf<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_registro_fitosanitario)
        pref = PreferenceManager(this)
        fitosanitarioViewModels  = ViewModelProviders.of(this).get(fitosanitarioViewModel::class.java)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        Log.d("prueba", "pruebaprueba")
        initViews()
        EtapaListar()
        setUpObservers()
        setUpViews()

        // setUpObservers()

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
        Entry = intent.getSerializableExtra("DOCENTRY").toString()

        if(Estado == "0") //MOSTRAR DATOS
        {
            LineId = intent.getSerializableExtra("LINEID").toString()
            rlButton.visibility = View.GONE

        }
        setCabecera()
    }
    fun Datos(){
        if(Estado == "0") //MOSTRAR DATOS
        {
            val gson = Gson()
            if(intent.getSerializableExtra("OBJECTPERSONA") !=null) {

                val people = intent.getSerializableExtra("OBJECTPERSONA") //as? PersonalDato
                FITDetalle = gson.fromJson(
                    people.toString(),
                    FitosanitarioDetalleResponse::class.java
                )
                mostrarDatos()

            }


        }
        else if(Estado == "1") //NUEVO
        {
            etCodigoFIT.isClickable =true


        }
        else if(Estado == "2")
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
                FITDetalle = gson.fromJson(
                    people.toString(),
                    FitosanitarioDetalleResponse::class.java
                )
                actualizarDatos()

            }
        }


    }

    @SuppressLint("SetTextI18n")
    fun setCabecera()
    {
        tvCodigoPEPFIT.setText(CodigoPEP)
        tvFechaFIT.setText(Fecha)
    }

    private fun EtapaListar() {
        Log.d("pasos","pasopaso")
        Log.d("pasos",CodigoPEP)

        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listEtapa(it,CodigoPEP,httpCacheDirectory, this) }
    }
    private fun AlmacenListar() {
        pref.getString(Constants.B1SESSIONID)?.let { fitosanitarioViewModels.listaAlmacen(it,httpCacheDirectory, this) }
    }
    private fun DistribucionListar() {
        Log.d("pasos","paso10")
        Log.d("pasos",Entry)

        pref.getString(Constants.B1SESSIONID)?.let { fitosanitarioViewModels.listaDistribucion(it,Entry.toInt(),FITDetalle.LineId,httpCacheDirectory, this) }
    }
    fun Spinner()
    {                Log.d("pasos","paso3")

        val adapter = this?.let {
            ArrayAdapter(
                it,
                R.layout.spinner, etapa
            )
        }
        val adapterAlmacen = this?.let {
            ArrayAdapter(
                it,
                R.layout.spinner, almacen
            )
        }
        if (adapterAlmacen != null) {
            adapter.setDropDownViewResource(R.layout.spinner_list)
            etAlmacenFIT.adapter = adapterAlmacen

            etAlmacenFIT.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    for( item in almacenList)
                    {
                        if(item.WarehouseName == almacen.get(position))
                        {
                            CodeAlmacen = item.WarehouseCode

                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        if (adapter != null) {
            adapter.setDropDownViewResource(R.layout.spinner_list)
            etEtapaFIT.adapter = adapter

            etEtapaFIT.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    for( item in etapaList)
                    {
                        if(item.U_VS_AGR_DSEP == etapa.get(position))
                        {
                            CodeEtapa = item.U_VS_AGR_CDEP

                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        Datos()

    }

    fun mostrarDatos()
    {
        for((index, item) in etapaList.withIndex())
        {
            if(item.Code == Etapa)
            {
                etEtapaFIT.setSelection(index)

            }
        }
        Log.d("almacenLista",almacenList.toString() )
        Log.d("almacenLista",FITDetalle.U_VS_AGR_CDAL )

        for((index, item) in almacenList.withIndex())
        {
            if(item.WarehouseCode == FITDetalle.U_VS_AGR_CDAL)
            {
                etAlmacenFIT.setSelection(index)

            }
        }

        etCodigoFIT.setText(FITDetalle.U_VS_AGR_CDQU)
        etCodigoFIT.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        etDescripcionFIT.setText(FITDetalle.U_VS_AGR_DSQU)
        etDescripcionFIT.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        etCantidadFIT.setText(FITDetalle.U_VS_AGR_TOQU.toString())
        etCantidadFIT.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        etDosisFIT.setText(FITDetalle.U_VS_AGR_DOSS)
        etDosisFIT.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        etCantidadHAFIT.setText(FITDetalle.U_VS_AGR_TOHT.toString())
        etCantidadHAFIT.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        etUnidadHAFIT.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        etVolumenFIT.setText(FITDetalle.U_VS_AGR_VCHT)
        etVolumenFIT.setBackgroundResource(R.drawable.edtx_personal_list_disabled)

        tvCantidadUnidad.setText("Cantidad("+FITDetalle.U_VS_AGR_UMQU+")")

        etCodigoFIT.isEnabled = false
        etDescripcionFIT.isEnabled = false
        etCantidadFIT.isEnabled = false
        etDosisFIT.isEnabled = false
        etCantidadHAFIT.isEnabled = false
        etVolumenFIT.isEnabled = false
        etUnidadHAFIT.isEnabled = false
        etEtapaFIT.isEnabled = false
        etAlmacenFIT.isEnabled = false

        ValidarLotes()

    }

    fun ValidarLotes()
    {
        Log.d("pasos",FITDetalle.U_VS_AGR_CDQU)
        pref.getString(Constants.B1SESSIONID)?.let { fitosanitarioViewModels.ValidarLote(it,FITDetalle.U_VS_AGR_CDQU,httpCacheDirectory, this) }
    }
    fun actualizarDatos()
    {

/*
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpObservers(){

        laborViewModels.etapaResult.observe(this, Observer {
            it?.let {
                Log.d("pasos","paso1")
                etapaList = it as java.util.ArrayList<EtapaProduccionListResponse>
                etapa = java.util.ArrayList<String>()

                for (item in it) {
                    etapa.add(item.U_VS_AGR_DSEP)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }

                AlmacenListar()

            }
        })
        fitosanitarioViewModels.ValidacionLote.observe(this, Observer {
            it?.let {
                Log.d("pasos",it)

                if(it=="tNO")
                {
                    OcultarDistribucion()
                }
                else
                {
                    DistribucionListar()
                    if(Estado == "0")
                    {
                        llLote2.visibility=View.GONE
                        llLote3.visibility=View.GONE
                        btnGuardarLote.visibility = View.GONE
                        etCantidadDisponible.visibility = View.GONE
                        tvCantidadDisponible.visibility = View.GONE
                        etCantidadTotal.setText("Cantidad total utilizada ("+FITDetalle.U_VS_AGR_UMQU+")")

                    }
                }


            }
        })
        fitosanitarioViewModels.Distribucion.observe(this, Observer {
            it?.let {

                tableDinamica(it.size,it)

            }
        })
        fitosanitarioViewModels.Almacen.observe(this, Observer {
             it?.let {
                 Log.d("pasos","paso2")

                 almacenList = it as java.util.ArrayList<Almacen>
                 almacen = java.util.ArrayList<String>()

                 for (item in it) {
                     almacen.add(item.WarehouseName)
                     //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                 }

                 Spinner()

             }
        })

        laborViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = it
            }
        })

    }
    fun OcultarDistribucion()
    {
        llDistribucionLotes.visibility= View.GONE
        llTables.visibility = View.GONE
    }
    @SuppressLint("ResourceAsColor")
    fun tableDinamica(size: Int, list: List<FitosanitarioDistribucionResponse>)
    {
        var tableTipo = TableLayout(this)
        tableTipo.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        tableTipo.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        tableTipo.setPadding(10, 5, 8, 0)

        var index = 0

        var item = 0

        while(index<size)
        {
            var tipoMenu = TableRow(this)
            tipoMenu.setLayoutParams(
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT))

            tipoMenu.setPadding(10, 0, 0, 0)
                while (item <= 4) {
                    if (item == 0) {
                        var texto = TextView(this)
                        texto.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f))
                        texto.setText( (index + 1).toString())
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
                        texto2.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
                        texto2.setText(list[index].U_VS_AGR_CDLT)
                        texto2.setPadding(0, 10, 0, 10)
                        texto2.setTypeface(null, Typeface.BOLD)
                        texto2.gravity = Gravity.CENTER_HORIZONTAL

                        texto2.setTextColor(R.color.black)
                        texto2.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto2)

                    }

                    if (item == 2) {
                        var texto3 = TextView(this)
                        texto3.setLayoutParams(
                            TableRow.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
                        texto3.setText(list[index].U_VS_AGR_TOQU.toString() + list[index].U_VS_AGR_UMQU)
                        texto3.setPadding(0, 10, 0, 10)
                        texto3.setTextColor(R.color.black)
                        texto3.setTypeface(null, Typeface.BOLD)
                        texto3.gravity = Gravity.CENTER_HORIZONTAL

                        texto3.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                        tipoMenu.addView(texto3)

                    }
                    if (item == 4) {
                        var texto3 = Button(this)
                        texto3.setLayoutParams(
                            TableRow.LayoutParams(
                                5,
                                30, 1f))

                        texto3.setPadding(0, 20, 0, 10)

                        texto3.gravity = Gravity.CENTER_HORIZONTAL

                        texto3.setBackgroundResource(R.drawable.ic_delete)
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

    fun validar()
    {
        /*if(etInicio.text.toString() == "")
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
        if(etCodigoPersona.text.toString() == "")
        {
            etCodigoPersona.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }

        if(etNumeroDocumento.text.toString() == "")
        {
            etNumeroDocumento.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }
        if(etNombre.text.toString() == "")
        {
            etNombre.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }

            return true*/
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
        etCodigoFIT.setOnClickListener {

            val intent = Intent(this, popupFitosanitarioActivity::class.java)
            var gson =  Gson()


                Log.d("PERSONALLIST11",personalexists.toString())
                intent.putStringArrayListExtra("OBJECT", personalexists)



            startActivityForResult(intent, REQUEST_ACTIVITY_BUSCAR)

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
        }
    }


}

