package com.application.venturaapp.fitosanitario.entity

import com.google.gson.annotations.SerializedName

data class FitosanitarioDetalleResponse(

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
        @SerializedName("U_VS_AGR_CDQU") // se trae de otro servicio
        var U_VS_AGR_CDQU: String,
        @SerializedName("U_VS_AGR_DSQU") // se trae de otro servici
        var U_VS_AGR_DSQU: String,
        @SerializedName("U_VS_AGR_TOQU") // se trae de otro servici
        var U_VS_AGR_TOQU: Double,
        @SerializedName("U_VS_AGR_CDAL")
        val U_VS_AGR_CDAL: String,
        @SerializedName("U_VS_AGR_CXQU")
        val U_VS_AGR_CXQU: Double,
        @SerializedName("U_VS_AGR_TOCF")
        val U_VS_AGR_TOCF: Double,
        @SerializedName("U_VS_AGR_TOHT")
        val U_VS_AGR_TOHT: Double,
        @SerializedName("U_VS_AGR_ESTA")
        val U_VS_AGR_ESTA: String,
        @SerializedName("U_VS_AGR_IDEP")
        val U_VS_AGR_IDEP: Int,
        @SerializedName("U_VS_AGR_LNEP")
        val U_VS_AGR_LNEP: Int,
        @SerializedName("U_VS_AGR_TOCQ")
        val U_VS_AGR_TOCQ: String,
        @SerializedName("U_VS_AGR_UMQU")
        val U_VS_AGR_UMQU: String,
        @SerializedName("U_VS_AGR_UMHT")
        val U_VS_AGR_UMHT: String,
        @SerializedName("U_VS_AGR_VCHT")
        val U_VS_AGR_VCHT: String,
        @SerializedName("U_VS_AGR_DOSS")
        val U_VS_AGR_DOSS: String,
)