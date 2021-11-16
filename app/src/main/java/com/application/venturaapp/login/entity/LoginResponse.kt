package com.application.venturaapp.login.entity

import com.google.gson.annotations.SerializedName

data class LoginResponse(

        @SerializedName("odata.metadata")
        val metadata: String,
        @SerializedName("Version")
        val Version: String,
        @SerializedName("SessionId")
        val SessionId: String,
        @SerializedName("SessionTimeout")
        val SessionTimeout: Int
)