package com.application.venturaapp.fertilizante.adapter

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
import com.application.venturaapp.fertilizante.listener.VSAGRRFERItemListener
import com.application.venturaapp.fitosanitario.entity.VSAGRRCOS
import com.application.venturaapp.fitosanitario.entity.VS_AGR_DSCOCollection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class FertilizantePEPAdapter(
    internal var listener: VSAGRRFERItemListener,
    laborLista: ArrayList<VS_AGR_DSCOCollection>,
    vsagrrcos: ArrayList<VSAGRRCOS>
) : RecyclerView.Adapter<FertilizantePEPAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)
    private val options2 =  vsagrrcos

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_fertilizante_detalle, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: FertilizantePEPAdapter.ViewHolder, i: Int) {
        val name = options[i]
        val options2 = options2[0]
        viewHolder.bindItem(name,options2, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvArticulo: TextView = itemView.findViewById(R.id.tvArticuloNombre)

        var textoJornales: TextView = itemView.findViewById(R.id.tvCampaña)

        var ivView: ImageView = itemView.findViewById(R.id.ivView)
        var ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)
        var ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
     /*   var itemView: LinearLayout = itemView.findViewById(R.id.llItem)
        var ivView: ImageView = itemView.findViewById(R.id.ivView)*/


        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(name: VS_AGR_DSCOCollection, vsagrrcos:VSAGRRCOS, position: Int) {
            tvArticulo.text = vsagrrcos.U_VS_AGR_CDAT
            tvCodigo.text = vsagrrcos.U_VS_AGR_FERG
            textoJornales.text = name.U_VS_AGR_TOAT.toString()

            ivView.setOnClickListener {
                listener.laborItemClickListener(name, vsagrrcos)
            }

            ivEdit.setOnClickListener {
                listener.laborUpdateClickListener(name, vsagrrcos)
            }

            ivDelete.setOnClickListener {
                listener.laborDeleteClickListener(name, vsagrrcos)
            }
       //     tvJornales.text = name
          /*  ivView.setOnClickListener {
                listener.laborItemClickListener(name, vsagrrcos)
            } */
        }
    }
}