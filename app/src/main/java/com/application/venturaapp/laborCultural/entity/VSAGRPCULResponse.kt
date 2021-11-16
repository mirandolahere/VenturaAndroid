package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName


data class VSAGRPCULResponse (

        @SerializedName("DocNum")
        val DocNum: Int,
        @SerializedName("Period")
        val Period: Int,
        @SerializedName("U_VS_AGR_CDCA")
        val U_VS_AGR_CDCA: String,
        @SerializedName("U_VS_AGR_CDPP")
        val U_VS_AGR_CDPP: String,
        @SerializedName("U_VS_AGR_CDEP")
        val U_VS_AGR_CDEP: String,
        @SerializedName("VS_AGR_DPCLCollection")
        val VS_AGR_DPCLCollection: List<VSAGRDPCLResponse>
)