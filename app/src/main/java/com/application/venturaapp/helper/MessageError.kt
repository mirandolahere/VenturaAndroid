package com.application.venturaapp.helper

import com.google.gson.annotations.SerializedName

data class MessageError(

        @SerializedName("lang")
        val lang: String,

        @SerializedName("value")
        val value: String

)
