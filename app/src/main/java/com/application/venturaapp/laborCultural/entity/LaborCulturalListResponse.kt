package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName

data class LaborCulturalListResponse(

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
        @SerializedName("RequestStatus")
        val RequestStatus: String,
        @SerializedName("Creator")
        val Creator: String,
        @SerializedName("Remark")
        val Remark: String,
        @SerializedName("DocEntry")
        val DocEntry: String,
        @SerializedName("Canceled")
        val Canceled: String,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("LogInst")
        val LogInst: String,
        @SerializedName("UserSign")
        val UserSign: String,
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
        @SerializedName("U_VS_AGR_COPE")
        val U_VS_AGR_COPE: String,
        @SerializedName("U_VS_AGR_COEP")
        val U_VS_AGR_COEP: String,
        @SerializedName("U_VS_AGR_DEEP")
        val U_VS_AGR_DEEP: String,
        @SerializedName("U_VS_AGR_FEOC")
        val U_VS_AGR_FEOC: String,
        @SerializedName("U_VS_AGR_CDCA")
        val U_VS_AGR_CDCA: String,
        @SerializedName("U_VS_AGR_CDPP")
        val U_VS_AGR_CDPP: String,
        @SerializedName("U_VS_AGR_CDEP")
        val U_VS_AGR_CDEP: String,
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_USCA")
        val U_VS_AGR_USCA: String,
        @SerializedName("U_VS_AGR_USAA")
        val U_VS_AGR_USAA: String,
        @SerializedName("U_VS_AGR_RORI")
        val U_VS_AGR_RORI: String,
        @SerializedName("U_VS_AGR_FERG")
        val U_VS_AGR_FERG: String,
        var TOTALJORNALES: Int,
        @SerializedName("VS_AGR_DCULCollection")
        val VS_AGR_DCULCollection: List<LaborCulturalDetalleResponse>?
)