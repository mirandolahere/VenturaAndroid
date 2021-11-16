package com.application.venturaapp.laborCultural.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.laborCultural.listener.PopupPersonalItemListener
import kotlin.collections.ArrayList


class popupDetalleAdapter(internal var listener: PopupPersonalItemListener, laborLista: List<PersonalDato>
                    ) : RecyclerView.Adapter<popupDetalleAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_personal_pop, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: popupDetalleAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvNombres: TextView = itemView.findViewById(R.id.tvNombres)
        var rlDetalle: LinearLayout = itemView.findViewById(R.id.rlDetalle)

        @SuppressLint("ResourceAsColor")
        fun bindItem(name: PersonalDato, position: Int) {

            tvCodigo.text = name.Code
            tvNombres.text = name.PrimerNombre +" " +name.ApellidoPaterno
            rlDetalle.setOnClickListener {
                listener.laborItemClickListener(name)
            }


        }
    }
}