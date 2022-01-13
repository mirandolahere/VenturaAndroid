package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class Lote (

        @SerializedName("Code")
        val  Code:String,
        @SerializedName("LineId")
        val  LineId: Int,
        @SerializedName("Object")
        val  Object:String,
        @SerializedName("U_VS_AGR_ACTV")
        val  U_VS_AGR_ACTV :String,
        @SerializedName("U_VS_AGR_CDLT")
        val  U_VS_AGR_CDLT :String,
        @SerializedName("U_VS_AGR_DSLT")
        val  U_VS_AGR_DSLT:String

)