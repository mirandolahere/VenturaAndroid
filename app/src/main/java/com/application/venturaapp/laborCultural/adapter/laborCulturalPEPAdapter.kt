package com.application.venturaapp.laborCultural.adapter

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
import com.application.venturaapp.home.listener.LaborItemListener
import com.application.venturaapp.laborCultural.entity.LaborCulturalListResponse
import com.application.venturaapp.laborCultural.listener.LaborPEPItemListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class laborCulturalPEPAdapter(internal var listener: LaborPEPItemListener, laborLista: ArrayList<LaborCulturalListResponse>
                    ) : RecyclerView.Adapter<laborCulturalPEPAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_labor_cultural_pep, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: laborCulturalPEPAdapter.ViewHolder, i: Int) {
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
        var itemView: LinearLayout = itemView.findViewById(R.id.llItem)
        var ivView: ImageView = itemView.findViewById(R.id.ivView)


        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(name: LaborCulturalListResponse, position: Int) {
            tvCodigo.text = name.U_VS_AGR_CDPP
            tvEtapa.text = name.U_VS_AGR_CDEP
            tvJornales.text = name.TOTALJORNALES.toString()

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