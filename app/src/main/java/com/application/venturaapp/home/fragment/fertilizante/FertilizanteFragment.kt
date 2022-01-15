package com.application.venturaapp.home.fragment.fertilizante

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.venturaapp.R
import com.application.venturaapp.fertilizante.fertilizanteActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.home.fragment.adapter.FertilizanteAdapter
import com.application.venturaapp.home.fragment.entities.*
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.home.fragment.produccion.laborViewModel
import com.application.venturaapp.home.listener.FitosanitarioItemListener
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.laborCultural.laborCulturalActivity
import com.application.venturaapp.login.LoginViewModel
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.retrofit.NetworkBroadcastReceiver
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.application.venturaapp.tables.LaborCulturalRoom
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_labor_cultural.*
import kotlinx.android.synthetic.main.fragment_labor_cultural.etBuscador
import kotlinx.android.synthetic.main.fragment_labor_cultural.pgbLaborRealizada
import kotlinx.android.synthetic.main.fragment_labor_cultural.rvLaborCultural
import okhttp3.Cache
import java.util.*
import kotlin.collections.ArrayList

class FertilizanteFragment  : Fragment(), FitosanitarioItemListener {
    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborViewModel
    lateinit var laborCulturalViewModels: laborCulturaViewModel
    lateinit var personalViewModels: personalViewModel
    lateinit var fertilizanteViewModels: fertilizanteViewModel


    lateinit var laborList  : ArrayList<LaborCulturalListResponse>
    var valuesJornales  = arrayListOf<Int>()
    private val REQUEST_ACTIVITY_FRAGMENT = 110
    internal lateinit var builder: AlertDialog.Builder
     var laborLista = arrayListOf<PEPDato>()
    lateinit var campanias  : ArrayList<String>
    lateinit var campaniasList  : ArrayList<Campania>
    var CodeCampania: String = ""
    var Descripcion: String = ""

    lateinit var fundoList: ArrayList<Fundo>
    lateinit var lotesList: ArrayList<Lote>
    lateinit var sectorList: ArrayList<Sector>

    lateinit var lotesCollection : ArrayList<Sector>


    var fundo = arrayListOf<String>()
    var sector = arrayListOf<String>()
    var lotes = arrayListOf<String>()

    var CodeFundo = ""
    var DescripcionFundo = ""
    var CodeSector = ""
    var DescripcionSector = ""
    var CodeLote = ""
    var DescripcionLote = ""


    var indice = 0
    lateinit var httpCacheDirectory : Cache
    lateinit var broadcast : BroadcastReceiver
    lateinit var viewModel: LoginViewModel

