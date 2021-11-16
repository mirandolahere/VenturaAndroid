package com.application.venturaapp.login
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.fragment.Sincronizacion
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.preference.PreferenceManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_labor_cultural.pgbLaborRealizada
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_usuario.*


class usuarioActivity   : AppCompatActivity() {

    var documentoLista = ArrayList<TipoDocumento>()
    private var CodigoPEP = ""
    private var Campania = ""
    private var codigoCampania = ""
    lateinit var viewModel: LoginViewModel
    private val REQUEST_ACTIVITY = 100
    private var Cultivo = ""
    private var Variedad = ""
    private  var tipoCampania = ""
    lateinit var pref: PreferenceManager
    lateinit var laborList  : ArrayList<LaborCulturalListResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_usuario)
        pref = PreferenceManager(this)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        initViews()

        setUpViews()
        setUpObservers()

    }
    fun initViews() {

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setUpObservers(){
        viewModel.messageUpdateResult.observe(this, Observer {
            pref.saveString(Constants.MESSAGE_ALERT, it.toString())

            startActivityForResult(Intent(this@usuarioActivity, AlertActivity::class.java),REQUEST_ACTIVITY)
            pref.saveString(Constants.RECC,"N")
            /*Snackbar.make(btnIngresar, it
                 ?: throw Exception("Invalid message: null"), Snackbar.LENGTH_LONG).show()*/
        })

        viewModel.messageErrorResult.observe(this, Observer {
            pref.saveString(Constants.MESSAGE_ALERT, it.toString())

            startActivity(Intent(this@usuarioActivity, AlertActivity::class.java))

            /*Snackbar.make(btnIngresar, it
                 ?: throw Exception("Invalid message: null"), Snackbar.LENGTH_LONG).show()*/
        })
    }
    fun validar():Boolean
    {
        if(etNuevaContrasenia.text.toString()!="")
        {
            btnActualizar.setBackgroundResource(R.drawable.btn_personal_guardar)
            return true
        }else
        {
            etNuevaContrasenia.setBackgroundResource(R.drawable.edittext_border_error)
            return false
        }
    }
    fun setUpViews()
    {
        etNuevaContrasenia.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s != null) {
                    if(s.length>0) {
                        btnActualizar.isEnabled = true
                        btnActualizar.setBackgroundResource(R.drawable.btn_personal_guardar)


                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        btnActualizar.setOnClickListener {
            if(validar()) {
                val bodyUpdate = JsonObject()
                bodyUpdate.addProperty("U_VS_AGR_RECC","N")
                bodyUpdate.addProperty("U_VS_AGR_CONT",etNuevaContrasenia.text.toString())
                Log.d("bodyUpdate",bodyUpdate.toString())
                pref.getString(Constants.B1SESSIONID)?.let { viewModel.ActualizarUsuario(it,
                        pref.getString(Constants.CODIGOUSUARIO)!!,bodyUpdate) }
            }


        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (pref.getString(Constants.B1SESSIONID) != null) {
                        startActivity(Intent(this@usuarioActivity, Sincronizacion::class.java))
                        finish()
                    }
                }

            }

            }
        }


}

