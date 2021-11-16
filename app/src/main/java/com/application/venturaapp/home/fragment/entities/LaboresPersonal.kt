package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class LaboresPersonal (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("Code")
        val Code: String,
        @SerializedName("Name")
        val Name: String,
        @SerializedName("DocEntry")
        val DocEntry: String,
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
        @SerializedName("U_VS_AGR_COJR")
        val U_VS_AGR_COJR: String,
        @SerializedName("U_VS_AGR_DEJR")
        val U_VS_AGR_DEJR: String,
        @SerializedName("U_VS_AGR_CXJR")
        val U_VS_AGR_CXJR: Double,
        @SerializedName("U_VS_AGR_TOHR")
        val U_VS_AGR_TOHR: Double,
        @SerializedName("U_VS_AGR_HEXT")
        val U_VS_AGR_HEXT: String,
        @SerializedName("U_VS_AGR_DEHR")
        val U_VS_AGR_DEHR: String,
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_USCA")
        val U_VS_AGR_USCA: String,

        @SerializedName("U_VS_AGR_USAA")
        val U_VS_AGR_USAA: String,
        @SerializedName("U_VS_AGR_RORI")
        val U_VS_AGR_RORI: String,
        @SerializedName("U_VS_AGR_CDRJ")
        val U_VS_AGR_CDRJ: String,
        @SerializedName("U_VS_AGR_DSRJ")
        val U_VS_AGR_DSRJ: String,
        @SerializedName("U_VS_AGR_CDRX")
        val U_VS_AGR_CDRX: String,
        @SerializedName("U_VS_AGR_DSRX")
        val U_VS_AGR_DSRX: String
)