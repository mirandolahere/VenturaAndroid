package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName

data class LaborCulturalDetalleResponse(

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
        @SerializedName("Nombre") // se trae de otro servicio
        var Nombre: String,
        @SerializedName("DescripcionDeLabor") // se trae de otro servici
        var DescripcionDeLabor: String,
        @SerializedName("NumeroDocumento") // se trae de otro servici
        var NumeroDocumento: String,
        @SerializedName("U_VS_AGR_CDLC")
        val U_VS_AGR_CDLC: String,
        @SerializedName("U_VS_AGR_CDPS")
        val U_VS_AGR_CDPS: String,
        @SerializedName("U_VS_AGR_HRIN")
        val U_VS_AGR_HRIN: String,
        @SerializedName("U_VS_AGR_HRFN")
        val U_VS_AGR_HRFN: String,
        @SerializedName("U_VS_AGR_ESTA")
        val U_VS_AGR_ESTA: String,
        @SerializedName("U_VS_AGR_IDEP")
        val U_VS_AGR_IDEP: Int,
        @SerializedName("U_VS_AGR_LNEP")
        val U_VS_AGR_LNEP: Int,
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_USCA")
        val U_VS_AGR_USCA: String,
        @SerializedName("U_VS_AGR_USAA")
        val U_VS_AGR_USAA: String,
        @SerializedName("U_VS_AGR_RORI")
        val U_VS_AGR_RORI: String,
        @SerializedName("U_VS_AGR_TOJR")
        val U_VS_AGR_TOJR: Int,
        @SerializedName("U_VS_AGR_TOHX")
        val U_VS_AGR_TOHX: Int
)