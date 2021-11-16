package com.application.venturaapp.helper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.venturaapp.R
import com.application.venturaapp.personalAdd.personalAdd
import com.application.venturaapp.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_alert_dialog.*
import kotlinx.android.synthetic.main.activity_alert_dialog.btnClose
import kotlinx.android.synthetic.main.activity_contabilizar.*


class ContabilizadorActivity  : AppCompatActivity() {

    lateinit var pref: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contabilizar)
        pref = PreferenceManager(this)
        this.setFinishOnTouchOutside(false);

        btnAceptar.setOnClickListener {
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()

            }
        btnClose.setOnClickListener {
            closeDialog()

        }

    }

    private fun closeDialog() {
        finish()
    }
}