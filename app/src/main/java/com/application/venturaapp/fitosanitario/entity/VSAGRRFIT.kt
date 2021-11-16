package com.application.venturaapp.fitosanitario.entity

import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class VSAGRRFIT (

        @SerializedName("DocNum")
        val DocNum: Int,
        @SerializedName("Period")
        val Period: Int,
        @SerializedName("Instance")
        val Instance: Int,
        @SerializedName("Series")
        val Series: Int,
        @SerializedName("Handwrtten")
        val Handwrtten: String,
        @SerializedName("Status")
        val Status: String,
        @SerializedName("Creator")
        val Creator: String,
        @SerializedName("Remark")
        val Remark: String,
        @SerializedName("DocEntry")
        val DocEntry: Int,
        @SerializedName("Canceled")
        val Canceled: String,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("LogInst")
        val LogInst: String,
        @SerializedName("UserSign")
        val UserSign: Int,

        @SerializedName("Transfered")
        val Transfered: String,
        @SerializedName("CreateDate")
        val CreateDate: String,
        @SerializedName("CreateTime")
        val CreateTime: String,
        @SerializedName("UpdateDate")
        val UpdateDate: String,
        @SerializedName("UpdateTime")
        val UpdateTime: String,
        @SerializedName("DataSource")
        val DataSource: String,
        @SerializedName("U_VS_AGR_CDCA")
        val U_VS_AGR_CDCA: String,
        @SerializedName("U_VS_AGR_CDPP")
        var U_VS_AGR_CDPP : String,
        @SerializedName("U_VS_AGR_CDEP")
        val U_VS_AGR_CDEP: String,
        @SerializedName("U_VS_AGR_USCA")
        val U_VS_AGR_USCA: String,
        @SerializedName("U_VS_AGR_USAA")
        val U_VS_AGR_USAA: String,
        @SerializedName("U_VS_AGR_RORI")
        val U_VS_AGR_RORI : String,
        @SerializedName("U_VS_AGR_ESTA")
        val U_VS_AGR_ESTA: String,
        @SerializedName("U_VS_AGR_FERG")
        val U_VS_AGR_FERG: String,
        var EtapaNombre: String,
        @SerializedName("VS_AGR_DRFTCollection")
        val VS_AGR_DRFTCollection: List<FitosanitarioDetalleResponse>?



)