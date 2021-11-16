package com.application.venturaapp.helper

import com.google.gson.annotations.SerializedName

data class TipoDocumento(

        @SerializedName("Key")
        var Key: String,
        @SerializedName("Value")
        var Value: String
)