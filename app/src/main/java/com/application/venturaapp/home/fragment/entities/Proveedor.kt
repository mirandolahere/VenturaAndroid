package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class Proveedor (


        @SerializedName("CardCode")
        val CardCode: String,
        @SerializedName("CardName")
        val CardName: String,
        @SerializedName("CardType")
        val CardType: String,
        @SerializedName("U_VS_AGR_PRRH")
        val U_VS_AGR_PRRH : String
)