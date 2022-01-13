package com.application.venturaapp.fertilizante
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.venturaapp.R
import com.application.venturaapp.fertilizante.adapter.FertilizantePEPAdapter
import com.application.venturaapp.fertilizante.listener.VSAGRRFERItemListener
import com.application.venturaapp.fitosanitario.entity.VSAGRRCOS
import com.application.venturaapp.fitosanitario.entity.VS_AGR_DSCOCollection
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.HomeActivity
import com.application.venturaapp.laborCultural.entity.EtapaProduccionListResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.laborCultural.laborCulturalPersonalActivity
import com.application.venturaapp.preference.PreferenceManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_labor_cultural.*
import kotlinx.android.synthetic.main.activity_labor_cultural.etBuscador
import kotlinx.android.synthetic.main.activity_labor_cultural.pgbLaborRealizada
import kotlinx.android.synthetic.main.activity_labor_cultural.rvLaborCultural
import kotlinx.android.synthetic.main.activity_labor_cultural.tvTituloPersonal
import okhttp3.Cache
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.internal.Intrinsics


class fertilizanteActivity   : AppCompatActivity(), VSAGRRFERItemListener {

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var codigoCampania = ""
    private var DocEntryPEP = ""
    lateinit var httpCacheDirectory : Cache
    private var DocEntryCosecha = 0
    private var Cultivo = ""
    private var FECHA = ""
    private var CODARTIUCLO = ""
    private var DESCRIPCIONARTICULO = ""
    private var ALMACEN = ""

    private var Variedad = ""
    private  var tipoCampania = ""
    lateinit var pref: PreferenceManager
    lateinit var fertilizanteViewModels: fertilizanteViewModel
    lateinit var laborViewModels: laborCulturaViewModel

    var VS_AGR_DSCOCollection  = arrayListOf<VS_AGR_DSCOCollection>()

    var laborList  = arrayListOf<VSAGRRCOS>()
    lateinit var etapa  : java.util.ArrayList<String>
    lateinit var etapaList  : java.util.ArrayList<EtapaProduccionListResponse>
    var Descripcion: String = ""
    var CodeEtapa: String = ""
    private val REQUEST_ACTIVITY = 100

