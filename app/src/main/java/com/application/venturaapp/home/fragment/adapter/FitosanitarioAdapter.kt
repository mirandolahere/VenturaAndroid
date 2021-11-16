package com.application.venturaapp.home.fragment.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.home.fragment.entities.LaborCultural
import com.application.venturaapp.home.fragment.entities.PEPDato
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.listener.FitosanitarioItemListener
import com.application.venturaapp.home.listener.LaborItemListener
import com.application.venturaapp.home.listener.MenuItemListener
import kotlinx.android.synthetic.main.activity_personal_add.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class FitosanitarioAdapter(internal var listener: FitosanitarioItemListener, personalLista: ArrayList<PEPDato>
                    ) : RecyclerView.Adapter<FitosanitarioAdapter.ViewHolder>() {

    private val options =  ArrayList(personalLista)


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_labor_cultural, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: FitosanitarioAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvUltimoRegistro: TextView = itemView.findViewById(R.id.tvUltimoRegistro)
        var tvJornales: TextView = itemView.findViewById(R.id.tvJornales)
        var txtJornales: TextView = itemView.findViewById(R.id.TextJornales)
        var Campaña1: TextView = itemView.findViewById(R.id.tvCampaña)
        var item: LinearLayout = itemView.findViewById(R.id.llItem)


        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(name: PEPDato, position: Int) {
            txtJornales.visibility=View.GONE
            tvJornales.visibility = View.GONE
            var simpleFormat = DateTimeFormatter.ISO_DATE;
            var convertedDate = LocalDate.parse(name.UpdateDate, simpleFormat)
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
            tvUltimoRegistro.setText(fecha)

            tvCodigo.text = name.Code
            Campaña1.text = name.U_VS_AGR_DSCA
            item.setOnClickListener {
                listener.laborItemClickListener(name)
            }
        }
    }
}