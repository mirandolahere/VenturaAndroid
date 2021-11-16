package com.application.venturaapp.fitosanitario.entity

import com.google.gson.annotations.SerializedName


data class GeneralResponse<T> (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("value")
        val datos: List<T>
)