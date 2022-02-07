package com.application.venturaapp.fertilizante.entity

import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat

data class Almacen (

        @SerializedName("WarehouseCode")
        val WarehouseCode: String,
        @SerializedName("WarehouseName")
        val WarehouseName: String
)