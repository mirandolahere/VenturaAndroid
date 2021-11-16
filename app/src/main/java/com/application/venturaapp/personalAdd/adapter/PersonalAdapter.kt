package com.application.venturaapp.personalAdd.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.home.fragment.entities.PersonalDatoCollection
import kotlinx.android.synthetic.main.activity_personal_add.*


class PersonalAdapter(internal var listener: ContactoItemListener,
                      personalLista: ArrayList<PersonalDatoCollection>, adapterParentesco: ArrayAdapter<String>, isTrue: Int
) : RecyclerView.Adapter<PersonalAdapter.ViewHolder>() {

    public val options =  ArrayList(personalLista)
    private val spinner =  adapterParentesco
    private val istrue =  isTrue

    lateinit var ParentescoPrimero: String

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_contacto, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: PersonalAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)

        viewHolder.rlCabecera1
    }
    fun getItem(position: Int): PersonalDatoCollection? {
        return options.get(position)
    }
    override fun getItemCount(): Int {
        return options.size
    }
    inner class Elements() : ArrayList<PersonalDatoCollection>()
    {

    }

    fun removeAt(position: Int) {

        options.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, options.size)

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var etCodigoPersonal1: EditText = itemView.findViewById(R.id.etCodigoPersonal1)
        var etNombresPersonal1: EditText = itemView.findViewById(R.id.etNombresPersonal1)
        var etTelefono1: EditText = itemView.findViewById(R.id.etTelefono1)
        var etTelefono2: EditText = itemView.findViewById(R.id.etTelefono2)
        var spParentesco: Spinner = itemView.findViewById(R.id.spParentesco)
        var llCabecera1: LinearLayout = itemView.findViewById(R.id.llCabecera1)

        var tvParentesco: TextView = itemView.findViewById(R.id.tvParentesco)
        var tvTelefono1: TextView = itemView.findViewById(R.id.tvTelefono1)
        var tvTelefono2: TextView = itemView.findViewById(R.id.tvTelefono2)
        var ivDelete : LinearLayout = itemView.findViewById(R.id.ivDelete)
        var ivExpand: ImageView = itemView.findViewById(R.id.ivExpand)
        var rlCabecera1 : LinearLayout = itemView.findViewById(R.id.rlCabecera1)


        fun bindItem(name: PersonalDatoCollection, position: Int) {

            if (istrue == 1) {
                spParentesco.isEnabled = true
                etTelefono1.isEnabled = true
                etTelefono2.isEnabled = true
                etNombresPersonal1.isEnabled = true
                if(name.U_VS_AGR_NOCM != "")
                { etNombresPersonal1.setBackgroundResource(R.drawable.edtx_personal_list_disabled)
                    etNombresPersonal1.isEnabled = false}


            } else if (istrue == 0){
                spParentesco.isEnabled = false
                etTelefono1.isEnabled = false
                etTelefono2.isEnabled = false
                etNombresPersonal1.isEnabled =false
                ivDelete.visibility = View.GONE
                etCodigoPersonal1.setBackgroundResource(R.drawable.edtx_personal_list)
                spParentesco.setBackgroundResource(R.drawable.edtx_personal_list)
                if(name.U_VS_AGR_NOCM != null||name.U_VS_AGR_NOCM != "")
                    etNombresPersonal1.setBackgroundResource(R.drawable.edtx_personal_list)
            }else
            {
                //ivDelete.visibility = View.GONE
            }

            if (name.LineId != null) {
                etCodigoPersonal1.setText(name.LineId.toString())
            }
            if (name.U_VS_AGR_NOCM != null||name.U_VS_AGR_NOCM != "") {
                etNombresPersonal1.setText(name.U_VS_AGR_NOCM)


            }
            if (name.U_VS_AGR_TEL1 != null || name.U_VS_AGR_TEL1 != "") {

                etTelefono1.setText(name.U_VS_AGR_TEL1)
            }
            if (name.U_VS_AGR_TEL2 != null || name.U_VS_AGR_TEL2 != "") {

                etTelefono2.setText(name.U_VS_AGR_TEL2)
            }

            etTelefono1.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null) {
                        if(s.length<7) {
                            etTelefono1.setBackgroundResource(R.drawable.edittext_border_error)
                        }else if (etTelefono1.text.toString() != name.U_VS_AGR_TEL1) {
                            name.U_VS_AGR_TEL1 = etTelefono1.text.toString()
                            etTelefono1.setBackgroundResource(R.drawable.edtx_personal_list)

                        }
                    }


                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            etTelefono2.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                    if (s != null) {
                        if(s.length<7) {
                            etTelefono2.setBackgroundResource(R.drawable.edittext_border_error)
                        }else if (etTelefono2.text.toString() != name.U_VS_AGR_TEL2) {
                            name.U_VS_AGR_TEL2 = etTelefono2.text.toString()
                            etTelefono2.setBackgroundResource(R.drawable.edtx_personal_list)

                        }
                    }

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

                etNombresPersonal1.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                        if (etNombresPersonal1.text.toString() != name.U_VS_AGR_NOCM) {

                            name.U_VS_AGR_NOCM = etNombresPersonal1.text.toString()
                            Log.d("tamanio", name.U_VS_AGR_NOCM)

                        }
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })


                spinner.setDropDownViewResource(R.layout.spinner_list)
                spParentesco.adapter = spinner
                spParentesco.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View, position: Int, id: Long
                    ) {

                        when (position) {
                            0 -> ParentescoPrimero = "H"
                            1 -> ParentescoPrimero = "E"
                            2 -> ParentescoPrimero = "M"
                            3 -> ParentescoPrimero = "P"
                            4 -> ParentescoPrimero = "I"
                            5 -> ParentescoPrimero = "S"
                            6 -> ParentescoPrimero = "A"
                            7 -> ParentescoPrimero = "O"

                        }

                        name.U_VS_AGR_PARE = ParentescoPrimero
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
                if (name.U_VS_AGR_PARE != null) {
                    when (name.U_VS_AGR_PARE) {
                        "H" -> spParentesco.setSelection(0)
                        "E" -> spParentesco.setSelection(1)
                        "M" -> spParentesco.setSelection(2)
                        "P" -> spParentesco.setSelection(3)
                        "I" -> spParentesco.setSelection(4)
                        "S" -> spParentesco.setSelection(5)
                        "A" -> spParentesco.setSelection(6)
                        "O" -> spParentesco.setSelection(7)

                    }
                }
            Log.d("mostrar", istrue.toString())

            llCabecera1.setOnClickListener {
                    when (tvParentesco.visibility) {
                        0 -> {
                            tvParentesco.visibility = View.GONE
                            tvTelefono1.visibility = View.GONE
                            tvTelefono2.visibility = View.GONE
                            spParentesco.visibility = View.GONE
                            etTelefono1.visibility = View.GONE
                            etTelefono2.visibility = View.GONE

                            ivExpand.setImageResource(R.drawable.ic_down)

                        }
                        8 -> {
                            tvParentesco.visibility = View.VISIBLE
                            tvTelefono1.visibility = View.VISIBLE
                            tvTelefono2.visibility = View.VISIBLE
                            spParentesco.visibility = View.VISIBLE
                            etTelefono1.visibility = View.VISIBLE
                            etTelefono2.visibility = View.VISIBLE

                            ivExpand.setImageResource(R.drawable.ic_up)

                        }
                    }

                }

                ivDelete.setOnClickListener {
                    listener.menuItemClickListener(name)
                }


            }


    }
}