package com.application.venturaapp.fitosanitario.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.fitosanitario.entity.VSAGRRFIT
import com.application.venturaapp.fitosanitario.listener.VSAGRRFITItemListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class FitosanitarioPEPAdapter(internal var listener: VSAGRRFITItemListener, laborLista: ArrayList<VSAGRRFIT>
                    ) : RecyclerView.Adapter<FitosanitarioPEPAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_labor_cultural_pep, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: FitosanitarioPEPAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvEtapa: TextView = itemView.findViewById(R.id.tvEtapa)
        var tvJornales: TextView = itemView.findViewById(R.id.tvJornales)
        var textoJornales: TextView = itemView.findViewById(R.id.textoJornales)

        var itemView: LinearLayout = itemView.findViewById(R.id.llItem)
        var ivView: ImageView = itemView.findViewById(R.id.ivView)


        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(name: VSAGRRFIT, position: Int) {
            tvCodigo.text = name.U_VS_AGR_CDPP
            tvEtapa.text = name.EtapaNombre

            tvJornales.visibility = View.GONE
            textoJornales.visibility = View.GONE

            var simpleFormat = DateTimeFormatter.ISO_DATE;
            var convertedDate = LocalDate.parse(name.U_VS_AGR_FERG, simpleFormat)
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

            tvFecha.text = fecha
       //     tvJornales.text = name
            ivView.setOnClickListener {
                listener.laborItemClickListener(name)
            }
        }
    }
}