package com.application.venturaapp.fertilizante.entity

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class UnidadMedida (

        @SerializedName("Code")
        val Code: String,
        @SerializedName("Name")
        val Name: String,
        @SerializedName("U_VS_UM_ABREV")
        val U_VS_UM_ABREV: String
)