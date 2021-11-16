package com.application.venturaapp.fitosanitario.entity

import com.application.venturaapp.laborCultural.entity.LaborCulturalDetalleResponse
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class Almacen (

        @SerializedName("WarehouseCode")
        val WarehouseCode: String,
        @SerializedName("WarehouseName")
        val WarehouseName: String,

)