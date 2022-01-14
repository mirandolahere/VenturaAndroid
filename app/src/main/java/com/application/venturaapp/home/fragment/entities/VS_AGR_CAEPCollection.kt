package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class VS_AGR_CAEPCollection (


        @SerializedName("Code")
        val Code: String,
        @SerializedName("LineId")
        val LineId: Int,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("U_VS_AGR_CDEP")
        val U_VS_AGR_CDEP: String,
        @SerializedName("U_VS_AGR_DSEP")
        val U_VS_AGR_DSEP: String,
        @SerializedName("U_VS_AGR_DURA")
        val U_VS_AGR_DURA: String,
        @SerializedName("U_VS_AGR_PEIC")
        val U_VS_AGR_PEIC: String,

        @SerializedName("U_VS_AGR_PEFC")
        val U_VS_AGR_PEFC: String,
        @SerializedName("U_VS_AGR_FEIP")
        val U_VS_AGR_FEIP: String,
        @SerializedName("U_VS_AGR_FEFP")
        val U_VS_AGR_FEFP: String,


        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_TETA")
        val U_VS_AGR_TETA: String,
        @SerializedName("U_VS_AGR_FEIC")
        val U_VS_AGR_FEIC: String,
        @SerializedName("U_VS_AGR_FEFC")
        val U_VS_AGR_FEFC: String,
)