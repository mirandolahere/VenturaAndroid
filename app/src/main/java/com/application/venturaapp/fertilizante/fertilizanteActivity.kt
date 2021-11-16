package com.application.venturaapp.fertilizante
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
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
import com.application.venturaapp.fitosanitario.adapter.FitosanitarioPEPAdapter
import com.application.venturaapp.fitosanitario.entity.VSAGRRFER
import com.application.venturaapp.fitosanitario.entity.VSAGRRFIT
import com.application.venturaapp.fitosanitario.listener.VSAGRRFITItemListener
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.HomeActivity
import com.application.venturaapp.laborCultural.entity.EtapaProduccionListResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_labor_cultural.*
import kotlinx.android.synthetic.main.activity_labor_cultural.etBuscador
import kotlinx.android.synthetic.main.activity_labor_cultural.pgbLaborRealizada
import kotlinx.android.synthetic.main.activity_labor_cultural.rvLaborCultural
import kotlinx.android.synthetic.main.activity_labor_cultural.tvTituloPersonal
import okhttp3.Cache


class fertilizanteActivity   : AppCompatActivity(), VSAGRRFERItemListener {

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var codigoCampania = ""
    private var DocEntryPEP = ""
    lateinit var httpCacheDirectory : Cache

    private var Cultivo = ""
    private var Variedad = ""
    private  var tipoCampania = ""
    lateinit var pref: PreferenceManager
    lateinit var fertilizanteViewModels: fertilizanteViewModel
    lateinit var laborViewModels: laborCulturaViewModel

    var laborList  = arrayListOf<VSAGRRFER>()
    lateinit var etapa  : java.util.ArrayList<String>
    lateinit var etapaList  : java.util.ArrayList<EtapaProduccionListResponse>
    var Descripcion: String = ""
    var CodeEtapa: String = ""

