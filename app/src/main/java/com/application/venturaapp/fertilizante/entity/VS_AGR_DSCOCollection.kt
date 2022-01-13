package com.application.venturaapp.fitosanitario.entity

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class VS_AGR_DSCOCollection (

        @SerializedName("DocEntry")
        val DocEntry: Int,
        @SerializedName("LineId")
        val LineId: Int,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("VisOrder")
        val VisOrder: Int,
        @SerializedName("U_VS_AGR_CDLT")
        val U_VS_AGR_CDLT: String,

        @SerializedName("U_VS_AGR_TOAT")
        val U_VS_AGR_TOAT: Double,
        @SerializedName("U_VS_AGR_FEVC")
        val U_VS_AGR_FEVC: String,
        @SerializedName("U_VS_AGR_COMN")
        val U_VS_AGR_COMN: String,
        @SerializedName("U_VS_AGR_USCA")
        val U_VS_AGR_USCA: String,
)