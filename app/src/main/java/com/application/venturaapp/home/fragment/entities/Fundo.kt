package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class Fundo (

        @SerializedName("Canceled")
        val  Canceled:String,
        @SerializedName("Code")
        val  Code:String,
        @SerializedName("CreateDate")
        val  CreateDate:String,
        @SerializedName("CreateTime")
        val  CreateTime:String,
        @SerializedName("DataSource")
        val  DataSource:String,
        @SerializedName("DocEntry")
        val  DocEntry:String,
        @SerializedName("Name")
        val  Name:String,
        @SerializedName("Object")
        val  Object:String,
        @SerializedName("U_VS_AGR_ACTV")
        val  U_VS_AGR_ACTV:String,
        @SerializedName("U_VS_AGR_DSPR")
        val  U_VS_AGR_DSPR:String,
        @SerializedName("U_VS_AGR_RORI")
        val  U_VS_AGR_RORI:String,
        @SerializedName("U_VS_AGR_TIPO")
        val  U_VS_AGR_TIPO:String,
        @SerializedName("U_VS_AGR_UBIG")
        val  U_VS_AGR_UBIG:String,
        @SerializedName("UpdateDate")
        val  UpdateDate:String,
        @SerializedName("UpdateTime")
        val  UpdateTime:String,
        @SerializedName("UserSign")
        val  UserSign:String,
        @SerializedName("odata.metadata")
        val metadata: String
)