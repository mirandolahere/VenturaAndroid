package com.application.venturaapp.helper

import com.google.gson.annotations.SerializedName

data class Body(

        @SerializedName("code")
        val code: Int,

        @SerializedName("message")
        val message: MessageError

)
