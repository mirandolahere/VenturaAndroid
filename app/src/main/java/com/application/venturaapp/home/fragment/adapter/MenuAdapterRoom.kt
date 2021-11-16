package com.application.venturaapp.home.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.venturaapp.R
import com.application.venturaapp.home.listener.MenuItemListenerRoom
import com.application.venturaapp.tables.PersonalDatoRoom


class MenuAdapterRoom(
    internal var listener: MenuItemListenerRoom, var list: List<PersonalDatoRoom> ) : RecyclerView.Adapter<MenuAdapterRoom.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_personal,
            viewGroup,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MenuAdapterRoom.ViewHolder, i: Int) {
        val name = list[i]

        viewHolder.bindItem(name, i)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCodigo: TextView = itemView.findViewById(R.id.tvCodigo)
        var tvNombres: TextView = itemView.findViewById(R.id.tvNombres)
        var tvDni: TextView = itemView.findViewById(R.id.tvDni)
        var ivView: ImageView = itemView.findViewById(R.id.ivView)
        var ivEdit: ImageView = itemView.findViewById(R.id.ivEdit)


        fun bindItem(name: PersonalDatoRoom, position: Int) {
            tvCodigo.text = name.Code
            if(name.SegundoNombre!=null)
            tvNombres.text = name.PrimerNombre + " " + name.SegundoNombre + " " +  name.ApellidoMaterno +" " + name.ApellidoPaterno
            else
                tvNombres.text = name.PrimerNombre + " " +  name.ApellidoMaterno +" " + name.ApellidoPaterno

            tvDni.text = name.NumeroDocumento
            ivView.setOnClickListener {
                listener.menuItemRoomClickListener(1, name)
            }
            ivEdit.setOnClickListener {
                listener.menuItemRoomClickListener(2, name)
            }
        }
    }
}