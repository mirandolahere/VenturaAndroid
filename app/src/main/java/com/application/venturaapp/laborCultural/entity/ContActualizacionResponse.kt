package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName


data class ContActualizacionResponse (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("U_VS_AGR_CDPP")
        val U_VS_AGR_CDPP: String,
        @SerializedName("DocumentNumber")
        val DocumentNumber: Int,
        @SerializedName("ItemNo")
        val ItemNo: String,
        @SerializedName("ProductionOrderLines")
        val ProductionOrderLines: List<ContabilizacionActualizacionResponse>
)