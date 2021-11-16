package com.application.venturaapp.fitosanitario.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.fitosanitario.entity.FitosanitarioDetalleResponse
import com.application.venturaapp.fitosanitario.listener.FitosanitarioDetalleItemListener
import com.application.venturaapp.home.listener.LaborItemListener
import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.listener.LaborDetalleItemListener
import kotlin.collections.ArrayList


class FitosanitarioDetalleAdapter(internal var listener: FitosanitarioDetalleItemListener, laborLista: List<FitosanitarioDetalleResponse>, etapa : String
                    ) : RecyclerView.Adapter<FitosanitarioDetalleAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)
    val etapa = etapa


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_labor_detalle, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: FitosanitarioDetalleAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvJornalValor: TextView = itemView.findViewById(R.id.tvJornalValor)
        var tvJornal: TextView = itemView.findViewById(R.id.tvJornal)

        var tvValorHora: TextView = itemView.findViewById(R.id.tvValorHora)
        var tvHora: TextView = itemView.findViewById(R.id.tvHora)
        var tvEtapaDes: TextView = itemView.findViewById(R.id.tvEtapaDes)


        var ivView: ImageView = itemView.findViewById(R.id.ivView)
        var tvNombres: TextView = itemView.findViewById(R.id.tvNombres)
        var tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
         var ivEdit : ImageView = itemView.findViewById(R.id.ivEdit)
        var ivDelete : ImageView = itemView.findViewById(R.id.ivDelete)
        var tvCodigoTexto: TextView = itemView.findViewById(R.id.tvCodigoTexto)
        var llEtapa: LinearLayout = itemView.findViewById(R.id.llEtapa)


        @SuppressLint("ResourceAsColor")
        fun bindItem(name: FitosanitarioDetalleResponse, position: Int) {
            llEtapa.visibility= View.VISIBLE
            tvNombres.visibility = View.GONE
            tvCodigoTexto.text = "Cod. Químico "
            tvCodigo.text = name.U_VS_AGR_CDQU
            tvJornal.text ="Cantidad: "
            tvJornalValor.text = name.U_VS_AGR_TOQU.toString()
            tvHora.text ="Almacén: "
            tvValorHora.text = name.U_VS_AGR_CDAL.toString()
            tvEtapaDes.text = etapa

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