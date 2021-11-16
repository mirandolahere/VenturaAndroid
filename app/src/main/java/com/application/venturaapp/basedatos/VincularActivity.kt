package com.application.venturaapp.basedatos


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.application.venturaapp.R
import com.application.venturaapp.login.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.Cache


class VincularActivity   : BottomSheetDialogFragment() {

    lateinit var    viewModel: LoginViewModel
    private val REQUEST_ACTIVITY = 100
    private val REQUEST_ACTIVITY_BUSCAR = 101
    lateinit var httpCacheDirectory : Cache
     var peopleClass  = arrayListOf<String> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pop_vincular, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)



        setupObserver()
    }

    private fun setupObserver() {
        TODO("Not yet implemented")
    }


}

