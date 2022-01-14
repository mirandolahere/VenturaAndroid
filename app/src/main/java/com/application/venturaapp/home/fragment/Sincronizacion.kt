package com.application.venturaapp.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
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
        setUpObservers()
        campaniaListar()


    }
    private fun campaniaListar() {
        pgbDatos.visibility = View.VISIBLE
        pref.getString(Constants.B1SESSIONID)?.let { laborViewModels.consultarCampania(it
            ,
            httpCacheDirectory, this)
        }
    }
    private fun fundoListar() {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let {

                laborViewModels.consultarFundo(
                    it,
                    httpCacheDirectory,
                    this
                )

        }
    }
    fun sectorListar(){
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let {

                laborViewModels.consultarSector(
                    "",
                    it,
                    httpCacheDirectory,
                    this
                )

        }
    }
    private fun laborListar() {
        pgbDatos.visibility = View.VISIBLE
        pref.getString(Constants.B1SESSIONID)?.let {

                laborViewModels.listaLaborCultural(
                    it,
                    httpCacheDirectory,
                    this
                )

        }
    }

    private fun LaborListar() {
        pgbDatos.visibility = View.VISIBLE

        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.listaLaborJornalCultural(it,

            httpCacheDirectory, this) }
    }
    private fun LaboresListar() {
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarLabor(it,httpCacheDirectory, this) }
    }

    fun ListarDetalle(docEntry: String)
    {
        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.listaDetalleLaborCultural(it, docEntry,
            httpCacheDirectory, this) }

    }

    private fun laborPlanificada(code: String) {
        pref.getString(Constants.B1SESSIONID)?.let { laborCulturalViewModels.laborPlanificada(
            it,
            code,
            null,
            httpCacheDirectory,
            this
        ) }
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
        laborViewModels.campaniaResult.observe(this, Observer {
            it?.let {
                fundoListar()

            }
        })
        laborViewModels.fundoResult.observe(this, Observer {
            it?.let {
                sectorListar()

            }
        })

        laborViewModels.sectorResult.observe(this, Observer {
            it?.let {
                laborListar()

            }
        })

        laborViewModels.laborResult.observe(this, Observer {

            var i = 0
            while (i<it.size)
            {
                laborPlanificada(it[i].Code)

                i++
            }
            LaborListar()


        })

        laborCulturalViewModels.responseLaborPlanificadaResult.observe(this, Observer {


        })

        laborCulturalViewModels.laborResult.observe(this, Observer {
         it?.let {
                    var i = 0
                    while (i<it.size) {
                        ListarDetalle(it[i].DocEntry)
                        i++
                    }

         }
        })
        laborCulturalViewModels.laborPorCodeResult.observe(this, Observer {
            LaboresListar()

        })

        personalViewModels.laborResult.observe(this, Observer {
            it?.let {


                pgbDatos.visibility = View.GONE
                ivPersonalCheck.visibility = View.VISIBLE
                btnSincronizacion.isEnabled = true
                btnSincronizacion.setBackgroundResource(R.drawable.btn_personal_guardar)
                pref.saveString(Constants.SINCRONIZACION, "TRUE")

            }
        })

    }
}