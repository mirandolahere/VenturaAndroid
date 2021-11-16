package com.application.venturaapp.laborCultural.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.home.listener.LaborItemListener
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.listener.LaborDetalleItemListener
import kotlin.collections.ArrayList


class laborCulturalDetalleAdapter(internal var listener: LaborDetalleItemListener, laborLista: List<LaborCulturalDetalleResponse>
                    ) : RecyclerView.Adapter<laborCulturalDetalleAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_labor_detalle, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: laborCulturalDetalleAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvJornalValor: TextView = itemView.findViewById(R.id.tvJornalValor)
        var tvValorHora: TextView = itemView.findViewById(R.id.tvValorHora)
        var ivView: ImageView = itemView.findViewById(R.id.ivView)
        var tvNombres: TextView = itemView.findViewById(R.id.tvNombres)
        var tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
         var ivEdit : ImageView = itemView.findViewById(R.id.ivEdit)
        var ivDelete : ImageView = itemView.findViewById(R.id.ivDelete)


        @SuppressLint("ResourceAsColor")
        fun bindItem(name: LaborCulturalDetalleResponse, position: Int) {
            tvCodigo.text = name.U_VS_AGR_CDPS
            tvJornalValor.text = name.U_VS_AGR_TOJR.toString()
            tvValorHora.text = name.U_VS_AGR_TOHX.toString()
            tvNombres.text = name.Nombre.toString()

            when(name.U_VS_AGR_ESTA)
            {
                "P" -> {tvEstado.text ="(Pendiente)"
                        tvEstado.setTextColor(Color.parseColor("#F9B122"))
                    ivView.visibility=View.GONE
                    ivEdit.visibility = View.VISIBLE}
                "C" -> {tvEstado.text ="(Contabilizado)"
                    tvEstado.setTextColor(Color.parseColor("#2BBD6F"))
                    ivEdit.visibility=View.GONE
                    ivDelete.visibility=View.GONE

                    ivView.visibility = View.VISIBLE}

            }


            ivView.setOnClickListener {
                listener.laborItemClickListener(name)
            }

            ivEdit.setOnClickListener {
                listener.laborItemUpdateClickListener(name)
            }
            ivDelete.setOnClickListener {
                listener.laborItemDeletelickListener(name)
            }
        }
    }
}