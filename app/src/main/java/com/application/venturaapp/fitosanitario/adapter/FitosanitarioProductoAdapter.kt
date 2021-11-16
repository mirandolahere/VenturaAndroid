package com.application.venturaapp.fitosanitario.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.fitosanitario.entity.VSAGRFEQU
import com.application.venturaapp.fitosanitario.listener.VSAGRFEQUEItemListener


class FitosanitarioProductoAdapter(
    internal var listener: VSAGRFEQUEItemListener, laborLista: ArrayList<VSAGRFEQU>
) : RecyclerView.Adapter<FitosanitarioProductoAdapter.ViewHolder>() {

    private val options =  ArrayList(laborLista)
    private var lastSelectedPosition = -1

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_fertilizante,
            viewGroup,
            false
        )
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: FitosanitarioProductoAdapter.ViewHolder, i: Int) {
        val name = options[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigoSelected: TextView = itemView.findViewById(R.id.tvCodigoSelected)
        var rbSelected: RadioButton = itemView.findViewById(R.id.rbSelected)


        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(name: VSAGRFEQU, position: Int) {
            tvCodigoSelected.text = name.Name

            rbSelected.setOnClickListener {
                lastSelectedPosition = adapterPosition;
                notifyDataSetChanged()

                listener.laborItemClickListener(name)
            }
        }
    }
}