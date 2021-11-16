package com.application.venturaapp.helper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.venturaapp.R
import com.application.venturaapp.personalAdd.personalAdd
import com.application.venturaapp.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_contabilizar_confirmar.*


class ContabilizadorConfirmarActivity  : AppCompatActivity() {

    lateinit var pref: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contabilizar_confirmar)
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