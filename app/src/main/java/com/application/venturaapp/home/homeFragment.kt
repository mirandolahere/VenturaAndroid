package com.application.venturaapp.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.venturaapp.R
import com.application.venturaapp.helper.AlertActivity
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.home.fragment.adapter.MenuAdapter
import com.application.venturaapp.home.fragment.adapter.MenuAdapterRoom
import com.application.venturaapp.home.fragment.entities.PersonalDato
import com.application.venturaapp.home.listener.MenuItemListener
import com.application.venturaapp.home.listener.MenuItemListenerRoom
import com.application.venturaapp.personalAdd.personalAdd
import com.application.venturaapp.preference.PreferenceManager
import com.application.venturaapp.tables.PersonalDatoRoom
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_personal.*
import java.util.*


class homeFragment  : Fragment() {
    lateinit var pref: PreferenceManager
    internal lateinit var builder: AlertDialog.Builder


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

