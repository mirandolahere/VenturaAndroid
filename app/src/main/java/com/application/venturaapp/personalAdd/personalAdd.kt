package com.application.venturaapp.personalAdd
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.helper.DatePickerFragment
import com.application.venturaapp.helper.TipoDocumento
import com.application.venturaapp.home.fragment.entities.LaboresPersonal
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.fragment.entities.PersonalDatoCollection
import com.application.venturaapp.home.fragment.entities.Proveedor
import com.application.venturaapp.home.fragment.personal.personalViewModel
import com.application.venturaapp.personalAdd.adapter.ContactoItemListener
import com.application.venturaapp.personalAdd.adapter.PersonalAdapter
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.tables.PersonalDB
import com.application.venturaapp.tables.PersonalDatoDAO
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_personal_add.*
import kotlinx.android.synthetic.main.activity_personal_add.loading
import kotlinx.android.synthetic.main.fragment_labor_cultural.*
import okhttp3.Cache
import java.io.Serializable
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class personalAdd   : AppCompatActivity(),ContactoItemListener  {

    lateinit var pref: PreferenceManager
    var documentoLista = ArrayList<TipoDocumento>()
    lateinit var personalViewModels: personalViewModel
    var isCode:Boolean = true
    var isName:Boolean = true
    val bodyEliminar = JsonObject()
    val bodyUpdate = JsonObject()

    var isFecha:Boolean = true
    var isSecondeName:Boolean = true
    var islastName : Boolean = true
    var isSecondLastName :Boolean = true
    var isNumberDOC : Boolean = true
    var isLaborDefecto : Boolean = true
    lateinit var peopleClass : PersonalDato
    lateinit var idDocumento: String
    lateinit var idTipoEmpleado: String
    var CodeLabor: String = ""
    var DescripcionLabor: String = ""
    var ProveedorCode: String = ""
    var ProveedorNombre: String = ""

    lateinit var personalUpdate : PersonalDato
    lateinit var personalContactoLista: java.util.ArrayList<PersonalDatoCollection>
    lateinit var adapterParentesco : ArrayAdapter<String>
    var labores = arrayListOf<String>()
    lateinit var laboresList  : ArrayList<LaboresPersonal>
     var proveedor= arrayListOf<String>()
     var proveedorCode= arrayListOf<String>()

     var proveedorLista = arrayListOf<Proveedor>()
    var personalContactoListaNuevo = arrayListOf<PersonalDatoCollection>()
     var accion : Serializable = ""
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_DELETE = 101
    lateinit var httpCacheDirectory : Cache

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_personal_add)
        pref = PreferenceManager(this)
        personalViewModels = ViewModelProviders.of(this).get(personalViewModel::class.java)
        loading.visibility=View.VISIBLE
        val cacheSize = (5 * 1024 * 1024).toLong()

        httpCacheDirectory = Cache(cacheDir, cacheSize)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        setUpObservers()
        LaboresListar()
        ProveedorListar()

        setUpViews()



    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun initViews() {
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo_isotipo)
        if(intent.getSerializableExtra("OBJECTPERSONA") !=null) {
            val people = intent.getSerializableExtra("OBJECTPERSONA") //as? PersonalDato
             accion = intent.getSerializableExtra("ACCION")!! //as? PersonalDato

            val gson = Gson()
             peopleClass = gson.fromJson(people.toString(), PersonalDato::class.java)
            if (peopleClass != null && accion == 1) {
                MostrarDatos(peopleClass)
            }
            if (peopleClass != null && accion == 2) {
                ActualizarDatos(peopleClass)

            }
        }
        if(intent.getSerializableExtra("ACCION") == 0) {
            Nuevo()
        }

        loading.visibility=View.GONE
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpObservers(){
        personalViewModels.laborResult.observe(this, Observer {
            it?.let {
                laboresList = it as ArrayList<LaboresPersonal>
                labores = java.util.ArrayList<String>()
                for (item in it) {
                    labores.add(item.Name)
                }
                Spinner()
                initViews()
            }
        })

        personalViewModels.proveedorResult.observe(this, Observer {
            it?.let {
                proveedorLista = it as ArrayList<Proveedor>
                proveedor = java.util.ArrayList<String>()
                for (item in it) {
                    if(item.U_VS_AGR_PRRH =="Y") {
                        proveedor.add(item.CardName)
                        proveedorCode.add(item.CardCode+"-"+item.CardName)
                    }

                }
                Spinner()
                initViews()
            }
        })

        personalViewModels.messageResult.observe(this, Observer {
            loading.visibility = View.GONE

            pref.saveString(Constants.MESSAGE_ALERT, it.toString())
            startActivityForResult(Intent(this@personalAdd, AlertActivity::class.java),REQUEST_ACTIVITY)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Log.d("ELIMINAR","ELIMINAR2")


        })
        personalViewModels.messageResultDelete.observe(this, Observer {

           /* pref.saveString(Constants.MESSAGE_ALERT, it.toString())
            startActivityForResult(Intent(this@personalAdd, AlertActivity::class.java),REQUEST_ACTIVITY_DELETE)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Log.d("ELIMINAR","ELIMINAR")*/
            loading.visibility = View.GONE
            val body3 = JsonArray()
            val body = JsonObject()

            var body5 = JsonObject()
            var primerNombre : String = ""
            var segundoNombre : String = ""
            var primerApellido : String = ""
            var segundoApellido : String = ""
            val accion = intent.getSerializableExtra("ACCION")
            var isCorrect2 :Boolean = true
            var isCorrect1 :Boolean = true

            val date = LocalDate.parse(
                    etFecha.text,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
            )

            val fecha: String =
                    date.year.toString() + "-" + date.monthValue + "-" + date.dayOfMonth.toString()

            if (peopleClass.FechaNacimiento != fecha) {
                //personalUpdate.FechaNacimiento.replace(personalUpdate.FechaNacimiento,etFecha.text.toString())
                bodyUpdate.addProperty("U_VS_AGR_FENA", fecha)


            } else {
            }

            if (peopleClass.PrimerNombre != etPrimerNombre.text.toString()) {
                //personalUpdate.PrimerNombre = etPrimerNombre.text.toString()
                bodyUpdate.addProperty("U_VS_AGR_BPNO", etPrimerNombre.text.toString())
                primerNombre = etPrimerNombre.text.toString()

            } else {
                primerNombre = etPrimerNombre.text.toString()
            }

            if (peopleClass.SegundoNombre != etSegundoNombre.text.toString()) {
                // personalUpdate.SegundoNombre = etSegundoNombre.text.toString()
                bodyUpdate.addProperty("U_VS_AGR_BPN2", etSegundoNombre.text.toString())
                segundoNombre = etSegundoNombre.text.toString()
            } else {
                segundoNombre = etSegundoNombre.text.toString()

            }

            if (peopleClass.ApellidoPaterno != etPrimerApellido.text.toString()) {
                //personalUpdate.ApellidoPaterno = etPrimerApellido.text.toString()
                bodyUpdate.addProperty("U_VS_AGR_BPAP", etPrimerApellido.text.toString())
                primerApellido = etPrimerApellido.text.toString()
            } else {
                primerApellido = etPrimerApellido.text.toString()
            }

            if (peopleClass.ApellidoMaterno != etSegunApellido.text.toString()) {
                // personalUpdate.ApellidoMaterno = etSegunApellido.text.toString()
                bodyUpdate.addProperty("U_VS_AGR_BPAM", etSegunApellido.text.toString())
                segundoApellido = etSegunApellido.text.toString()
            } else {
                segundoApellido = etSegunApellido.text.toString()
            }

            bodyUpdate.addProperty(
                    "Name",
                    "$primerApellido $segundoApellido $primerNombre $segundoNombre"
            )
            if (idTipoEmpleado != null) {
                // personalUpdate.TipoEmpleado = idTipoEmpleado
                bodyUpdate.addProperty("U_VS_AGR_TEMP", idTipoEmpleado)


            } else {
            }

            if(idTipoEmpleado == "T")
            {
                bodyUpdate.addProperty("U_VS_AGR_CPRV", ProveedorCode)
                bodyUpdate.addProperty("U_VS_AGR_NPRV", ProveedorNombre)

            }

            if (CodeLabor != "") {
                //personalUpdate.CodigoLabor = CodeLabor
                //personalUpdate.DescripcionLabor = DescripcionLabor
                bodyUpdate.addProperty("U_VS_AGR_DELA", DescripcionLabor)
                bodyUpdate.addProperty("U_VS_AGR_LBCA", CodeLabor)

            } else {
            }

            try {
                val size: Int = (rvContactos.getAdapter() as PersonalAdapter).getItemCount()
                Log.d("size", size.toString())

                var index = 0
                while (index < size) {
                    Log.d("tamanio", index.toString())

                    val element = (rvContactos.getAdapter() as PersonalAdapter).getItem(index)
                    if (element != null) {
                        body5 = JsonObject()
                        if (element.LineId != 0) {
                            body5.addProperty("LineId", element.LineId)

                        }
                        if (element.U_VS_AGR_PARE != "") {
                            body5.addProperty("U_VS_AGR_PARE", element.U_VS_AGR_PARE)

                        }
                        if (element.U_VS_AGR_TEL1 !=null) {
                            if (element.U_VS_AGR_TEL1.length >= 7) {
                                body5.addProperty("U_VS_AGR_TEL1", element.U_VS_AGR_TEL1)
                            }else
                            {
                                body5.addProperty("U_VS_AGR_TEL1", "")
                            }
                        } else {
                            body5.addProperty("U_VS_AGR_TEL1", "")
                        }

                        if (element.U_VS_AGR_TEL2 != null) {
                            if (element.U_VS_AGR_TEL2.length >= 7) {
                                body5.addProperty("U_VS_AGR_TEL2", element.U_VS_AGR_TEL2)
                            }else
                            {
                                body5.addProperty("U_VS_AGR_TEL2", "")

                            }
                        } else {
                            body5.addProperty("U_VS_AGR_TEL2", "")
                        }

                        if (element.U_VS_AGR_NOCM != "") {
                            body5.addProperty("U_VS_AGR_NOCM", element.U_VS_AGR_NOCM)
                            body3.add(body5) }
                    }
                    index++
                }
                if (body3.size() > 0) {
                    bodyUpdate.add("VS_AGR_CONTCollection", body3)
                }
            }catch (e:Exception)
            {
            }
            Log.d("JSONUPDATE", bodyUpdate.toString())

            pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                personalViewModels.updatePersonal(
                        it1, etCodigoPersona.text.toString(), bodyUpdate
                )
            }


        })
        personalViewModels.pgbVisibility.observe(this, Observer {
            it?.let {
                loading.visibility = it
            }
        })
    }
    fun Spinner()
    {

        val Documentos = resources.getStringArray(R.array.Documentos)
        val Empleado = resources.getStringArray(R.array.Empleado)
        val Parentesco = resources.getStringArray(R.array.Parentesco)

        if (Documentos != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner, Documentos
            )

            val adapterDoc = ArrayAdapter(
                this,
                R.layout.spinner, Empleado
            )

             adapterParentesco = ArrayAdapter(
                 this,
                 R.layout.spinner, Parentesco
             )
            val adapterLabores = ArrayAdapter(
                this,
                R.layout.spinner, labores
            )
            val adapterProveedor = ArrayAdapter(
                    this,
                    R.layout.spinner, proveedor
            )
            val adapterCodigoProveedor = ArrayAdapter(
                    this,
                    R.layout.spinner, proveedorCode
            )
            adapter.setDropDownViewResource(R.layout.spinner_list)
            adapterDoc.setDropDownViewResource(R.layout.spinner_list)
            adapterParentesco.setDropDownViewResource(R.layout.spinner_list)
            adapterLabores.setDropDownViewResource(R.layout.spinner_list)
            adapterProveedor.setDropDownViewResource(R.layout.spinner_list)
            adapterCodigoProveedor.setDropDownViewResource(R.layout.spinner_list)

            splDocumento.adapter = adapter
            splTipoEmpleado.adapter = adapterDoc
            /*spParentesco.adapter = adapterParentesco
            spParentescoSegundo.adapter = adapterParentesco*/
            etLaborDefecto.adapter = adapterLabores
            splCodigo.adapter = adapterCodigoProveedor
            splNombre.adapter = adapterProveedor
            splDocumento.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    when (position)
                    {
                        0 -> idDocumento = "1"
                        1 -> idDocumento = "4"
                        2 -> idDocumento = "6"
                        3 -> idDocumento = "7"
                        4 -> idDocumento = "A"
                        5 -> idDocumento = "0"
                    }

                    Log.d("JSONPERSONAL",idDocumento)


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
            etFecha.setOnClickListener {
                showDatePickerDialog()
            }
            splTipoEmpleado.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    when (position)
                    {
                        0 -> idTipoEmpleado = "P"
                        1 -> idTipoEmpleado = "T"
                    }

                    if(position==1)
                    {
                        llTituloEmpleado.visibility = View.VISIBLE
                        llTipoEmpleado.visibility = View.VISIBLE
                    }else
                    {
                        llTituloEmpleado.visibility = View.GONE
                        llTipoEmpleado.visibility = View.GONE
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
            etLaborDefecto.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    DescripcionLabor = labores.get(position)
                    Log.d("JSONPERSONAL",DescripcionLabor)

                    for( item in laboresList)
                    {
                        if(item.Name == DescripcionLabor)
                        {
                            CodeLabor = item.Code

                            Log.d("JSONPERSONAL",CodeLabor)

                        }
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
            }


            splNombre.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
            ) {

                ProveedorNombre = proveedor.get(position)

                for( item in proveedorLista)
                {
                    if(item.CardName == ProveedorNombre)
                    {
                        ProveedorCode = item.CardCode
                        splCodigo.setSelection(position)

                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
            }

                splCodigo.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                ) {

                    ProveedorCode = proveedorCode.get(position)

                    for( item in proveedorLista)
                    {
                        if(item.CardCode == ProveedorCode)
                        {
                            ProveedorNombre = item.CardName
                            splNombre.setSelection(position)


                        }
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }



    }
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            // +1 because January is zero
            var mes = ""
            var dia = ""

            if(month<9)
            {
               mes = "0"+ (month+1)
            }else
            {
                 mes = (month+1).toString()
            }

            if(day<10)
            {
                dia = "0"+ (day)
            }else
            {
                dia = (day).toString()
            }
            val selectedDate = dia + "-" + mes + "-" + year
            etFecha.setText(selectedDate)
        })

        newFragment.show(supportFragmentManager, "datePicker")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun MostrarDatos(people: PersonalDato)
    {
        idAgregarContactos.visibility = View.GONE

        etCodigoPersona.setText(people.Code)
        etCodigoPersona.isEnabled = false
        etCodigoPersona.setBackgroundResource(R.drawable.edtx_personal_list)
        if(people.TipoEmpleado=="T")
        {
            llTituloEmpleado.visibility=View.VISIBLE
            llTipoEmpleado.visibility = View.VISIBLE
            for((index, item) in proveedorLista.withIndex())
            {
                if(item.CardCode == people.CodigoProveedor)
                {
                    splCodigo.setSelection(index)
                    splNombre.setSelection(index)
                    splCodigo.isEnabled = false
                    splNombre.isEnabled = false

                }
            }

        }
        /*val format = DateTimeFormatter.ofPattern("yy-mm-dd", Locale.ENGLISH)
        val date = LocalDate.parse(people.FechaNacimiento.toString(), format)*/
        if(people.FechaNacimiento!=null) {
            var simpleFormat = DateTimeFormatter.ISO_DATE;
            var convertedDate = LocalDate.parse(people.FechaNacimiento, simpleFormat)
            val mes: String
            val dia: String

            if (convertedDate.monthValue < 10)
                mes = "0" + convertedDate.monthValue
            else
                mes = convertedDate.monthValue.toString()

            if (convertedDate.dayOfMonth < 10)
                dia = "0" + convertedDate.dayOfMonth
            else
                dia = convertedDate.dayOfMonth.toString()
            val fecha: String = dia + "-" + mes + "-" + convertedDate.year.toString()
            etFecha.setText(fecha)
        }
        etFecha.isEnabled = false
        etPrimerNombre.setText(people.PrimerNombre)
        etPrimerNombre.isEnabled = false
        etSegundoNombre.setText(people.SegundoNombre)
        etSegundoNombre.isEnabled = false
        etPrimerApellido.setText(people.ApellidoPaterno)
        etPrimerApellido.isEnabled = false
        etSegunApellido.setText(people.ApellidoMaterno)
        etSegunApellido.isEnabled = false
        etNumeroDocumento.setText(people.NumeroDocumento)
        etNumeroDocumento.isEnabled = false

        for((index, item) in laboresList.withIndex())
        {
            if(item.Code == people.CodigoLabor)
            {
                etLaborDefecto.setSelection(index)
            }
        }

        etLaborDefecto.isEnabled = false


        when (people.TipoDocumento)
        {
            "1" -> splDocumento.setSelection(0)
            "4" -> splDocumento.setSelection(1)
            "6" -> splDocumento.setSelection(2)
            "7" -> splDocumento.setSelection(3)
            "A" -> splDocumento.setSelection(4)
            "0" -> splDocumento.setSelection(5)
        }
        splDocumento.isEnabled = false

        when (people.TipoEmpleado)
        {
            "P" -> splTipoEmpleado.setSelection(0)
            "T" ->
                    splTipoEmpleado.setSelection(1)


        }
        splTipoEmpleado.isEnabled = false

        rlButton.visibility = View.GONE
        if(people.Collection!=null) {
            personalContactoLista = people.Collection as java.util.ArrayList<PersonalDatoCollection>
            if (personalContactoLista.size != 0) {
                if (personalContactoLista.get(0).U_VS_AGR_NOCM != null) {
                    rvContactos.adapter =
                        (PersonalAdapter(this, personalContactoLista, adapterParentesco, 0))
                    rvContactos.layoutManager = LinearLayoutManager(applicationContext)
                }
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpViews()
    {
        containerPersonal.setOnClickListener {
            hideSoftKeyboard()
        }


        btnPersonal.setOnClickListener {

            val body3 = JsonArray()
            val body = JsonObject()

            var body5 = JsonObject()
            var primerNombre : String = ""
            var segundoNombre : String = ""
            var primerApellido : String = ""
            var segundoApellido : String = ""
            val accion = intent.getSerializableExtra("ACCION")
            var isCorrect2 :Boolean = true
            var isCorrect1 :Boolean = true

            if(validarCampos(accion)) {
                val date = LocalDate.parse(
                    etFecha.text,
                    DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
                )
                var mes:String = date.monthValue.toString()
                var dia:String = date.dayOfMonth.toString()
                if(date.monthValue<10)
                {
                    mes = "0"+date.monthValue
                }

                if(date.dayOfMonth<10)
                {
                    dia = "0"+date.dayOfMonth
                }

                val fecha: String =
                    date.year.toString() + "-" + mes + "-" + dia

                //actualizar contacto
                if (accion == 2) {

                    Log.d("VS_AGR_CONTCollection", bodyEliminar.toString())
                    loading.visibility = View.VISIBLE
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    //en caso se elimino permanentemente un contacto
                    pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                        personalViewModels.deleteContacto(
                                it1, etCodigoPersona.text.toString(), bodyEliminar
                        )
                    }


                }
                //nuevo registro
                if (accion == 0) {
                    if (checkInternet())
                    {
                        if (fecha != null) {
                            //personalUpdate.FechaNacimiento.replace(personalUpdate.FechaNacimiento,etFecha.text.toString())
                            body.addProperty("U_VS_AGR_FENA", fecha)


                        } else {
                        }

                    if (etPrimerNombre.text.toString() != "") {
                        //personalUpdate.PrimerNombre = etPrimerNombre.text.toString()
                        body.addProperty("U_VS_AGR_BPNO", etPrimerNombre.text.toString())
                        primerNombre = etPrimerNombre.text.toString()

                    } else {

                    }

                    if (etSegundoNombre.text.toString() != "") {
                        // personalUpdate.SegundoNombre = etSegundoNombre.text.toString()
                        body.addProperty("U_VS_AGR_BPN2", etSegundoNombre.text.toString())
                        segundoNombre = etSegundoNombre.text.toString()
                    } else {

                    }

                    if (etPrimerApellido.text.toString() != "") {
                        //personalUpdate.ApellidoPaterno = etPrimerApellido.text.toString()
                        body.addProperty("U_VS_AGR_BPAP", etPrimerApellido.text.toString())
                        primerApellido = etPrimerApellido.text.toString()
                    } else {
                    }

                    if (etSegunApellido.text.toString() != "") {
                        // personalUpdate.ApellidoMaterno = etSegunApellido.text.toString()
                        body.addProperty("U_VS_AGR_BPAM", etSegunApellido.text.toString())
                        segundoApellido = etSegunApellido.text.toString()
                    } else {

                    }
                    if (etNumeroDocumento.text.toString() != "") {
                        // personalUpdate.ApellidoMaterno = etSegunApellido.text.toString()
                        body.addProperty("U_VS_AGR_DOIN", etNumeroDocumento.text.toString())
                    } else {

                    }
                    body.addProperty(
                        "Name",
                        "$primerApellido $segundoApellido $primerNombre $segundoNombre"
                    )
                    if (idTipoEmpleado != null) {
                        // personalUpdate.TipoEmpleado = idTipoEmpleado
                        body.addProperty("U_VS_AGR_TEMP", idTipoEmpleado)
                    } else {

                    }

                    if (idTipoEmpleado == "T") {
                        body.addProperty("U_VS_AGR_CPRV", ProveedorCode)
                        body.addProperty("U_VS_AGR_NPRV", ProveedorNombre)

                    }


                    if (idDocumento != null) {
                        // personalUpdate.TipoEmpleado = idTipoEmpleado
                        body.addProperty("U_VS_AGR_TIDO", idDocumento)
                    } else {

                    }

                    if (CodeLabor != "") {
                        //personalUpdate.CodigoLabor = CodeLabor
                        //personalUpdate.DescripcionLabor = DescripcionLabor
                        body.addProperty("U_VS_AGR_DELA", DescripcionLabor)
                        body.addProperty("U_VS_AGR_LBCA", CodeLabor)

                    } else {
                    }
                    body.addProperty("U_VS_AGR_ACTV", "Y")
                    body.addProperty("Code", "P" + etNumeroDocumento.text.toString())

                    if (body3.size() > 1) {
                        body.add("VS_AGR_CONTCollection", body3)
                    }

                    try {
                        val size: Int = (rvContactos.getAdapter() as PersonalAdapter).getItemCount()
                        Log.d("tamanio", size.toString())
                        var index: Int = 0
                        while (index < size) {
                            Log.d("tamanio", index.toString())

                            val element =
                                (rvContactos.getAdapter() as PersonalAdapter).getItem(index)
                            if (element != null) {
                                body5 = JsonObject()
                                if (element.LineId != 0) {
                                    body5.addProperty("LineId", element.LineId)

                                }
                                if (element.U_VS_AGR_PARE != "") {
                                    body5.addProperty("U_VS_AGR_PARE", element.U_VS_AGR_PARE)

                                }
                                if (element.U_VS_AGR_TEL1 != "") {
                                    if (element.U_VS_AGR_TEL1.length >= 7) {
                                        body5.addProperty("U_VS_AGR_TEL1", element.U_VS_AGR_TEL1)
                                        isCorrect1 = true
                                    } else {
                                        isCorrect1 = false
                                    }

                                }
                                if (element.U_VS_AGR_TEL2 != "") {
                                    if (element.U_VS_AGR_TEL2.length >= 7) {
                                        body5.addProperty("U_VS_AGR_TEL2", element.U_VS_AGR_TEL2)
                                        isCorrect2 = true
                                    } else {
                                        isCorrect2 = false
                                    }

                                }
                                if (element.U_VS_AGR_NOCM != "") {
                                    body5.addProperty("U_VS_AGR_NOCM", element.U_VS_AGR_NOCM)
                                    body3.add(body5)

                                }

                            }
                            index++
                        }
                        if (body3.size() > 0) {
                            body.add("VS_AGR_CONTCollection", body3)
                        }
                        Log.d("tamanio", body.toString())
                    } catch (e: Exception) {
                    }
                    if (isCorrect1 && isCorrect2) {
                        loading.visibility = View.VISIBLE
                        getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        );
                        pref.getString(Constants.B1SESSIONID)?.let { it1 ->
                            personalViewModels.addPersonal(
                                it1, body
                            )
                        }
                    }
                }
                    else
                    {
                        val primerApellido = etPrimerApellido.text.toString()
                        val segundoApellido = etSegunApellido.text.toString()
                        val segundoNombre = etSegundoNombre.text.toString()
                        val primerNombre = etPrimerNombre.text.toString()
                        var proveedorCode:String = ""
                        var proveedorNom:String = ""
                        if (idTipoEmpleado == "T") {
                            proveedorCode = ProveedorCode
                            proveedorNom = ProveedorNombre

                        }


                        val personal = PersonalDatoRoom(0,
                            null,
                            "P"+etNumeroDocumento.text,
                            "$primerApellido $segundoApellido $primerNombre $segundoNombre",
                            primerNombre,segundoNombre,primerApellido,segundoApellido,idDocumento,
                            etNumeroDocumento.text.toString(),null,null,null,
                            CodeLabor, DescripcionLabor,fecha,null,idTipoEmpleado,
                            proveedorCode,proveedorNom
                            , "Y", null,null,null

                        )
                        personalViewModels.insertPersonalRoom(personal)
                        pref.saveString(Constants.MESSAGE_ALERT, "Se guardó en la memoria interna.")
                        startActivityForResult(Intent(this@personalAdd, AlertActivity::class.java),REQUEST_ACTIVITY)
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }

            }
            else
            {

            }

        }

        idAgregarContactos.setOnClickListener {
             var personal : PersonalDatoCollection = PersonalDatoCollection(
                 "",
                 0,
                 "",
                 "",
                 "",
                 "",
                 ""
             )
            Log.d("nuevocontacto",accion.toString())
            if(accion ==2)
            {    val size: Int = (rvContactos.getAdapter() as PersonalAdapter).getItemCount()

                val element = (rvContactos.getAdapter() as PersonalAdapter).getItem(size-1)

                if (element != null) {
                        if(element.U_VS_AGR_NOCM!="" || size==0) {
                            personalContactoLista.add(personal)
                            rvContactos.adapter = (PersonalAdapter(this, personalContactoLista, adapterParentesco, 1))
                            rvContactos.layoutManager = LinearLayoutManager(applicationContext)
                        }

                }


            }else
            {
                val size: Int = (rvContactos.getAdapter() as PersonalAdapter).getItemCount()

                val element = (rvContactos.getAdapter() as PersonalAdapter).getItem(size-1)

                if (element != null) {
                    if (element.U_VS_AGR_NOCM != "") {
                        personalContactoListaNuevo.add(personal)

                        rvContactos.adapter = (PersonalAdapter(this, personalContactoListaNuevo, adapterParentesco, 2))
                        rvContactos.layoutManager = LinearLayoutManager(applicationContext)
                    }
                }
            }
        }
    }
    fun checkInternet() : Boolean
    {
        val ConnectionManager = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected

    }

    fun validarCampos(accion: Serializable?):Boolean
    {
        if(accion==2) {
            if (etPrimerNombre.text.isEmpty()) {
                etPrimerNombre.setBackgroundResource(R.drawable.edittext_border_error)
                isName = false

            } else {
                etPrimerNombre.setBackgroundResource(R.drawable.edtx_personal_list)
                isName = true
            }

            if (etPrimerApellido.text.isEmpty()) {
                etPrimerApellido.setBackgroundResource(R.drawable.edittext_border_error)
                islastName = false
            } else {
                etPrimerApellido.setBackgroundResource(R.drawable.edtx_personal_list)
                islastName = true
            }

            if (etSegunApellido.text.isEmpty()) {
                etSegunApellido.setBackgroundResource(R.drawable.edittext_border_error)
                isSecondLastName = false
            } else {
                etSegunApellido.setBackgroundResource(R.drawable.edtx_personal_list)
                isSecondeName = true
            }
            return isName && islastName && isSecondeName

        }


        if(accion == 0)
        {
            if (etFecha.text.isEmpty()) {
                etFecha.setBackgroundResource(R.drawable.edittext_border_error)
                isFecha = false

            } else {
                etFecha.setBackgroundResource(R.drawable.edtx_personal_list)
                isFecha = true
            }

            if (etPrimerNombre.text.isEmpty()) {
                etPrimerNombre.setBackgroundResource(R.drawable.edittext_border_error)
                isName = false

            } else {
                etPrimerNombre.setBackgroundResource(R.drawable.edtx_personal_list)
                isName = true
            }
            if (etSegunApellido.text.isEmpty()) {
                etSegunApellido.setBackgroundResource(R.drawable.edittext_border_error)
                isSecondLastName = false
            } else {
                etSegunApellido.setBackgroundResource(R.drawable.edtx_personal_list)
                isSecondLastName = true
            }

            if (etPrimerApellido.text.isEmpty()) {
                etPrimerApellido.setBackgroundResource(R.drawable.edittext_border_error)
                islastName = false
            } else {
                etPrimerApellido.setBackgroundResource(R.drawable.edtx_personal_list)
                islastName = true
            }

            if (etNumeroDocumento.text.isEmpty()) {
                etNumeroDocumento.setBackgroundResource(R.drawable.edittext_border_error)
                isNumberDOC = false

            } else {
                etNumeroDocumento.setBackgroundResource(R.drawable.edtx_personal_list)
                isNumberDOC = true
            }
            return isName && islastName && isFecha && isNumberDOC  && isSecondLastName

        }
     /*   if(etNumeroDocumento.text.isEmpty())
        {
            etNumeroDocumento.setBackgroundResource(R.drawable.edittext_border_error)
        }
        else
        {
            etNumeroDocumento.setBackgroundResource(R.drawable.edtx_personal_list)
            isNumberDOC = false
        }*/
        return false

    }
    fun Nuevo()
    {

        btnPersonal.text = "Guardar"
        btnPersonal.setBackgroundResource(R.drawable.btn_personal_guardar)

    }
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    fun ActualizarDatos(people: PersonalDato) {
        etCodigoPersona.setText(people.Code)
        etCodigoPersona.isEnabled = false

        if (people.TipoEmpleado == "T") {
            llTituloEmpleado.visibility = View.VISIBLE
            llTipoEmpleado.visibility = View.VISIBLE
            for ((index, item) in proveedorLista.withIndex()) {
                if (item.CardCode == people.CodigoProveedor) {
                    splCodigo.setSelection(index)
                    splNombre.setSelection(index)
                }
            }

        }



        if (people.FechaNacimiento != null) {
            var simpleFormat = DateTimeFormatter.ISO_DATE;
            var convertedDate = LocalDate.parse(people.FechaNacimiento, simpleFormat)
            val mes: String
            val dia: String
            if (convertedDate.monthValue < 10)
                mes = "0" + convertedDate.monthValue
            else
                mes = convertedDate.monthValue.toString()

            if (convertedDate.dayOfMonth < 10)
                dia = "0" + convertedDate.dayOfMonth
            else
                dia = convertedDate.dayOfMonth.toString()
            val fecha: String = dia + "-" + mes + "-" + convertedDate.year.toString()
            etFecha.setText(fecha)
        }
        etPrimerNombre.setText(people.PrimerNombre)
        etSegundoNombre.setText(people.SegundoNombre)
        etPrimerApellido.setText(people.ApellidoPaterno)
        etSegunApellido.setText(people.ApellidoMaterno)
        etNumeroDocumento.setText(people.NumeroDocumento)
        etNumeroDocumento.isEnabled = false
        etNumeroDocumento.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
        when (people.TipoDocumento) {
            "1" -> splDocumento.setSelection(0)
            "4" -> splDocumento.setSelection(1)
            "6" -> splDocumento.setSelection(2)
            "7" -> splDocumento.setSelection(3)
            "A" -> splDocumento.setSelection(4)
            "0" -> splDocumento.setSelection(5)
        }

        splDocumento.isEnabled = false
        splDocumento.setBackgroundResource(R.drawable.edtx_personal_list_disabled)


        when (people.TipoEmpleado) {
            "P" -> splTipoEmpleado.setSelection(0)
            "T" -> {
                splTipoEmpleado.setSelection(1)
            }

        }
        for ((index, item) in laboresList.withIndex()) {
            if (item.Code == people.CodigoLabor) {
                etLaborDefecto.setSelection(index)
            }
        }
        if (people.Collection != null)
        {
            personalContactoLista = people.Collection as java.util.ArrayList<PersonalDatoCollection>
            if (personalContactoLista.size != 0) {
                if (personalContactoLista.get(0).U_VS_AGR_NOCM != null) {
                    rvContactos.adapter =
                        (PersonalAdapter(this, personalContactoLista, adapterParentesco, 1))
                    rvContactos.layoutManager = LinearLayoutManager(applicationContext)
                }
            }
        }
    }

    private fun LaboresListar() {
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.consultarLabor(it,httpCacheDirectory, this) }
    }
    private fun ProveedorListar() {
        pref.getString(Constants.B1SESSIONID)?.let { personalViewModels.listaProveedor(it,httpCacheDirectory, this) }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun menuItemClickListener(position: PersonalDatoCollection?) {

        val body3 = JsonArray()
        var body5 = JsonObject()
        var primerNombre: String = ""
        var segundoNombre: String = ""
        var primerApellido: String = ""
        var segundoApellido: String = ""
        val accion = intent.getSerializableExtra("ACCION")

        val date = LocalDate.parse(
                etFecha.text,
                DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
        )

        val fecha: String =
                date.year.toString() + "-" + date.monthValue + "-" + date.dayOfMonth.toString()

        bodyEliminar.addProperty("U_VS_AGR_FENA", peopleClass.FechaNacimiento)

        bodyEliminar.addProperty("U_VS_AGR_BPNO", peopleClass.PrimerNombre)

        bodyEliminar.addProperty("U_VS_AGR_BPN2", peopleClass.SegundoNombre)

        bodyEliminar.addProperty("U_VS_AGR_BPAP", peopleClass.ApellidoPaterno)

        bodyEliminar.addProperty("U_VS_AGR_BPAM", peopleClass.ApellidoMaterno)

        bodyEliminar.addProperty("Name", peopleClass.Name)

        bodyEliminar.addProperty("U_VS_AGR_TEMP", peopleClass.TipoEmpleado)

        bodyEliminar.addProperty("U_VS_AGR_TIDO", peopleClass.TipoDocumento)

        bodyEliminar.addProperty("U_VS_AGR_DOIN", peopleClass.NumeroDocumento)

        bodyEliminar.addProperty("U_VS_AGR_NUCO", peopleClass.NumeroContrato)

        bodyEliminar.addProperty("U_VS_AGR_DELA", peopleClass.DescripcionLabor)

        bodyEliminar.addProperty("U_VS_AGR_LBCA", peopleClass.CodigoLabor)

        bodyEliminar.addProperty("U_VS_AGR_CPRV", peopleClass.CodigoProveedor)

        bodyEliminar.addProperty("U_VS_AGR_NPRV", peopleClass.NombreProveedor)

        bodyEliminar.addProperty("Code", peopleClass.Code)

        bodyEliminar.addProperty("U_VS_AGR_DIRE", peopleClass.Dirrecion)

        bodyEliminar.addProperty("U_VS_AGR_TEL1", peopleClass.Telefono1)

        bodyEliminar.addProperty("U_VS_AGR_ACTV", peopleClass.Activo)

        bodyEliminar.addProperty("U_VS_AGR_TEL2", peopleClass.Telefono2)

        val size: Int = (rvContactos.getAdapter() as PersonalAdapter).getItemCount()
        var eliminar:Int = 0
        var index: Int = 0
        while (index < size) {
            val element = (rvContactos.getAdapter() as PersonalAdapter).getItem(index)
            if (position != null) {
                if (element != null && element.LineId!=position.LineId ) {
                    body5 = JsonObject()
                    if(element.LineId!=0) {
                        if (element.LineId != 0) {
                            body5.addProperty("LineId", element.LineId)
                        }
                        if (element.U_VS_AGR_PARE != "") {
                            body5.addProperty("U_VS_AGR_PARE", element.U_VS_AGR_PARE)
                        }
                        if (element.U_VS_AGR_TEL1 != "") {
                            body5.addProperty("U_VS_AGR_TEL1", element.U_VS_AGR_TEL1)
                        }
                        if (element.U_VS_AGR_TEL2 != "") {
                            body5.addProperty("U_VS_AGR_TEL2", element.U_VS_AGR_TEL2)
                        }
                        if (element.U_VS_AGR_NOCM != "") {
                            body5.addProperty("U_VS_AGR_NOCM", element.U_VS_AGR_NOCM)
                            body3.add(body5)
                        }
                    }
                }else {
                        eliminar = index

                }
            }
            index++
        }
        if (body3.size() > 0) {
            bodyEliminar.add("VS_AGR_CONTCollection", body3)
        }

         (rvContactos.getAdapter() as PersonalAdapter).removeAt(eliminar)


    }

    fun hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ELIMINAR",requestCode.toString())
        Log.d("ELIMINAR",resultCode.toString())

        when (requestCode) {
            REQUEST_ACTIVITY -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(RESULT_OK)
                    finish()
                }

            }
            REQUEST_ACTIVITY_DELETE -> {
                if (resultCode == Activity.RESULT_OK) {

                }

            }
        }
    }

}

