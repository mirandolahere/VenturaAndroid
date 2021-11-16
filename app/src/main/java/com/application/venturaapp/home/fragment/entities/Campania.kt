package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class Campania (

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
        @SerializedName("UserSign")
        val UserSign: String,
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
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_ESTA")
        val U_VS_AGR_ESTA: String,
        @SerializedName("U_VS_AGR_TICA")
        val U_VS_AGR_TICA: String,
        @SerializedName("U_VS_AGR_NMES")
        val U_VS_AGR_NMES: String,
        @SerializedName("U_VS_AGR_PEIC")
        val U_VS_AGR_PEIC: String,
        @SerializedName("U_VS_AGR_PEFC")
        val U_VS_AGR_PEFC: String,
        @SerializedName("U_VS_AGR_FEIC")
        val U_VS_AGR_FEIC: String,
        @SerializedName("U_VS_AGR_FEFC")
        val U_VS_AGR_FEFC: String
)