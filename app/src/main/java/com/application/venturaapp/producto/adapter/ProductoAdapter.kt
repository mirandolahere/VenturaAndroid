package com.application.venturaapp.producto.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.fitosanitario.listener.FitosanitarioDetalleItemListener
import com.application.venturaapp.producto.entity.Producto
import com.application.venturaapp.producto.listener.ProductoListener


class ProductoAdapter(
    internal var listener: ProductoListener,
    laborLista: List<Producto>,
    etapa: String
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)
    val etapa = etapa
    private var lastSelectedPosition = -1


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_fertilizante,
            viewGroup,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ProductoAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var rbSelected: RadioButton = itemView.findViewById(R.id.rbSelected)
        var tvCodigoSelected: TextView = itemView.findViewById(R.id.tvCodigoSelected)


        @SuppressLint("ResourceAsColor")
        fun bindItem(name: Producto, position: Int) {
          tvCodigoSelected.setText("Codigo")

            rbSelected.setOnClickListener {
                lastSelectedPosition = getAdapterPosition();
                listener.laborItemClickListener(name)

                notifyDataSetChanged();
            }


        }
    }
}