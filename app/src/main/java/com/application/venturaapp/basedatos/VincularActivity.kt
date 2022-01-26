package com.application.venturaapp.basedatos


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.login.LoginViewModel
import com.application.venturaapp.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.pop_vincular.*
import kotlinx.android.synthetic.main.pop_vincular.etUsuario
import okhttp3.Cache


class VincularActivity   : BottomSheetDialogFragment() {

    lateinit var    viewModel: LoginViewModel
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_BUSCAR = 101
    lateinit var httpCacheDirectory : Cache
     var peopleClass  = arrayListOf<String> ()
    lateinit var pref: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pop_vincular, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pref = context?.let { PreferenceManager(it.applicationContext) }!!

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)


        setupObserver()
        setupViews()


    }

    private fun setupViews() {

        btnValidar.setOnClickListener {
            if(validarCampos()) {
                pgbLaborRealizada.visibility = View.VISIBLE
                viewModel.LoginGeneral(
                    etDireccion.text.toString(),
                    etPuerto.text.toString(),
                    etBaseDatos.text.toString(),
                    etUsuario.text.toString(),
                    etContrasenia.text.toString()
                )
            } else
                Toast.makeText(activity, "Campos incompletos.", Toast.LENGTH_SHORT).show()

        }
    }
    private fun validarCampos() : Boolean
    {
        if(etDireccion.text.toString() == ""){
             return false
        }

        if(etPuerto.text.toString() == ""){
            return false
        }

        if(etBaseDatos.text.toString() == ""){
            return false
        }

        if(etUsuario.text.toString() == ""){
            return false
        }

        if(etContrasenia.text.toString() == ""){
            return false
        }

        return true
    }
    private fun setupObserver() {

        viewModel.loginResult.observe(this, Observer {
            it?.let {
                pgbLaborRealizada.visibility = View.GONE

                pref.saveString(Constants.URL, etDireccion.text.toString())
                pref.saveString(Constants.PUERTO, etPuerto.text.toString())
                pref.saveString(Constants.COMPANYDB,   etBaseDatos.text.toString())
                pref.saveString(Constants.USER, etUsuario.text.toString())
                pref.saveString(Constants.PASSWORD, etContrasenia.text.toString())

                btnEstado.text = "Conectado"

                    btnIngresar.isClickable = true
                    btnVincular.setText("Conectado")
                    btnVincular.isClickable = false
                    btnVincular.setBackgroundColor(resources.getColor(R.color.verdeVentura))

            }
        })

        viewModel.messageResult.observe(this, Observer {
            pgbLaborRealizada.visibility = View.GONE
            Toast.makeText(activity, "Error de conexión.", Toast.LENGTH_SHORT).show()
        })
    }


}

