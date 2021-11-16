package com.application.venturaapp.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.home.HomeActivity
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.home.fragment.produccion.laborViewModel
import com.application.venturaapp.laborCultural.laborCulturaViewModel
import com.application.venturaapp.login.LoginViewModel
import com.application.venturaapp.preference.PreferenceManager
import kotlinx.android.synthetic.main.fragment_labor_cultural.*
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.fragment_sincronizacion.*
import okhttp3.Cache

class Sincronizacion : AppCompatActivity() {
    lateinit var personalViewModels: personalViewModel
    lateinit var pref: PreferenceManager
    lateinit var httpCacheDirectory : Cache
    lateinit var laborViewModels: laborViewModel
    lateinit var laborCulturalViewModels: laborCulturaViewModel
    lateinit var viewModel: LoginViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sincronizacion)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        onInit()
        setUpViews()
        sincroPersonal()

    }
    private fun laborPlanificada(CodigoPEP: String)
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.laborPlanificada(it,CodigoPEP,null, httpCacheDirectory, this) }

    }
    private fun listaEtapa(code: String) {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.listEtapa(it,code, httpCacheDirectory, this) }

    }
    private fun LaborPorPEPListar(code: String) {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.listaLaborCultural(it, code,
            httpCacheDirectory, this) }
    }
    private fun LaboresListar() {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarLabor(it,
            httpCacheDirectory, this) }
    }
    private fun ProveedorListar() {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.listaProveedor(it,
            httpCacheDirectory, this) }
    }
    private fun laborListar() {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.listaLaborCultural(it
            ,
            httpCacheDirectory, this) }
    }
    private fun campaniaListar() {
        pgbDatos.visibility = View.VISIBLE
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.consultarCampania(it
            ,
            httpCacheDirectory, this)
        }
    }
    private fun sincroPersonal()
    {
        pgbDatos.visibility = View.VISIBLE
        setUpObservers()
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarPersonal(
            it,
            httpCacheDirectory, this
        ) }
    }
    private fun LaborListar() {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.listaLaborJornalCultural(it,

            httpCacheDirectory, this) }
    }
    private fun ListarDetalle(docEntry: String)
    {        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.listaDetalleLaborCultural(it, docEntry,
            httpCacheDirectory, this) }

    }
    private fun onInit() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        laborViewModels = ViewModelProviders.of(this).get(laborViewModel::class.java)
        laborCulturalViewModels = ViewModelProviders.of(this).get(laborCulturaViewModel::class.java)

        pref = PreferenceManager(this)

    }
    private fun setUpViews() {

        btnSincronizacion.setOnClickListener {

            startActivity(Intent(this@Sincronizacion, HomeActivity::class.java))
            finish()

        }
    }
    private fun setUpObservers() {

        personalViewModels.laborResult.observe(this, Observer {
            it?.let {
                ProveedorListar()

            }
        })
        personalViewModels.proveedorResult.observe(this, Observer {
            it?.let {
                campaniaListar()
            }
        })
        personalViewModels.personalResult.observe(this, Observer {
            it?.let {
                LaboresListar()

            }
        })
        laborViewModels.campaniaResult.observe(this, Observer {
            it?.let {
                laborListar()

            }
        })
        laborViewModels.laborResult.observe(this, Observer {
            it?.let {

                var i = 0
                while (i<it.size)
                {
                    LaborPorPEPListar(it[i].Code)
                    listaEtapa(it[i].Code)
                    laborPlanificada(it[i].Code)

                    i++
                }

            }
        })

        laborCulturalViewModels.laborResult.observe(this, Observer {
            it?.let {
                var i = 0
                while (i<it.size) {
                    ListarDetalle(it[i].DocEntry)
                    i++
                }
                LaborListar()
            }
        })
        laborCulturalViewModels.laborResult.observe(this, Observer {
            it?.let {
                pgbDatos.visibility = View.GONE
                ivPersonalCheck.visibility = View.VISIBLE
                btnSincronizacion.isEnabled = true
                btnSincronizacion.setBackgroundResource(R.drawable.btn_personal_guardar)
                pref.saveString(Constants.SINCRONIZACION, "TRUE")


            }
        })
        personalViewModels.messageResult.observe(this, Observer {
            it?.let {
                pgbDatos.visibility = View.GONE
                pref.saveString(Constants.MESSAGE_ALERT, it.toString())
                startActivity(Intent(this@Sincronizacion, AlertActivity::class.java))
            }
        })
    }
}