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
import java.lang.NullPointerException
import java.lang.StringBuilder
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
            tvCodigo.text = name.U_VS_AGR_CDLC
            tvJornalValor.text = name.U_VS_AGR_TOJR.toString()

            if (!(name.U_VS_AGR_HRFN == null || name.U_VS_AGR_HRIN == null)) {
                val textView = tvNombres
                val append = StringBuilder().append("Jornada : ")
                val u_vs_agr_hrin: String = name.U_VS_AGR_HRIN
                if (u_vs_agr_hrin != null) {
                    val append2: StringBuilder = append.append(
                        u_vs_agr_hrin.removeRange(5,7).toString()
                    ).append(" - ")
                    val u_vs_agr_hrfn: String = name.U_VS_AGR_HRFN
                    if (u_vs_agr_hrfn != null) {
                        textView.setText(
                            append2.append(
                                u_vs_agr_hrfn.removeRange(
                                    5,8
                                ).toString()
                            ).toString()
                        )
                    } else {
                        throw NullPointerException("null cannot be cast to non-null type kotlin.CharSequence")
                    }
                } else {
                    throw NullPointerException("null cannot be cast to non-null type kotlin.CharSequence")
                }
            }
          //  tvValorHora.text = name.U_VS_AGR_TOHX.toString()
           // tvNombres.text = name.Nombre.toString()

          /*  when(name.U_VS_AGR_ESTA)
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

            }*/


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