package com.application.venturaapp.fitosanitario

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.venturaapp.R
import com.application.venturaapp.fitosanitario.adapter.FitosanitarioProductoAdapter
import com.application.venturaapp.fitosanitario.entity.VSAGRFEQU
import com.application.venturaapp.fitosanitario.listener.VSAGRFEQUEItemListener
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.laborCultural.adapter.popupDetalleAdapter
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.listener.PopupPersonalItemListener
import com.application.venturaapp.preference.PreferenceManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_fertilizante_pop.*
import okhttp3.Cache
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class popupFitosanitarioActivity   : AppCompatActivity(), VSAGRFEQUEItemListener {

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var Cultivo = ""
    private var Fecha = ""
    private var TipoCampania = ""
    private var Etapa = ""
    private var Estado = ""
    private var DocEntry:Int? = 0
    val codeList = ArrayList<String>()
    lateinit var personalViewModels: personalViewModel
    var productoList = arrayListOf<VSAGRFEQU> ()
    lateinit var pref: PreferenceManager
    lateinit var fitosanitarioViewModels: fitosanitarioViewModel

    lateinit var laborList  : ArrayList<LaborCulturalListResponse>
    lateinit var personalLabor : LaborCulturalDetalleResponse
    var CodigoLabor = ""
    var CodigoPersonal =""
    var  json=""
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_BUSCAR = 101
    lateinit var httpCacheDirectory : Cache
     var peopleClass  = arrayListOf<String> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.item_fertilizante_pop)
        pref = PreferenceManager(this)
       // laborViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)
        fitosanitarioViewModels = ViewModelProviders.of(this).get(fitosanitarioViewModel::class.java)


        // var gson =  Gson()

        if(intent.getStringArrayListExtra("OBJECT")?.size !=0) {
             peopleClass = intent.getStringArrayListExtra("OBJECT") as ArrayList<String>
            Log.d("PERSONALLISTA3", peopleClass.toString())
           // peopleClass = gson.fromJson(people.toString(), PersonalDato::class.java) as ArrayList<PersonalDato> //as? PersonalDato

        }

        //setUpViews()
        setUpObservers()
        listarQuimico()
        // setUpObservers()

    }
    private fun listarQuimico() {
        pref.getString(Constants.B1SESSIONID)?.let { fitosanitarioViewModels.listaProducto(
            it,
            httpCacheDirectory,this
        ) }
    }

    fun setUpObservers(){


        fitosanitarioViewModels.Producto.observe(this, Observer {
            it?.let {
                if (it.isNotEmpty()) {

                    for (item in it){
                        if(item.U_VS_AGR_TART == "Q"){
                            productoList.add(item)
                        }
                    }


                    rlDetalle.adapter = (FitosanitarioProductoAdapter(this, productoList))
                    rlDetalle.layoutManager = LinearLayoutManager(this)

                }
            }})


     /*   laborViewModels.messageUpdateResult.observe(this, Observer {
            it?.let {
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivityForResult(Intent(this@popupFitosanitarioActivity, AlertActivity::class.java), REQUEST_ACTIVITY)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d("ELIMINAR", "ELIMINAR2")
            }
        })*/
    }
    private fun filter(s: String)
    {
        val filter: java.util.ArrayList<VSAGRFEQU> = java.util.ArrayList()

        for (item in productoList) {
            if (item.Name.toLowerCase().contains(s.toLowerCase())
                    || item.Code.toLowerCase().contains(s.toLowerCase())) {
                filter.add(item)
            }
        }
        rlDetalle.adapter = (FitosanitarioProductoAdapter(this, filter))
        rlDetalle.layoutManager = LinearLayoutManager(this)


    }
    fun setUpViews() {

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ELIMINAR", requestCode.toString())
        Log.d("ELIMINAR", resultCode.toString())

        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(RESULT_OK)
                    finish()
                }

            }
        }
    }

    override fun laborItemClickListener(position: VSAGRFEQU?) {

        if (position != null) {
           /* pref.saveString(Constants.CODIGOBUSCADOR ,position.Code)
            pref.saveString(Constants.DNIBUSCADOR ,position.NumeroDocumento)
           // pref.saveString(Constants.LABORBUSCADOR ,position.CodigoLabor)
            pref.saveString(Constants.NOMBREBUSCADOR ,position.Name)
            //pref.saveString(Constants.DESCRIPCIONBUSCADOR ,position.DescripcionLabor)
            val resultIntent = Intent()

            setResult(RESULT_OK, resultIntent)

            finish()*/


        }

    }


}

