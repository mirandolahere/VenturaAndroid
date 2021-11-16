package com.application.venturaapp.laborCultural.entity

import com.google.gson.annotations.SerializedName


data class SendResponse (

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("DocEntry")
        val DocEntry: Int,
        @SerializedName("DocNum")
        val DocNum: Int

)