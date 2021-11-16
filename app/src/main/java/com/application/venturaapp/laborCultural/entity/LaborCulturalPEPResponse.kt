package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName

data class LaborCulturalPEPResponse(

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("value")
        val value: List<LaborCulturalListResponse>
)