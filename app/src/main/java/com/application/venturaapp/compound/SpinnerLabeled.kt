package com.application.venturaapp.compound

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.spinner_labeled.view.*
import android.widget.TextView
import android.graphics.Color
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.application.venturaapp.R
import com.application.venturaapp.helper.TipoDocumento

class SpinnerLabeled @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, defStyleAttr) {

    val TAG = SpinnerLabeled::class.java.simpleName

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.spinner_labeled, this)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.SpinnerLabeled)
        try {
            val text = ta.getString(R.styleable.SpinnerLabeled_label_text)
            //txv_label.text = text
            val onHide = ta.getBoolean(R.styleable.SpinnerLabeled_hide_line, true)
            vw_line.visibility = if (onHide) View.GONE else View.VISIBLE
            val colorString: String? = ta.getString(R.styleable.SpinnerLabeled_selected_color)
            val textSize = ta.getDimensionPixelSize(R.styleable.SpinnerLabeled_android_textSize, 14)
        } finally {
            ta.recycle()
        }
    }

    fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener?) {
        spnGenerico.setSelection(0, false)
        spnGenerico.onItemSelectedListener = listener
    }

    fun setOnItemSelectedListenerWithoutSelect(listener: AdapterView.OnItemSelectedListener?){
        spnGenerico.onItemSelectedListener = listener
    }

    /*fun setLabelText(text: String) {
        txv_label.text = text
    }*/

    fun loadSpinnerWithOneItem(dato: String) {
        val datos = arrayListOf(dato)
        val adapter = ArrayAdapter(context, R.layout.spinner_item, datos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnGenerico.adapter = adapter
    }

    fun loadSpinnerGenerico(datos: ArrayList<TipoDocumento>) {
        val adapter = ArrayAdapter(context, R.layout.spinner_item, datos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnGenerico.adapter = adapter
    }

    fun loadSpinnerWithHint(datos: List<String>) {
        val adapter = object : ArrayAdapter<String>(context, R.layout.spinner, datos) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?,
                                         parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnGenerico.adapter = adapter
    }

    fun disableSpinner() {
        spnGenerico.isEnabled = false
    }
}