    override fun laborItemClickListener(labor: PEPDato) {
        var gson =  Gson()
        var jsonClass = gson.toJson(labor)
        var tipoCampania:String =""
        for(item in campaniasList)
        {
            if(item.Name ==labor.U_VS_AGR_DSCA)
            {
                tipoCampania = item.U_VS_AGR_TICA

            }
        }
        val intent = Intent(activity, fertilizanteActivity::class.java)
        intent.putExtra("CODIGOPEP",labor.Code)
        intent.putExtra("CAMPAÑA",labor.U_VS_AGR_DSCA)
        intent.putExtra("CODIGOCAMPANIA",labor.U_VS_AGR_CDCA)

        intent.putExtra("CULTIVO",labor.U_VS_AGR_CDCL)
        intent.putExtra("VARIEDAD",labor.U_VS_AGR_CDVA)
        intent.putExtra("TIPOCAMPANIA",tipoCampania)
        intent.putExtra("DOCENTRYPEP",labor.U_VS_AGR_DEOF)
        intent.putExtra("ARTICULO",labor.U_VS_AGR_CDAT)
        intent.putExtra("DESCRIPCION",labor.U_VS_AGR_DSAT)


        startActivityForResult(intent, REQUEST_ACTIVITY_FRAGMENT )
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_labor_cultural, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = activity?.cacheDir?.let { Cache(it, cacheSize) }!!
        broadcast =  NetworkBroadcastReceiver()

        onInit()

        if(verificarData() )
            broascastReceiver()

        setUpViews()
        setUpObservers()
        campaniaListar()

    }
    fun verificarData():Boolean
    {
        //verificamos si hay datos en la memoria del telefono
        var personal : List<PersonalDatoRoom>? = personalViewModels.getPersonalRoom()
        var labor : List<LaborCulturalRoom>? = laborCulturalViewModels.getLaborRoom()
        var labordetalle : List<LaborCulturalDetalleRoom>? = laborCulturalViewModels.getLaborDetalleAllRoom()

        if(personal?.size==0 && labor?.size == 0 && labordetalle?.size == 0)
            return false

        return true

    }
    protected fun broascastReceiver()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N)
        {
            activity?.registerReceiver(broadcast,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )

        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            activity?.registerReceiver(broadcast,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )

        }
    }
    protected fun unregisterNetwork()
    {
        activity?.unregisterReceiver(broadcast)
    }
    override fun onDestroy() {
        super.onDestroy()
        if(!checkInternet())
            unregisterNetwork()
    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }
    private fun setUpObservers() {
        laborCulturalViewModels.laborResult.observe(this, Observer {
            it?.let {
                laborList = java.util.ArrayList()
                laborList = it as java.util.ArrayList<LaborCulturalListResponse>
                InsertarJornal(it)


            }
        })
        laborViewModels.laborResult.observe(this, Observer {
            it?.let {

                if (it.size != 0)
                {
                    val codeList = java.util.ArrayList<String>()

                    var peps = PEPDato("","",0,"","","","","","","","","","","","",
                        "",0.0,0.0,"","","","","","","","",
                        "","","","","","",
                        "","","","","","",
                        "","","","","","",
                        "","","","","","",
                        "","","","")
                    for(item in campaniasList.iterator()){

                        if(item.VS_AGR_CAPPCollection.size!=0) {
                            for (pep in item.VS_AGR_CAPPCollection.iterator()) {
                                peps.Code = pep.U_VS_AGR_CDPP
                                peps.Name = pep.U_VS_AGR_DSPP
                                peps.U_VS_AGR_CDFD = pep.U_VS_AGR_CDFD
                                peps.U_VS_AGR_DSFD = pep.U_VS_AGR_DSFD
                                peps.U_VS_AGR_CDSC = pep.U_VS_AGR_CDSC
                                peps.U_VS_AGR_DSSC = pep.U_VS_AGR_DSSC
                                peps.U_VS_AGR_CDLT = pep.U_VS_AGR_CDLT
                                peps.U_VS_AGR_CDCL = item.U_VS_AGR_CDCL
                                peps.U_VS_AGR_CDVA = item.U_VS_AGR_CDVA
                                peps.U_VS_AGR_DSVA = item.U_VS_AGR_DSVA
                                peps.U_VS_AGR_CDAT = item.U_VS_AGR_CDAT
                                peps.U_VS_AGR_DSAT = item.U_VS_AGR_DSAT
                                peps.U_VS_AGR_CDCA = item.Code
                                peps.U_VS_AGR_DSCA = item.Name
                                peps.UpdateDate = item.UpdateDate
                                laborLista.add(peps)
                                peps = PEPDato("","",0,"","","","","","","","","","","","",
                                    "",0.0,0.0,"","","","","","","","",
                                    "","","","","","",
                                    "","","","","","",
                                    "","","","","","",
                                    "","","","","","",
                                    "","","","")


                            }

                        }
                    }

                  //  laborLista = it as java.util.ArrayList<PEPDato>
                    enableViews(true)
                    for (item in it) {
                        codeList.add(item.Code)
                        //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                    }
                    rvLaborCultural.layoutManager = LinearLayoutManager(activity)
                    pgbLaborRealizada.visibility = View.GONE

                    LaborListar()


                }
                else
                {
                    pgbLaborRealizada.visibility = View.GONE
                    enableViews(true)

                    Snackbar.make(requireActivity().window.decorView.findViewById(android.R.id.content),
                        "No se encontró información", Snackbar.LENGTH_SHORT)
                }



            }
        })
        laborViewModels.campaniaResult.observe(this, Observer {
            it?.let {
                Log.d("PRUEBA","PRUEBA")
                 campanias = ArrayList<String>()
                campaniasList = it as ArrayList<Campania>
                campanias.add("Todas")
                for (item in it) {
                    campanias.add(item.Name)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }
                fundoListar()

            }
        })
        laborViewModels.fundoResult.observe(this, Observer {
            it?.let {

                fundo = java.util.ArrayList<String>()
                fundoList = it as java.util.ArrayList<Fundo>
                fundo.add("Todas")
                for (item in it) {
                    fundo.add(item.Name)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }
                Spinner()
                laborListar()

            }
        })


        laborViewModels.sectorResult.observe(this, Observer {
            it?.let {

                sector = java.util.ArrayList<String>()
                sectorList = it as java.util.ArrayList<Sector>
                lotesCollection = it
                sector.add("Todas")

                for (item in it) {
                    sector.add(item.Name)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }
                spinnerSector()
            }
        })


        laborViewModels.messageResult.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = View.GONE
                enableViews(true)
                showMessage(it)
            }
        })
        laborViewModels.codeResult.observe(this, Observer {
            it?.let {
                viewModel.LoginGeneral()

            }
        })
        viewModel.loginResult.observe(this, Observer {
            it?.let {

                pref.saveString(Constants.SESSIONID, Gson().toJson(it.SessionId))
                pref.saveString(Constants.B1SESSIONID, "B1SESSION=" + it.SessionId)

                pref.saveString(Constants.VERSION, it.Version)

                laborListar()
                campaniaListar()


            }
        })
    }
    private fun LaborListar() {
        pref.getString(Constants.B1SESSIONID)?.let {
            activity?.let { it1 ->
                laborCulturalViewModels.listaLaborJornalCultural(
                    it,
                    httpCacheDirectory,
                    it1
                )
            }
        }
    }
    private fun spinnerSector() {
        val adapterSector = context?.let {
            ArrayAdapter(
                it,
                R.layout.spinner, sector
            )
        }

        if (adapterSector != null) {
            adapterSector.setDropDownViewResource(R.layout.spinner_list)
            splSector.adapter = adapterSector

            splSector.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    DescripcionSector = sector.get(position)

                    for( item in sectorList)
                    {
                        if(item.Name == DescripcionSector)
                        {
                            CodeSector = item.Code
                            lotesListar()
                            break

                        }
                        else{
                            CodeSector = DescripcionSector
                        }
                    }

                    filter(CodeCampania, CodeFundo, CodeSector, CodeLote)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    fun lotesListar(){

        lotes.clear()
        lotes.add("Todas")

        for(item in lotesCollection){
            if(item.Code == CodeSector){

                lotesList = item.VS_AGR_LOTECollection as java.util.ArrayList<Lote>

                for(item in item.VS_AGR_LOTECollection){
                    lotes.add(item.U_VS_AGR_DSLT)
                }

                loteSpinner()
            }
        }
    }

    private fun loteSpinner() {
        val adapterLote = context?.let {
            ArrayAdapter(
                it,
                R.layout.spinner, lotes
            )
        }

        if (adapterLote != null) {
            adapterLote.setDropDownViewResource(R.layout.spinner_list)
            splLote.adapter = adapterLote

            splLote.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    DescripcionLote = lotes.get(position)

                    for( item in lotesList)
                    {
                        if(item.U_VS_AGR_DSLT == DescripcionLote)
                        {
                            CodeLote = item.U_VS_AGR_CDLT
                            break

                        } else{
                            CodeLote = DescripcionLote
                        }
                    }

                    filter(CodeCampania, CodeFundo, CodeSector, CodeLote)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
    fun InsertarJornal(list: ArrayList<LaborCulturalListResponse>)
    {
      /*  var index=0
        var jornales=0
        for(pep in laborLista)
        {
            for(labor in  list)
            {
                if(pep.Code == labor.U_VS_AGR_CDPP)
                {
                    for(detalle in labor?.VS_AGR_DCULCollection!!)
                    {
                        jornales+=detalle.U_VS_AGR_TOJR


                    }
                }
            }
            Log.d("JORNALESSSS", jornales.toString())
            laborLista[index].TotalJornales = jornales
            index++
            jornales=0
        }
        jornales = 0
        index=0 */
        rvLaborCultural.adapter = (FertilizanteAdapter(this,laborLista))
        rvLaborCultural.layoutManager = LinearLayoutManager(activity)
        pgbLaborRealizada.visibility = View.GONE

        for((index,item) in laborList.withIndex())
        {
           item.TOTALJORNALES = 0
        }
        pgbLaborRealizada.visibility = View.GONE


    }
    fun Spinner()
    {
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.spinner, campanias
            )
        }

        val adapterFundo = context?.let {
            ArrayAdapter(
                it,
                R.layout.spinner, fundo
            )
        }

        if (adapter != null) {
            adapter.setDropDownViewResource(R.layout.spinner_list)
            splCampaniasVigentes.adapter = adapter

            splCampaniasVigentes.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    Descripcion = campanias.get(position)
                    //Log.d("JSONPERSONAL",Descripcion)

                    for( item in campaniasList)
                    {
                        if(item.Name == Descripcion)
                        {
                            CodeCampania = item.Code
                            break

                        }else{
                            CodeCampania = Descripcion
                        }
                    }
                    filter(CodeCampania, CodeFundo, CodeSector, CodeLote)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        if (adapterFundo != null) {
            adapterFundo.setDropDownViewResource(R.layout.spinner_list)
            splFundo.adapter = adapterFundo

            splFundo.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    DescripcionFundo = fundo.get(position)

                    for( item in fundoList)
                    {
                        if(item.Name == DescripcionFundo)
                        {
                            CodeFundo = item.Code
                            sectorListar()
                            break

                        }else{
                            CodeFundo = DescripcionFundo
                        }
                    }
                    filter(CodeCampania, CodeFundo, CodeSector, CodeLote)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

    }
    private fun onInit() {
        fbAdd.visibility =View.GONE
        laborViewModels = ViewModelProviders.of(this).get(laborViewModel::class.java)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        fertilizanteViewModels = ViewModelProviders.of(this).get(fertilizanteViewModel::class.java)
        laborCulturalViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        pref = PreferenceManager(view!!.context)
        tvTitulo.text = "COSECHA"
        builder = AlertDialog.Builder(view!!.context)
        builder.setCancelable(false)

        /*val parser = JsonParser()
        val mJson = parser.parse(pref.getString(Constants.LOGGED_USER))

        loginResponse = Gson().fromJson(mJson, LoginDato::class.java)*/
    }
    private fun laborListar() {
        pgbLaborRealizada.visibility = View.VISIBLE
        enableViews(false)
        pref.getString(Constants.B1SESSIONID)?.let {
            activity?.let { it1 ->
                laborViewModels.listaLaborCultural(
                    it,
                    httpCacheDirectory,
                    it1
                )
            }
        }
    }
    fun sectorListar(){
        enableViews(false)
        pref.getString(Constants.B1SESSIONID)?.let {
            activity?.let { it1 ->
                laborViewModels.consultarSector(
                    CodeFundo,
                    it,
                    httpCacheDirectory,
                    it1
                )
            }
        }
    }
    private fun setUpViews() {
       /* etBuscador.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                filter(s.toString());

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })*/
    }

    private fun showMessage(m: String?) {
       /* builder.setTitle(Constants.APP_NAME)
        builder.setMessage(m)
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()*/
    }

    private fun enableViews(enable: Boolean) {
      /*  rvInspecciones.isEnabled = enable
        btnInspeccionNoPlaneada.isEnabled = enable
        ivMapaInspeccionesPendienes.isEnabled = enable*/
    }
    private fun filter(pep: String, CodeFundo: String, CodeSector: String, CodeLote: String)
    {
        val filter: ArrayList<PEPDato> = ArrayList()
        for (item in laborLista) {

            if ((item.U_VS_AGR_CDCA==pep ||  pep == "Todas") &&
                (item.U_VS_AGR_CDFD==CodeFundo ||  CodeFundo == "Todas") &&
                (item.U_VS_AGR_CDSC==CodeSector ||  CodeSector == "Todas" || CodeSector == "") &&
                (item.U_VS_AGR_CDLT==CodeLote ||  CodeLote == "Todas" || CodeLote == "")) {

                filter.add(item)
            }
        }
        rvLaborCultural.adapter = (FertilizanteAdapter(this,filter))
        rvLaborCultural.layoutManager = LinearLayoutManager(activity)


    }
    private fun campaniaListar() {
        enableViews(false)
        pref.getString(Constants.B1SESSIONID)?.let {
            activity?.let { it1 ->
                laborViewModels.consultarCampania(
                    it,
                    httpCacheDirectory,
                    it1
                )
            }
        }
    }

    private fun fundoListar() {
        enableViews(false)
        pref.getString(Constants.B1SESSIONID)?.let {
            activity?.let { it1 ->
                laborViewModels.consultarFundo(
                    it,
                    httpCacheDirectory,
                    it1
                )
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ELIMINAR", requestCode.toString())
        Log.d("ELIMINAR", resultCode.toString())

        when (requestCode) {
            REQUEST_ACTIVITY_FRAGMENT -> {
                if (resultCode == Activity.RESULT_OK) {

                    campaniaListar()
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d("ELIMINAR", "ELIMINAR")

                }

            }
        }
    }
}