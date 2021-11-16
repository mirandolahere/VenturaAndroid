package com.application.venturaapp.fitosanitario

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
import com.application.venturaapp.fitosanitario.adapter.FitosanitarioDetalleAdapter
import com.application.venturaapp.fitosanitario.entity.FitosanitarioDetalleResponse
import com.application.venturaapp.fitosanitario.listener.FitosanitarioDetalleItemListener
import com.application.venturaapp.helper.*
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.laborCultural.adapter.laborCulturalDetalleAdapter
import com.application.venturaapp.laborCultural.entity.ContActualizacionResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class FitosanitarioDetalleActivity   : AppCompatActivity() , FitosanitarioDetalleItemListener {

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
    var hrx = arrayListOf<Int>()
    var labores_HRXT = arrayListOf<String>()

    var labores = arrayListOf<String>()
    var jornales  = arrayListOf<Int>()
    var hrs  = arrayListOf<Int>()
    var laborDes  = arrayListOf<String>()

    var LineId: Int = 0

    lateinit var pref: PreferenceManager
    lateinit var fitosanitarioViewModels: fitosanitarioViewModel

    lateinit var laborViewModels: laborCulturaViewModel
    lateinit var laborList  : ArrayList<LaborCulturalListResponse>
    var DetalleList  =arrayListOf<FitosanitarioDetalleResponse>()
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
        fitosanitarioViewModels = ViewModelProviders.of(this).get(fitosanitarioViewModel::class.java)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        initViews()
        ListarDetalle()

        setUpViews()
        setUpObservers()

    }
    fun initViews() {
        tvTituloDetalle.text = "Detalle fitosanitario"
        llTable.visibility = View.GONE
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


    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }
    fun ListarDetalle()
    {
        Log.d("DocEntryDocEntry",DocEntry)
        pref.getString(Constants.B1SESSIONID)?.let { fitosanitarioViewModels.listaDetalleFitosanitario(it, DocEntry,
            httpCacheDirectory, this) }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                Log.d("arrow", "arrow")
                val myIntent = Intent(applicationContext, fitosanitarioActivity::class.java)
                myIntent.putExtra("CODIGOPEP", CodigoPEP)
                myIntent.putExtra("CAMPAÑA", Campania)
                myIntent.putExtra("CULTIVO", Cultivo)
                myIntent.putExtra("VARIEDAD", Variedad)
                myIntent.putExtra("CODIGOCAMPANIA", CodCampania)
                myIntent.putExtra("TIPOCAMPANIA", TipoCampania)
                myIntent.putExtra("DOCENTRYPEP", DocEntry)


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


        fitosanitarioViewModels.VSAGRFITDetalle.observe(this, Observer {
            it?.let {
                if (it.VS_AGR_DRFTCollection?.size  != 0) {
                    DetalleList = it.VS_AGR_DRFTCollection as ArrayList<FitosanitarioDetalleResponse>
                    for (item in it.VS_AGR_DRFTCollection!!)
                    {
                        if(item.U_VS_AGR_ESTA=="P")
                        {
                            ivSincronizacion.visibility=View.VISIBLE
                        }
                    }

                    rvLaborCultural.adapter = (FitosanitarioDetalleAdapter(this, it.VS_AGR_DRFTCollection!!, Etapa))
                    rvLaborCultural.layoutManager = LinearLayoutManager(this)



                } else {

                }
                pgbLaborRealizada.visibility = View.GONE

            }
        })


        fitosanitarioViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = it

            }

        })
        fitosanitarioViewModels.messageResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it)
                startActivityForResult(Intent(this@FitosanitarioDetalleActivity, AlertActivity::class.java), REQUEST_ACTIVITY_ERROR_CONTABILIZAR)
            }
        })



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


            val intent = Intent(this, RegistroFitosanitarioActivity::class.java)
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
            val intent = Intent(this, ContabilizadorActivity::class.java)
            startActivityForResult(intent,REQUEST_ACTIVITY_MESSAGE)

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

    private fun filter(s: String)
    {
        val filter: java.util.ArrayList<FitosanitarioDetalleResponse> = java.util.ArrayList()

        for (item in DetalleList) {
            if (item.U_VS_AGR_CDQU.toLowerCase().contains(s.toLowerCase()) ||item.U_VS_AGR_DSQU.toLowerCase().contains(s.toLowerCase()) ) {
                filter.add(item)
            }
        }
        rvLaborCultural.adapter = (FitosanitarioDetalleAdapter(this, filter, Etapa))
        rvLaborCultural.layoutManager = LinearLayoutManager(this)
    }
    override fun laborItemClickListener(position: FitosanitarioDetalleResponse?) {
        var gson =  Gson()
        var jsonClass = gson.toJson(position)
        var tipoCampania:String =""
        Log.d("ITEMDETALLE", jsonClass)
        val intent = Intent(this, RegistroFitosanitarioActivity::class.java)
        intent.putExtra("CODIGOPEP", CodigoPEP)
        intent.putExtra("CULTIVO", Cultivo)
        intent.putExtra("CAMPANIA", Campania)
        intent.putExtra("FECHA", Fecha)
        intent.putExtra("ETAPA", Etapa)
        intent.putExtra("TIPOCAMPANIA", TipoCampania)
        intent.putExtra("OBJECTPERSONA", jsonClass)
        intent.putExtra("ESTADO", 0)
        intent.putExtra("DOCENTRY",DocEntry)

        if (position != null) {
            intent.putExtra("LINEID", position.LineId)
        }

        startActivity(intent)
    }
    override fun laborItemDeletelickListener(position: FitosanitarioDetalleResponse?) {
        if(checkInternet()) {
            if (position != null) {
                LineId = position.LineId
            }
            pref.saveString(Constants.MESSAGE_ALERT, "¿Desea eliminar esta la labor cultural?")
            startActivityForResult(
                Intent(
                    this@FitosanitarioDetalleActivity,
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
    override fun laborItemUpdateClickListener(position: FitosanitarioDetalleResponse?) {
        var gson =  Gson()
        var jsonClass = gson.toJson(position)
        var tipoCampania:String =""

       /* val intent = Intent(this, laborCulturalPersonalActivity::class.java)
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

        startActivityForResult(intent, REQUEST_ACTIVITY)*/
    }

    override fun onBackPressed() {
      /*  Log.d("arrow", "arrow")
        val myIntent = Intent(applicationContext, laborCulturalActivity::class.java)
        myIntent.putExtra("CODIGOPEP", CodigoPEP)
        myIntent.putExtra("CAMPAÑA", Campania)
        myIntent.putExtra("CULTIVO", Cultivo)
        myIntent.putExtra("VARIEDAD", Variedad)
        myIntent.putExtra("CODIGOCAMPANIA", CodCampania)
        myIntent.putExtra("TIPOCAMPANIA", TipoCampania)
        myIntent.putExtra("DOCENTRYPEP", DocEntryPEP)


        startActivity(myIntent)*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("RELOAD", resultCode.toString())
        Log.d("RELOAD", requestCode.toString())

        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                   startActivity(intent)
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
                }

            }
        }
    }

}

