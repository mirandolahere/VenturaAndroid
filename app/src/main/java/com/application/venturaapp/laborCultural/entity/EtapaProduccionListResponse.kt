package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName

data class EtapaProduccionListResponse(

        @SerializedName("Code")
        val Code: String,
        @SerializedName("LineId")
        val LineId: String,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("LogInst")
        val LogInst: String,
        @SerializedName("U_VS_AGR_COEP")
        val U_VS_AGR_COEP: String,
        @SerializedName("U_VS_AGR_DETE")
        val U_VS_AGR_DETE: String,
        @SerializedName("U_VS_AGR_PINI")
        val U_VS_AGR_PINI: String,
        @SerializedName("U_VS_AGR_PFIN")
        val U_VS_AGR_PFIN: String,
        @SerializedName("U_VS_AGR_FINI")
        val U_VS_AGR_FINI: String,
        @SerializedName("U_VS_AGR_FFIN")
        val U_VS_AGR_FFIN: String,
        @SerializedName("U_VS_AGR_CDEP")
        val U_VS_AGR_CDEP: String,
        @SerializedName("U_VS_AGR_DSEP")
        val U_VS_AGR_DSEP: String,
        @SerializedName("U_VS_AGR_PEIC")
        val U_VS_AGR_PEIC: String,
        @SerializedName("U_VS_AGR_PEFC")
        val U_VS_AGR_PEFC: String,
        @SerializedName("U_VS_AGR_FEIP")
        val U_VS_AGR_FEIP: String,
        @SerializedName("U_VS_AGR_FEFP")
        val U_VS_AGR_FEFP: String,
        @SerializedName("U_VS_AGR_NMES")
        val U_VS_AGR_NMES: String,
        @SerializedName("U_VS_AGR_ACTV")
        val U_VS_AGR_ACTV: String,
        @SerializedName("U_VS_AGR_FEIC")
        val U_VS_AGR_FEIC: String,
        @SerializedName("U_VS_AGR_FEFC")
        val U_VS_AGR_FEFC: String
)