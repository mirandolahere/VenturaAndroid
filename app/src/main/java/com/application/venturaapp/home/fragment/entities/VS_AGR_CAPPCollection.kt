package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class VS_AGR_CAPPCollection (


        @SerializedName("Code")
        val Code: String,
        @SerializedName("LineId")
        val LineId: Int,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("U_VS_AGR_CDPP")
        val U_VS_AGR_CDPP: String,
        @SerializedName("U_VS_AGR_DSPP")
        val U_VS_AGR_DSPP: String,
        @SerializedName("U_VS_AGR_ESTA")
        val U_VS_AGR_ESTA: String,
        @SerializedName("U_VS_AGR_CDFD")
        val U_VS_AGR_CDFD: String,

        @SerializedName("U_VS_AGR_DSFD")
        val U_VS_AGR_DSFD: String,
        @SerializedName("U_VS_AGR_CDSC")
        val U_VS_AGR_CDSC: String,
        @SerializedName("U_VS_AGR_DSSC")
        val U_VS_AGR_DSSC: String,


        @SerializedName("U_VS_AGR_CDLT")
        val U_VS_AGR_CDLT: String,
        @SerializedName("U_VS_AGR_DSLT")
        val U_VS_AGR_DSLT: String,
        @SerializedName("U_VS_AGR_DEOF")
        val U_VS_AGR_DEOF: Int,
        @SerializedName("U_VS_AGR_DNOF")
        val U_VS_AGR_DNOF: Int,
)