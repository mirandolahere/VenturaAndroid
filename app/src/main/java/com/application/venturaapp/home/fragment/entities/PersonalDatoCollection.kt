package com.application.venturaapp.home.fragment.entities

import com.google.gson.annotations.SerializedName

data class PersonalDatoCollection (


        @SerializedName("Code")
        var Code: String,
        @SerializedName("LineId")
        var LineId: Int,
        @SerializedName("Object")
        val Object: String,
        @SerializedName("U_VS_AGR_NOCM")
        var U_VS_AGR_NOCM: String,
        @SerializedName("U_VS_AGR_PARE")
        var U_VS_AGR_PARE: String,
        @SerializedName("U_VS_AGR_TEL1")
        var U_VS_AGR_TEL1: String,
        @SerializedName("U_VS_AGR_TEL2")
        var U_VS_AGR_TEL2: String
)