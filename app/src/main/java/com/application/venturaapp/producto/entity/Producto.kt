package com.application.venturaapp.producto.entity

import com.google.gson.annotations.SerializedName

data class Producto (

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
        @SerializedName("U_VS_AGR_TART")
        val U_VS_AGR_TART: String,
        @SerializedName("U_VS_AGR_DSCM")
        val U_VS_AGR_DSCM: String,
        @SerializedName("U_VS_AGR_DQIA")
        val U_VS_AGR_DQIA: String,
        @SerializedName("U_VS_AGR_PXUM")
        val U_VS_AGR_PXUM: Double,
        @SerializedName("U_VS_AGR_DOSS")
        val U_VS_AGR_DOSS: String,
        @SerializedName("U_VS_AGR_TOHT")
        val U_VS_AGR_TOHT: Double,
        @SerializedName("U_VS_AGR_VCHT")
        val U_VS_AGR_VCHT: Double,
        @SerializedName("U_VS_AGR_IMAG")
        val U_VS_AGR_IMAG: String,
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_USCA")
        val U_VS_AGR_USCA: Double,
        @SerializedName("U_VS_AGR_USAA")
        val U_VS_AGR_USAA: String,
        @SerializedName("U_VS_AGR_RORI")
        val U_VS_AGR_RORI: String,
        @SerializedName("U_VS_AGR_UMIT")
        val U_VS_AGR_UMIT: Double,
        @SerializedName("U_VS_AGR_UMHT")
        val U_VS_AGR_UMHT: Double,


)