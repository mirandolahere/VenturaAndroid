package com.application.venturaapp.fitosanitario.entity

import com.google.gson.annotations.SerializedName

data class FitosanitarioDistribucionResponse(

        @SerializedName("DocEntry")
        val DocEntry: Int,
        @SerializedName("LineId")
        val LineId: Int,
        @SerializedName("VisOrder")
        val VisOrder: Int,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("LogInst")
        val LogInst: String,
        @SerializedName("U_VS_AGR_LNDR")
        var U_VS_AGR_LNDR: Int,
        @SerializedName("U_VS_AGR_CDAL")
        val U_VS_AGR_CDAL: String,
        @SerializedName("U_VS_AGR_IDUB")
        val U_VS_AGR_IDUB: String,
        @SerializedName("U_VS_AGR_CDUB")
        val U_VS_AGR_CDUB: String,
        @SerializedName("U_VS_AGR_CDLT")
        val U_VS_AGR_CDLT: String,
        @SerializedName("U_VS_AGR_TOQU")
        val U_VS_AGR_TOQU: Double,
        @SerializedName("U_VS_AGR_USCA")
        val U_VS_AGR_USCA: String,
        @SerializedName("U_VS_AGR_USAA")
        val U_VS_AGR_USAA: String,
        @SerializedName("U_VS_AGR_RORI")
        val U_VS_AGR_RORI: String,
        @SerializedName("U_VS_AGR_CDQU")
        val U_VS_AGR_CDQU: String,
        @SerializedName("U_VS_AGR_UMQU")
        val U_VS_AGR_UMQU: String,
)