package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName

data class EtapaProduccionResponse(

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("VS_AGR_CAEPCollection")
        val VS_AGR_EPEPCollection: List<EtapaProduccionListResponse>
)