    private val REQUEST_ACTIVITY_FRAGMENT = 110
    private val REQUEST_ACTIVITY_DETALLE = 120

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_labor_cultural)
        tvTituloPersonal.text = "Consumo de fertilizantes"
        pref = PreferenceManager(this)
        fertilizanteViewModels = ViewModelProviders.of(this).get(fertilizanteViewModel::class.java)
        laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)

        initViews()
        setCabecera()
        setUpViews()
        setUpObservers()
        EtapaListar()

    }
    fun Spinner()
    {
        val adapter = this?.let {
            ArrayAdapter(
                it,
                R.layout.spinner, etapa
            )
        }

        if (adapter != null) {
            adapter.setDropDownViewResource(R.layout.spinner_list)
            spnEtapa.adapter = adapter

            spnEtapa.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    Descripcion = etapa.get(position)
                    Log.d("JSONPERSONAL",Descripcion)

                    for( item in etapaList)
                    {
                        if(item.U_VS_AGR_DSEP == Descripcion)
                        {
                            CodeEtapa = item.U_VS_AGR_CDEP
                            Log.d("JSONPERSONAL",CodeEtapa)

                        }
                    }
                    filter(etBuscador.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
    private fun filter(s: String)
    {
        val filter: ArrayList<VSAGRRFER> = ArrayList()
        Log.d("item.U_VS_AGR_DSCA",Descripcion)

        for (item in laborList) {

            if ( (item.EtapaNombre == Descripcion )|| (Descripcion =="Todas" ) ) {

                filter.add(item)
            }
        }
        rvLaborCultural.adapter = (FertilizantePEPAdapter(this,filter))
        rvLaborCultural.layoutManager = LinearLayoutManager(this)


    }
    fun initViews() {
        spnEtapa.visibility = View.VISIBLE
        etBuscador.visibility = View.GONE
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
        laborViewModels.etapaResult.observe(this, Observer {
            it?.let {
                etapaList = it as java.util.ArrayList<EtapaProduccionListResponse>
                etapa = java.util.ArrayList<String>()
                etapa.add("Todas")

                for (item in it) {
                    etapa.add(item.U_VS_AGR_DSEP)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }
                Spinner()
                LaborListar()

            }
        })
        fertilizanteViewModels.VSAGRFER.observe(this, Observer {
            it?.let { it1 ->
                /* val personal = PersonalDB.getDatabase(getApplication()).personalDao()
                 val list: List<LaborCulturalRoom>? = personal?.getLaborPorPEP(CodigoPEP)

                 if (list != null) {
                     for(item in list) {

                         var laborroom : LaborCulturalListResponse =
                             LaborCulturalListResponse(0,
                             0,0,0,"","","","","",item.id.toString(),"",
                             "","","","","","","","","",
                             "","","","",item.U_VS_AGR_CDCA,item.U_VS_AGR_CDPP,
                             item.U_VS_AGR_CDEP,item.U_VS_AGR_ACTV,"","","",
                                 item.U_VS_AGR_FERG,0,null
                             )
                         laborList.add(laborroom)
                     }
                 }
                 */
               /* var totalJornales = 0
                var index = 0
                for (item in laborList) {
                    if(item.VS_AGR_DCULCollection?.size!=0 && item.VS_AGR_DCULCollection != null) {
                        for (detail in item.VS_AGR_DCULCollection!!) {
                            totalJornales += detail.U_VS_AGR_TOJR
                        }

                        var personal : List<LaborCulturalDetalleRoom>? = laborViewModels.getLaborDetalleRoom(item.DocEntry.toInt())
                        if (personal != null) {
                            for(item in personal)
                            {
                                totalJornales += item.U_VS_AGR_TOJR
                            }
                        }
                    }

                    laborList.get(index).TOTALJORNALES = totalJornales
                    index++
                    totalJornales = 0
                }
                */
                laborList = it1 as ArrayList<VSAGRRFER>

                laborList.sortByDescending { it.U_VS_AGR_FERG }

                for (item in laborList) {
                    for(etapa in etapaList)
                    {
                        item.EtapaNombre = etapa.U_VS_AGR_DSEP
                    }
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }

                /* Spinner()*/


                rvLaborCultural.adapter = (FertilizantePEPAdapter(this, laborList))
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
            /*val intent = Intent(this, addLaborCulturalActivity::class.java)
            intent.putExtra("CODIGOPEP", CodigoPEP)
            intent.putExtra("CULTIVO", Cultivo)
            intent.putExtra("VARIEDAD", Variedad)
            intent.putExtra("CAMPANIA", Campania)
            intent.putExtra("CODIGOCAMPANIA", codigoCampania)
            intent.putExtra("TIPOCAMPANIA", tipoCampania)
            intent.putExtra("DOCENTRYPEP", DocEntryPEP)



            startActivity(intent)*/

        }
        etBuscador.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //filter(s.toString());

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
    /*private fun filter(s: String)
    {
        val filter: java.util.ArrayList<LaborCulturalListResponse> = java.util.ArrayList()

        for (item in laborList) {
            if (item.U_VS_AGR_CDEP.toLowerCase().contains(s.toLowerCase())) {
                filter.add(item)
            }
        }
        rvLaborCultural.adapter = (laborCulturalPEPAdapter(this, filter))
        rvLaborCultural.layoutManager = LinearLayoutManager(this)

    }*/
    private fun LaborListar() {
        pref.getString(Constants.B1SESSIONID)?.let { fertilizanteViewModels.listaFertilizante(it, CodigoPEP,httpCacheDirectory, this) }
    }
    private fun EtapaListar() {
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listEtapa(it,CodigoPEP,httpCacheDirectory, this) }
    }
    fun hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    override fun laborItemClickListener(position: VSAGRRFER?) {

        /*var gson =  Gson()
        var jsonClass = gson.toJson(position)

        val intent = Intent(this, laborCulturalDetalleActivity::class.java)
        intent.putExtra("CODIGOPEP", CodigoPEP)
        intent.putExtra("CAMPANIA", Campania)
        intent.putExtra("CULTIVO", Cultivo)
        intent.putExtra("VARIEDAD", Variedad)
        intent.putExtra("CODIGOCAMPANIA", codigoCampania)
        intent.putExtra("DOCENTRYPEP", DocEntryPEP)


        if (position != null) {
            intent.putExtra("FECHA", position.U_VS_AGR_FERG)
                intent.putExtra("ETAPA", position.U_VS_AGR_CDEP)
            intent.putExtra("TIPOCAMPANIA", tipoCampania)
            intent.putExtra("DOCENTRY", position.DocEntry)
            startActivityForResult(intent, REQUEST_ACTIVITY_DETALLE)

        }*/

    }
    override fun onBackPressed() {

        val myIntent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(myIntent)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ELIMINAR", requestCode.toString())
        Log.d("ELIMINAR", resultCode.toString())

        when (requestCode) {
           /* REQUEST_ACTIVITY_FRAGMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(RESULT_OK)
                    finish()
                }*/


            }

        }


}

