package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName

data class ContabilizacionActualizacionResponse(

        @SerializedName("LineNumber")
        val LineNumber: Int,
        @SerializedName("ItemNo")
        val ItemNo: String,
        @SerializedName("PlannedQuantity")
        val PlannedQuantity: Double,
        @SerializedName("U_VS_AGR_CDPE")
        val U_VS_AGR_CDPE: String,
        @SerializedName("U_VS_AGR_CDRA")
        val U_VS_AGR_CDRA: String,
        @SerializedName("U_VS_AGR_OBAG")
        val U_VS_AGR_OBAG: String,
        @SerializedName("U_VS_AGR_HRXT")
        val U_VS_AGR_HRXT: String
)