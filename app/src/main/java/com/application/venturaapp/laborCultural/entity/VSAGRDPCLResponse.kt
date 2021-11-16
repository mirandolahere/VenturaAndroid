package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName


data class VSAGRDPCLResponse (

        @SerializedName("DocEntry")
        val DocEntry: Int,
        @SerializedName("LineId")
        val LineId: Int,
        @SerializedName("VisOrder")
        val VisOrder: Int,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("U_VS_AGR_CDLC")
        val U_VS_AGR_CDLC: String,
        @SerializedName("U_VS_AGR_CDPE")
        val U_VS_AGR_CDPE: String,
        @SerializedName("U_VS_AGR_TOJR")
        val U_VS_AGR_TOJR: Int
)