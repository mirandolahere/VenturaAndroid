package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class PEPDato (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("Code")
        var Code: String,
        var TotalJornales: Int=0,
        @SerializedName("Name")
        var Name: String,
        @SerializedName("DocEntry")
        val Cabecera: String,
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
        var UpdateDate: String,
        @SerializedName("UpdateTime")
        val UpdateTime: String,
        @SerializedName("DataSource")
        val DataSource: String,
        @SerializedName("U_VS_AGR_FECI")
        val U_VS_AGR_FECI: String,
        @SerializedName("U_VS_AGR_COCA")
        val U_VS_AGR_COCA: String,
        @SerializedName("U_VS_AGR_NOCA")
        val U_VS_AGR_NOCA: Double,
        @SerializedName("U_VS_AGR_COFU")
        val U_VS_AGR_COFU: Double,
        @SerializedName("U_VS_AGR_NOFU")
        val U_VS_AGR_NOFU: String,
        @SerializedName("U_VS_AGR_NOSE")
        val U_VS_AGR_NOSE: String,
        @SerializedName("U_VS_AGR_COLO")
        val U_VS_AGR_COLO: String,
        @SerializedName("U_VS_AGR_NOLO")
        val U_VS_AGR_NOLO: String,
        @SerializedName("U_VS_AGR_COCU")
        val U_VS_AGR_COCU: String,
        @SerializedName("U_VS_AGR_NOCU")
        val U_VS_AGR_NOCU: String,
        @SerializedName("U_VS_AGR_COVA")
        val U_VS_AGR_COVA : String,
        @SerializedName("U_VS_AGR_NOVA")
        val U_VS_AGR_NOVA: String,
        @SerializedName("U_VS_AGR_EPEP")
        val U_VS_AGR_EPEP : String,
        @SerializedName("U_VS_AGR_DEEP")
        val U_VS_AGR_DEEP: String,

        @SerializedName("U_VS_AGR_CEPR")
        val U_VS_AGR_CEPR: String,
        @SerializedName("U_VS_AGR_DEGR")
        val U_VS_AGR_DEGR : String,
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_PEIC")
        val U_VS_AGR_PEIC: String,
        @SerializedName("U_VS_AGR_PEFC")
        val U_VS_AGR_PEFC : String,
        @SerializedName("U_VS_AGR_RORI")
        val U_VS_AGR_RORI: String,

        @SerializedName("U_VS_AGR_CDCA")
        var U_VS_AGR_CDCA: String,
        @SerializedName("U_VS_AGR_DSCA")
        var U_VS_AGR_DSCA : String,
        @SerializedName("U_VS_AGR_CDFD")
        var U_VS_AGR_CDFD: String,

        @SerializedName("U_VS_AGR_DSFD")
        var U_VS_AGR_DSFD: String,
        @SerializedName("U_VS_AGR_CDSC")
        var U_VS_AGR_CDSC : String,
        @SerializedName("U_VS_AGR_DSSC")
        var U_VS_AGR_DSSC: String,
        @SerializedName("U_VS_AGR_DSLO")
        val U_VS_AGR_DSLO: String,
        @SerializedName("U_VS_AGR_CDCL")
        var U_VS_AGR_CDCL : String,
        @SerializedName("U_VS_AGR_CDVA")
        var U_VS_AGR_CDVA: String,

        @SerializedName("U_VS_AGR_DSVA")
        var U_VS_AGR_DSVA: String,
        @SerializedName("U_VS_AGR_CDAT")
        var U_VS_AGR_CDAT : String,
        @SerializedName("U_VS_AGR_DSAT")
        var U_VS_AGR_DSAT: String,

        @SerializedName("U_VS_AGR_ESTA")
        val U_VS_AGR_ESTA: String,
        @SerializedName("U_VS_AGR_CDGE")
        val U_VS_AGR_CDGE : String,
        @SerializedName("U_VS_AGR_DSGE")
        val U_VS_AGR_DSGE: String,
        @SerializedName("U_VS_AGR_FEIP")
        val U_VS_AGR_FEIP: String,
        @SerializedName("U_VS_AGR_FEFP")
        val U_VS_AGR_FEFP : String,
        @SerializedName("U_VS_AGR_CDLT")
        var U_VS_AGR_CDLT: String,
        @SerializedName("U_VS_AGR_DEOF")
        val U_VS_AGR_DEOF: String,
        @SerializedName("U_VS_AGR_CDPP")
        val U_VS_AGR_CDPP: String
        )