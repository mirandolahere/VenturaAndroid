package com.application.venturaapp.helper

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.application.venturaapp.R
import com.application.venturaapp.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_alert_dialog.*


class AlertActivity  : AppCompatActivity() {

    lateinit var pref: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_dialog)
        pref = PreferenceManager(this)
        this.setFinishOnTouchOutside(false);
        val text = pref.getString(Constants.MESSAGE_ALERT)
        if(text=="¿Desea eliminar esta la labor cultural?")
            btnCancel.visibility = View.VISIBLE
        tvMessageDialog.text = text
        btnClose.setOnClickListener {
                closeDialog()

            }
        btnCancel.setOnClickListener {
            finish()

        }

    }

    private fun closeDialog() {
        val resultIntent = Intent()
        setResult(RESULT_OK, resultIntent)
        finish()
        finish()
    }
}