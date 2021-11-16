package com.application.venturaapp.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.basedatos.VincularActivity
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.home.HomeActivity
import com.application.venturaapp.home.fragment.Sincronizacion
import com.application.venturaapp.preference.PreferenceManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Cache
import java.util.regex.Pattern


class
LoginActivity : AppCompatActivity() {

    lateinit var    viewModel: LoginViewModel
    lateinit var pref: PreferenceManager
    lateinit var httpCacheDirectory : Cache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)
        setContentView(R.layout.activity_login)
        initParameters()
        if (isUserLogged()) {
            if(pref.getString(Constants.RECC) == "N")
                launchHome()
            else
            {
                startActivity(Intent(this@LoginActivity, usuarioActivity::class.java))
                finish()
            }

        } else{
            setupViews()
           // Spinner()
            setupObservers()
        }
    }


    private fun initParameters() {

        pref = PreferenceManager(this)


        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }
    fun hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    private fun setupViews() {
      /*  btn_enter.setOnClickListener {
            enableViews(false)
            viewModel.userLogin(etx_username.text.toString().trim(), etx_password.text.toString().trim())
        }*/
        container.setOnClickListener {
            hideSoftKeyboard()
        }
        tvOlvidarContraseña.setOnClickListener {
            olvideView(false)

        }

        btnVincular.setOnClickListener {
            VincularActivity().apply {
                show(supportFragmentManager, "MY_BOTTOM_SHEET")
            }

        }

        tvCancelar.setOnClickListener {
            returnLogin(false)

        }
        btnIngresar.setOnClickListener {
            enableViews(false)
            if(checkInternet())
                viewModel.LoginGeneral()
            else {
                if( pref.getString(Constants.B1SESSIONID)!= null) {
                    pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                        viewModel.userLogin(
                            etUsuario.text.toString().trim(),
                            etPassword.text.toString().trim(),
                            it1,
                            httpCacheDirectory,
                            this,
                            0
                        )
                    }
                }else
                {
                    val builder =  AlertDialog.Builder(this)

                    builder.setTitle("¡Error!")
                    builder.setMessage("Active sus datos móviles para la primera sincronización")
                    builder.show()
                }//
            }

        }


        btnEnviar.setOnClickListener {
            enableViews(false)
            if(validar(etEmail.text.toString().trim()))
            {
                etEmail.setBackgroundResource(R.drawable.edittext_border_error)
            }else
            viewModel.forgetPass(etEmail.text.toString().trim())
        }
    }
    fun validar(code: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        return pattern.matcher(code).matches()
    }
    fun Spinner()
    {
        val Empresas = resources.getStringArray(R.array.Empresas)
        val adapter = ArrayAdapter(this,
                R.layout.spinner_list, Empresas)
        adapter.setDropDownViewResource(R.layout.spinner_list)
        spEmpresa.isEnabled = false


    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }
    private fun setupObservers() {
        viewModel.loginResult.observe(this, Observer {
            it?.let {

                pref.saveString(Constants.SESSIONID, Gson().toJson(it.SessionId))
                pref.saveString(Constants.B1SESSIONID, "B1SESSION=" + it.SessionId)

                pref.saveString(Constants.VERSION, it.Version)
                pref.getString(Constants.B1SESSIONID)?.let { it1 -> viewModel.userLogin(etUsuario.text.toString().trim(), etPassword.text.toString().trim()
                       , it1,httpCacheDirectory,this, 0) } //

            }
        })
        viewModel.loginUsersResult.observe(this, Observer {
            it?.let {

                pref.saveString(Constants.USERNAME, it.U_VS_AGR_BPAP+" " + it.U_VS_AGR_BPAM +" "+it.U_VS_AGR_BPNO )
                if( it.U_VS_AGR_DSPR!=null) {
                    pref.saveString(Constants.PROFILECODE, it.U_VS_AGR_DSPR)
                    pref.saveString(Constants.CODIGOUSUARIO, it.Code)
                    pref.saveString(Constants.RECC, it.U_VS_AGR_RECC)

                }
                else
                    pref.saveString(Constants.PROFILECODE, "")
                pref.saveString(Constants.CODIGOUSUARIO, it.Code)
                pref.saveString(Constants.RECC, it.U_VS_AGR_RECC)



                enableViews(true)
                Sync(it.U_VS_AGR_RECC)
            }
        })
       viewModel.messageResult.observe(this, Observer {
           loading.visibility = View.GONE
           enableViews(true)
           pref.deleteKey(Constants.SESSIONID)
           pref.deleteKey(Constants.VERSION)
           pref.saveString(Constants.MESSAGE_ALERT, it.toString())
           startActivity(Intent(this@LoginActivity, AlertActivity::class.java))
           if (etEmail.visibility == 0) {
               etEmail.setBackgroundResource(R.drawable.edtx_login_background)
               etEmail.setText("")
           }
           /*Snackbar.make(btnIngresar, it
                ?: throw Exception("Invalid message: null"), Snackbar.LENGTH_LONG).show()*/
       })
        viewModel.pgbVisibility.observe(this, Observer {
            it?.let {
                loading.visibility = it
            }
        })
    }
    private fun Sync(uVsAgrRecc: String) {
        if(checkInternet()) {
            if (uVsAgrRecc == "N") {
                if (pref.getString(Constants.B1SESSIONID) != null) {
                    startActivity(Intent(this@LoginActivity, Sincronizacion::class.java))
                    finish()
                }
            } else {
                if (pref.getString(Constants.B1SESSIONID) != null) {
                    startActivity(Intent(this@LoginActivity, usuarioActivity::class.java))
                    finish()
                }
            }
        }else
        {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
    }
    private fun launchHome() {

        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finish()
    }

    private fun enableViews(enable: Boolean){
        if(pref.getString(Constants.B1SESSIONID) != null)
        {etUsuario.isEnabled = enable
        etPassword.isEnabled = enable}
       //btn_enter.isEnabled = enable
    }
    private fun olvideView(enable: Boolean){
        etPassword.visibility = View.GONE
        etUsuario.visibility = View.GONE
        tituloOlvide.visibility = View.VISIBLE
        btnIngresar.visibility = View.GONE
        btnEnviar.visibility = View.VISIBLE
        tvOlvidarContraseña.visibility = View.GONE
        tvCancelar.visibility = View.VISIBLE
        titulo.visibility=View.VISIBLE
        etEmail.visibility=View.VISIBLE
        spEmpresa.visibility=View.GONE
        //btn_enter.isEnabled = enable
    }

    private fun returnLogin(enable: Boolean){
        etPassword.visibility = View.VISIBLE
        etUsuario.visibility = View.VISIBLE
        tituloOlvide.visibility = View.GONE
        btnIngresar.visibility = View.VISIBLE
        btnEnviar.visibility = View.GONE
        tvOlvidarContraseña.visibility = View.VISIBLE
        tvCancelar.visibility = View.GONE
        titulo.visibility=View.GONE
        etEmail.visibility=View.GONE
        spEmpresa.visibility=View.VISIBLE

        //btn_enter.isEnabled = enable
    }


    private fun isUserLogged(): Boolean {
        return pref.contains(Constants.SESSIONID)!!

    }


}