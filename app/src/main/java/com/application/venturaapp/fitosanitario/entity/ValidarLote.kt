package com.application.venturaapp.fitosanitario.entity

import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class ValidarLote (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("value")
        val value: String,

)