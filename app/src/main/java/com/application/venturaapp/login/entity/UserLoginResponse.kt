package com.application.venturaapp.login.entity

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("Code")
        val Code: String,
        @SerializedName("Name")
        val Name: String,
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
        @SerializedName("U_VS_AGR_BPNO")
        val U_VS_AGR_BPNO: String,
        @SerializedName("U_VS_AGR_BPN2")
        val U_VS_AGR_BPN2: String,
        @SerializedName("U_VS_AGR_BPAP")
        val U_VS_AGR_BPAP: String,
        @SerializedName("U_VS_AGR_BPAM")
        val U_VS_AGR_BPAM: String,
        @SerializedName("U_VS_AGR_DESC")
        val U_VS_AGR_DESC: String,
        @SerializedName("U_VS_AGR_MOVL")
        val U_VS_AGR_MOVL: String,
        @SerializedName("U_VS_AGR_TELF")
        val U_VS_AGR_TELF: String,
        @SerializedName("U_VS_AGR_CORR")
        val U_VS_AGR_CORR: String,
        @SerializedName("U_VS_AGR_CONT")
        val U_VS_AGR_CONT: String,
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_RECC")
        val U_VS_AGR_RECC: String,
        @SerializedName("U_VS_AGR_PRUS")
        val U_VS_AGR_PRUS: String,
        @SerializedName("U_VS_AGR_CDPR")
        val U_VS_AGR_CDPR: String,
        @SerializedName("U_VS_AGR_DSPR")
        val U_VS_AGR_DSPR: String
)