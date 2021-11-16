package com.application.venturaapp.home.fragment.produccion

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
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.home.fragment.adapter.laborCulturalAdapter
import com.application.venturaapp.home.fragment.entities.*
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.home.listener.LaborItemListener
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
import kotlinx.android.synthetic.main.fragment_personal.*
import okhttp3.Cache
import java.util.ArrayList

class laborCulturalFragment  : Fragment(), LaborItemListener {
    lateinit var pref: PreferenceManager
    lateinit var laborViewModels: laborViewModel
    lateinit var laborCulturalViewModels: laborCulturaViewModel

    lateinit var personalViewModels: personalViewModel

    lateinit var laborList  : ArrayList<LaborCulturalListResponse>
    var valuesJornales  = arrayListOf<Int>()
    private val REQUEST_ACTIVITY_FRAGMENT = 110
    internal lateinit var builder: AlertDialog.Builder
     var laborLista = arrayListOf<PEPDato>()
    lateinit var campanias  : ArrayList<String>
    lateinit var campaniasList  : ArrayList<Campania>
    var CodeCampania: String = ""
    var Descripcion: String = ""
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
        val intent = Intent(activity, laborCulturalActivity::class.java)
        intent.putExtra("CODIGOPEP",labor.Code)
        intent.putExtra("CAMPAÑA",labor.U_VS_AGR_DSCA)
        intent.putExtra("CODIGOCAMPANIA",labor.U_VS_AGR_CDCA)

        intent.putExtra("CULTIVO",labor.U_VS_AGR_CDCL)
        intent.putExtra("VARIEDAD",labor.U_VS_AGR_CDVA)
        intent.putExtra("TIPOCAMPANIA",tipoCampania)
        intent.putExtra("DOCENTRYPEP",labor.U_VS_AGR_DEOF)

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
        laborListar()
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
       laborViewModels.laborResult.observe(this, Observer {
            it?.let {
                laborList= ArrayList()
                if (it.size != 0)
                {
                    val codeList = ArrayList<String>()

                    laborLista = it as ArrayList<PEPDato>
                    enableViews(true)
                    for (item in it) {
                        codeList.add(item.Code)
                        //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                    }

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
        laborCulturalViewModels.laborResult.observe(this, Observer {
            it?.let {
                laborList = ArrayList()
                laborList = it as ArrayList<LaborCulturalListResponse>
                InsertarJornal(it)


            }
        })



        laborViewModels.campaniaResult.observe(this, Observer {
            it?.let {
                 campanias = ArrayList<String>()
                campaniasList = it as ArrayList<Campania>
                campanias.add("Todas")
                for (item in it) {
                    campanias.add(item.Name)
                    //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                }
                Spinner()
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
    fun InsertarJornal(list: ArrayList<LaborCulturalListResponse>)
    {
        var index=0
        var jornales=0
        for(pep in laborLista)
        {
            for(labor in  list)
            {
                if(pep.Code == labor.U_VS_AGR_CDPP)
                {  Log.d("JORNALESSSS",pep.Code +" "+ labor.U_VS_AGR_CDPP)
                    for(detalle in labor?.VS_AGR_DCULCollection!!)
                    {
                        jornales+=detalle.U_VS_AGR_TOJR
                        Log.d("JORNALESSSS", jornales.toString())

                    }
                }
            }
            Log.d("JORNALESSSS", jornales.toString())
            laborLista[index].TotalJornales = jornales
            index++
            jornales=0
        }
        jornales = 0
        index=0
        rvLaborCultural.adapter = (laborCulturalAdapter(this,laborLista))
        rvLaborCultural.layoutManager = LinearLayoutManager(activity)
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
                             Log.d("JSONPERSONAL",CodeCampania)

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
    private fun onInit() {

        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        laborViewModels = ViewModelProviders.of(this).get(laborViewModel::class.java)
        laborCulturalViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        pref = PreferenceManager(view!!.context)

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
    private fun filter(s: String)
    {
        val filter: ArrayList<PEPDato> = ArrayList()
        Log.d("item.U_VS_AGR_DSCA",Descripcion)
        for (item in laborLista) {

            if (( (item.U_VS_AGR_DSCA == Descripcion && (item.Code.toLowerCase().contains(s.toLowerCase())))||
                            (Descripcion =="Todas" && (item.Code.toLowerCase().contains(s.toLowerCase()))) )
                   ) {
                Log.d("item.U_VS_AGR_DSCA",item.U_VS_AGR_DSCA)

                filter.add(item)
            }
        }
        rvLaborCultural.adapter = (laborCulturalAdapter(this,filter))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ELIMINAR", requestCode.toString())
        Log.d("ELIMINAR", resultCode.toString())

        when (requestCode) {
            REQUEST_ACTIVITY_FRAGMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    LaborListar()
                    campaniaListar()
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d("ELIMINAR", "ELIMINAR")

                }

            }
        }
    }
}