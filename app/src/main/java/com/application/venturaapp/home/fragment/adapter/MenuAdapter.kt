package com.application.venturaapp.home.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.listener.MenuItemListener


class MenuAdapter(
        internal var listener: MenuItemListener, personalLista: ArrayList<PersonalDato>
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private val options =  ArrayList(personalLista)


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_personal, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MenuAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvNombres: TextView = itemView.findViewById(R.id.tvNombres)
        var tvDni: TextView = itemView.findViewById(R.id.tvDni)
        var ivView: ImageView = itemView.findViewById(R.id.ivView)
        var ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
        var tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        var tvApellidos: TextView = itemView.findViewById(R.id.tvApellidos)



        fun bindItem(name: PersonalDato, position: Int) {
            tvCodigo.text = name.Code
            var concaternar :String = ""
            var concaternarApellidos :String = ""

            if(name.PrimerNombre != null)
                concaternar = name.PrimerNombre
            if(name.SegundoNombre !=null)
                concaternar = concaternar +" " + name.SegundoNombre

            tvNombres.text = concaternar


            if(name.ApellidoPaterno!=null )
                concaternarApellidos = name.ApellidoPaterno

            if(name.ApellidoMaterno !=null)
                concaternarApellidos = concaternarApellidos + " " + name.ApellidoMaterno

            tvApellidos.text = concaternarApellidos


            if(name.Activo == "Y")
                tvEstado.text = "Activo"
            else
                tvEstado.text = "No Activo"

            tvDni.text = name.NumeroDocumento
            ivView.setOnClickListener {
                listener.menuItemClickListener(1, name)
            }
            ivEdit.setOnClickListener {
                listener.menuItemClickListener(2, name)
            }
        }
    }
}