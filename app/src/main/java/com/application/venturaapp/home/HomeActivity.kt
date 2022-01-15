package com.application.venturaapp.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.home.fragment.Sincronizacion
import com.application.venturaapp.home.fragment.fertilizante.FertilizanteFragment
import com.application.venturaapp.home.fragment.fitosanitario.FitosanitarioFragment
import com.application.venturaapp.home.fragment.maquinaria.MaquinariaFragment
import com.application.venturaapp.R
import com.application.venturaapp.home.fragment.produccion.laborCulturalFragment
import com.application.venturaapp.login.LoginActivity
import com.application.venturaapp.preference.PreferenceManager
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_personal_add.*
import kotlinx.android.synthetic.main.menu_main.*

import androidx.drawerlayout.widget.DrawerLayout


import android.widget.LinearLayout

import android.R.string.no




class HomeActivity : AppCompatActivity() {
    var homeActivity: Activity? = null
    private var toolbar: Toolbar? = null
    lateinit var btn_logout: Button
    lateinit var etNombreUsuarioMenu: TextView
    lateinit var etCargoUsuario: TextView

    val REQUEST_PERMISSION_CODE = 101
    private val listaPermisos = mutableListOf<String>()
    lateinit var pref: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homeActivity = this

        onInit()
        setupViews()
        setFragment(homeFragment())

        setupPermission()
    }
    private fun isSync(): Boolean {
        return pref.contains(Constants.SINCRONIZACION)!!

    }
    private fun onInit() {
       pref = PreferenceManager(this)
        val parser = JsonParser()
        //val mJson = parser.parse(pref.getString(Constants.LOGGED_USER))
        //loginResponse = Gson().fromJson<LoginDato>(mJson, LoginDato::class.java)
    }
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.fml_frag_container, fragment)
            .commit()
    }
    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        btn_logout = findViewById(R.id.btn_logout)
        etNombreUsuarioMenu = findViewById(R.id.etNombreUsuarioMenu)
        etCargoUsuario = findViewById(R.id.etCargoUsuario)
        etNombreUsuarioMenu.setText(pref.getString(Constants.USERNAME))
        etCargoUsuario.setText(pref.getString(Constants.PROFILECODE))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.logo_isotipo)
        val drawerToggle =
            object : ActionBarDrawerToggle(this, home_ventura, toolbar, R.string.open_drawer, R.string.close_drawer) {
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView!!)
                }

                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView!!)
                }
            }

        btn_logout.setOnClickListener(View.OnClickListener {
            if (homeActivity != null)
                this@HomeActivity.homeActivity!!.finish()


           pref.deleteKey(Constants.SESSIONID)
            pref.deleteKey(Constants.VERSION)
            pref.deleteKey(Constants.SINCRONIZACION)

            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        })

        home_ventura.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        /*etNombreUsuarioMenu.text = loginResponse?.nombreUsuario
        etCargoUsuario.text = loginResponse?.descripcionCargo*/

        trDatosMaestros.setOnClickListener {
            llDatosMaestros.visibility = View.VISIBLE
            when(llProduccion.visibility)
            {
                0 -> {
                    llProduccion.visibility = View.GONE
                }
            }

        }

        /*llDatosMaestros.setOnClickListener {
            setFragment(personalFragment())
            home_ventura.closeDrawers()
        }

        trPreparacion.setOnClickListener {
            setFragment(personalFragment())
            home_ventura.closeDrawers()
        } */
        trProduccion.setOnClickListener {
            llProduccion.visibility = View.VISIBLE
            when(llDatosMaestros.visibility)
            {
                0 -> {
                    llDatosMaestros.visibility = View.GONE
                }
            }
        }
        tvLabor.setOnClickListener {

            setFragment(laborCulturalFragment())
            home_ventura.closeDrawers()
        }
      /*  tvFitosanitario.setOnClickListener { //consumo de insumos

            setFragment(FitosanitarioFragment())
            home_ventura.closeDrawers()
        }
        tvMaquinarias.setOnClickListener {

            setFragment(MaquinariaFragment())
            home_ventura.closeDrawers()
        }*/

        tvFertilizantes.setOnClickListener { //cosecha

            setFragment(FertilizanteFragment())
            home_ventura.closeDrawers()
        }

    }

    private fun setupPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (withPermissions()) {
            } else {
                requestPermissions(REQUEST_PERMISSION_CODE, this)
            }
        }
    }

    fun withPermissions(): Boolean {
        listaPermisos.clear()
        val accessFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (accessFineLocationPermission != PackageManager.PERMISSION_GRANTED)
            listaPermisos.add(Manifest.permission.ACCESS_FINE_LOCATION)
        val externalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (externalPermission != PackageManager.PERMISSION_GRANTED)
            listaPermisos.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED)
            listaPermisos.add(Manifest.permission.CAMERA)
        val audioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        if (audioPermission != PackageManager.PERMISSION_GRANTED)
            listaPermisos.add(Manifest.permission.RECORD_AUDIO)

        return listaPermisos.isEmpty()
    }

    fun requestPermissions(requestCode: Int, activity: Activity) {
        ActivityCompat.requestPermissions(activity, listaPermisos.toTypedArray(), requestCode)
    }
}