    private val REQUEST_ACTIVITY_ELIMINAR = 110
    private val REQUEST_ACTIVITY_DETALLE = 120

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_labor_cultural)
        tvTituloPersonal.text = "Cosecha"
        pref = PreferenceManager(this)
        fertilizanteViewModels = ViewModelProviders.of(this).get(fertilizanteViewModel::class.java)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)

        initViews()
        setCabecera()
        setUpViews()
        setUpObservers()
        listaCosecha()

        //EtapaListar()

    }
    fun listaCosecha(){

        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listaCosecha(it,CodigoPEP,codigoCampania,httpCacheDirectory, this) }

    }

    private fun filter(s: String)
    {
        val filter: ArrayList<VS_AGR_DSCOCollection> = ArrayList()
        for (item in laborList.iterator()) {
            for(i in item.VS_AGR_DSCOCollection)
            if (item.U_VS_AGR_FERG == s)
                filter.add(i)

        }

        rvLaborCultural.adapter = (FertilizantePEPAdapter(this, filter, laborList))
        rvLaborCultural.layoutManager = LinearLayoutManager(this)


    }
    fun initViews() {

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo_isotipo)
             CodigoPEP = intent.getSerializableExtra("CODIGOPEP").toString()
             Campania = intent.getSerializableExtra("CAMPAÑA").toString()
             Cultivo = intent.getSerializableExtra("CULTIVO").toString()
             Variedad = intent.getSerializableExtra("VARIEDAD").toString()
            tipoCampania = intent.getSerializableExtra("TIPOCAMPANIA").toString()
            codigoCampania= intent.getSerializableExtra("CODIGOCAMPANIA").toString()
            DocEntryPEP = intent.getSerializableExtra("DOCENTRYPEP").toString()



    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val myIntent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(myIntent)


            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.fml_frag_container, fragment)
            .commit()
    }
    fun setCabecera()
    {
        etCodigoPEP.setText(CodigoPEP)
        etCampania.setText(Campania)
        etCultivo.setText(Cultivo)
        etVariedad.setText(Variedad)



        tvCampania.visibility = View.GONE
        etCampania.visibility = View.GONE
        tvCultivo.visibility = View.GONE
        etCultivo.visibility = View.GONE
        tvVariedad.visibility = View.GONE
        etVariedad.visibility = View.GONE

        ivExpand.setImageResource(R.drawable.ic_down)

    }

    fun setUpObservers(){
        laborViewModels.respondeDeleteCosechaResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivityForResult(
                    Intent(
                        this@fertilizanteActivity,
                        AlertActivity::class.java
                    ), REQUEST_ACTIVITY
                )
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        })
        laborViewModels.responseCosechaResult.observe(this, Observer {
            it?.let { it1 ->

                laborList = it1 as ArrayList<VSAGRRCOS>
                laborList.sortByDescending { it.U_VS_AGR_FERG }
                var position = 0

                for ((i,item) in laborList.withIndex()) {
                    for((index,etapa) in item.VS_AGR_DSCOCollection.withIndex())
                    {
                        VS_AGR_DSCOCollection.add(etapa)
                        if(laborList[i].DocEntry == laborList[index].DocEntry)
                            laborList[i].position =  index
                    }
                }

                FECHA = laborList[0].U_VS_AGR_FERG
                CODARTIUCLO = laborList[0].U_VS_AGR_CDAT
                DESCRIPCIONARTICULO = laborList[0].U_VS_AGR_DSAT
                ALMACEN = laborList[0].U_VS_AGR_CDAL


                /* Spinner()*/



                rvLaborCultural.adapter = (FertilizantePEPAdapter(this, VS_AGR_DSCOCollection, laborList))
                rvLaborCultural.layoutManager = LinearLayoutManager(this)

                pgbLaborRealizada.visibility = View.GONE
            }
        })

        fertilizanteViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = it
            }
        })
    }
    fun setUpViews()
    {
        rlLabor.setOnClickListener {
            hideSoftKeyboard()
        }

        ivPersonalAdd.setOnClickListener {

            /*startActivity(Intent(this@personalFragment, personalAdd::class.java))
            finish()*/

            val intent = Intent(this, cosechaAgregarActivity::class.java)
            intent.putExtra("CODIGOPEP", CodigoPEP)
            intent.putExtra("CULTIVO", Cultivo)
            intent.putExtra("CAMPANIA", Campania)
            intent.putExtra("FECHA", FECHA)
            intent.putExtra("ARTICULO", CODARTIUCLO)
            intent.putExtra("CODIGOCAMPANIA",codigoCampania)
            intent.putExtra("DESCRIPCION", DESCRIPCIONARTICULO)
            intent.putExtra("ALMACEN", ALMACEN)
            intent.putExtra("ESTADO", 1)

            startActivityForResult(intent, REQUEST_ACTIVITY)
        }
        etBuscador.setOnClickListener {
            fecha()

        }
            llCabecera.setOnClickListener {
            when(tvCampania.visibility)
            {
                0 -> {
                    tvCampania.visibility = View.GONE
                    etCampania.visibility = View.GONE
                    tvCultivo.visibility = View.GONE
                    etCultivo.visibility = View.GONE
                    tvVariedad.visibility = View.GONE
                    etVariedad.visibility = View.GONE
                    ivExpand.setImageResource(R.drawable.ic_down)

                }
                8 -> {
                    tvCampania.visibility = View.VISIBLE
                    etCampania.visibility = View.VISIBLE
                    tvCultivo.visibility = View.VISIBLE
                    etCultivo.visibility = View.VISIBLE
                    tvVariedad.visibility = View.VISIBLE
                    etVariedad.visibility = View.VISIBLE
                    ivExpand.setImageResource(R.drawable.ic_up)

                }
            }

        }
    }
    fun fecha(){
        val mcurrentTime: Calendar = Calendar.getInstance()
        Intrinsics.checkNotNullExpressionValue(mcurrentTime, "Calendar.getInstance()")
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener
        { view, year, monthOfYear, dayOfMonth ->

            val dayFormateada: String
            val monthFormateada: String
            val monthSelect = monthOfYear + 1
            dayFormateada = if (dayOfMonth < 10) {
                "0$dayOfMonth"
            } else {
                dayOfMonth.toString()
            }
            monthFormateada = if (monthSelect < 10) {
                "0$monthSelect"
            } else {
                monthSelect.toString()
            }
            val fechaBusqueda = "$year-$monthFormateada-$dayFormateada"
            etBuscador.setText(
                fechaBusqueda
            )
            if (!Intrinsics.areEqual(
                    fechaBusqueda,
                    ""
                )
            ) {
                filter(fechaBusqueda)
            }
        }, mcurrentTime.get(1), mcurrentTime.get(2), mcurrentTime.get(5)
        )
        datePickerDialog.setTitle("Seleccione la fecha")
        datePickerDialog.show()
    }

    fun hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    override fun laborItemClickListener(position: VS_AGR_DSCOCollection ,  vsagrrcos : VSAGRRCOS) {

        var gson =  Gson()
        var jsonClass = gson.toJson(position)
        var jsonClassDetalle = gson.toJson(vsagrrcos)

        val intent = Intent(this, cosechaAgregarActivity::class.java)
        intent.putExtra("CODIGOPEP", CodigoPEP)
        intent.putExtra("CAMPANIA", Campania)
        intent.putExtra("CULTIVO", Cultivo)
        intent.putExtra("VARIEDAD", Variedad)
        intent.putExtra("CODIGOCAMPANIA", codigoCampania)
        intent.putExtra("DOCENTRYPEP", DocEntryPEP)
        intent.putExtra("CODIGOCAMPANIA",codigoCampania)
        intent.putExtra("FECHA", FECHA)
        intent.putExtra("ARTICULO", CODARTIUCLO)
        intent.putExtra("DESCRIPCION", DESCRIPCIONARTICULO)
        intent.putExtra("ALMACEN", ALMACEN)
        intent.putExtra("ESTADO", 0)


        if (position != null) {
            intent.putExtra("TIPOCAMPANIA", tipoCampania)
            intent.putExtra("DOCENTRY", position.DocEntry)
            intent.putExtra("REFENCIA", vsagrrcos.U_VS_AGR_TOAT)
            intent.putExtra("OPERACION", vsagrrcos.U_VS_AGR_CLAS)
            intent.putExtra("MEDIDA", vsagrrcos.U_VS_AGR_UMAT)

            intent.putExtra("TOTAL", position.U_VS_AGR_TOAT)

            intent.putExtra("OBJECT", jsonClass)
            intent.putExtra("OBJECTPERSONA", jsonClassDetalle)

            startActivityForResult(intent, REQUEST_ACTIVITY_DETALLE)

        }

    }

    override fun laborUpdateClickListener(position: VS_AGR_DSCOCollection ,  vsagrrcos : VSAGRRCOS) {

        var gson =  Gson()
        var jsonClass = gson.toJson(position)
        var jsonClassDetalle = gson.toJson(vsagrrcos)

        val intent = Intent(this, cosechaAgregarActivity::class.java)
        intent.putExtra("CODIGOPEP", CodigoPEP)
        intent.putExtra("CAMPANIA", Campania)
        intent.putExtra("CULTIVO", Cultivo)
        intent.putExtra("VARIEDAD", Variedad)
        intent.putExtra("CODIGOCAMPANIA", codigoCampania)
        intent.putExtra("DOCENTRYPEP", DocEntryPEP)
        intent.putExtra("CODIGOCAMPANIA",codigoCampania)
        intent.putExtra("FECHA", FECHA)
        intent.putExtra("ARTICULO", CODARTIUCLO)
        intent.putExtra("DESCRIPCION", DESCRIPCIONARTICULO)
        intent.putExtra("ALMACEN", ALMACEN)
        intent.putExtra("ESTADO", 2)
        intent.putExtra("LINEID", position.LineId)


        if (position != null) {
            intent.putExtra("TIPOCAMPANIA", tipoCampania)
            intent.putExtra("DOCENTRY", position.DocEntry)
            intent.putExtra("REFENCIA", vsagrrcos.U_VS_AGR_TOAT)
            intent.putExtra("OPERACION", vsagrrcos.U_VS_AGR_TOAT)
            intent.putExtra("MEDIDA", position.U_VS_AGR_TOAT)

            intent.putExtra("TOTAL", position.U_VS_AGR_TOAT)

            intent.putExtra("OBJECT", jsonClass)
            intent.putExtra("OBJECTPERSONA", jsonClassDetalle)

            startActivityForResult(intent, REQUEST_ACTIVITY_DETALLE)

        }


    }

    override fun laborDeleteClickListener(position: VS_AGR_DSCOCollection ,  vsagrrcos : VSAGRRCOS) {
        val intent = Intent(this, cosechaAgregarActivity::class.java)

        if(checkInternet()) {

            DocEntryCosecha=  vsagrrcos.DocEntry
            intent.putExtra("ESTADO", 3)

            pref.saveString(Constants.MESSAGE_ALERT, "¿Desea eliminar esta cosecha?")
            startActivityForResult(
                Intent(
                    this@fertilizanteActivity,
                    AlertActivity::class.java
                ), REQUEST_ACTIVITY_ELIMINAR
            )
        }else
        {
            val builder =  android.app.AlertDialog.Builder(this)

            builder.setTitle("Eliminar cosecha")
            builder.setMessage("Operación no permitida de modo offline.")
            builder.show()
        }



    }
    override fun onBackPressed() {

        val myIntent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(myIntent)

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
                    if (resultCode == Activity.RESULT_CANCELED) {
                        startActivity(intent)
                        listaCosecha()

                    }
                    if (resultCode == Activity.RESULT_OK) {
                        startActivity(intent)
                    }
                }
            REQUEST_ACTIVITY_DETALLE -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    startActivity(intent)
                    listaCosecha()

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
                        laborViewModels.deleteCosecha(
                            it,
                            DocEntryCosecha,
                            httpCacheDirectory,
                            this
                        )
                    }
                }

            }
        }
    }
}

