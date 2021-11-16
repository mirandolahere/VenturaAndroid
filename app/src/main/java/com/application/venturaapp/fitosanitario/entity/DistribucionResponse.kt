package com.application.venturaapp.fitosanitario.entity

import com.google.gson.annotations.SerializedName


data class DistribucionResponse (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("VS_AGR_DSFTCollection")
        val datos: List<FitosanitarioDistribucionResponse>
)