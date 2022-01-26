package com.application.venturaapp.home.fragment.personal

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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.home.fragment.adapter.MenuAdapter
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.listener.MenuItemListener
import com.application.venturaapp.home.listener.MenuItemListenerRoom
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.login.LoginViewModel
import com.application.venturaapp.personalAdd.personalAdd
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.retrofit.NetworkBroadcastReceiver
import com.application.venturaapp.tables.LaborCulturalDetalleRoom
import com.application.venturaapp.tables.LaborCulturalRoom
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_personal_add.*
import kotlinx.android.synthetic.main.fragment_personal.*
import okhttp3.Cache
import java.util.*


@Suppress("DEPRECATION")
class personalFragment  : Fragment(), MenuItemListener , MenuItemListenerRoom {
    lateinit var pref: PreferenceManager
    lateinit var personalViewModels: personalViewModel
    lateinit var laborCulturaViewModels: laborCulturaViewModel
    lateinit var viewModel: LoginViewModel

    internal lateinit var builder: AlertDialog.Builder
     var personalLista = arrayListOf<PersonalDato>()
    private val REQUEST_ACTIVITY = 101
    lateinit var httpCacheDirectory : Cache
    lateinit var broadcast : BroadcastReceiver

    override fun menuItemClickListener(accion: Int?, position: PersonalDato) {
        var gson =  Gson()
        var jsonClass = gson.toJson(position)
        if(checkInternet() || (!checkInternet() && accion == 1) ) {
            val intent = Intent(activity, personalAdd::class.java)
            intent.putExtra("ACCION", accion)
            intent.putExtra("OBJECTPERSONA", jsonClass)
            startActivityForResult(intent, REQUEST_ACTIVITY)
        }else
        {
            val builder =  android.app.AlertDialog.Builder(activity)
            builder.setTitle("Editar personal")
            builder.setMessage("Operación no permitida de modo offline.")
            builder.show()
        }
    }
    override fun menuItemRoomClickListener(accion: Int?, position: PersonalDatoRoom) {
        var gson =  Gson()
        var jsonClass = gson.toJson(position)

        val intent = Intent(activity, personalAdd::class.java)
        intent.putExtra("ACCION", accion)
        intent.putExtra("OBJECTPERSONA", jsonClass)
        startActivity(intent)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        httpCacheDirectory = activity?.cacheDir?.let { Cache(it, 10 * 1024 * 1024) }!!
        broadcast =  NetworkBroadcastReceiver()
        onInit()

            if(verificarData() )
                broascastReceiver()

            setUpViews()
            setUpObservers()
            personalListar()

        enableViews(false)
    }
    fun verificarData():Boolean
    {
        //verificamos si hay datos en la memoria del telefono
        var personal : List<PersonalDatoRoom>? = personalViewModels.getPersonalRoom()
        var labor : List<LaborCulturalRoom>? = laborCulturaViewModels.getLaborRoom()
        var labordetalle : List<LaborCulturalDetalleRoom>? = laborCulturaViewModels.getLaborDetalleAllRoom()

        if(personal?.size==0 && labor?.size == 0 && labordetalle?.size == 0)
            return false

        return true

    }
    protected fun broascastReceiver()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            activity?.registerReceiver(broadcast,IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            activity?.registerReceiver(broadcast,IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

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
        personalViewModels.personalResult.observe(this, Observer {
            it?.let {
                if (it.size != 0) {
                    val codeList = ArrayList<String>()
                     personalLista = arrayListOf()

                    //personalLista = it as ArrayList<PersonalDato>

                    for(item in it)
                    {
                        if(item.Activo == "Y")
                            personalLista.add(item)

                    }

                    enableViews(true)
                    for (item in it) {
                        if(item.Activo == "Y")
                            codeList.add(item.Code)
                        //idInspeccionList.add(inspeccionePendiente.idInspeccion)
                    }
                    var personal : List<PersonalDatoRoom>? = personalViewModels.getPersonalRoom()
                    if (personal != null) {
                        for(item in personal) {
                            val personal = PersonalDato(
                                "",
                                item.Code,
                                item.Name,
                                item.PrimerNombre,item.SegundoNombre,item.ApellidoPaterno,item.ApellidoMaterno,item.TipoDocumento,
                                item.NumeroDocumento,null,null,null,
                                item.CodigoLabor, item.DescripcionLabor,item.FechaNacimiento,null,item.TipoEmpleado,
                                item.CodigoProveedor,item.NombreProveedor, "Y", null,null,null,null)
                            personalLista.add(personal)
                        }
                    }
                    rvInspecciones.adapter = (MenuAdapter(this, personalLista))
                    rvInspecciones.layoutManager = LinearLayoutManager(activity)

                    pgbPendienteRealizada.visibility = View.GONE

                } else {
                    pgbPendienteRealizada.visibility = View.GONE
                    enableViews(true)

                    Snackbar.make(
                            requireActivity().window.decorView.findViewById(android.R.id.content),
                            "No se encontró información", Snackbar.LENGTH_SHORT
                    )
                }

            }
        })

        personalViewModels.messageResult.observe(this, Observer {
            it?.let {
                pgbPendienteRealizada.visibility = View.GONE
                enableViews(true)
                //showMessage(it)
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivity(Intent(requireContext(), AlertActivity::class.java))
            }
        })

        personalViewModels.codeResult.observe(this, Observer {
            it?.let {
                viewModel.LoginGeneral( pref.getString(Constants.URL)!!,
                    pref.getString(Constants.PUERTO)!!,
                    pref.getString(Constants.COMPANYDB)!!,
                    pref.getString(Constants.USER)!!,
                    pref.getString(Constants.PASSWORD)!!)

            }
        })
        viewModel.loginResult.observe(this, Observer {
            it?.let {

                pref.saveString(Constants.SESSIONID, Gson().toJson(it.SessionId))
                pref.saveString(Constants.B1SESSIONID, "B1SESSION=" + it.SessionId)

                pref.saveString(Constants.VERSION, it.Version)

                personalListar()


            }
        })
    }

    private fun onInit() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        laborCulturaViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        pref = PreferenceManager(view!!.context)

        builder = AlertDialog.Builder(view!!.context)
        builder.setCancelable(false)

        val parser = JsonParser()
       // val mJson = parser.parse(pref.getString(Constants.SESSIONID))

        //loginResponse = Gson().fromJson(mJson, LoginDato::class.java)
    }
     fun personalSave()
    {
        var personal : List<PersonalDatoRoom>? = personalViewModels.getPersonalRoom()
        if (personal != null) {
            for(item in personal) {
                val personal = PersonalDato(
                    "",
                    item.Code,
                    item.Name,
                    item.PrimerNombre,item.SegundoNombre,item.ApellidoPaterno,item.ApellidoMaterno,item.TipoDocumento,
                    item.NumeroDocumento,null,null,null,
                    item.CodigoLabor, item.DescripcionLabor,item.FechaNacimiento,null,item.TipoEmpleado,
                    item.CodigoProveedor,item.NombreProveedor, "Y", null,null,null,null)
                personalLista.add(personal)
            }
        }
    }
    private fun personalListar() {

        pgbPendienteRealizada.visibility = View.VISIBLE
        enableViews(false)
        pref.getString(Constants.B1SESSIONID)?.let { context?.let { it1 ->
            personalViewModels.consultarPersonal(it, httpCacheDirectory ,
                it1
            )
        } }
    }
    private fun setUpViews() {
        rlFragmentPersonal.setOnClickListener {

        }

        ivPersonalAdd.setOnClickListener {

            if(checkInternet()) {
                val intent = Intent(activity, personalAdd::class.java)
                intent.putExtra("ACCION", 0)
                startActivityForResult(intent, REQUEST_ACTIVITY)

            }else
                {
                    val builder =  android.app.AlertDialog.Builder(activity)

                    builder.setTitle("Agregar personal")
                    builder.setMessage("Operación no permitida de modo offline.")
                    builder.show()
                }
        }

        swipeRefreshLayout.setOnRefreshListener {
            setUpObservers()
            personalListar()
            swipeRefreshLayout.isRefreshing = false;

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
    }

    private fun filter(s: String)
    {
        val filter: ArrayList<PersonalDato> = ArrayList()

        for (item in personalLista) {
            if (item.Name.toLowerCase().contains(s.toLowerCase())
                    || item.Code.toLowerCase().contains(s.toLowerCase())) {
                filter.add(item)
            }
        }
        rvInspecciones.adapter = (MenuAdapter(this, filter))
        rvInspecciones.layoutManager = LinearLayoutManager(activity)

    }

    private fun showMessage(m: String?) {
        builder.setTitle(Constants.APP_NAME)
        builder.setMessage(m)
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun enableViews(enable: Boolean) {
        ivPersonalAdd.isEnabled = enable
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("RESULT_OK",resultCode.toString())
        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                }
                if (resultCode == Activity.RESULT_OK) {
                    personalListar()
                }
            }
        }
    }
}

